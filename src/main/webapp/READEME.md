UserCotroller Interface name
/user/
login.do post 登录接口 (username , password)
logout.do get 注销接口 (session)
register.do post 注册接口 (UserBean)
check_valid.do get 检测接口 (str,type)
get_user_info.do get 获取用户信息接口 (session)
forget_get_question.do get 忘记密码问题 (username)
forget_check_answer.do post (String username,String question,String answer)
forget_reset_password.do post (String username,String passwordNew,String forgetToken)
reset_password.do get 重置密码 (session,passwordOld,passwordNew)
update_information.do get 更新用户信息 (session,UserBean)
get_information get 获取用户详细信息 (session)
/manage/user/
login.do post 管理员登录 (username,password)
/manage/category/
add_category.do 新增分类
set_category_name.do
get_category.do
get_deep_category.do
/manage/product/
detail.do
set_sale_status.do
save.do
list.do
search.do
upload.do
richtext_imt_upload.do
/product/
detail.do
list.do
/cart/
add.do
update.do
delete_product.do
list.do
select_all.do
un_select_all.do
select.do
un_select.do
get_cart_product_count.do

