package com.taotao.rest.service;

import com.taotao.common.TaotaoResult;

public interface CacheManagerService {
    TaotaoResult clearCache(String key);

    TaotaoResult clearContentCache(Long contentCategoryId);
}
