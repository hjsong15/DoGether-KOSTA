function execPostCode() {
    new daum.Postcode({
        oncomplete: function(data) {
           // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

           // 도로명 주소의 노출 규칙에 따라 주소를 조합한다.
           // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
         let fullRoadAddr = data.roadAddress; // 도로명 주소 변수
         let extraRoadAddr = ''; // 도로명 조합형 주소 변수

           // 법정동명이 있을 경우 추가한다. (법정리는 제외)
           // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
           if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
               extraRoadAddr += data.bname;
           }
           // 건물명이 있고, 공동주택일 경우 추가한다.
           if(data.buildingName !== '' && data.apartment === 'Y'){
              extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
           }
           // 도로명, 지번 조합형 주소가 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
           if(extraRoadAddr !== ''){
               extraRoadAddr = ' (' + extraRoadAddr + ')';
           }
           // 도로명, 지번 주소의 유무에 따라 해당 조합형 주소를 추가한다.
           if(fullRoadAddr !== ''){
               fullRoadAddr += extraRoadAddr;
           }

           // 우편번호와 주소 정보를 해당 필드에 넣는다.
           console.log(data.zonecode);
           console.log(fullRoadAddr);
           
           $("[name=addr1]").val(data.zonecode);
           $("[name=addr2]").val(fullRoadAddr);      
       }
    }).open();
}

$(".join_link").click (function() {
	//여기부터 해보려고 했음/..;
	
});
/*전화번호 하이픈

 function chk_tel(){
	 
      let str = ("#chk_tel").val();      

        if (len == 8) {
            if (str.substring(0, 2) == 02) {
                error_numbr(str, field);
            }
             else {
                field.value = phone_format(1, str);
            }
        } else if (len == 9) {
            if (str.substring(0, 2) == 02) {
                field.value = phone_format(2, str);
            } else {
                error_numbr(str, field);
            }
        } else if (len == 10) {
            if (str.substring(0, 2) == 02) {
                field.value = phone_format(2, str);
            } else {
                field.value = phone_format(3, str);
            }
        } else if (len == 11) {
            if (str.substring(0, 2) == 02) {
                error_numbr(str, field);
            } else {
                field.value = phone_format(3, str);
            }
        } else {
            error_numbr(str, field);
        }
    }

    function checkDigit(num) {
        var Digit = "1234567890";
        var string = num;
        var len = string.length
        var retVal = "";

        for (i = 0; i < len; i++) {
            if (Digit.indexOf(string.substring(i, i + 1)) >= 0) {
                retVal = retVal + string.substring(i, i + 1);
            }
        }
        return retVal;
    }

    function phone_format(type, num) {
        if (type == 1) {
            return num.replace(/([0-9]{4})([0-9]{4})/, "$1-$2");
        } else if (type == 2) {
            return num.replace(/([0-9]{2})([0-9]+)([0-9]{4})/, "$1-$2-$3");
        } else {
            return num.replace(/(^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})/, "$1-$2-$3");
        }
    }

    function error_numbr(str, field) {
        alert("정상적인 번호가 아닙니다.");
        field.value = "";
        field.focus();
        return;
    }
    */ 