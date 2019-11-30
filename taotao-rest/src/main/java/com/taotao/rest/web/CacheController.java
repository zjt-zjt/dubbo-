package com.taotao.rest.web;

import com.taotao.common.TaotaoResult;
import com.taotao.rest.service.CacheManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
public class CacheController {

    @Autowired
    private CacheManagerService cacheManagerService;

    @GetMapping("/clear/{key}")
    public TaotaoResult clearCache(@PathVariable String key) {
        return cacheManagerService.clearCache(key);
    }

    @GetMapping("/clear/content/cat/{contentCatId}")
    public TaotaoResult clearContentCache(@PathVariable Long contentCatId) {
        System.out.println("进入");
        return cacheManagerService.clearContentCache(contentCatId);
    }



}



