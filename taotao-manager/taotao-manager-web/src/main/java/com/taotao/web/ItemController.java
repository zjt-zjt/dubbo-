package com.taotao.web;

import com.taotao.common.EasyUIResult;
import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.service.AddService;
import com.taotao.service.ITbitemService;
import com.taotao.service.TbItemParamService;
import com.taotao.service.impl.TbitemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ItemController {
     @Autowired
    private AddService addService;


    @Autowired
    private TbitemServiceImpl itemService;
    @Autowired
    private TbItemParamService tbItemParamService;



//商品列表展示
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIResult itemList(@RequestParam("page") Integer page,
                                 @RequestParam("rows") Integer rows){

        List<TbItem> tbItems = itemService.itemPageService(page,rows);
        long count = itemService.itemCountService();

        EasyUIResult easyUIResult = new EasyUIResult(count,tbItems);
        return  easyUIResult;
    }
//跳转到添加页面
    @RequestMapping("item-add")
    public String result(){

        return "item-add";
    }
//添加商品
    @RequestMapping("/item/save")
    @ResponseBody
    public TaotaoResult saveTbItem(TbItem tbItem, String desc,String itemParams) {
        TaotaoResult result = addService.saveItem(tbItem, desc,itemParams);
        return result;
    }
//删除商品
   @RequestMapping("/item/delete")
   @ResponseBody
   public TaotaoResult delete(HttpServletRequest request){
       TaotaoResult taotaoResult =null;
     String id=  request.getParameter("ids");
       for(String id1 :id.split(",")){
            String status = "3";
            taotaoResult  = itemService.update(id1, status);
       }
          return  taotaoResult;

   }

//下架商品
    @RequestMapping("/rest/item/instock")
    @ResponseBody
    public TaotaoResult instock(HttpServletRequest request){
        TaotaoResult taotaoResult =null;
        String id=  request.getParameter("ids");
        //System.out.println(id);
        for(String id1 :id.split(",")){
            String status = "2";
            taotaoResult  = itemService.update(id, status);
        }
        return  taotaoResult;

    }

//上架商品
    @RequestMapping("/rest/item/reshelf")
    @ResponseBody
    public TaotaoResult reshelf(HttpServletRequest request){
        TaotaoResult taotaoResult =null;
        String id=  request.getParameter("ids");
        for(String id1 :id.split(",")){
            String status = "1";
            taotaoResult  = itemService.update(id, status);
        }
        return  taotaoResult;

    }
//跳转到编辑页面
    @RequestMapping("/item-edit")
    public String edit(HttpServletRequest request){

        return  "item-edit";
    }

//编辑商品
    @RequestMapping("/rest/item/update")
    @ResponseBody
    public TaotaoResult editTbItem(TbItem tbItem, String desc,String itemParams) {
        TaotaoResult result = itemService.saveItem(tbItem, desc,itemParams);
        return result;
    }
//回显规格参数数据
    @RequestMapping("/rest/item/param/item/query/{id}")
    @ResponseBody
    public  TaotaoResult ItemParamItem (@PathVariable long id){
        TaotaoResult itemParamItem = tbItemParamService.getItemParamItem(id);
        return  itemParamItem;

    }
//回显描述数据
    @RequestMapping("/rest/item/query/item/desc/{id}")
    @ResponseBody
    public  TaotaoResult desc (@PathVariable long id){
        TaotaoResult itemParamItem = tbItemParamService.getItemParamItemDesc(id);
        return  itemParamItem;
    }



}
