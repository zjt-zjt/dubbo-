package com.taotao.portal.web;

import com.taotao.pojo.SearchResult;
import com.taotao.portal.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PortalSearchServiceCon {
    @Autowired
    private SearchService searchService;

    @RequestMapping("/search")
    public String search(@RequestParam("q") String keywords, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "30") Integer pageSize, Model model) {
        SearchResult searchResult = searchService.search(keywords, pageNum, pageSize);
        model.addAttribute("query", keywords);
        model.addAttribute("totalPages", searchResult.getTotalPages());
        model.addAttribute("page", searchResult.getPageNum());
        model.addAttribute("itemList", searchResult.getItemList());
        return "search";
    }
}
