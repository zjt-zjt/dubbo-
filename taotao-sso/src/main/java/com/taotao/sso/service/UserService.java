package com.taotao.sso.service;

import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {
    /**
     *    注册资料检查
     *      @param param
     *      @param type
     *      @return
     **/
    TaotaoResult checkData(String param, Integer type);
    /**
     * 用户注册
     * @param tbUser
     * @return
     */
    TaotaoResult register(TbUser tbUser);

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    TaotaoResult login(String username, String password, HttpServletRequest request, HttpServletResponse response);

    /**
    * 按照token获取用户登录信息
    * @param token
    * @return
     */
    TaotaoResult getUserByToken(String token);
   //安全退出
    TaotaoResult logout(String token,HttpServletRequest req, HttpServletResponse resp);
}
