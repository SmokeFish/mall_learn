package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by think9 on 19-12-1.
 */
public interface IUserService {

    ServerResponse<User> login(String username , String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str ,String type);

    ServerResponse<String> forgetResetPassword(String username ,String passwordNew,String forgetToken);

    ServerResponse<String> selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username,String question,String answer);

    ServerResponse<String> resetPassword(User user, String passwordOld ,String passwordNew);

    ServerResponse<User> updateInformation(User user);

    ServerResponse<User> getInformation(int userId);
}
