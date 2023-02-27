

$(function() {
	
	let token = $("meta[name='_csrf']").attr("content");
	let header = $("meta[name='_csrf_header']").attr("content");
	$(".pwd-hide").hide();
	$(".pwd-hide2").hide();
	
	$("#m_pwd").blur(function() {
		pwd_check();
	});

	$("#m_pwd2").blur(function() {
		pwd2_check();
	});

	$("#m_name").blur(function() {
		name_check();
	});


$("#m_nick").blur(function() {
		nick_check();
	});
	
	
$("#nick_check").click(function() {
		nick_check2();
	});	


$("#update_btn").click(function() {
 let pwd=$("#m_pwd").val();
 let pwd2=$("#m_pwd2").val();


	if (pwd != null&&pwd != "" && pwd2 != null && pwd2 != ""&& pwd==pwd2) {
		alert("정보가 변경되었습니다");
		update_form.submit();
		}else if(pwd == null || pwd == ""){
			alert("비밀번호를 입력해주세요")
		}else if(pwd2 == null || pwd2 == ""){
			alert("비밀번호 확인을 입력해주세요")

	}else {
	alert("비밀번호를 확인해 주세요")
	return false;
	}
	});	


$(".join_link").click(function(){
	const xuser= confirm("탈퇴하시겠습니까?");
	if(xuser){
		alert("탈퇴되었습니다")
		deleteform.submit();
	}else{
		alert("취소되었습니다")
	}

})


	
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

	function name_check() {
		let name = $("#m_name").val();

		if (name == null || name == "") {
			$(".name_error").css("display", "flex");
			$(".name_error").css("color", "red");
			$(".name_error").text("이름을 입력해주세요");
		}
		else if (name.match(name_pattern) == null) {
			$(".name_error").css("display", "flex");
			$(".name_error").css("color", "red");
			$(".name_error").text("특수문자,영어,숫자는 사용할수 없습니다. 한글만 입력하여주세요.");
		}
		else {
			$(".name_error").css("display", "flex");
			$(".name_error").css("color", "blue");
			$(".name_error").text("사용가능합니다");
		}
	}
	
	
	
	function nick_check() {
		let nick = $("#m_nickname").val();

		if (nick == null || nick == "") {
			$(".nick_error").css("display", "flex");
			$(".nick_error").css("color", "red");
			$(".nick_error").text("닉네임을 입력해주세요");
		}
		else {
			nick_check2();
		}
	}
	
	
	
	function nick_check2() {
		let nick = $("#m_nick").val();
		$.ajax({
			url: '/members/info/nick',
			type: 'post',
			data: {
				m_nickname: nick
			},
			beforeSend: function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			success: function(cnt2) {  
				if (cnt2 == 0 && nick != "") {
					$(".nick_error").css("display", "flex");
					$(".nick_error").text("사용 가능한 닉네임입니다");
					$(".nick_error").css("color", "blue");
				}
				else {
					$(".nick_error").css("display", "flex");
					$(".nick_error").text("이미 존재하는 닉네임입니다");
					$(".nick_error").css("color", "red");
				}
			},
			error: function() {
				console.log("닉네임 중복체크 ajax 실패")
				$(".nick_error").css("display", "flex");
				$(".nick_error").text("다시 시도해 주세요");
			}
		});
	}
	

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

});