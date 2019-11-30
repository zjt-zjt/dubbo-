package com.taotao.portal.web;

import com.taotao.portal.service.TbItemParamItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TbItemParamItemController {
    @Autowired
    private TbItemParamItemService tbItemParamItemService;

    @RequestMapping("/item/param/{id}")
    @ResponseBody
    public String getItemParam(@PathVariable Long id) {
        String html = tbItemParamItemService.getItemParam(id);
        return html;
    }
}
