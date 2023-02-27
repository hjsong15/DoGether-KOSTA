
$(function() {
	$(".pwd-hide").hide();

	let error = $(".error").val();
	if (error != 0) {
		$(".email_error").css("display", "flex");
		$(".email_error").text(error);
	}
	//let token = $("meta[name='_csrf']").attr("content");
	//	let header = $("meta[name='_csrf_header']").attr("content");
	let pattern = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";

	$("#m_email").blur(function() {
		email_check();
	});

	$("#m_pwd").blur(function() {
		pwd_check();
	});

	$("#login").click(function() {
		let email = $("#m_email").val();
		let pwd = $("#m_pwd").val();
		if (email == null || email == "") {
			email_check()
		}
		else if (pwd == null || pwd == "") {
			pwd_check()
		}
		else {
			loginform.submit();
		}
	});


	$(".pwd-show").on("click", function() {
		$("#m_pwd").attr("type", "text");
		$(".pwd-show").hide();
		$(".pwd-hide").show();
	});

	$(".pwd-hide").on("click", function() {
		$("#m_pwd").attr("type", "password");
		$(".pwd-hide").hide();
		$(".pwd-show").show();
	});

	$("#m_pwd").on("keyup", function() {
		if ($(this).val() === "") {
			$(".pwd-show").hide();
			$(".pwd-hide").hide();
		} else {
			if ($("#m_pwd").attr("type") === "password") {
				$(".pwd-show").show();
				$(".pwd-hide").hide();
			} else {
				$(".pwd-show").hide();
				$(".pwd-hide").show();
			}
		}
	});


	function email_check() {
		let email = $("#m_email").val();//이메일 유효성 api
		if (email == null || email == "") {
			$(".email_error").css("display", "flex");
			$(".email_error").text("이메일을 입력해주세요");
		}
		else if (email.match(pattern) == null) {
			$(".email_error").css("display", "flex");
			$(".email_error").text("잘못된 형식의 이메일 주소입니다");
		}
		else {
			$(".email_error").css("display", "none");
		}
	}


	function pwd_check() {

		let pwd = $("#m_pwd").val();

		if (pwd == null || pwd == "") {
			$(".pwd_error").css("display", "flex");
			$(".pwd_error").css("color", "red");
			$(".pwd_error").text("비밀번호를 입력해 주세요");
		}
	}



});
