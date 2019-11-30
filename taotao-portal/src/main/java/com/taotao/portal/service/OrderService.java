package com.taotao.portal.service;

import com.taotao.common.TaotaoResult;
import com.taotao.portal.pojo.Order;

public interface OrderService {
    TaotaoResult createService(Order order);
}
