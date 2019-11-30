package com.taotao.service.impl;

import com.taotao.common.EUTreeNode;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.service.ITbitemCatSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class TbitemCatServiceImpl implements ITbitemCatSevice {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    /*查询所有商品类别树*/
    @Override
    public List<EUTreeNode> getItemCatTree(Long parentId) {
        // 按照parentId查询分类数据
        TbItemCatExample example = new TbItemCatExample();
        example.createCriteria().andParentIdEqualTo(parentId);
        List<TbItemCat> tbItemCats = tbItemCatMapper.selectByExample(example);

        List<EUTreeNode> euTreeNodes = new ArrayList<>();

        // 把查询到的分类数据封装成EasyUI异步树数据结构
        for(TbItemCat itemCat : tbItemCats) {
            EUTreeNode node = new EUTreeNode();
            node.setId(itemCat.getId());
            node.setText(itemCat.getName());
            node.setState(itemCat.getIsParent() ? "closed" : "open");
            euTreeNodes.add(node);
        }
        return euTreeNodes;
    }
}
