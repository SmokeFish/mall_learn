package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by think9 on 19-12-1.
 */
public interface IUserService {

   ServerResponse<User> login(String username , String password);
}
