$(function() {
	$(".logout_href").click(function() {
		$(".logoutform").submit();
	});


	$(".category_drop_menu").hide();
	$(".mymenu_drop_menu1").hide();
	$(".mymenu_drop_menu2").hide();
	$(".header_category_box").click(function() {
		$(".category_drop_menu").show();
		$(".category_drop_menu").toggleClass("hide");
	});
	$(".header_mymenu_drop").click(function() {
		$(".mymenu_drop_menu1").show();
		$(".mymenu_drop_menu1").toggleClass("hide");
	});
	$(".header_mymenu_drop2").click(function() {
		$(".mymenu_drop_menu2").show();
		$(".mymenu_drop_menu2").toggleClass("hide");
	});

	$("#modal_open").click(function() {
		$(".modal_form").css("display", "flex");
	});

	$(document).mouseup(function(e) {
		if ($(".modal_form").has(e.target).length === 0) {
			$(".modal_form").hide();
		}
	});

	$(document).keydown(function(e) {
		//keyCode 구 브라우저, which 현재 브라우저
		var code = e.keyCode || e.which;

		if (code == 27) { // 27은 ESC 키번호
			$('.modal_form').hide();
		}
	});



	$("#search_bt").click(function() {
		let f = $('#search').val();
		if (f == "") {
			alert("검색어를 입력하세요");
			return false;
		} else {
			$(".search_form").submit();
		}
	});
});