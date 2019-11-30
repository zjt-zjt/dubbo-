package com.taotao.order.service.impl;

import com.taotao.common.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;


import java.util.Date;
import java.util.List;
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired(required = false)
    private TbOrderMapper orderMapper;
    @Autowired(required = false)
    private TbOrderItemMapper orderItemMapper;
    @Autowired(required = false)
    private TbOrderShippingMapper orderShippingMapper;
    //redis中的订单key
    //@Value("${ORDER_ID_KEY}")
       @Value("ORDER_ID_KEY")
       private String ORDER_ID_KEY;
    //@Value("${ORDER_BEGIN_ID}")
       @Value("10")
      private Long ORDER_BEGIN_ID;
       @Value("1")
       private  String ORDER_ITEM_ID;
//    @Autowired
//    private JedisCluster jedisCluster;

     @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    @Override
    public TaotaoResult createOrder(TbOrder order, List<TbOrderItem> itemList, TbOrderShipping orderShipping) {

        //订单表
        //生成订单号
        //String orderIdStr = jedisCluster.get(ORDER_ID_KEY);
        String orderIdStr =  redisTemplate.opsForValue().get(ORDER_ID_KEY,0,-1);
        Long orderId = null;
        if (StringUtils.isBlank(orderIdStr)) {
            //如果redis中没有订单号使用初始订单号初始化
            redisTemplate.opsForValue().set(ORDER_ID_KEY, ORDER_BEGIN_ID.toString());
            //jedisCluster.set(ORDER_ID_KEY, ORDER_BEGIN_ID.toString());
            orderId = ORDER_BEGIN_ID;
        } else {
            //生成订单号
            //orderId = jedisCluster.incr(ORDER_ID_KEY);
            orderId = redisTemplate.opsForValue().increment(ORDER_ID_KEY);
        }
        //设置订单号
        order.setOrderId(orderId.toString());
        Date date = new Date();
        //订单创建时间
        order.setCreateTime(date);
        //订单更新时间
        order.setUpdateTime(date);
        //插入订单表
        orderMapper.insert(order);
        //插入订单商品表
        for (TbOrderItem tbOrderItem : itemList) {
            //取订单商品id

            //Long orderItemId = jedisCluster.incr("ORDER_ITEM_ID");
            Long orderItemId = redisTemplate.opsForValue().increment("ORDER_ITEM_ID");
            tbOrderItem.setId(orderItemId.toString());
            tbOrderItem.setOrderId(orderId.toString());
            //添加到订单商品表
            orderItemMapper.insert(tbOrderItem);
        }
        //插入物流表
        orderShipping.setOrderId(orderId.toString());
        orderShipping.setCreated(date);
        orderShipping.setUpdated(date);
        orderShippingMapper.insert(orderShipping);

        return TaotaoResult.ok(orderId.toString());
    }

}
