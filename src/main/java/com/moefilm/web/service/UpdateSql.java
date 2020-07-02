package com.moefilm.web.service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.util.ObjectUtils;

public class UpdateSql {

    private Object target;
    private String idName;
    private Object idValue;
    private SqlType currentType;

    public enum SqlType {
        INSERT, UPDATE, DELETE, INSERTWITHVALUE
    }

    public UpdateSql(SqlType sqlType, Object target) {
        this.target = target;
        switch (sqlType) {
            case INSERT:
                currentType = SqlType.INSERT;
                createInsert();
                break;
            case INSERTWITHVALUE:
                currentType = SqlType.INSERTWITHVALUE;
                createInsertWithValue();
                break;
            case UPDATE:
                currentType = SqlType.UPDATE;
                createUpdate();
                break;
            case DELETE:
                currentType = SqlType.DELETE;
                createDelete();
                break;
            default:
                break;
        }
    }

    public UpdateSql(Class<?> target) {
        String tableName = getTableNameForClass(target);
        getFields(target);

        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("DELETE FROM ").append(tableName).append(" WHERE ");
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                TableId id = field.getAnnotation(TableId.class);
                if (null != id) {
                    sqlBuffer.append(field.getName()).append("=?");
                }
            }
        }
        this.sqlBuffer = sqlBuffer.toString();
    }

    /**
     * 创建跟删除
     */
    private void createDelete() {
        String tableName = getTableName();
        getFields(target.getClass());
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("DELETE FROM ").append(tableName).append(" WHERE ");
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                TableId id = field.getAnnotation(TableId.class);
                if (null != id) {
                    sqlBuffer.append(field.getName()).append("=?");
                    param.add(readField(field));
                }
            }
        }
        this.sqlBuffer = sqlBuffer.toString();
    }

    protected Object readField(Field field) {
        Object val = null;
        try {
            val = FieldUtils.readField(field, target, true);
        } catch (Exception e) {
            throw new RuntimeException(currentType.name(), e);
        }
        return val;
    }

    /**
     * 创建跟新语句
     */
    private void createUpdate() {
        String tableName = getTableName();
        getFields(target.getClass());
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("UPDATE ").append(tableName).append(" SET ");

        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                TableId id = field.getAnnotation(TableId.class);
                if (id == null) {
                    sqlBuffer.append(field.getName()).append("=?");
                    param.add(readField(field));
                } else {
                    idName = field.getName();
                    idValue = readField(field);
                }
            }
        }
        if (idName == null) {
            throw new RuntimeException("not found of " + target.getClass() + "'s ID");
        }
        sqlBuffer.append(" WHERE ").append(idName).append("=?");
        param.add(idValue);

        this.sqlBuffer = sqlBuffer.toString();
    }

    /**
     * 根据注解获取表名
     */
    private String getTableName() {
        String tableName = null;
        Class<?> clazz = target.getClass();
        tableName = getTableNameForClass(clazz);
        return tableName;
    }

    private String getTableNameForClass(Class<?> clazz) {
        String tableName;
        TableName table = clazz.getAnnotation(TableName.class);
        if (null != table) {
            tableName = table.value();
            if ("".equalsIgnoreCase(tableName)) {
                tableName = clazz.getSimpleName();
            }
        } else {
            tableName = clazz.getSimpleName();
        }
        return tableName;
    }

    /**
     * 创建插入语句
     */
    private void createInsert() {
        String tableName = getTableName();
        getFields(target.getClass());
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("INSERT INTO ").append(tableName).append("(");

        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                TableId id = field.getAnnotation(TableId.class);
                if (id == null) {
                    sqlBuffer.append(field.getName()).append(",");
                    param.add(readField(field));
                }
            }
        }
        int length = sqlBuffer.length();
        sqlBuffer.delete(length - 1, length).append(")values(");
        int size = param.size();
        for (int x = 0; x < size; x++) {
            if (x != 0) {
                sqlBuffer.append(",");
            }
            sqlBuffer.append("?");
        }
        sqlBuffer.append(")");

        this.sqlBuffer = sqlBuffer.toString();
    }

    /**
     * 创建插入语句,带具体值
     */
    private void createInsertWithValue() {
        String tableName = getTableName();
        getFields(target.getClass());
        StringBuilder sqlBuffer = new StringBuilder();
        sqlBuffer.append("INSERT INTO ").append(tableName).append("(");

        Map<String,String> ignoreFieldMap = new HashMap<>();

        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                TableId id = field.getAnnotation(TableId.class);
                TableField fieldAnno = field.getAnnotation(TableField.class);

                if (id == null || id.type() == IdType.INPUT || id.type() == IdType.NONE) {

                    String fieldName = com.baomidou.mybatisplus.toolkit.StringUtils.camelToUnderline(field.getName());

                    //如果是忽略字段   加入到忽略Map中 因为存在  基础类的关系
                    if((fieldAnno != null && !fieldAnno.exist())){
                        ignoreFieldMap.put(fieldName,fieldName);
                        continue;
                    }

                    if ( fieldAnno == null || ignoreFieldMap.get(fieldName)==null){
                        sqlBuffer.append(fieldName)
                                .append(",");
                        param.add(readField(field));
                        paramType.add(field.getType().toString());
                    }

                }
            }
        }

        int length = sqlBuffer.length();
        sqlBuffer.delete(length - 1, length).append(")values(");
        int size = param.size();
        for (int x = 0; x < size; x++) {
            if (x != 0) {
                sqlBuffer.append(",");
            }
            Object val = (Object) param.get(x);
            String type = paramType.get(x);
            if (type != null) {
                if(type.contains("Byte")||type.contains("Short")||type.contains("Integer")||type.contains("Float")||type.contains("Long")||type.contains("Double")) {
                    if(ObjectUtils.isEmpty(val)) {
                        sqlBuffer.append("0"); //直接默认为0;
                    }else {
                        sqlBuffer.append(val);
                    }
                }else if(type.contains("Boolean")) {
                    if(ObjectUtils.isEmpty(val)) {
                        sqlBuffer.append(0); //直接默认为0;
                    }else {
                        if(((Boolean)val).booleanValue()){
                            sqlBuffer.append(1);
                        }else {
                            sqlBuffer.append(0);
                        }
                    }
                }else if(type.contains("Date")||type.contains("Timestamp")) {
                    if(ObjectUtils.isEmpty(val)) {
                        sqlBuffer.append("'").append(DateUtils.formatDate(new Date())).append("'");
                    }else {
                        sqlBuffer.append("'").append(DateUtils.formatDate((Date) val)).append("'");
                    }
                }else {
                    String valString = ( val==null ? "":val.toString() ) ;
                    sqlBuffer.append("'").append(valString).append("'");
                }
            }

        }
        sqlBuffer.append(")");

        this.sqlBuffer = sqlBuffer.toString();
    }

    private List<Object> param = new Vector<Object>();
    private List<String> paramType = new Vector<String>();

    private String sqlBuffer;

    public List<Object> getParam() {
        return param;
    }

    public String getSqlBuffer() {
        return sqlBuffer;
    }

    public String getIdName() {
        return idName;
    }

    public Object getIdValue() {
        return idValue;
    }

    List<Field> fields = new Vector<Field>();

    protected void getFields(Class<?> clazz) {
        if (Object.class.equals(clazz)) {
            return;
        }
        Field[] fieldArray = clazz.getDeclaredFields();
        for (Field filed : fieldArray) {
            fields.add(filed);
        }
        getFields(clazz.getSuperclass());
    }
}
