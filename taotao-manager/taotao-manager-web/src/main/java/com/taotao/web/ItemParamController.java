package com.taotao.web;

import com.taotao.common.EasyUIResult;
import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemParam;
import com.taotao.service.TbItemParamService;
import com.taotao.service.impl.TbitemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/item/param")
public class ItemParamController {
    @Autowired
    private TbItemParamService paramService;
//规格参数列表展示
    @RequestMapping("/list")
    @ResponseBody
    public EasyUIResult itemParamList(@RequestParam("page") Integer page,
                                 @RequestParam("rows") Integer rows){

        List<TbItemParam> tbItems = paramService.itemParamPageService(page,rows);
        long count = paramService.itemParamCountService();
        EasyUIResult easyUIResult = new EasyUIResult(count,tbItems);
        return  easyUIResult;
    }



    //规格参数模板添加
    @RequestMapping("/save/{itemCatId}")
    @ResponseBody
    public TaotaoResult saveItemParam(@PathVariable Long itemCatId, String paramData) {
        TaotaoResult result =paramService .saveItemParam(itemCatId, paramData);
        return result;
    }

//新增商品套用模板动态生成规格参数表单
    @RequestMapping("/query/itemcatid/{id}")
    @ResponseBody
    public TaotaoResult queryItemParamByItemCatId(@PathVariable Long id) {
        TaotaoResult result = paramService.getItemParamByItemCatId(id);
        return result;
    }

//删除规格参数
   @RequestMapping("/delete")
   @ResponseBody
   public  TaotaoResult delete (HttpServletRequest request){
       TaotaoResult taotaoResult =null;
       String id=  request.getParameter("ids");
       for(String id1: id.split(",")){
        taotaoResult  =   paramService.delete(id1);
       }
         return  taotaoResult;
    }
//跳转编辑页面
    @RequestMapping("/item-param-edit")
   // @ResponseBody
    public String paramEdit(){
        return  "item-param-edit";
    }


//编辑规格参数
    @RequestMapping("/update/{itemCatId}")
    @ResponseBody
    public TaotaoResult UpdateItemParam(@PathVariable Long itemCatId, String paramData) {
        TaotaoResult result =paramService .EditItemParam(itemCatId, paramData);
        return result;
    }

}
