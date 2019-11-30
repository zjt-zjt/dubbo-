package com.taotao.rest.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taotao.common.TaotaoResult;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.rest.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired(required = false)
    private TbContentMapper contentMapper;
    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    @Override
    public TaotaoResult getContentList(long cid) {

//        TbContentExample example = new TbContentExample();
//        //添加条件
//        TbContentExample.Criteria criteria = example.createCriteria();
//        criteria.andCategoryIdEqualTo(cid);
//        List<TbContent> list = contentMapper.selectByExample(example);
//        return TaotaoResult.ok(list);


        // 从Redis缓存中尝试获取需要的数据
        List<TbContent> tbContentList = getTbContentFromCache(cid);

    if(tbContentList==null){
        TbContentExample example = new TbContentExample();
        //添加条件
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        tbContentList = contentMapper.selectByExample(example);
        pushTbContent2Cache(cid, tbContentList);
    }
        return TaotaoResult.ok(tbContentList);
    }

         //从Redis缓存中尝试获取需要的数据
     public    List<TbContent>   getTbContentFromCache(long cid){
         ValueOperations<String, String> op = redisTemplate.opsForValue();
         String s = op.get("taotao_content_cat_" + cid, 0, -1);
         List<TbContent> tbContentList = JSON.parseArray(s, TbContent.class);
         //System.out.println(tbContentList.get(0).getId());
         return tbContentList  ;
  }

    public    void   pushTbContent2Cache(long cid,List<TbContent> tbContentList){
        ValueOperations<String, String> op = redisTemplate.opsForValue();
        op.set("taotao_content_cat_"+cid, JSON.toJSONString(tbContentList));

    }



}