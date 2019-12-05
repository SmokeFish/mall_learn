package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Hasee on 2019/12/4.
 */
@Controller
@RequestMapping("/manage/user")
public class UserManagerController {

    @Autowired
    private IUserService iUserService;

    /**
     * 后台管理用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    //set map key and method
    @ResponseBody
    //declare response body
    public ServerResponse<User> login(java.lang.String username , java.lang.String password , HttpSession session){
        ServerResponse<User> response = iUserService.login(username,password);
        if(response.isSuccess()){
            if(response.getData().getRole().equals(Const.Role.ROLE_ADMIN)) {
                session.setAttribute(Const.CURRENT_USER, response.getData());
            } else {
                response = ServerResponse.createByError("用户权限不足");
            }
        }
        return response;
    }

}
