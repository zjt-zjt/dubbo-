package com.taotao.web;


import com.taotao.common.EUTreeNode;
import com.taotao.common.TaotaoResult;
import com.taotao.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/list")
    @ResponseBody
    public List<EUTreeNode> getContentCategoryList(@RequestParam(value="id", defaultValue="0")long parentid) {
        List<EUTreeNode> list = contentCategoryService.getContentCategoryList(parentid);

        return list;
    }

    @RequestMapping("/create")
    @ResponseBody
    public TaotaoResult addNode(Long parentId, String name)  {

        TaotaoResult result = contentCategoryService.addNode(parentId, name);
        return result;
    }

    @RequestMapping("/update")
    @ResponseBody
    public TaotaoResult updateNode(Long id, String name)  {
        TaotaoResult result = contentCategoryService.updateNode(id, name);
        return result;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public TaotaoResult deleteNode(Long id,Long parentId )  {

        TaotaoResult result = contentCategoryService.deleteNode(id, parentId );
        return result;
    }


}
