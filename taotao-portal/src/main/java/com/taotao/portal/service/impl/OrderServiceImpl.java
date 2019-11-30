package com.taotao.portal.service.impl;

import com.taotao.common.HttpUtil;
import com.taotao.common.JsonUtils;
import com.taotao.common.TaotaoResult;
import com.taotao.portal.pojo.Order;
import com.taotao.portal.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Value("http://localhost:8085")
    //@Value("${ORDER_BASE_URL}")
    private String ORDER_BASE_URL;

    @Value("/order/create")
    //@Value("${ORDER_CREATE_URL}")
   // @Value("/order/order-cart")
    private String ORDER_CREATE_URL;


    @Override
    public TaotaoResult createService(Order order) {

        //把pojo转换成json数据
        String json = JsonUtils.objectToJson(order);
        //调用订单系统服务提交订单
        String resultStr = HttpUtil.doPostJson(ORDER_BASE_URL + ORDER_CREATE_URL, json);
        //转换成java对象
        TaotaoResult taotaoResult = TaotaoResult.format(resultStr);

        return taotaoResult;
    }
}
