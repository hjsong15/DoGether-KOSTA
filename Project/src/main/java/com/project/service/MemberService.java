package com.project.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.mapper.MemberMapper;
import com.project.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

//	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	// private final PasswordEncoder passwordEncoder;
	private final BCryptPasswordEncoder passwordEncoder; // 멤버 회원가입시 멤버의 비밀번호를 암호화하기 위한 스프링 시큐리티,

	private final MemberMapper mMapper;

	@Transactional // 모든 작업들이 성공해야만 최종적으로 데이터베이스에 반영하도록 한다.
	public void MemberAdd(Member m) {
		m.setM_pwd(passwordEncoder.encode(m.getM_pwd()));
		mMapper.MemberAdd(m);

	}

	public Member FindID(String m_email) {
		return mMapper.FindID(m_email);
	}

	public String FindAddr(String m_email) {
		return mMapper.FindAddr(m_email);
	}

	public String FindPwd(String m_email, String m_name) {
		return mMapper.FindPwd(m_email, m_name);
	}

	@Override
	public UserDetails loadUserByUsername(String m_email) throws UsernameNotFoundException {
		Member m = FindID(m_email);
		if (m != null) {
			return new PrincipalDetails(m);
		}
		return null;

	}

	public void editMember(Member m) {
		m.setM_pwd(passwordEncoder.encode(m.getM_pwd()));
		m.setM_nickname(m.getM_nickname());
		m.setM_name(m.getM_name());
		m.setM_address(m.getM_address());
		m.setM_tel(m.getM_tel());
		//m.setM_image(m.getM_image());
		mMapper.editMember(m);
	}

	public void changePwd(Member m) {
		m.setM_pwd(passwordEncoder.encode(m.getM_pwd()));
		System.out.println("서비스" + m);
		mMapper.changePwd(m);
	}

	public void delete(Member m) {
		mMapper.delete(m);
	}

//	public void changePwd(Member m,String m_pwd,String m_email) {
//		m.setM_pwd(m.getM_pwd());
//		mMapper.changePwd(m, m_pwd, m_email);
//	}

//	public void changePwd(String m_pwd,String m_eamil){
//		//m_email.setM_pwd(passwordEncoder.encode(m_email.getM_pwd()));
//		changePwd(m_pwd, m_eamil);
//		
//		System.out.println("서비스" +m);
//		m.setM_email(m.getM_email());
//		m.setM_pwd(passwordEncoder.encode(m.getM_pwd()));
//		
//		mMapper.changePwd(m_pwd,m_email);
//	}

	/*** 이메일 인증 ****/

	@Transactional
	public void EmailNum(String m_code, String m_email) {
		mMapper.EmailNum(m_code, m_email);
	}

	public String EmailCode(String m_code, String m_email) {
		return mMapper.EmailCode(m_code, m_email);
	}

	public ArrayList<Member> AllMember() {
		return mMapper.AllMember();
	}

	public int emailCheck(String m_email) {
		int cnt = mMapper.emailCheck(m_email);
		System.out.println("이메일 중복체크 : " + cnt);
		return cnt;
	}

	public int nickCheck(String m_nickname) {
		int cnt2 = mMapper.nickCheck(m_nickname);
		System.out.println("닉네임 중복체크 :" + cnt2);
		return cnt2;
	}
}