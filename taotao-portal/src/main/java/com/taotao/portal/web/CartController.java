package com.taotao.portal.web;

import com.taotao.common.TaotaoResult;
import com.taotao.portal.pojo.Item;
import com.taotao.portal.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @RequestMapping("/add/{itemId}")
    public String addItem(@PathVariable Long itemId,
                          HttpServletRequest request, HttpServletResponse response, Model model) {
        //添加商品信息
        TaotaoResult result = cartService.addItem(itemId, request, response);
        //错误信息
        if (result.getStatus() != 200) {
            model.addAttribute("message", result.getMsg());
            return "error/exception";
        }
        //把购物车中的商品传递给页面
        model.addAttribute("cartList", result.getData());
        return "cart";
    }


    @RequestMapping("/cart")
    public String showCart(HttpServletRequest request, Model mode) {
        //取购物车信息
        List<Item> list = cartService.getCartItemsList(request);
        mode.addAttribute("cartList", list);
        return "cart";
    }


    @RequestMapping("/update/num/{itemId}/{num}")
    @ResponseBody
    public TaotaoResult updateNumById(@PathVariable Long itemId, @PathVariable Integer num,
                                      HttpServletRequest request, HttpServletResponse response) {
        TaotaoResult result = cartService.changeItemNum(itemId, num, request, response);
        return result;
    }

    @RequestMapping("/delete/{itemId}")
    public String deleteItemById(@PathVariable Long itemId,
                                 HttpServletRequest request, HttpServletResponse response,
                                 Model model) {
        List<Item> list = cartService.deleteItem(itemId, request, response);
        model.addAttribute("cartList", list);
        return "cart";
    }


}
