package com.taotao.rest.web;

import com.taotao.common.TaotaoResult;
import com.taotao.rest.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/itemcat")
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    @CrossOrigin
    @RequestMapping("/all")
    public Object getItemCat() {
        TaotaoResult taotaoResult = itemCatService.getItemCat();
        return taotaoResult;
    }

}
