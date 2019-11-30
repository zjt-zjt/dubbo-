package com.taotao.sso.web;

import com.taotao.common.ExceptionUtil;
import com.taotao.common.JsonUtils;
import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@RestController
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 注册 信息检查
     *
     * @param param
     * @param type
     * @return
     */
    @RequestMapping("/check/{param}/{type}")
    @ResponseBody
    public TaotaoResult checkData(@PathVariable String param, @PathVariable Integer type) {
        TaotaoResult taotaoResult = userService.checkData(param, type);
        return taotaoResult;
    }

    /**
     * 用户注册
     *
     * @param tbUser
     * @return
     **/
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult registerUser(TbUser tbUser) {
        TaotaoResult taotaoResult = userService.register(tbUser);
        return taotaoResult;
    }


    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult registerUser(String username, String password, HttpServletRequest req, HttpServletResponse resp) {
        TaotaoResult taotaoResult = userService.login(username, password, req, resp);
        return taotaoResult;
    }

    /**
     * 按照Token获取user登录信息
     *
     * @param token
     * @param callback
     * @return
     */
    @CrossOrigin(origins = {"localhost", "127.0.0.1", "www.taotao.com"})
    @RequestMapping(value = "/token/{token}", method = RequestMethod.GET)
    @ResponseBody
    public Object getUserByToken(@PathVariable String token, @RequestParam(required = false) String callback) {
        TaotaoResult taotaoResult = userService.getUserByToken(token);
        if (!StringUtils.isBlank(callback)) {
            return callback + "(" + JsonUtils.objectToJson(taotaoResult) + ");";
        }
        return taotaoResult;
    }
//    @ResponseBody
//    @RequestMapping(value = "/logout/{token}")
//    @CrossOrigin
//    public TaotaoResult userLogout(@PathVariable String token, HttpServletRequest req, HttpServletResponse resp) {
//       TaotaoResult result = userService.logout(token,req, resp);
////                Cookie[] cookies = req.getCookies();
////                for(Cookie cookie:cookies){
////                    //System.out.println(cookie.getName());
////                    if(cookie.getName().equals("TT_TOKEN")){
////                        cookie.setMaxAge(0);
////                        cookie.setPath("/");
////                        resp.addCookie(cookie);
////
////                    }
//
//        return result;
//
//    }
//

    @ResponseBody
    @RequestMapping(value = "/logout/{token}")
    @CrossOrigin
    public TaotaoResult userLogout(@PathVariable String token, HttpServletRequest req, HttpServletResponse resp) {
        TaotaoResult result = userService.logout(token,req, resp);
//                Cookie[] cookies = req.getCookies();
//                for(Cookie cookie:cookies){
//                    //System.out.println(cookie.getName());
//                    if(cookie.getName().equals("TT_TOKEN")){
//                        cookie.setMaxAge(0);
//                        cookie.setPath("/");
//                        resp.addCookie(cookie);
//
//                    }

        return result;

    }

}