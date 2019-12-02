package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by think9 on 19-12-1.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {

        int resultCount = userMapper.checkUsername(username);
        if(resultCount == 0 ){
            return ServerResponse.createByError("there is no this user");
        }

        //todo password in md5

        User user = userMapper.selectLogin(username, password);
        if(user == null){
            return ServerResponse.createByError("password is wrong");
        }

        user.setPassword(StringUtils.EMPTY);//set user password null for security,and return
        return ServerResponse.createBySuccess("login success",user);
    }
}
