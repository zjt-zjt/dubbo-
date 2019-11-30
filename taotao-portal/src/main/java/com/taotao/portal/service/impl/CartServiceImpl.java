package com.taotao.portal.service.impl;

import com.taotao.common.CookieUtils;
import com.taotao.common.HttpUtil;
import com.taotao.common.JsonUtils;
import com.taotao.common.TaotaoResult;
import com.taotao.portal.pojo.Item;
import com.taotao.portal.service.CartService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    //服务url
    @Value("http://localhost:8081")
    private String REST_BASE_URL;
    //商品服务url
    @Value("/item/get_item_detail/")
    private String ITEMS_ITEM_URL;
    //COOKIE中购物车商品对应的key
    @Value("cart")
    private String CART_ITEMS_LIST_KEY;
    //购物车cookie生存期
    @Value("30")
    private Integer CART_ITEMS_EXPIRE_TIME;
    /**
     * 添加购物车商品
     * <p>Title: addItem</p>
     * <p>Description: </p>
     * @param itemId
     * @param request
     * @param response
     * @return
     * @see com.taotao.portal.service.CartService#addItem(java.lang.Long, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */

    @Override
    public TaotaoResult addItem(Long itemId, HttpServletRequest request, HttpServletResponse response) {



        //根据商品id查询商品信息
        Item item = getItemById(itemId);
        if (item == null) {
            return TaotaoResult.build(400, "未查询到该商品信息");
        }
        //取cookie中购物车商品列表
        List<Item> cartItems = getItemListFromCookie(request);
        //判断该商品是否存在于购物车中
        boolean itemExists = false;
        for (Item i : cartItems) {
            if (i.getId().longValue() == itemId.longValue()) {
                //购物车中有此商品
                i.setNum(i.getNum() + 1);
                itemExists = true;
                break;
            }

        }
        if (! itemExists) {
            //设置数量为1
            item.setNum(1);
            //把商品添加到购物车
            cartItems.add(item);
        }
        //把购物车信息写入cookie中
        CookieUtils.setCookie(request, response, CART_ITEMS_LIST_KEY, JsonUtils.objectToJson(cartItems), CART_ITEMS_EXPIRE_TIME, true);

        return TaotaoResult.ok(cartItems);

    }

    private Item getItemById(Long itemId) {
        //根据商品id查询商品信息
        String resultStr = HttpUtil.doGet(REST_BASE_URL + ITEMS_ITEM_URL + itemId);
        //转换成taotaoResult
        TaotaoResult result = TaotaoResult.formatToPojo(resultStr, Item.class);
        //取商品信息
        Item item  = null;
        if (result.getStatus() == 200) {
            item = (Item) result.getData();
        }

        return item;
    }


    private List<Item> getItemListFromCookie(HttpServletRequest request) {
        //取cookie中购物车商品列表
        String cartItemsStr = CookieUtils.getCookieValue(request, CART_ITEMS_LIST_KEY, true);
        //如果不为空那么就转换成java对象
        List<Item> cartItems = null;
        if (!StringUtils.isBlank(cartItemsStr)) {
            cartItems = JsonUtils.jsonToList(cartItemsStr, Item.class);
        } else {
            cartItems = new ArrayList<>();
        }
        return cartItems;
    }

    /**
     * 取购物车列表
     * <p>
     * Title: getCartItemsList
     * </p>
     * <p>
     * Description:
     * </p>
     *
     * @return
     *
     */
    @Override
    public List<Item> getCartItemsList(HttpServletRequest request) {
        // 从cookie中取商品列表
        List<Item> itemsList = getItemListFromCookie(request);
        return itemsList;
    }

    @Override
    public TaotaoResult changeItemNum(long itemId, int num, HttpServletRequest request, HttpServletResponse response) {
        //从cookie中取商品列表
        List<Item> list = getItemListFromCookie(request);
        //从商品列表中找到要修改数量的商品
        for (Item item : list) {
            if (item.getId() == itemId) {
                //找到商品，修改数量
                item.setNum(num);
                break;
            }
        }
        //把商品信息写入cookie
        CookieUtils.setCookie(request, response, CART_ITEMS_LIST_KEY, JsonUtils.objectToJson(list), CART_ITEMS_EXPIRE_TIME, true);

        return TaotaoResult.ok();
    }


    @Override
    public List<Item> deleteItem(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        List<Item> itemsList = getCartItemsList(request);
        // 找到购物车中的商品，并删除之
        for (Item item : itemsList) {
            if (item.getId().longValue() == itemId.longValue()) {
                itemsList.remove(item);
                break;
            }
        }
        // 更新cookie中的购物车数据
        CookieUtils.setCookie(request, response, CART_ITEMS_LIST_KEY, JsonUtils.objectToJson(itemsList), CART_ITEMS_EXPIRE_TIME, true);
        return itemsList;
    }

}
