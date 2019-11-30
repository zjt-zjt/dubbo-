package com.taotao.service;

import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbItem;

public interface AddService {
//商品的添加,描述的添加,规格参数的添加
    TaotaoResult saveItem(TbItem tbItem, String desc,String itemParams);
}
