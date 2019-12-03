package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if(user == null){
            return ServerResponse.createByError("password is wrong");
        }

        user.setPassword(StringUtils.EMPTY);//set user password null for security,and return
        return ServerResponse.createBySuccess("login success",user);
    }

    @Override
    public ServerResponse<String> register(User user) {
//
//        int resultCount = userMapper.checkUsername(user.getUsername());
//        if(resultCount > 0){
//            return ServerResponse.createByError("this username already used");
//        }
//
//        resultCount = userMapper.checkEmail(user.getEmail());
//        if(resultCount > 0){
//            return ServerResponse.createByError("this email already used");
//        }

        ServerResponse<String> checkResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!checkResponse.isSuccess()){
            return checkResponse;
        }
        checkResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!checkResponse.isSuccess()){
            return checkResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        //turn password to MD5 code
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if(resultCount == 0){
            return ServerResponse.createByError("register failed");
        }
        return ServerResponse.createBySuccessMessage("register success");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if(StringUtils.isNotBlank(str)){
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if(resultCount == 0){
                    return ServerResponse.createByError("this username is invalid");
                }
            }
            if(Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if(resultCount == 0){
                    return ServerResponse.createByError("this email is invalid");
                }
            }
        }else{
            return ServerResponse.createByError("invalid blank input");
        }
        return ServerResponse.createBySuccessMessage("passed by checking");

    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse<String> checkResponse = this.checkValid(username,Const.USERNAME);
        if(!checkResponse.isSuccess()){
            return checkResponse;
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccessMessage(question);
        }
        return ServerResponse.createByError("question is blank");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if(resultCount > 0){
            //anwer right
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return  ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByError("anwer is wrong");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if(StringUtils.isNotBlank((forgetToken))){
            return ServerResponse.createByError("params wrong,need token");
        }
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            return ServerResponse.createByError("user is not exist");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByError("token invalid or past due");
        }
        if(StringUtils.equals(forgetToken,token)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,md5Password);
            if(rowCount > 0){
                return ServerResponse.createBySuccessMessage("update password successful");
            }
        }else {
            return ServerResponse.createByError("token is wrong , do reset again");
        }
        return ServerResponse.createByError("update false");

    }

    @Override
    public ServerResponse<String> resetPassword(User user, String passwordOld, String passwordNew) {
        int resultCount = userMapper.checkPassword(user.getId(),MD5Util.MD5EncodeUtf8(passwordOld));
        if(resultCount == 0){
            return ServerResponse.createByError("password wrong");
        }
        String md5PasswordNew = MD5Util.MD5EncodeUtf8(passwordNew);
        user.setPassword(md5PasswordNew);
        resultCount = userMapper.updateByPrimaryKeySelective(user);
        if(resultCount > 0){
            return ServerResponse.createBySuccessMessage("update successful");
        }
        return ServerResponse.createByError("update false");
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        int resultCount = userMapper.checkEmailByUserId(user.getId(),user.getEmail());
        if(resultCount > 0){
            return ServerResponse.createByError("email already exist,try another email");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0){
            return ServerResponse.createBySuccess("update successful",updateUser);
        }
        return ServerResponse.createByError("update false");
    }
}
