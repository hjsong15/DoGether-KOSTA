$(function() {

	let token = $("meta[name='_csrf']").attr("content");
	let header = $("meta[name='_csrf_header']").attr("content");
	let chk = "alsrbgurwn";

	//$("#join_btn").prop("disabled", true);

	//이름 정규식 (한글만)
	const name_pattern = "^[가-힣]{2,15}$";
	$(".pwd-hide").hide();
	$(".pwd-hide2").hide();
	$("#code_check").hide();

	$("#m_email").blur(function() {
		email_check();
	});


	$("#code_send").click(function() {
		let email = $("#m_email").val();

		if (email == null || email == "") {
			code_check
		}
		else {
			code_send();
		}
	});

	$("#code_check").click(function() {
		code_check();
	});

	$("#m_pwd").keyup(function() {
		pwd_check();
	});

	$("#m_pwd").blur(function() {
		let pwd = $("#m_pwd").val();
		let pwd2 = $("#m_pwd2").val();

		let error = $(".pwd2_error").text("비밀번호가 일치합니다");

		if (error && (pwd != pwd2)) {

			$(".pwd2_error").text("비밀번호가 일치하지 않습니다")
			$(".pwd2_error").css("color", "red");
		}
		else {
			pwd_check();
		}
	});

	$("#m_pwd2").keyup(function() {
		pwd2_check();
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
	$("#m_name").keyup(function() {
		name_check();
	});

	$("#join_btn").click(function() {
		let email = $("#m_email").val();
		let pwd = $("#m_pwd").val();
		let pwd2 = $("#m_pwd2").val();
		let name = $("#m_name").val();

		if (email == null || email == "") {
			email_check();
		} else if (pwd == null || pwd == "") {
			pwd_check();
		} else if (pwd2 == null || pwd2 == "") {
			pwd2_check();

		} else if (name == null || name == "") {
			name_check();
		}

		else if (!$("#join_y").is(":checked")) {
			alert("이용약관에 동의하셔야 가입이 가능합니다.")
		}
		else {
			alert("가입이 완료되었습니다. 로그인해주세요");
			joinForm.submit();
		}
	});
	

	//이메일 공백,형식 확인
	function email_check() {
		//이메일 정규식 (@ . 포함)
		const mail_pattern = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
		let email = $("#m_email").val();

		if (email == null || email == "") {
			$(".email_error").css("display", "flex");
			$(".email_error").text("이메일을 입력해 주세요");
			$("#code_send").prop("disabled", true);
		}
		else if (email.match(mail_pattern) == null) {
			$(".email_error").css("display", "flex");
			$(".email_error").text("잘못된 형식의 이메일 주소입니다");
			$("#code_send").prop("disabled", true);
		}
		else {
			email_check2();
		}
	}
	//이메일 중복체크 후 인증번호발송 활성화
	function email_check2() {
		let email = $("#m_email").val();
		$.ajax({
			url: '/members/info/email',
			type: 'post',
			data: {
				m_email: email
			},
			beforeSend: function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			success: function(cnt) {  //0 아이디 없음, 1 아이디 있음
				if (cnt == 0 && email != "") {
					$(".email_error").css("display", "flex");
					$(".email_error").text("사용 가능한 이메일입니다. 이메일 인증을 해주세요");
					$(".email_error").css("color", "green");
					$("#code_send").prop("disabled", false);
				}
				else {
					$(".email_error").css("display", "flex");
					$(".email_error").text("이미 존재하는 이메일입니다");
					$(".email_error").css("color", "red");
					$("#code_send").prop("disabled", true);
				}
			},
			error: function() {
				console.log("이메일 중복체크 ajax 실패")
				$(".email_error").css("display", "flex");
				$(".email_error").text("다시 시도해 주세요");
			}
		});
	}
	//인증번호 발송 후 타이머 시작
	function code_send() {
		let email = $("#m_email").val();

		if ($(".email_error").text("사용 가능한 이메일입니다. 이메일 인증을 해주세요")) {

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
					$("#code_send").prop("disabled", true);
					$("#code_send").css("background", "grey");
					$(".email_error").css("display", "flex");
					$(".email_error").text("인증번호가 전송되었습니다");
					$(".code_error").css("display", "none");
					$('#code_check').show();
					chk = data;

					timer_start();
					$("#timer").show();
				},
				error: function() {
					$(".code_error").css("display", "flex");
					$(".email_error").text("일시적인 전송에러가 발생하였습니다. 다시 시도해 주세요");
				}
			})
		}
	}

	//★완료 후 타이머시간 변경, 주석해제
	function timer_start() {

		let current_time = 0;

		// 인증코드 유효성 true
		code_valid = true;
		// 현재 발송 시간 초기화
		current_time = 0
		// 30초
		let count = 180;

		timer.innerHTML = "03:00"
		// 1초마다 실행
		timer_thread = setInterval(function() {

			minutes = parseInt(count / 60, 10);
			seconds = parseInt(count % 60, 10);

			minutes = minutes < 10 ? "0" + minutes : minutes;
			seconds = seconds < 10 ? "0" + seconds : seconds;


			timer.innerHTML = minutes + ":" + seconds;


			// 타이머 끝
			if (--count < 0) {
				//++time_chk;33
				timer_stop();
				//	timer.textContent = "00:00"

				$("#code_check").hide();
				$("#code_send").attr("value", "재전송");
				$("#code_send").css("color", "#ffffff");
				$("#code_send").css("background", "#8080c0");
				$("#code_send").prop("disabled", false);

				$(".code_error").css("display", "flex");
				$(".code_error").text("인증 시간이 만료되었습니다. 재전송해 주세요")
				$("#timer").hide();
				$(".email_error").css("display", "none");
			}
			current_time++

		}, 1000);

	}

	// 타이머 종료
	function timer_stop() {

		clearInterval(timer_thread)
		// 유효시간 만료
		code_valid = false
	}

	// 인증코드가 유효하면 true, 만료되었다면 false 반환
	function iscodeValid() {
		return code_valid;
	}

	//인증번호 체크 유효성 검사
	function code_check() {
		$(".code_error").css("display", "flex");
		let num = $("#m_code").val();


		if (num == chk) {
			$(".code_error").css("display", "none");
			$(".email_error").css("display", "flex");
			$(".email_error").text("인증되었습니다");
			$(".email_error").css("color", "blue");
			$("#code_send").hide();

			$("#m_code").hide();
			$("#info_code").hide();
			//	$("#m_email").prop("disabled", true);
			$("#m_email").attr("readonly", true);
			timer_stop();
			$("#timer").hide();

		} else if (num == "" || num == null) {
			$(".code_error").text("인증번호를 입력해주세요");
		}
		else {
			$(".code_error").text("번호가 일치하지 않습니다");
		}
	}

	//비밀번호 유효성 검사 (연속된 문자 불가/ 특수문자 영어 숫자 포함) ★아이디랑 같은 문자불가 yet
	function pwd_check() {
		let email = $("#m_email").val();
		let pwd = $("#m_pwd").val();
		let pwd_pattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/;
		let pwd_pattern2 = /(\w)\1\1/;

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
			$(".pwd2_error").css("color", "red");
			$(".pwd2_error").text("비밀번호가 일치하지않습니다.");

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



})