package com.taotao.service;

import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbItem;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ITbitemService {
    List<TbItem> itemAllService();
    //分页查询
    List<TbItem> itemPageService(int page, int row);
    //获取总数据
    long itemCountService();
    //根据id删除,进行上下架
    TaotaoResult update(String id,String status);
    //TaotaoResult saveItem(TbItem tbItem, String desc);
    //编辑商品
    TaotaoResult saveItem(TbItem tbItem, String desc,String itemParams);
}
