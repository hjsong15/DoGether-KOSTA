$(function() {
   const token = $("meta[name='_csrf']").attr("content");
   const header = $("meta[name='_csrf_header']").attr("content");

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
            clearInterval(timer);
            $(id).text('판매종료 되었습니다!');
            $(id).css("color", "gray");
            return;
         }

         let days = Math.floor(distDt / _day);
         let hours = Math.floor((distDt % _day) / _hour);
         let minutes = Math.floor((distDt % _hour) / _minute);
         let seconds = Math.floor((distDt % _minute) / _second);

         let html = days + '일 ' + hours + '시간 ' + minutes + '분 ' + seconds + '초 ';

         $(id).text(html);
      }

      timer = setInterval(showRemaining, 100);
   }

   $(".product_remaining").each(function() {
      countDownTimer($(this), $(this).text());
   });


   $('#remove_btn').click(function() {

      let suc = 0;
      let failed = 0;
      $("input[type=checkbox]:checked").each(function(index) {
         let chk = $(this).val();
         let idchk = $(this).attr('id');
         if (idchk != 'cbx_chkAll') {
            $.ajax({
               type: "delete",
               url: "/orders/carts",
               data: {
                  chk: chk
               },
               beforeSend: function(xhr) { /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                  xhr.setRequestHeader(header, token);
               },
               success: function() {
                  ++suc;
                  alert("삭제가 완료되었습니다.");
                  location.reload();

               },
               error: function() {
                  ++failed;
               }
            });
         }
      });

   });

   $("#moalbutton").click(function() {
      $(".modal").css("display", "flex");
      $('input:checkbox:not(:checked)').each(function(index, item) {
         let dis = $(item).attr("class");
         $("#" + dis).attr("disabled", true);
      });
   });

   $(document).mouseup(function(e) {
      if ($(".modal").has(e.target).length === 0) {
         $(".modal").hide();
         $('input:checkbox:not(:checked)').each(function(index, item) {
            let dis = $(item).attr("class");
            $("#" + dis).removeAttr("disabled");
         });
      }
   });

   $(document).keydown(function(e) {
      //keyCode 구 브라우저, which 현재 브라우저
      var code = e.keyCode || e.which;

      if (code == 27) { // 27은 ESC 키번호
         $('.modal').hide();
         $('input:checkbox:not(:checked)').each(function(index, item) {
            let dis = $(item).attr("class");
            $("#" + dis).removeAttr("disabled");
         });
      }
   });

   $(document).ready(function() {
      $("#cbx_chkAll").click(function() {
         if ($("#cbx_chkAll").is(":checked")) $("input[name=o_id]").prop("checked", true);
         else $("input[name=o_id]").prop("checked", false);
      });

      $("input[name=o_id]").click(function() {
         var total = $("input[name=o_id]").length;
         var checked = $("input[name=o_id]:checked").length;

         if (total != checked) $("#cbx_chkAll").prop("checked", false);
         else $("#cbx_chkAll").prop("checked", true);
      });
   });

});