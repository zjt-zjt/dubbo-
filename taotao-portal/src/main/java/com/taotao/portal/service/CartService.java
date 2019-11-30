package com.taotao.portal.service;

import com.taotao.common.TaotaoResult;
import com.taotao.portal.pojo.Item;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CartService {

    TaotaoResult addItem(Long itemId, HttpServletRequest request, HttpServletResponse response);
    List<Item> getCartItemsList(HttpServletRequest request);
    TaotaoResult changeItemNum(long itemId, int num, HttpServletRequest request, HttpServletResponse response);
    List<Item> deleteItem(Long itemId, HttpServletRequest request, HttpServletResponse response);
}
