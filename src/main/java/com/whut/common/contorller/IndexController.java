package com.whut.common.contorller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

/**
 * 主要页面映射
 * @author yy
 * @time 2018-03-26
 */
@Controller
public class IndexController {

    // 登陆页面不拦截
    @RequestMapping("/login")
    public String login(HttpServletRequest request) {
        return "login";
    }
    // 注册页面不拦截
    @RequestMapping("/register")
    public String register(HttpServletRequest request) {
        return "register";
    }
    // 退出不拦截
    @RequestMapping("/quit")
    public String loginOut(HttpServletRequest request) {
        request.getSession().invalidate();
        return "login";
    }
    // 主页面拦截
    @RequestMapping("/main")
    public String main(HttpServletRequest request) {
        return request.getSession().getAttribute("user") == null ? "login" : "main";
    }
}
