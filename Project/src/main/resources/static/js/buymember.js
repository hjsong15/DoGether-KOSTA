/*<![CDATA[*/
let myKey = "iH5hwc1qRUyfHb3NTdchiw";


function drawList(list) {
	let html = '';
	list.forEach(row => { // list 의 갯수만큼 반복진행 row 라는 변수명으로 list명을 대체함 화살표함수
		html += `
    		<div class="buymember_boxlist">
									<div class="buymember_content">
										<div class="buy_topbox">
											<div class="buy_toptag1 tag">
												<span>제품명:${row.p_name}</span>
											</div>
											<div class="buy_toptag2 tag ">
												<span>가격:${row.p_endprice}</span>
											</div>
											<div class="buy_toptag2 tag">
												<span>구매수량:${row.o_quantity}</span>
											</div>
										</div>
										<div class="buy_botbox">
											<div class="buy_tag2 tag">
												<span>구매자:${row.m_email}</span>
											</div>

											<div class="buy_tag1 tag">
												<span>옵션 : ${row.o_option1}
												`;
		if (row.o_option2 != null) {
			html += `,${row.o_option2}`;
		}
		html += `</span></div>
										<div class="buy_tag1 tag">
												<span>구매일자:${row.o_date_md}</span>
										</div>	
									</div>
								</div>
									<div class="PostBtn">
										`;
		if (row.o_postCode == null) {
			html += `<button type="button" class="PostCoad_txtBtn" value="${row.o_id}">운송장</button>`;
		} else {
			html += `<button type="button" class="PostCoad_EndBtn" value="${row.o_id}" disabled="disabled">등록O</button>`;
		}
		html += "</div></div>";
	});

	$(".buymember_list_box").html(html); // #list에 해당 html을 대체해서 넣어준다.
	return false;
}


// 페이지 HTML draw  하단의 버튼에 해당하는 스크립트로 페이징의 핵심
function drawPage(pagination, params) {
	//SearchDto의 기본 Default값을 바탕으로 mybatis의 count를 같이 받아와 계산후 저장시켜둔 pagingnation과 SearchDto를 받아옴
	if (!pagination || !params) {
		// pagination, params가 존재하지 않는다면 해당 스크립트를 출력
		$(".paging_btn").html("결과값이 존재하지 않습니다.");
		return false;
	}

	let html = '';

	// 첫 페이지, 이전 페이지

	if (pagination.existPrevPage) { // 현재페이지에서 보이는 스타트페이지가 1이 아닐경우 해당 스크립트의 HTML이 만들어진다. ( <  이전 페이지 ) 
		html += `
        <a href="javascript:void(0);" onclick="movePage(1)" class="page_bt first">첫 페이지</a>
        <a href="javascript:void(0);" onclick="movePage(${pagination.startPage - 1})" class="page_bt prev">이전 페이지</a>
    `;
	}

	// 페이지 번호
	html += '<p>';
	// 1,2,3,4,5~~~ 페이지의 번호를 만들어내는 구문
	for (let i = pagination.startPage; i <= pagination.endPage; i++) {
		html += (i !== params.page)
			? `<a href="javascript:void(0);" onclick="movePage(${i});">${i}</a>`
			: `<span class="on">${i}</span>`
	}
	html += '</p>';

	// 다음 페이지, 마지막 페이지
	if (pagination.existNextPage) {
		html += `
        <a href="javascript:void(0);" onclick="movePage(${pagination.endPage + 1});" class="page_bt next">다음 페이지</a>
        <a href="javascript:void(0);" onclick="movePage(${pagination.totalPageCount});" class="page_bt last">마지막 페이지</a>
    `;
	}
	$(".paging_btn").html(html);
	return false;
}
// 페이지 이동
function movePage(page) {
	const queryParams = {
		page: (page) ? page : 1,
		recordSize: 10,
		pageSize: 10
	}
	location.href = location.pathname + '?' + new URLSearchParams(queryParams).toString();
}


$(function() {
	let token = $("meta[name='_csrf']").attr("content");
	let header = $("meta[name='_csrf_header']").attr("content");

	$(document).on('click', '.PostCoad_txtBtn', function() {
		let o_id = $(this).val();
		let html = `
			<div class="PostCodeModal_text">
					<span>운송장 번호 등록하기</span>
					<input type="text" name="o_postCode" placeholder="운송장 입력" />
					<select name="o_postCompanyKey" id="o_postCompanyList">
					</select>
				</div>
				<button type="button" class="postadd">입력</button>
		`;
		html += "<input type='hidden' name='o_id' value='" + o_id + "'>";
		$("#PostCodeModal").html(html);
		$("#PostCodeModal").css("display", "flex");

		$.ajax({
			type: "GET",
			dataType: "json",
			url: "http://info.sweettracker.co.kr/api/v1/companylist?t_key=" + myKey,
			success: function(data) {
				let parseData = JSON.parse(JSON.stringify(data));
				let CompanyArray = data.Company;
				let myData = "";
				$.each(CompanyArray, function(key, value) {
					myData += ('<option value=' + value.Code + '>' + '택배사 :' + value.Name + '</option>');
				});
				$("#o_postCompanyList").html(myData);
			}
		});
	});

	$(document).mouseup(function(e) {
		if ($("#PostCodeModal").has(e.target).length === 0) {
			$("#PostCodeModal").hide();
		}
	});
	$(document).keydown(function(e) {
		var code = e.keyCode || e.which;
		if (code == 27) { // 27은 ESC 키번호
			$('.PostCodeModal').hide();
		}

	});


	$(document).on('click', '.postadd', function() {
		let form = $("#PostCodeModal").serialize();
		let o_id = $("input[name=o_id]").val();
		$.ajax({
			type: "POST",
			url: "/orders/" + o_id + "/posts",
			traditional: true,
			data: form,
			beforeSend: function(xhr) { /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
				xhr.setRequestHeader(header, token);
			},
			success: function(result) {
				alert(result);
			},
			error: function(e) {
				alert('에러');
			}
		});

	});

	$(".tag_select").change(function() {
		
		$(".select_form").submit();
	});
});

/*]]>*/