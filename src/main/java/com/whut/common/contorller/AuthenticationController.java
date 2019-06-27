package com.whut.common.contorller;


import com.alibaba.fastjson.JSONObject;
import com.whut.common.entity.User;
import com.whut.common.service.EmailService;
import com.whut.common.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * 登陆
 * @author yy
 * @time 2018-03-26
 */
@Controller
@RequestMapping("/authentication")
public class AuthenticationController {

    private final UserService userService;

    private final
    EmailService emailService;

    @Autowired
    public AuthenticationController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public JSONObject login(HttpServletRequest request)
    {
        JSONObject object = new JSONObject();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //根据用户名或者email或者phone查找用户
        User user = userService.findByUsername(username);
        if(user==null) {
            object.put("success",false);
            object.put("message","该用户不存在");
            return object;
        }
        if(!user.getPassword().equals(password)) {
            object.put("success",false);
            object.put("message","密码错误");
            return object;
        }
        if(!user.isEnabled()) {
            object.put("success",false);
            object.put("message","请先激活邮箱");
            return object;
        }
        request.getSession().setAttribute("user",user);
        object.put("success",true);
        object.put("message","登录成功");
        return object;
    }

    @PostMapping(value = "/register")
    @ResponseBody
    public JSONObject register(HttpServletRequest request){
        JSONObject object = new JSONObject();

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String code = UUID.randomUUID().toString();
        User user = userService.findByUsername(username);
        if (user!=null) {
            object.put("success",false);
            object.put("message","该用户已注册");
            return object;
        }
        user=userService.findByEmail(email);
        if (user!=null) {
            object.put("success",false);
            object.put("message","该邮箱已注册");
            return object;
        }
        //保存到数据库
        userService.addUser(new User(username,password,email,false,code));
        //查看是否注册成功
        user = userService.findByUsername(username);
        if (user == null){
            object.put("success",false);
            object.put("message","服务器内部错误");
            return object;
        }
        //注册成功后，发送账户激活链接
        String codeUrl = String.format("http://localhost:10086/authentication/activate?id=%d&code=%s",
                user.getId(), user.getCode());
        String text = String.format("%s先生/女士您好，请点击此链接激活账号:%s", username, codeUrl);
        Map<String,String> dataMap = new HashMap<>();
        dataMap.put("codeUrl",codeUrl);
        dataMap.put("text",text);
        boolean success = emailService.sendHtmlEmail(
                user.getEmail(),
                "激活邮件",
                "emailTemplate/activate",
                dataMap);
        //如果发送失败,删除用户
        if(!success)
        {
            userService.deleteUser(username);
            object.put("success",false);
            object.put("message","邮件发送失败，请确认邮箱地址是否正确");
            return object;
        }
        object.put("success",true);
        object.put("message","激活邮件已发送到您的邮箱,请前往激活");
        return object;
    }

    @GetMapping("/activate")
    public String activate(HttpServletRequest request) {

        Integer id = Integer.valueOf(request.getParameter("id"));
        String code = request.getParameter("code");
        //根据用户id查找用户
        User user = userService.findById(id);
        //验证无误，状态更改为1，即激活
        if (user!=null && user.getCode().equals(code)) {
            //修改状态
            user.setEnabled(true);
            userService.updateUser(user.getUsername(), user);
            return "emailTemplate/activateSuccess";
        }
        return "emailTemplate/activateFailed";
    }

    @PostMapping(value = "/username")
    @ResponseBody
    public JSONObject username(HttpServletRequest request)
    {
        JSONObject object = new JSONObject();
        User user = (User) request.getSession().getAttribute("user");
        if(user==null) {
            object.put("success",false);
            object.put("message","登录超时，请重新登录");
            return object;
        }
        object.put("success",true);
        object.put("message",user.getUsername());
        return object;
    }
}
