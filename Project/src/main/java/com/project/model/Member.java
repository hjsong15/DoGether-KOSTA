package com.project.model;


import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor //여기에 필드에 쓴 모든생성자만 만들어줌
@NoArgsConstructor //기본 생성자를 만들어줌
@Data // @Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode
@Builder
public class Member {
	
 	 private int m_id; 
	 private String m_email; //이메일 및 로그인 아이디  *** 이메일인증
	 private String m_nickname; // 회원 별명
	 private String m_name; //회원 이름
	 private String m_pwd; //로그인 비밀번호
	 private String m_address; //회원 주소
	 private String m_tel; //핸드폰 번호 *** 핸드폰인증
	 private String m_image; //프로필 이미지 
	 private String m_type; //가입유형 ***    1.일반(ROLE_USER)  2.판매(ROLE_SELLER) 3.관리자(ROLE_ADMIN)    //4.블랙리스트(ROLE_BLACK)  5.탈퇴(ROLE_XUSER) 
	 
	 private int m_cash; //적립금
	 private int m_temp; //캐시 임시저장소
	 
	 private Timestamp m_reg_date;  //가입일
	 private Timestamp m_update_date;  //회원정보 수정일
	 
	 private int m_code;  //회원가입시 이메일 인증번호6자리
	}
