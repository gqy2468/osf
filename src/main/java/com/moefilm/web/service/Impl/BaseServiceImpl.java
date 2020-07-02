package com.moefilm.web.service.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.moefilm.web.service.BaseService;
import com.moefilm.web.service.UpdateSql;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;

/**
 * @author： Admin
 * @createTime：2017-11-9
 * @description:
 */
@Transactional(rollbackFor = Exception.class)
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    private Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

    @Override
    public boolean insert(T entity) {
        return super.insert(entity);
    }

    @Override
    public boolean insertBatchByNativeSql(List<T> datas, int batchSize) {
        if (CollectionUtils.isEmpty(datas)) {
            throw new IllegalArgumentException("Error: entityList must not be empty");
        }
        SqlSession batchSqlSession = sqlSessionBatch();
        Connection conn = batchSqlSession.getConnection();
        AtomicInteger counter = new AtomicInteger(0);
        Long begin = System.currentTimeMillis();
        int size = datas.size();
        //这里得到的是具体的轮数
        int round = getRound(size, batchSize);
        String sql = null;
        PreparedStatement pst = null;
        try {
            // 设置事务为非自动提交
            conn.setAutoCommit(false);
            pst = conn.prepareStatement("");
            // 插入表
            for (int i = 1; i <= round; i++) {
                batchSize = getBatchSize(i, round, size,batchSize);
                // 构建完整sql
                sql = batchSql(batchSize, datas, counter);
                pst.addBatch(sql);
                pst.executeBatch();
                conn.commit();
            }
            counter.set(0);
        } catch (SQLException e) {
            logger.error("批量插入操作发生SQL异常",e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                logger.error("回滚失败",e);
            }
        }finally {
            // 头等连接
            try {
                pst.close();
            } catch (SQLException e) {
                logger.error("关闭连接失败",e);
            }
        }
        // 结束时间
        Long end = System.currentTimeMillis();
        // 耗时
        logger.info("\nTotals " + size + " rows of inserted data and takes " + (end - begin) / 1000
                + " seconds " + (end - begin) % 1000 + " millisecond !");
        return true;
    }

    private  int getBatchSize(int index, int round, int size,int batchSize) {
        if (size <= batchSize) { // 如果插入的条数小于轮询次数,那么只轮询一次。所有的直接插入到提交批次中
            return size;
        }
        if (index * batchSize > size) {// 如果最后一轮超过了最大次数
            batchSize = size - (round - 1) * batchSize;
        }
        return batchSize;
    }

    private  int getRound(int size, int batchSize) {
        int round= size/batchSize; //默认是当前批次
        if (size <= batchSize) { // 如果插入的条数小于当前次数,那么只轮询一次。所有的直接插入到提交批次中
            round = 1;
        } else {
            // 如果还有剩余，那么加一轮
            if (size % batchSize != 0) {
                round++;
            }
        }
        return round;
    }

    private  String batchSql(int batchSize, List<T> datas, AtomicInteger counter) {
        boolean isInitSql = false;
        String prefix = "";
        int prefixLength = 0;
        // sql前缀
        StringBuilder sqlBuild = new StringBuilder(80960);
        // 第次提交步长
        for (int j = 1; j <= batchSize; j++) {
            // 构建sql后缀
            int location = counter.getAndIncrement();
            T t = datas.get(location);

            UpdateSql sqlInstance = new UpdateSql(UpdateSql.SqlType.INSERTWITHVALUE, t);
            String sql = sqlInstance.getSqlBuffer();
            if (!isInitSql) {
                prefixLength = sql.lastIndexOf("values(")+"values".length();
                prefix = sql.substring(0, prefixLength);
                sqlBuild.append(sql.substring(prefixLength)).append(",");
                isInitSql = true;
            } else {
                sqlBuild.append(sql.substring(prefixLength)).append(",");
            }
        }
        String sql = prefix + sqlBuild.substring(0, sqlBuild.length() - 1);
        //排除掉日期引起的问题。

        // 清空上一次添加的数据
        sqlBuild = new StringBuilder();
        return sql;
    }


    @Override
    public boolean delete(Wrapper<T> wrapper) {
        return super.delete(wrapper);
    }

    @Override
    public List<T> selectByMap(Map<String, Object> columnMap) {
        return super.selectByMap(columnMap);
    }


    public List<T> selectList(Wrapper<T> wrapper) {
        return super.selectList(wrapper);
    }


    @Override
    public List<Map<String, Object>> selectMaps(Wrapper<T> wrapper) {
        return super.selectMaps(wrapper);
    }

    @Override
    public List<Object> selectObjs(Wrapper<T> wrapper) {
        return super.selectObjs(wrapper);
    }
}