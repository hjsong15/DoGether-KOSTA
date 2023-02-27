$(function() {

	const token = $("meta[name='_csrf']").attr("content");
	const header = $("meta[name='_csrf_header']").attr("content");
	let pattern = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
	let chk="1dm"
	$("#change_pwd").hide();
	$(".pwd-hide").hide();
	$(".pwd-hide2").hide();
	//const로 바꿔도 될 둣

	
	$("#m_email").blur(function() {
		let email = $("#m_email").val();

		if (email == null || email == "") {
			$(".email_error").css("display", "flex");
			$(".email_error").text("이메일을 입력해주세요");
		//	$("#code_send").prop("disabled", true);
		}
		else if (email.match(pattern) == null) {
			$(".email_error").css("display", "flex");
			$(".email_error").text("잘못된 형식의 이메일 주소입니다");
		//	$("#code_send").prop("disabled", true);
		}
		else {
			$(".email_error").css("display", "none");
		}
	})

	$("#code_send").click(function() {
		let email = $("#m_email").val();
		let name = $("#m_name").val();
		$.ajax({
			url: '/members/info',
			type: 'post',
			data: {
				m_email:email,
				m_name:name
			}, beforeSend: function(xhr) {
				xhr.setRequestHeader(header, token);
			},

			success: function(user) {
				
				//0 아이디 없음, 1 아이디c 있음
				console.log(user)
				
				if (user==name&& name!="") {
				codeSend();
					console.log("가입회원");
					$(".email").val(email);

				} else {
					$(".code_error").css("display", "flex");
					$(".code_error").text("일치하는 정보가 없습니다");
					console.log("일치하는 정보가 없습니다")
				}
			},
			error: function() {
				console.log("비밀번호 찾기 메일전송 ajax 실패")
				$(".email_error").css("display", "flex");
				$(".email_error").text("다시 시도해주세요");
			}
		});
	});




	$("#code_check").click (function() {

		$(".code_error").css("display", "flex");
		let num = $("#m_code").val();
	//	let email=$("#m_email").val();

		if (num == chk) {
			$(".email_error").css("display", "none");
			$(".code_error").text("인증되었습니다");
			$(".code_error").css("color", "blue");
			 $("#m_email").attr("readonly",true)
 $("#m_name").attr("readonly",true)

			$("#codechk").hide();
			$("#code_check").hide();
			//$("#email").text(email);
			$("#change_pwd").show();
		} else if (num == "" || num == null) {
			$(".code_error").text("인증번호를 입력해주세요");
		}
		else {
			$(".code_error").text("번호가 일치하지 않습니다");
		}



	});
	
		$("#m_pwd").blur(function() {
		pwd_check();
	});

	$("#m_pwd2").blur(function() {
		pwd2_check();
	});
	
	$("#findPwd").click(function(){
		let pwd = $("#m_pwd").val();
		let pwd2 = $("#m_pwd2").val();
	 if (pwd == null || pwd == "") {
			pwd_check();
		} else if (pwd2 == null || pwd2 == "") {
			pwd2_check();
 	 }else{
		 	findPwd();
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

	$(".pwd-show2").on("click", function() {
		$("#m_pwd2").attr("type", "text");
		$(".pwd-show2").hide();
		$(".pwd-hide2").show();
	});

	$(".pwd-hide2").on("click", function() {
		$("#m_pwd2").attr("type", "password");
		$(".pwd-hide2").hide();
		$(".pwd-show2").show();
	});

	$("#m_pwd2").on("keyup", function() {
		if ($(this).val() === "") {
			$(".pwd-show").hide();
			$(".pwd-hide").hide();
		} else {
			if ($("#m_pwd2").attr("type") === "password") {
				$(".pwd-show2").show();
				$(".pwd-hide2").hide();
			} else {
				$(".pwd-show2").hide();
				$(".pwd-hide2").show();
			}
		}
	});
 function findPwd(){
	findpwd.submit();
  }


function codeSend(){
let email = $("#m_email").val();
		$.ajax({
			url: "/members/info/send",
			type: "POST",
			data: {
				m_email: email
			},
			beforeSend: function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			success: function(data) {
		console.log("codeSend")
				$("#code_send").prop("disabled", true);
			$(".code_error").css("display", "none");
				$(".email_error").css("display", "flex");
					$(".email_error").css("color", "green");
			$(".email_error").text("인증번호가 전송되었습니다");
		$('#code_check').show();
				chk = data;

			
			},
			error: function() {
				$(".code_error").css("display", "flex");
				$(".email_error").text("일시적인 전송에러가 발생하였습니다. 다시 시도해 주세요");
}
			})
		}
		
		
		function pwd_check() {

		let pwd = $("#m_pwd").val();
		let pwd_pattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/;
		let pwd_pattern2 = /(\w)\1\1/;
		let email = $("#m_email").val();

		if (pwd == null || pwd == "") {
			$(".pwd_error").css("display", "flex");
			$(".pwd_error").css("color", "red");
			$(".pwd_error").text("비밀번호를 입력해 주세요");
		} else if (email == pwd) {
			$(".pwd2_error").css("display", "flex");
			$(".pwd_error").text("비밀번호는 아이디와 같을 수 없습니다.");
		}
		else if (pwd.match(pwd_pattern2)) {
			$(".pwd_error").css("display", "flex");
			$(".pwd_error").css("color", "red");
			$(".pwd_error").text("3개 이상 연속된 문자/숫자는 사용이 불가합니다");
		}
		else if (pwd.match(pwd_pattern)) {
			$(".pwd_error").css("display", "flex");
			$(".pwd_error").text("사용가능한 비밀번호입니다.");
			$(".pwd_error").css("color", "green");
		}
		else if (pwd.match(pwd_pattern) == null) {
			$(".pwd_error").css("display", "flex");
			$(".pwd_error").text("영어/숫자/특수문자를 포함하고, 6자리 이상이어야 합니다.");
		}

	}

	function pwd2_check() {
		let pwd = $("#m_pwd").val();
		let pwd2 = $("#m_pwd2").val();

		if (pwd2 == null || pwd2 == "") {
			$(".pwd2_error").css("display", "flex");
			$(".pwd2_error").css("color", "red");
			$(".pwd2_error").text("비밀번호 확인을 위해 다시 입력해 주세요")
			//		$("#m_pwd").prop("disabled", false);
			//$("#m_pwd").attr("readonly",true); 
		}
		else if (pwd === pwd2) {
			$(".pwd2_error").css("display", "flex");
			$(".pwd2_error").css("color", "blue");
			$(".pwd2_error").text("비밀번호가 일치합니다");
			//	$("#m_pwd").prop("disabled", true);
		}
		else {
			$(".pwd2_error").css("display", "flex");
			$(".pwd_error").css("color", "red");
			$(".pwd2_error").text("비밀번호가 일치하지않습니다.");
			$("#m_pwd").prop("disabled", false);
		}
	}

});

