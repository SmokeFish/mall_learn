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
            return ServerResponse.createByError("用户不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if(user == null){
            return ServerResponse.createByError("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);//set user password null for security,and return
        return ServerResponse.createBySuccess("登陆成功",user);
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
            return ServerResponse.createByError("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if(StringUtils.isNotBlank(str)){
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if(resultCount == 0){
                    return ServerResponse.createByError("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if(resultCount == 0){
                    return ServerResponse.createByError("邮箱地址已存在");
                }
            }
        }else{
            return ServerResponse.createByError("输入为空");
        }
        return ServerResponse.createBySuccessMessage("检测通过");

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
        return ServerResponse.createByError("问题为空");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if(resultCount > 0){
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return  ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByError("答案不正确");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if(StringUtils.isNotBlank((forgetToken))){
            return ServerResponse.createByError("参数错误");
        }
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            return ServerResponse.createByError("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByError("token 不存在或者已经失效");
        }
        if(StringUtils.equals(forgetToken,token)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,md5Password);
            if(rowCount > 0){
                return ServerResponse.createBySuccessMessage("密码更新成功");
            }
        }else {
            return ServerResponse.createByError("token 不正确或者已失效,请重试");
        }
        return ServerResponse.createByError("更新失败");

    }

    @Override
    public ServerResponse<String> resetPassword(User user, String passwordOld, String passwordNew) {
        int resultCount = userMapper.checkPassword(user.getId(),MD5Util.MD5EncodeUtf8(passwordOld));
        if(resultCount == 0){
            return ServerResponse.createByError("密码错误");
        }
        String md5PasswordNew = MD5Util.MD5EncodeUtf8(passwordNew);
        user.setPassword(md5PasswordNew);
        resultCount = userMapper.updateByPrimaryKeySelective(user);
        if(resultCount > 0){
            return ServerResponse.createBySuccessMessage("更新成功");
        }
        return ServerResponse.createByError("更新失败");
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        int resultCount = userMapper.checkEmailByUserId(user.getId(),user.getEmail());
        if(resultCount > 0){
            return ServerResponse.createByError("邮箱已存在,请更换邮箱地址");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0){
            return ServerResponse.createBySuccess("更新成功",updateUser);
        }
        return ServerResponse.createByError("更新失败");
    }

    @Override
    public ServerResponse<User> getInformation(int userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByError("找不到该用户");
        }
        user.setPassword(StringUtils.EMPTY);
        user.setAnswer(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }
}
