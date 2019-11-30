package com.taotao.service;

import com.taotao.common.EasyUIResult;
import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbItem;

public interface ContentService {
    //展示内容
    EasyUIResult getContentList(long catId, Integer page, Integer rows);
    //添加内容
    TaotaoResult addContent(TbContent content);
    //编辑内容
    TaotaoResult updateTbContent(TbContent tbContent, String desc);

    //删除内容
    TaotaoResult delete(String id);
}
