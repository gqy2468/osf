package com.moefilm.web.util;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

/**
 * 代码生成器
 *
 * @author moefilm.com
 */
public class MybatisPlusUtil {
    private String userName;
    private String passWord;
    private String dbName;
    private String dbIPAddress;
    private boolean isEnableLombok = true;
    private static Class<?> superMapper = BaseMapper.class;
    private static Class<?> superServiceClass = IService.class;
    private static Class<?> superServiceImplClass = ServiceImpl.class;

    private MybatisPlusUtil() {

    }

    private MybatisPlusUtil(String dbUserName, String dbUserPass, String dbIPAddress, String databaseName, boolean isEnableLombok) {
        this.userName = dbUserName;
        this.passWord = dbUserPass;
        this.dbIPAddress = dbIPAddress;
        this.dbName = databaseName;
        this.isEnableLombok = isEnableLombok;
    }

    /**
     * @param module         业务域名称目前只有user-center
     * @param tablePrefix    表名称
     * @param packageName    要生成实体存放的包
     * @param author         开发人员
     * @param dbUserName     数据库用户名
     * @param dbUserPass     数据库密码
     * @param dbIPAddress    数据库ip地址
     * @param databaseName   数据库名称
     * @param isEnableLombok 是否启用lombok
     * @return
     */
    public static MybatisPlusUtil generator(String module, String tablePrefix, String packageName, String author,
                                             String dbUserName, String dbUserPass, String dbIPAddress, String databaseName, boolean isEnableLombok) {
        MybatisPlusUtil instance = new MybatisPlusUtil(dbUserName, dbUserPass, dbIPAddress, databaseName, isEnableLombok);
        instance.shell(tablePrefix, packageName, author);
        return instance;
    }

    private void shell( String tablePrefix, String packageName, String author) {
        final String basePath = MybatisPlusUtil.class.getResource("/").getPath().replace("/target/classes/", "");
        System.out.println(basePath);
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(basePath + "/src/main/java");
        gc.setFileOverride(true);
        gc.setActiveRecord(false);
        // XML 二级缓存
        gc.setEnableCache(false);
        // XML Result Map
        gc.setBaseResultMap(true);
        // XML Column List
        gc.setBaseColumnList(true);
        gc.setAuthor(author);
        gc.setOpen(false);
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
            @Override
            public DbColumnType processTypeConvert(String fieldType) {
            // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
            System.out.println(fieldType);
            return super.processTypeConvert(fieldType);
            }
        });
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername(userName);
        dsc.setPassword(passWord);
        dsc.setUrl("jdbc:mysql://" + dbIPAddress + ":3306/" + dbName + "?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&serverTimezone=GMT-8");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 表名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 需要生成的表
        strategy.setInclude(tablePrefix);
        // 自定义 mapper 父类
        strategy.setSuperMapperClass(superMapper.getCanonicalName());
        // 自定义 service 父类
        strategy.setSuperServiceClass(superServiceClass.getCanonicalName());
        // 自定义 service 实现类父类
        strategy.setSuperServiceImplClass(superServiceImplClass.getCanonicalName());
        strategy.setEntityLombokModel(isEnableLombok);
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(packageName);

        TemplateConfig tc = new TemplateConfig();
        tc.setController("");
        tc.setEntity("/velocity/entity.java.vm");
        tc.setMapper("/velocity/mapper.java.vm");
        tc.setXml("/velocity/mapper.xml.vm");
        tc.setService("");
        tc.setServiceImpl("");
        pc.setEntity("model");
        pc.setMapper("mapper");
        pc.setXml("mapper");
        pc.setServiceImpl("");
        pc.setService("");
        mpg.setPackageInfo(pc);
        mpg.setTemplate(tc);
        mpg.setGlobalConfig(gc);

        // 执行生成
        mpg.execute();
    }
}
