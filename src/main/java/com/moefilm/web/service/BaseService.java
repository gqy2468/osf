package com.moefilm.web.service;

import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * @ClassName: BaseService
 * @Description: 基类Service定义
 * @author: moefilm.com
 * @date: 2020年1月21日
 * @param <T>
 */
public interface BaseService<T> extends IService<T> {
    public boolean insertBatchByNativeSql(List<T> datas, int batchSize);
}
