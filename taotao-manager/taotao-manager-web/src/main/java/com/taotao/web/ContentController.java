package com.taotao.web;

import com.taotao.common.EasyUIResult;
import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;
    //返回内容列表
    @RequestMapping("/content/query/list")
    @ResponseBody
    public EasyUIResult getContentList(Long categoryId, Integer page, Integer rows) throws Exception {
        EasyUIResult result = contentService.getContentList(categoryId, page, rows);

        return result;

    }

    //新增内容
    @RequestMapping("/content/save")
    @ResponseBody
    public TaotaoResult addContent(TbContent content) throws Exception {
        TaotaoResult result = contentService.addContent(content);
        return result;
    }

    //编辑内容
    @RequestMapping("/rest/content/edit")
    @ResponseBody
    public TaotaoResult editContent(TbContent tbContent, String desc)  {
        TaotaoResult result = contentService.updateTbContent(tbContent,desc);
        return result;
    }


    //删除内容
    @RequestMapping("/content/delete")
    @ResponseBody
    public TaotaoResult deleteContent(HttpServletRequest request)  {
        TaotaoResult taotaoResult =null;
        String id=  request.getParameter("ids");
        for(String id1 :id.split(",")){
            taotaoResult  = contentService.delete(id1);
        }

        return  taotaoResult;
    }


}
