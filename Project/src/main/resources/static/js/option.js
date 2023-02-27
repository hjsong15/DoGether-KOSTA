
window.onload = function() {
	check();
}
function check() {
	let length = $(".delopt").length;
	if (length == 1) {
		$("#Updateoption_btn").attr("disabled", true);
	}
};
$(function() {
	let token = $("meta[name='_csrf']").attr("content");
	let header = $("meta[name='_csrf_header']").attr("content");
	let opt_pid_p_fk = $("input[name=opt_pid_p_fk]").val();




	let num_check = function(value, cla) {
		const num_chk = /[^0-9]/g;
		if (num_chk.test(value)) {
			$(cla).val("");
			alert("수량은 숫자만 입력하세요");
			return false;
		}
	};
	$('input[name=opt_quantity]').keyup(function() {
		let value = $(this).val();
		num_check(value, $(this));
	});
	$('input[name=opt_quantity_one]').keyup(function() {
		let value = $(this).val();
		num_check(value, $(this));
	});

	$("#Addoption_btn").click(function() {
		let opt_option1 = $("input[name=opt_option1]").val();
		let opt_option2 = [];
		let opt_quantity = [];
		let addPageCheck=$(this).attr("class");
		const blank_chk = /^\s+|\s+$/g;
		$("input[name=opt_option2]").each(function(index, item) {
			if (!$(item).val().replace(blank_chk, '') == '' || !$(item).val() == null) {
				opt_option2.push($(item).val());
			}
		});
		$("input[name=opt_quantity]").each(function(index, item) {
			if (!$(item).val().replace(blank_chk, '') == '' || !$(item).val() == null) {
				opt_quantity.push($(item).val());
			}

		});
		if (opt_option2.length != opt_quantity.length) {
			alert("옵션간 입력 갯수가 일치하지 않습니다.");
			return false;
		}
		$.ajax({
			type: "POST",
			url: "/products/options",
			traditional: true,
			data: {
				opt_pid_p_fk: opt_pid_p_fk,
				opt_option1: opt_option1,
				opt_opt2_list: opt_option2,
				opt_quantity_list: opt_quantity
			},
			beforeSend: function(xhr) { /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
				xhr.setRequestHeader(header, token);
			},
			success: function(result) {
				alert("추가완료");
				$("input[name=opt_option1]").val("");
				$("input[name=opt_option2]").val("");
				$("input[name=opt_quantity]").val("");
				$(".type_btn_box").css("display", "none");

				if (result == "OneOptionAdd") {
					$(".option_form").css("display", "none");
					$(".opt_null_up").css("display", "flex");
				};
				if(addPageCheck!='add'){
					location.reload();
				}
				
			},
			error: function(e) {
				alert('에러');
			}
		});
	});
	$(".Addoption_one").click(function() {
		let opt_pid_p_fk = $("input[name=opt_pid_p_fk]").val();
		let opt_option1 = [];
		let opt_quantity = [];
		let addPageCheck=$(this).attr("id");
		const blank_chk = /^\s+|\s+$/g;
		$("input[name=opt_option1_one]").each(function(index, item) {
			if (!$(item).val().replace(blank_chk, '') == '' || !$(item).val() == null) {
				opt_option1.push($(item).val());
			}
		});
		$("input[name=opt_quantity_one]").each(function(index, item) {
			if (!$(item).val().replace(blank_chk, '') == '' || !$(item).val() == null) {
				opt_quantity.push($(item).val());
			}

		});

		if (opt_option1.length != opt_quantity.length) {
			alert("옵션간 입력 갯수가 일치하지 않습니다.");
			return false;
		}

		$.ajax({
			type: "POST",
			url: "/products/options",
			traditional: true,
			data: {
				opt_pid_p_fk: opt_pid_p_fk,
				opt_opt1_list: opt_option1,
				opt_quantity_list: opt_quantity
			},
			beforeSend: function(xhr) { /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
				xhr.setRequestHeader(header, token);
			},
			success: function(result) {
				alert("추가완료");
				$(".type_btn_box").css("display", "none");
				if (result == "OneOptionAdd") {
					$(".option_form").css("display", "none");
					$(".opt_null").css("display", "flex");
				};
				$("input[name=chk]").val("chk");
				if(addPageCheck!='add'){
					location.reload();
				}
			},
			error: function(e) {
				alert('에러');

			}
		});
	});




	let inputadd = function(box, name, cla) {
		let html = "<input type='text' name='" + name + "' class='" + cla + "'>";
		let cl = $('.' + cla).length;
		if (cl > 6) {
			alert('옵션별 최대 입력수 제한');
		} else {
			$('.' + box).append(html);
		}
	};
	$("#tag_option2_add").click(() => {
		inputadd("tag_option2", "opt_option2", "tag_input_option2");
	});
	$("#tag_quantity_add").click(() => {
		inputadd("tag_quantity", "opt_quantity", "tag_input_quantity");
	});
	$("#option_form_1_opt1").click(() => {
		inputadd("opt_tag1_opt1", "opt_option1_one", "opt1_option1");
	});
	$("#option_form_1_quan").click(() => {
		inputadd("opt_tag1_quantity", "opt_quantity_one", "opt1_quantity");
	});
	$("#option_form_1_opt2").click(() => {
		inputadd("opt_tag1_opt2", "opt_option1_one", "opt1_option1");
	});
	$("#option_form_1_quan2").click(() => {
		inputadd("opt_tag2_quantity", "opt_quantity_one", "opt1_quantity");
	});
	$("#One_option").click(() => {
		let chk = $(".opt_box2").length;
		if (chk == 0) {
			$(".opt_null").css("display", "flex");
			$(".option_form").css("display", "none");
		} else {
			$(".option_form_1").css("display", "flex");
			$(".option_form").css("display", "none");
		}
	});
	$("#All_option").click(() => {
		let chk = $(".opt_box2").length;
		if (chk == 0) {
			$(".option_form").css("display", "flex");
			$(".opt_null").css("display", "none");
		} else {
			$(".option_form_1").css("display", "none");
			$(".option_form").css("display", "flex");
		}


	});


});