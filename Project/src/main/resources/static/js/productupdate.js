$(function() {
	let numberchk = false;

	let token = $("meta[name='_csrf']").attr("content");
	let header = $("meta[name='_csrf_header']").attr("content");
	let index = $(".discount_add").length;
	let date = new Date(new Date().getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, -5);
	$(".recruidate").attr("min", date);
	$(".duedate").attr("min", date);

	$("#discount_btn").click(() => {
		let html = `<div class="discount_add">
						<span>설정:</span>
						<input type="text" name="p_discount_quan" placeholder="할인 기준 수량" data-name="기준 수량" class="disquan disquan`+ index + `">
						<input type="text" name="p_discount_count" placeholder="할인율" data-name="할인율" class="discount discount`+ index + `">
					</div>`;
		let cl = $('.discount_add').length;
		if (cl >= 3) {
			alert('할인율은 3개 까지만 등록가능')
		} else {
			$('.input_tag_discount').append(html);
			++cl;
			++index;
			$("#count").attr("value", cl);
		}
	});
	$("#discount_revbtn").click(() => {
		if (index > 1) {
			--index;
			let classChk = $(".discount_add").length - 1;
			$(".discount_add")[classChk].remove();
			let cl = $('.discount_add').length;
			$("#count").attr("value", cl);
		}
	});
	$("input[type=file]").change(function() {
		let fileVal = $(this).val();
		if (fileVal != "") {
			var ext = fileVal.split('.').pop().toLowerCase(); //확장자분리
			//아래 확장자가 있는지 체크
			if ($.inArray(ext, ['jpg', 'jpeg', 'gif', 'png', 'jfif']) == -1) {
				alert('jpg,gif,jpeg,png' + '파일만 업로드 할수 있습니다.');
				return;
			}
		}
	});

	$("input[name=p_recruitdate]").change(function() {
		if ($(".recruidate").val() < date) {
			alert('현재 시간보다 이전의 날짜는 설정할 수 없습니다.');
			$(".recruidate").val(date);
			return false;
		}
	});
	$("input[name=p_duedate]").change(function() {
		if ($(".duedate").val() < date) {
			alert('현재 시간보다 이전의 날짜는 설정할 수 없습니다.');
			$(".duedate").val(date);
			return false;
		} else if ($(".recruidate").val() >= $(".duedate").val()) {
			alert("마감시간은 시작일자보다 늦을 수 없습니다.");
			$(".duedate").val(date);
			return false;
		}
	});
	$('input[name=p_discount_quan]').on('keyup', function() {
		if (isNaN($(this).val())) {
			alert('할인 수량은 숫자만 입력하세요.');
			$(this).val("");
			numberchk = false;
			return false;
		} else {
			numberchk = true;
		}
	});
	$('input[name=p_discount_count]').on('keyup', function() {
		if (isNaN($(this).val())) {
			alert('할인율은 숫자만 입력하세요.');
			$(this).val("");
			return false;
		}
		if ($(this).val() >= 100) {
			alert('할인율은 0~99까지');
			$(this).val("");
			numberchk = false;
			return false;
		} else {
			numberchk = true;
		}
	});

	$('input[name=p_price]').on('keyup', function() {
		if (isNaN($(this).val())) {
			alert('가격은 숫자만 입력하세요.');
			$(this).val("");
			return false;
		}
	});


	$("#addpro_btn").click(() => {
		let date_chk = new Date(new Date().getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, -5);
		let chk = 0;
		let ret = true;
		let quantity = 0;
		let count = 0;
		$("input[name=p_discount_count]").each(function(index, item) {
			if ($(item).val() >= 100) {
				alert('할인율은 0~99까지');
				numberchk = false;
				return false;
			} else {
				numberchk = true;
			}

			if (index == 0) {
				count = $(item).val();
			} else {
				let chk = $(item).val();
				if (parseInt(count) >= parseInt(chk)) {
					alert("할인율이 유효하지 않습니다 순서를 확인해주세요");
					$(item).val("");
					ret = false;
					return false;
				} else {
					count = $(item).val();
					ret = true;
				}
			}
		});
		$("input[name=p_discount_quan]").each(function(index, item) {
			if (index == 0) {
				quantity = $(item).val();
			} else {
				let chk = $(item).val();
				if (parseInt(quantity) >= parseInt(chk)) {
					alert("할인기준 수량이 유효하지 않습니다 순서를 확인해주세요");
					$(item).val("");
					ret = false;
					return false;
				} else {
					quantity = $(item).val();
					ret = true;
				}
			}
		});

		$("#product_form").find("input[type=text]").each(function(index, item) {
			// 아무값없이 띄어쓰기만 있을 때도 빈 값으로 체크되도록 trim() 함수 호출
			if ($(this).val().trim() == '') {
				if ($(this).attr("name") == 'p_discount_quan' || $(this).attr("name") == 'p_discount_count') {
					if (isNaN($(this).val())) {
						$(this).val("");
						alert("할인 수량,할인율 숫자만 입력");
						++chk;
						ret = false;
						return false;
					}
				}
				alert($(this).attr("data-name") + " 항목을 입력하세요.");
				++chk;
				ret = false;
				return false;
			}
		});
		if (!ret) {
			return false;
		}


		if ($(".recruidate").val() >= $(".duedate").val()) {
			alert("마감시간은 시작일자보다 늦을 수 없습니다.");
			++chk;
			return false;
		}
		if (date_chk >= $(".recruidate").val()) {
			alert("시작일자는 현재시간보다 늦을 수 없습니다.");
			++chk;
			return false;
		}
		if (chk == 0 && numberchk) {
			$("#product_form").submit();
		} else {
			alert(chk + "개의 작성값에 이상이 있습니다.");
		}
	});


	$(document).mouseup(function(e) {
		if ($(".modal_form_pimg").has(e.target).length === 0) {
			$(".modal_form_pimg").hide();
		}
		if ($(".modal_form_contentimg").has(e.target).length === 0) {
			$(".modal_form_contentimg").hide();
		}
	});

	$(document).keydown(function(e) {
		//keyCode 구 브라우저, which 현재 브라우저
		var code = e.keyCode || e.which;

		if (code == 27) { // 27은 ESC 키번호
			$('.modal_form_pimg').hide();
			$('.modal_form_contentimg').hide();
		}

	});

	$(".img_box").click(function() {
		$(".modal_form_pimg").fadeIn();
		$(".modal_form_pimg").css("display", "flex");
	});
	$(".contentimg_box").click(function() {
		$(".modal_form_pimg").fadeIn();
		$(".modal_form_contentimg").css("display", "flex");
	});

	$('input[type=file]').change(function() {
		let img = $(this).attr("id");
		setImageFromFile(this, '.' + img);
	});

	function setImageFromFile(input, expression) {
		if (input.files && input.files[0]) {
			var reader = new FileReader();
			reader.onload = function(e) {
				$(expression).attr('src', e.target.result);
			}
			reader.readAsDataURL(input.files[0]);
		}
	}
});
