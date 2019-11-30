package com.taotao.portal.web;

import com.taotao.common.ExceptionUtil;
import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.portal.pojo.Item;
import com.taotao.portal.pojo.Order;
import com.taotao.portal.service.CartService;
import com.taotao.portal.service.OrderService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;


    /**
     * 显示订单确认画面
     * <p>Title: showOrderCart</p>
     * <p>Description: </p>
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/order-cart")
    public String showOrderCart(HttpServletRequest request, TbUser user, Model model) {
        //根据用户信息，取出用户的收货地址列表
        //本项目中使用静态数据模拟。。。。

        //从cookie中把商品列表取出来
        List<Item> itemsList = cartService.getCartItemsList(request);
        model.addAttribute("cartList", itemsList);
        return "order-cart";
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createOrder(HttpServletRequest request, Order order, Model model) {
        System.out.println("进入create");
        //从request中取用户信息
        TbUser user = (TbUser) request.getAttribute("user");
        //把用户信息补充到order对象中
        if(!(user==null)){
            order.setUserId(user.getId());
            order.setBuyerNick(user.getUsername());
       }


        //提交订单
        TaotaoResult result = null;
        try {
            result = orderService.createService(order);
            //订单创建成功
            if (result.getStatus() == 200) {
                model.addAttribute("orderId", result.getData());
                model.addAttribute("payment", order.getPayment());
                //两天后送达
                DateTime dateTime = new DateTime();
                dateTime = dateTime.plusDays(2);
                model.addAttribute("date", dateTime.toString("yyyy-MM-dd"));
                return "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
        //订单创建失败
        model.addAttribute("message", result.getMsg());
        return "error/exception";


    }
}