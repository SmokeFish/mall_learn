
	/*
		表单校验：
			1.用户名：单词字符，长度8到20位
			2.密码：单词字符，长度8到20位
			3.email：邮件格式
			4.姓名：非空
			5.手机号：手机号格式
			6.出生日期：非空
			7.验证码：非空
	 */

	//校验用户名
	//单词字符，长度8到20位
	function checkUsername() {
        //1.获取用户名值
		var username = $("#username").val();
		//2.定义正则
		var reg_username = /^\w{4,16}$/;
		closeError($("#username"));
		//3.判断，给出提示信息
	    var flag = reg_username.test(username);
	    if(flag || username==""){
	        //用户名合法
            $("#username").css("border","");
			//
			//console.log($("#username").val()+""+$("#username").attr("name"));
			if(username!="") checkValid($("#username"));
		}else{
	        //用户名非法,加一个红色边框
			$("#username").css("border-bottom","1px solid red");
			//动态弹出提示框
			errorMessage($("#username"),"用户名必须在4~16位之间");
		}    
        return flag;
    }

    //校验密码
    function checkPassword() {
        //1.获取密码值
        var password = $("#password").val();
        //2.定义正则
        var reg_password = /^\w{8,20}$/;

        //3.判断，给出提示信息
        var flag = reg_password.test(password);
        if(flag || password==""){
            //密码合法
            $("#password").css("border","");
            closeError($("#password"));
        }else{
            //密码非法,加一个红色边框
            $("#password").css("border-bottom","1px solid red");
            errorMessage($("#password"),"密码格式应为8~20位,区分大小写");
        }
        return flag;
    }

    //校验邮箱
	function checkEmail(){
	    //1.获取邮箱
		var email = $("#email").val();
		//2.定义正则		lzm1069@163.com
		var reg_email = /^\w+@\w+\.\w+$/;
		//3.判断
		var flag = reg_email.test(email);
		//
		closeError($("#email"));
		if(flag || email==""){
            $("#email").css("border","");
			//closeError($("#email"));
			if(email!="")checkValid($("#email"));
		}else{
            $("#email").css("border-bottom","1px solid red");
            errorMessage($("#email"),"请输入正确的邮箱地址");
		}
		return flag;
	}
    
	//校验电话号码
	function checkTelephone(){
	    //1.获取邮箱
		var telephone = $("#phone").val();
		//2.定义正则		lzm1069@163.com
		var reg_telephone = /^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/;

		//3.判断
		var flag = reg_telephone.test(telephone);
		if(flag || telephone == ""){
            $("#phone").css("border","");
            closeError($("#phone"));
		}else{
            $("#phone").css("border-bottom","1px solid red");
            errorMessage($("#phone"),"请输入正确的电话号码");
		}
		return flag;
	}
	
	function checkRepassword(){
		var flag = $("#password").val() == $("#re-password").val();
		if(flag){
            $("#re-password").css("border","");
            closeError($("#re-password"));
		}else{
            $("#re-password").css("border-bottom","1px solid red");
            errorMessage($("#re-password"),"两次输入密码不一致");
		}
		return flag;
	}
	
	function errorMessage($input,msg){
		if($input.next().length<=0){
			$div = $("<div></div>");
			$div.append(msg);
			$div.addClass("errored");
			$parent = $input.parent();
	//		console.log($parent);
			$parent.append($div);
		}
	}
	function closeError($input){
		if($input.next().length > 0)
			$input.next("div").remove();
	}

	function checkValid($input){
		//console.log($input.val()+""+$input.attr("name"));
		$.ajax({
			url:"/user/check_valid.do",
			data:{str:$input.val(),type:$input.attr("name")},
			type:"get",
			success:function(data){
				if(data.status){
					$input.css("border-bottom","1px solid red");
					errorMessage($input,data.msg);
				}else{
					closeError($input)
				}
			}
		});
	}

	$(function () {
        //当表单提交时，调用所有的校验方法
		$("#alert_span").hide();
		/*
		 *  提交
		 */
		$("#submit_form").on("click",function() {
			if (checkUsername() && checkPassword() && checkEmail() && checkRepassword() && checkTelephone()) {
				$.ajax({
					url: "/user/register.do",
					data: $("#register_form").serializeArray(),
					type: "post",
					success: function(data) {
						var msg = data.msg,
							status = data.status;
						if (status == 0) {
							alert("注册成功");
							window.location.href = "/html/login.html";
						} else {
							alert("注册失败");
						}
						$("#alert_span").html(msg).show();
					}
				})
			}
		})

        //当某一个组件失去焦点是，调用对应的校验方法
		$("#username").blur(checkUsername);
        $("#password").blur(checkPassword);
        $("#email").blur(checkEmail);
        $("#phone").blur(checkTelephone);
 		$("#re-password").blur(checkRepassword);

    });