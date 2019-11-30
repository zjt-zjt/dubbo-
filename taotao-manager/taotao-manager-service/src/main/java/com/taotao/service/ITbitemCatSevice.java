package com.taotao.service;

import com.taotao.common.EUTreeNode;

import java.util.List;

public interface ITbitemCatSevice {
    /*查询所有商品类别树*/
    List<EUTreeNode> getItemCatTree(Long parentId);

}
