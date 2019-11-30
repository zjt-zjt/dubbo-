package com.taotao.web;

import com.taotao.common.EUTreeNode;
import com.taotao.service.impl.TbitemCatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TbitemCatController {

    @Autowired
    private TbitemCatServiceImpl tbitemCatService;

    @RequestMapping("/item/cat/list")
    public List<EUTreeNode> gatAllItemCat(@RequestParam(value = "id", defaultValue = "0") long parentId){
        List<EUTreeNode> itemCatTree = tbitemCatService.getItemCatTree(parentId);
        return itemCatTree;
    }
}
