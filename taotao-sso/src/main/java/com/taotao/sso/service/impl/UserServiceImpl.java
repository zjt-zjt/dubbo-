package com.taotao.sso.service.impl;

import com.taotao.common.*;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    /**
     * Redis中存储用户Token Key的统一前缀
     */
    @Value("user_token")
    private String userTokenRedisKeyPrefix;

    @Autowired(required = false)
    private TbUserMapper tbUserMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     *    注册资料检查
     *      @param param
     *      @param type
     *      @return
     **/

    @Override
    public TaotaoResult checkData(String param, Integer type) {
        TaotaoResult taotaoResult = null;
        boolean result = false;
        switch (type) {
            case SystemConstants.TAOTAO_REGISTER_CHECK_TYPE_USERNAME:
                result = checkUserName(param);
                break;
            case SystemConstants.TAOTAO_REGISTER_CHECK_TYPE_PHONE:
                result = checkPhone(param);
                break;
            case SystemConstants.TAOTAO_REGISTER_CHECK_TYPE_EMAIL:
                result = checkEmail(param);
                break;
            default:
                taotaoResult = TaotaoResult.error("不支持的数据检查类型： " + type);
                break;
        }
        if(taotaoResult == null) {
            if(result) {
                taotaoResult = TaotaoResult.ok(result);
            } else {
                taotaoResult = TaotaoResult.error("数据已存在");
            }
        }
        return taotaoResult;
    }

    private boolean checkEmail(String param) {
        TbUserExample condition = new TbUserExample();
        condition.createCriteria().andEmailEqualTo(param);
        List<TbUser> userList = tbUserMapper.selectByExample(condition);

        // 如果查出来数据，说明邮箱已被其他用户绑定， 需要返回false，告诉前台不能用此邮箱
        return !(userList != null && userList.size() > 0);
    }

    private boolean checkPhone(String param) {
        TbUserExample condition = new TbUserExample();
        condition.createCriteria().andPhoneEqualTo(param);
        List<TbUser> userList = tbUserMapper.selectByExample(condition);

        // 如果查出来数据，说明手机已被其他用户绑定， 需要返回false，告诉前台不能用此数据
        return !(userList != null && userList.size() > 0);
    }


    private boolean checkUserName(String param) {
        TbUserExample condition = new TbUserExample();
        condition.createCriteria().andUsernameEqualTo(param);
        List<TbUser> userList = tbUserMapper.selectByExample(condition);

        // 如果查出来数据，说明用户名已被其他用户使用， 需要返回false，告诉前台不能用此数据
        return !(userList != null && userList.size() > 0);
    }

    private String getTokenRedisKey(String token) {
        return userTokenRedisKeyPrefix + "-" + token;
    }

    /**
     * 用户注册
     * @param tbUser
     * @return
     */
    @Override
    public TaotaoResult register(TbUser tbUser) {
        if(tbUser == null) {
            return TaotaoResult.error("数据不能为空");
        }
        try {
            // 完善用户信息

            // 加密密码
            String md5Pwd = DigestUtils.md5Hex(tbUser.getPassword().getBytes());
            tbUser.setPassword(md5Pwd);

            // 完善日期字段
            Date date = new Date();
            tbUser.setCreated(date);
            tbUser.setUpdated(date);

            // 保存到数据库
            tbUserMapper.insert(tbUser);
            return TaotaoResult.ok();
        } catch (Exception e) {
            log.error("注册用户失败", e);
            return TaotaoResult.error("保存用户失败");
        }
    }


    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public TaotaoResult login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return TaotaoResult.error("用户名或密码不能为空!");
        }
        try {
            TbUserExample condition = new TbUserExample();
            condition.createCriteria().andUsernameEqualTo(username);

            List<TbUser> userList = tbUserMapper.selectByExample(condition);
            if(userList == null || userList.size() < 1) {
                return TaotaoResult.error("用户名或密码错误!");
            }
            TbUser userInfo = userList.get(0);

            String md5Password = DigestUtils.md5Hex(password);
            if(!userInfo.getPassword().equals(md5Password)) {
                return TaotaoResult.error("用户名或密码错误!");
            }
            // 生成用户Token(也可以使用JWT工具生成token，安全性更高)
            String token = IDUtils.genImageNameByUUID();
            // 生成Redis Key
            String redisKey = getTokenRedisKey(token);
            // 存储到Redis之前，将用户密码清除。(可以降低安全风险)
            userInfo.setPassword(null);
            // 将用户信息存储到Redis
            redisTemplate.opsForValue().set(redisKey, JsonUtils.objectToJson(userInfo));
            // 设置Token过期时间
            redisTemplate.expire(redisKey, 30, TimeUnit.MINUTES);

            // 将Token写入Cookie
            CookieUtils.setCookie(request, response, "TT_TOKEN", token, (int)TimeUnit.MINUTES.toSeconds(30));

            return TaotaoResult.ok(token);
        } catch (Exception e) {
            log.error("登录失败", e);
            return TaotaoResult.error("登录失败");

        }
    }

/**
 * 按照token获取用户登录信息
 * @param token
 * @return
 */

@Override
public TaotaoResult getUserByToken(String token) {
    if(StringUtils.isBlank(token)) {
        return TaotaoResult.error("token不能为空！");
    }
    String redisKey = getTokenRedisKey(token);
    TbUser tbUser = null;
    try {
        String userJson = redisTemplate.opsForValue().get(redisKey);
        if(!(userJson==null)){
            tbUser = JsonUtils.jsonToPojo(userJson, TbUser.class);
        }else {
            TaotaoResult.error("失败");
        }

    } catch (Exception e) {
        log.error("获取用户信息失败, token: " + token, e);
    }
    if(tbUser == null) {
        return TaotaoResult.error("获取用户信息失败");
    }
    return TaotaoResult.ok(tbUser);
}

    @Override
    public TaotaoResult logout(String token ,HttpServletRequest req, HttpServletResponse resp) {
        redisTemplate.delete(getTokenRedisKey(token));
        System.out.println(token);
        //redisTemplate.expire(getTokenRedisKey(token), 0, TimeUnit.SECONDS);
        CookieUtils.deleteCookie(req,resp,"TT_TOKEN");
     //   CookieUtils.setCookie(req, resp, "TT_TOKEN", "", (int)TimeUnit.MINUTES.toSeconds(0));
//        Cookie[] cookies = req.getCookies();
//        for(Cookie cookie:cookies){
//            //System.out.println(cookie.getName());
//            if(cookie.getName().equals("TT_TOKEN")){
//                cookie.setMaxAge(0);
//                cookie.setPath("/");
//                resp.addCookie(cookie);
//
//            }

        return TaotaoResult.ok();
    }


}
