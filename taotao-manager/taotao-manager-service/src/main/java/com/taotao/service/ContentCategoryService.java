package com.taotao.service;

import com.taotao.common.EUTreeNode;
import com.taotao.common.TaotaoResult;

import java.util.List;

public interface ContentCategoryService {
     //展示内容分类列表
    List<EUTreeNode> getContentCategoryList(long parentid);
     //新增子节点
    TaotaoResult addNode(long parentId, String name);
    //重命名子节点
    TaotaoResult updateNode(long id, String name);
    //删除子节点
    TaotaoResult deleteNode(long id, long parentId);
}
