package com.taotao.rest.service.impl;

import com.taotao.common.TaotaoResult;
import com.taotao.rest.service.CacheManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CacheManagerServiceImpl implements CacheManagerService {
//    @Autowired
//    private RedisTemplate redisTemplate;

    //利用RedisTemplate一直不能清空缓存
    @Autowired
  private StringRedisTemplate redisTemplate;

//   @Value("${taotao.redis.keys.global_prefix}")
//    private String globalPrefix;
//
//    @Value("${taotao.redis.keys.content_prefix}")
//    private String contentKeyPrefix;

    @Value("taotao_content_cat_")
    private String globalPrefix;

//    @Value("89")
//    private String contentKeyPrefix;

    @Override
    public TaotaoResult clearCache(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult clearContentCache(Long contentCategoryId) {
//        String key = globalPrefix + contentKeyPrefix + contentCategoryId;
        //String key = globalPrefix + contentKeyPrefix ;
        String key = globalPrefix + contentCategoryId;
        return clearCache(key);
    }
}
