package com.taotao.rest.web;

import com.taotao.common.TaotaoResult;
import com.taotao.rest.service.TbItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
public class TbItemController {

    @Autowired
    private TbItemService tbItemService;

    @RequestMapping("/get_item_detail/{id}")
    public TaotaoResult getItemDetails(@PathVariable Long id) {
        TaotaoResult result = tbItemService.getItemDetails(id);
        return result;
    }
}
