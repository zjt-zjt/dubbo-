package com.taotao.portal.web;

import com.taotao.portal.service.TbItemDescService;
import com.taotao.portal.service.TbItemParamItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TbItemDescController {
    @Autowired
    private TbItemDescService tbItemDescService;

    @RequestMapping("/item/desc/{id}")
    @ResponseBody
    public String getTbItemDesc(@PathVariable Long id) {
        String html = tbItemDescService.getTbItemDesc(id);
        return html;
    }
}
