package com.taotao.service;

import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemParam;


import java.util.List;

public interface TbItemParamService {
    //规格参数模板添加
    TaotaoResult saveItemParam(Long itemCatId, String paramData);

    List<TbItemParam> itemParamService();
    //分页查询
    List<TbItemParam> itemParamPageService(int page, int row);
    //获取总数据
    long itemParamCountService();
    //新增商品套用模板动态生成规格参数表单
    TaotaoResult getItemParamByItemCatId(Long id);
    //删除规格参数
    TaotaoResult delete(String id);
   //规格参数数据回显
    TaotaoResult getItemParamItem(Long id);
    //描述数据回显
    TaotaoResult getItemParamItemDesc(Long id);
    //编辑规格参数
    TaotaoResult EditItemParam(Long itemCatId, String paramData);

}
