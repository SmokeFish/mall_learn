UserCotroller Interface name
/user/
login.do post 注册接口 (username , password)
logout.do get 注销接口 (session)
register.do post 注册接口 (UserBean)
check_valid.do get 检测接口 (str,type)
get_user_info.do get 获取用户信息接口 (session)
forget_get_question.do get 忘记密码问题 (username,passwordNew,forgetToken)
reset_password.do get 重置密码 (session,passwordOld,passwordNew)
update_information.do get 更新用户信息 (session,UserBean)