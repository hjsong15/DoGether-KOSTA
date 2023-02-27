$(function() {

	let forhtml = function(list, title) {
		let html = "";
		let index = 0;
		list.forEach(row => {
			if (index == 0) {
				html += `
				<div class="modal_tagbox">
					<div class="modal_tag1">
						<p>${title[0]}</p>
					</div>
					<div class="modal_tag2">
						<p>${title[1]}</p>
					</div>
					<div class="modal_tag3">
						<p>${title[2]}</p>
					</div>
					<div class="modal_tag4">
						<p>${title[3]}</p>
					</div>
					<div class="modal_tag5">
						<p>${title[4]}</p>
					</div>
				</div>
				`;
			}
			if (title[0] == '주문번호') {
				html += `<div class="modal_tagbox">
						<div class="modal_tag1">
							<p>${row.o_id}</p>
						</div>
						<div class="modal_tag2">
							<p>${row.o_quantity}</p>
						</div>
						<div class="modal_tag3">
							<p>${row.p_name}</p>
						</div>
						<div class="modal_tag4">
							<p>${row.o_option1}</p>
						</div>
						<div class="modal_tag5">
							<p>${row.o_option2}</p>
						</div>
					</div>`;
			}
			if (title[0] == '제품번호') {
				html += `<div class="modal_tagbox">
						<div class="modal_tag1">
							<p>${row.p_id}</p>
						</div>
						<div class="modal_tag2">
							<p>${row.p_chk}</p>
						</div>
						<div class="modal_tag3">
							<p>${row.p_name}</p>
						</div>
						<div class="modal_tag4">
							<p>${row.p_duedate}</p>
						</div>
						<div class="modal_tag5">
							<p>${row.p_sell}</p>
						</div>
					</div>`;
			}
			index++;
		});
		return html;
	};

	$(document).mouseup(function(e) {
		if ($(".chart_modal_con").has(e.target).length === 0) {
			$(".chart_modal_con").hide();
		}
	});
	$(document).keydown(function(e) {
		var code = e.keyCode || e.which;
		if (code == 27) { // 27은 ESC 키번호
			$('.chart_modal_con').hide();
		}

	});

	$(".atag").click(function() {
		const regex = /[^0-9]/g;
		let a_text = $(this).text();
		let a_value = parseInt(a_text.replace(regex, ""));
		let hre = $(this).attr('id');
		let html = "";
		if (a_value == 0) {
			alert("불러올 리스트가 존재하지 않습니다");
		} else {
			if (hre == 'NowAllSell') {
				let title = ['주문번호', '주문수량', '제품명', '선택옵션1', '선택옵션2']
				html = forhtml(NowAllSell, title);
			}
			if (hre == 'NowAllProduct') {
				let title = ['제품번호', '공고상태', '제품명', '종료시간', '판매량']
				html = forhtml(NowAllProduct, title);
			}
			$(".chart_modal_con").css("display", "flex");
			$(".chart_modal_box").html(html);
		}

	});



});




function MonthFailProList(MonthFailPro, Month) {
	if (Month == "-1 MONTH") {
		$(".ctxt2").text("한달간 공고 진행상태");
	} else if (Month = "-6 MONTH") {
		$(".ctxt2").text("6개월간 공고 진행상태");
	}
	let Count = []; // x축 데이터 배열 생성
	if (MonthFailPro[0] != null) {
		let context = document.getElementById('type4').getContext('2d');

		MonthFailPro.forEach(row => {
			Count.push(row.count);
		});
		let myChart = new Chart(context, {
			type: 'pie',
			data: {
				labels: ["판매실패", "판매완료"],
				datasets: [{
					label: '한달간 공고 진행상태',
					data: Count,      // 섭취량, 총급여량 - 섭취량
					backgroundColor: [

						'#6633CC',
						'#9933CC',

					],
					borderWidth: 1,
					scaleBeginAtZero: true,
				}
				]
			},
			options: {
				responsive: true,
				plugins: {
					legend: {
						position: 'top',
					},
					title: {
						display: true,
						text: 'Chart.js Pie Chart'
					}
				}
			},

		});
	} else {
		$("#text4").html("불러올 통계 데이터가 없습니다.</br> 공고 종료이후 데이터가 적용됩니다.");
	}
};



function MonthCategory(MonthCategoryList, Month) {
	let CountDate = []; // x축 데이터 배열 생성
	let CategoryList = [];

	let title = "";
	if (Month == "-1 MONTH") {
		$(".ctxt1").text("한달간 카테고리별 판매수량");

	} else if (Month = "-6 MONTH") {
		$(".ctxt1").text("6개월간 카테고리별 판매수량");
	}

	if (MonthCategoryList[0] != null) {
		let context = document.getElementById('type3').getContext('2d');
		MonthCategoryList.forEach(row => {
			CountDate.push(row.count);
			CategoryList.push(row.category);
		});
		let myChart = new Chart(context, {
			type: 'doughnut',
			data: {
				labels: CategoryList,
				datasets: [{
					label: title,
					data: CountDate,      // 섭취량, 총급여량 - 섭취량
					backgroundColor: [
						'#0000CC',
						'#000033',
						'#6633CC',
						'#9933CC',
						'#FF6600',
						'#FFFF33',
						'#FF0066',
						'#660099',
						'#9900CC',
						'#009966',
					],
					borderWidth: 1,
					scaleBeginAtZero: true,
				}
				]
			},

		});
	} else {
		$("#text3").html("불러올 통계 데이터가 없습니다.</br> 공고 종료이후 데이터가 적용됩니다.");
	}
};



function WeekSellList(WeekSell, Day) {

	let context = document.getElementById('type2').getContext('2d');
	let title = "";
	let DaysList = []; // x축 데이터 배열 생성
	let DayValue = [];
	let WeekDay = [];
	let arrayIndex = 0;
	if (Day == '-7 DAY') {
		title = "최근 7일간 판매 매출";
		for (let i = 7; i >= 1; i--) {
			DaysList.push(moment().subtract(i, 'day').format("MM-DD"));

		}
	} else if (Day == '-14 DAY') {
		title = "최근 14일간 판매 매출";
		for (let i = 14; i >= 1; i--) {
			DaysList.push(moment().subtract(i, 'day').format("MM-DD"));
		}
	}
	if (WeekSell.length != 0) {
			for (let d = 0; d < Week.length; d++) {
			WeekDay.push(Week[d].dateTxt);
		}
		for (let i = 0; i < DaysList.length; i++) {
			if (WeekDay[arrayIndex] == DaysList[i]) {
				DayValue.push(WeekSell[arrayIndex].endprice);
				arrayIndex++;
			} else {
				DayValue.push(0);
			}
		}

		let myChart = new Chart(context, {
			type: 'line',
			data: {
				labels: DaysList,
				datasets: [{
					label: title,
					data: DayValue,
					backgroundColor: [
						'rgba(255, 99, 132, 0.3)',
						'rgba(54, 162, 235, 1)',
						'rgba(255, 206, 86, 0.2)',
						'rgba(255, 206, 86, 0.2)',
						'rgba(75, 192, 192, 0.2)',
						'rgba(153, 102, 255, 0.2)',
						'rgba(255, 159, 64, 0.2)'
					],
					borderColor: [
						'rgba(255, 99, 132, 1)',
						'rgba(54, 162, 235, 1)',
						'rgba(255, 206, 86, 1)',
						'rgba(255, 206, 86, 1)',
						'rgba(75, 192, 192, 1)',
						'rgba(153, 102, 255, 1)',
						'rgba(255, 159, 64, 1)'
					],
					borderWidth: 2
				}]
			},
			options: {
				responsive: false,
				scales: {
					yAxes: [{
						ticks: {
							beginAtZero: true
						}
					}]
				},
			}
		});
	} else {
		$(".text2").html("불러올 통계 데이터가 없습니다.</br> 공고 종료이후 데이터가 적용됩니다.");
	}
};

function WeekList(Week, Day) {
	let DaysList = []; // x축 데이터 배열 생성
	let DayValue = [];
	let WeekDay = [];
	let title = "";
	let ind = 0;
	if (Day == '-7 DAY') {
		title = "최근 7일간 제품 판매완료 수량";
		for (let i = 7; i >= 1; i--) {
			DaysList.push(moment().subtract(i, 'day').format("MM-DD"));
		}
	} else if (Day == '-14 DAY') {
		title = "최근 14일간 제품 판매완료 수량";
		for (let i = 14; i >= 1; i--) {
			DaysList.push(moment().subtract(i, 'day').format("MM-DD"));
		}
	}

	if (Week.length != 0) {
		for (let d = 0; d < Week.length; d++) {
			WeekDay.push(Week[d].dateTxt);
		}
		for (let j = 0; j < DaysList.length; j++) {
			if (WeekDay[ind] === DaysList[j]) {
				DayValue.push(Week[ind].count);
				ind++;
			} else {
				DayValue.push(0);
			}
		}

		let context = document
			.getElementById('type1')
			.getContext('2d');
		let myChart = new Chart(context, {
			type: 'bar', // 차트의 형태
			data: { // 차트에 들어갈 데이터
				labels: DaysList,//x축
				datasets: [
					{ //데이터
						label: title, //차트 제목
						fill: false, // line 형태일 때, 선 안쪽을 채우는지 안채우는지
						data: DayValue,
						backgroundColor: [
							'rgba(255, 99, 132, 0.6)',
							'rgba(54, 162, 235, 0.6)',
							'rgba(255, 206, 86, 0.6)',
							'rgba(75, 192, 192, 0.6)',
							'rgba(153, 102, 255, 0.6)',
							'rgba(255, 159, 64, 0.6)',
							'rgba(255, 159, 64, 0.6)'
						],
						borderColor: [
							'rgba(255, 99, 132, 1)',
							'rgba(54, 162, 235, 1)',
							'rgba(54, 162, 235, 1)',
							'rgba(255, 206, 86, 1)',
							'rgba(75, 192, 192, 1)',
							'rgba(153, 102, 255, 1)',
							'rgba(255, 159, 64, 1)'
						],
						borderWidth: 1
					}]
			},
			options: {
				responsive: false,
				scales: {
					yAxes: [{
						ticks: {
							beginAtZero: true
						}
					}]
				},
			}
		});
	} else {
		$(".text1").html("불러올 통계 데이터가 없습니다.</br> 공고 종료이후 데이터가 적용됩니다.");


	}
}
