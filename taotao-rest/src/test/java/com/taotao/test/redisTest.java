package com.taotao.test;

import com.alibaba.fastjson.JSON;
import com.taotao.common.TaotaoResult;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.rest.RestApplication;
import com.taotao.rest.service.ContentService;
import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestApplication.class)
public class redisTest {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private TbContentMapper tbContentMapper;
    @Test
    public void testPutKV() {
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo((long) 89);
        List<TbContent> list = tbContentMapper.selectByExample(example);
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        ops.set("zhangsan", JSON.toJSONString(list));
        System.out.println(ops.get("zhangsan"));

    }

}
