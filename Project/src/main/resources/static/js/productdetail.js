$(function() {
	const token = $("meta[name='_csrf']").attr("content");
	const header = $("meta[name='_csrf_header']").attr("content");
	let Option_Array = [];
	let img = $(".mini_img").attr("src");
	let p_id = $("#p_id").val();
	let p_recruit_date = $("#p_recruitdate").val();
	let opt1_default = $(".option1").val();
	$(".big_img").attr("src", img);


	new Swiper('.swiper-container', {

		slidesPerView: 3, // 동시에 보여줄 슬라이드 갯수
		spaceBetween: 20, // 슬라이드간 간격
		slidesPerGroup: 3, // 그룹으로 묶을 수, slidesPerView 와 같은 값을 지정하는게 좋음

		// 그룹수가 맞지 않을 경우 빈칸으로 메우기
		// 3개가 나와야 되는데 1개만 있다면 2개는 빈칸으로 채워서 3개를 만듬
		loopFillGroupWithBlank: true,

		loop: true, // 무한 반복

		pagination: { // 페이징
			el: '.swiper-pagination',
			clickable: true, // 페이징을 클릭하면 해당 영역으로 이동, 필요시 지정해 줘야 기능 작동
		},
		navigation: { // 네비게이션
			nextEl: '.swiper-button-next', // 다음 버튼 클래스명
			prevEl: '.swiper-button-prev', // 이번 버튼 클래스명
		},
	});




	const countDownTimer = function(id, date) {
		let _vDate = new Date(date); // 전달 받은 일자
		let _second = 1000;
		let _minute = _second * 60;
		let _hour = _minute * 60;
		let _day = _hour * 24;
		let timer;
		function showRemaining() {
			let now = new Date();
			let distDt = _vDate - now;

			if (distDt < 0) {
				if (date == $("#p_duedate").val()) {
					clearInterval(timer);
					$("#" + id).text('판매종료 되었습니다!');
					$(".prodetail_btn_box").css("display", "none");
					$(".time_box").css("margin-top", "150px");
					$(".time_box").css('background-color', 'gray');
					$("select").prop('disabled', true);
					$("#quantity").css("display", "none");
					return;
				} else {
					let p_duedate = $("#p_duedate").val();
					clearInterval(timer);
					countDownTimer('time', p_duedate);
					$(".prodetail_btn_box").css("display", "flex");
					$(".time_box").css("margin-top", 0);
				}
			}

			let days = Math.floor(distDt / _day);
			let hours = Math.floor((distDt % _day) / _hour);
			let minutes = Math.floor((distDt % _hour) / _minute);
			let seconds = Math.floor((distDt % _minute) / _second);

			let html = days + '일' + hours + '시간' + minutes + '분' + seconds + '초';
			if (date == $("#p_duedate").val()) {
				html += '후 마감';
			} else {
				html += '후 오픈';
			}
			$("#" + id).text(html);
		}

		timer = setInterval(showRemaining, 100);
	}

	let dateObj = new Date();
	dateObj.setDate(dateObj.getDate() + 1);
	countDownTimer('time', p_recruit_date);


	let addcategory = function(val) {
		$.ajax({
			type: "GET",
			url: "/products/options/" + p_id,
			traditional: true,
			data: {
				opt_option1: val
			},
			beforeSend: function(xhr) { /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
				xhr.setRequestHeader(header, token);
			},
			success: function(data) {
				console.log(data);
				Option_Array = data;
				let html = ""
				if (data[0].opt_option2 != null) {
					$.each(data, function(index, item) {
						html += `
						<option value="${item.opt_option2}">${item.opt_option2}</option>
				`;
						if (index == 0) {
							$(".option_quanity").html("선택한 옵션의 남은 수량 :" + item.opt_quantity + "개");
						}
					});
					$(".option2").html(html);
					$("#option2").css('display', 'flex');
				} else {
					$.each(data, function(index, item) {
						if (val == item.opt_option1) {
							$(".option_quanity").html("선택한 옵션의 남은 수량 :" + item.opt_quantity + "개");
						}

					});
				}
			},
			error: function(e) {
				alert('에러');

			}
		});
	}

	$(".option1").change(function() {
		let opt_option1_value = $(this).val();
		addcategory(opt_option1_value);
	});

	$(".option2").change(function() {
		const opt_option2_value = $(this).val();
		$.each(Option_Array, function(idx, val) {
			if (opt_option2_value == val.opt_option2) {
				$(".option_quanity").html("선택한 옵션의 남은 수량 :" + val.opt_quantity + "개");
			}
		});
	});

	addcategory(opt1_default);

	$('.mini_img').click(function() {
		let value = $(this).attr("src");
		$(".big_img").attr("src", value);
	});





	$("#addCart").click(function() {
		const p_id = $("#p_id").val();
		const opt_option1 = $("select[name=opt_option1]").val();
		const opt_option2 = $("select[name=opt_option2]").val();
		const o_quantity = $("select[name=o_quantity]").val();
		const login_chk = $(".login_chk").val();
		if (login_chk != 'anonymousUser') {
			$.ajax({
				type: "POST",
				url: "/orders/carts",
				data: {
					o_product_p_fk: p_id,
					o_quantity: o_quantity,
					o_option1: opt_option1,
					o_option2: opt_option2
				},
				beforeSend: function(xhr) { /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
					xhr.setRequestHeader(header, token);
				},

				success: function(result) {
					console.log(result);
					alert(result);



				},
				error: function(e) {
					alert("에러입니다");

				}
			});
		} else {
			alert("로그인이 필요한 서비스입니다");
		}



	});


});




$(function() {
	const token = $("meta[name='_csrf']").attr("content");
	const header = $("meta[name='_csrf_header']").attr("content");

	$("#review_btn").click(function() {

		const p_id = $("#p_id").val();

		$.ajax({
			url: "/reviews/comment",
			type: "GET",
			beforeSend: function(xhr) { /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
				xhr.setRequestHeader(header, token);
			},
			data: {
				p_id: p_id
			},
			success: function(check) {

				if (check == true) {
					addReview();
				} else {
					alert("구매 후 작성해주세요!");
				}
			},
			error: function(e) {
				console.log("ERROR : ", e);
				alert("fail");
			}

		})






		function addReview() {
			const form = $("#addReview")[0];
			let data = new FormData(form);
			let content = $('#r_content').val();

			if (content == "" || content == null) {
				alert("내용을 입력하세요!");
			} else {

				$.ajax({
					url: "/reviews",
					type: "POST",
					data: data,
					enctype: "multipart/form-data",
					beforeSend: function(xhr) { /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
						xhr.setRequestHeader(header, token);
					},
					processData: false,
					contentType: false,
					success: function() {
						alert("작성 완료!");
						PageList();
						$('#r_content').val('');


					},
					error: function(e) {
						console.log("ERROR : ", e);
						alert("에러");
					}
				});
			}
		}

	});

})

$(document).ready(function() {
	PageList();
});


const drawStar = (target) => {
	$(`.star span`).css({ width: `${target.value * 20}%` });
}







function PageList() {
	const r_pnickname_m_fk = $("#r_pnickname_m_fk").val();
	$.ajax({
		type: "GET",
		url: "/reviews",
		data: {
			r_pnickname_m_fk: r_pnickname_m_fk,
			recordSize: 10,
			pageSize: 10

		},
		success: function(result) {
			let list = result.prolist;
			let params = result.params;
			let pagination = result.pagination;
			let img = result.img;
			let m_nickname = result.m_nickname;
			drawList(list, img, m_nickname);
			drawPage(pagination, params);
			$("#rvcnt").text("판매자후기(" + result.reviewct + ")개");
			$("#rvstar").text("평점(" + result.reviewstar + ")");

		},
		error: function(e) {
			alert('에러');

		}
	});
}

function drawList(list,img,m_nickname) {
    let html = "";
    
    list.forEach(result => {
    	
    	html += "<div class='review_form_c '>";
    	html += "<form>"
    	html += "<input type='hidden' name='r_id' id='r_id' value="+result.r_id+">";
    	html += "</form>";
    	html += "<div class='review_box_a'>";
		html += "<p class='p1'>"+result.r_nickname_m_fk+"</p>";
		html += "<span class='review_star1'>평점:"+result.r_rating+"</span>";
		html += "<p class='p2'>"+result.r_datetxt+"</p>";
		html += "</div>";//review_box_a
		for(let i =0; i<img.length; i++){
			if(result.r_id==img[i].img_rid_r_fk){
				html += "<div class='review_img_box'>";
				html += "<img src='/projectimg/"+img[i].img_name+"'>";
				html += "</div>";
			}
		}
		
		html += "<div class='review_text'>";
		html += "<p>" + result.r_content + " </p>";
		html += "</div>";	
		
		if(m_nickname == result.r_nickname_m_fk){
			html += "<div class='reviewDel_btn'>"
			html += "<button type=' button' id='reviewDel_btn'>"+'삭제'+"</button>"
			html += "</div>";
		}
				
		html += "</div>";
		
		
		
	
		});
    
		
		$(".review_box").html(html);
		
		$("#reviewDel_btn").click(function(){
	    	const r_id = $("#r_id").val();
	    	const token = $("meta[name='_csrf']").attr("content");
			const header = $("meta[name='_csrf_header']").attr("content");
			$.ajax({
				url : "/reviews",
				type : "DELETE",
				data : {
					r_id : r_id
				},
				beforeSend: function(xhr) { /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
					xhr.setRequestHeader(header, token);
				},
				success : function(data){
					alert("삭제완료");
					PageList();
				},
				error : function(e) {
					console.log("ERROR : ", e);
					alert("DEL에러");
				}
			});
			
			
		});
		
 }
 




function drawPage(pagination, params) {
	if (!pagination || !params) {
		$(".btn_box").html("");
		throw new Error('Missing required parameters...');
	}

	let html = "";
	// 첫 페이지, 이전 페이지

	if (pagination.existPrevPage) {
		html += `
	            <a href="javascript:void(0);" onclick="movePage(1)">첫 페이지</a>
	            <a href="javascript:void(0);" onclick="movePage("+ pagination.startPage-1 +")">이전 페이지</a>
	        `;
	}
	// 페이지 번호
	html += '<p>';
	// 1,2,3,4,5~~~ 페이지의 번호를 만들어내는 구문
	for (let i = pagination.startPage; i <= pagination.endPage; i++) {
		if (i !== params.page) {
			html += "<a href='javascript:void(0);' class='" + i + "'onclick='movePage(" + i + ");'>" + i + "</a>";
		} else {
			html += "<span class='on' id='on'>" + i + "</span>";
		}

	}
	html += '</p>';

	// 다음 페이지, 마지막 페이지
	if (pagination.existNextPage) {
		html += `
          <a href="javascript:void(0);" onclick="movePage("+pagination.endPage+1 +");">다음 페이지</a>
          <a href="javascript:void(0);" onclick="movePage("+pagination.totalPageCount+");" >마지막 페이지</a>
      `;
	}
	$(".btn_box").html(html);

}


// 페이지 이동
function movePage(page) {
	const r_pnickname_m_fk = $("#r_pnickname_m_fk").val();
	$.ajax({
		type: "GET",
		url: "/reviews",
		data: {
			r_pnickname_m_fk: r_pnickname_m_fk,
			page: (page) ? page : 1,
			recordSize: 10,
			pageSize: 10

		},
		success: function(result) {
			let list = result.prolist;
			let params = result.params;
			let pagination = result.pagination;
			let img = result.img;
			drawList(list, img);
			drawPage(pagination, params);
			$("#rvcnt").text("판매자후기(" + result.reviewct + ")개");
			$("#rvstar").text("평점(" + result.reviewstar + ")");

		},
		error: function(e) {
			alert('에러');

		}
	});
}
