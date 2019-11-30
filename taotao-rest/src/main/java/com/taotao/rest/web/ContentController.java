package com.taotao.rest.web;

import com.taotao.common.TaotaoResult;
import com.taotao.rest.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/content")
public class ContentController {
    @Autowired(required = false)
    private ContentService contentService;

    @RequestMapping("/category/{cid}")
    @ResponseBody
    public TaotaoResult getContentList(@PathVariable Long cid) {
        TaotaoResult result = null;
        try {
            result = contentService.getContentList(cid);
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, e.getMessage());
        }
        return result;
    }

}
