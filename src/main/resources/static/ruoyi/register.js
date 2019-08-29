$(function() {
    validateRule();
	$('.imgcode').click(function() {
		var url = ctx + "captcha/captchaImage?type=" + captchaType + "&s=" + Math.random();
		$(".imgcode").attr("src", url);
	});
});

$.validator.setDefaults({
    submitHandler: function() {
		register();
    }
});

function register() {
	$.modal.loading($("#btnSubmit").data("loading"));
	var userName = $.common.trim($("input[name='userName']").val());
    var password = $.common.trim($("input[name='password']").val());
    var confirmPassword = $.common.trim($("input[name='confirmPassword']").val());
    var mobileNo = $.common.trim($("input[name='mobileNo']").val());
    var email = $.common.trim($("input[name='email']").val());
    var validateCode = $("input[name='validateCode']").val();
    
    if (!(/^[a-zA-Z]\w{4,16}$/.test(userName))){
    	$.modal.closeLoading();
		$('.imgcode').click();
		$(".code").val("");
		$.modal.msg("用户名由以字母开头的4~16位字母和数字组成");
		return false;
    }else if (!(/^[\d_a-zA-Z]{6,12}$/.test(password))) {
    	$.modal.closeLoading();
		$('.imgcode').click();
		$(".code").val("");
		$.modal.msg("密码格式不正确，6~12位字母、数字和下划线");
	    return false;
    }else if (!password == confirmPassword){
    	$.modal.closeLoading();
		$('.imgcode').click();
		$(".code").val("");
		$.modal.msg("重复密码不正确");
	    return false; 
    } else if(!(/^1[3|5|8][0-9]\d{4,8}$/.test(mobileNo))){ 
    	$.modal.closeLoading();
		$('.imgcode').click();
		$(".code").val("");
		$.modal.msg("手机号码不正确");
	    return false; 
	 } else if(!(/^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/.test(email))){ 
    	$.modal.closeLoading();
		$('.imgcode').click();
		$(".code").val("");
		$.modal.msg("邮箱不正确");
	    return false; 
	 }
   
    $.ajax({
        type: 'POST',
        url: ctx + 'register',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify({
            'userName': userName,
            'password': md5(password),
            'mobileNo': mobileNo,
            'email': email,
            'validateCode': validateCode
        }),
        success: function(r) {
            if (r.code == 0) {
                location.href = ctx + 'register_success';
            } else {
            	$.modal.closeLoading();
            	$('.imgcode').click();
            	$(".code").val(r.code);
            	$.modal.msg(r.msg);
            }
        }
    });
}

function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
        	userName: {
                required: true
            },
            password: {
                required: true
            },
            confirmPassword: {
            	required: true
            },
            mobileNo: {
            	required: true
            },
            email: {
            	required: true
            },
            validateCode: {
            	required: true
            }
        },
        messages: {
        	userName: {
                required: icon + "请输入您的用户名~",
            },
            password: {
                required: icon + "请输入您的密码~",
            },
            confirmPassword: {
                required: icon + "请输入重复密码~",
            },
            mobileNo: {
                required: icon + "请输入您的手机号~",
            },
            email: {
                required: icon + "请输入您的邮箱~",
            },
            validateCode: {
                required: icon + "请输入验证码~",
            }
        }
    })
}
