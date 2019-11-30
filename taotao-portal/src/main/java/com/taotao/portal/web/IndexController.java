package com.taotao.portal.web;

import com.taotao.portal.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @Autowired
    private AdService adService;

    @RequestMapping("/")
    public String showIndex(Model model)  {
        String adResult = adService.getAdItemList();
        model.addAttribute("ad1", adResult);
        return "index";
    }
}
