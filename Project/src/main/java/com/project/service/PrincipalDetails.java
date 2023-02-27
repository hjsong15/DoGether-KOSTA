package com.project.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.project.model.Member;

public class PrincipalDetails implements UserDetails,OAuth2User{
	//사용자의 정보를 담아주는 인터페이스, 직접 상속받아서 사용한다.
	//오버라이딩 되는 메소드들만 Spring Security에서 알아서 사용하기 때문에 별도로 클래스를 만들지 않고 멤버변수를 추가해서 같이 사용해도 무방.
	//해당 멤버변수들은 getter, setter를 만들어서 사용하면 된다.
	private Member m;
	private Map<String,Object> attributes;
	
	public PrincipalDetails(Member m) {
	
		this.m = m;
	}
	public PrincipalDetails(Member m,Map<String,Object> attributes) {
		this.attributes=attributes;
		this.m = m;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() { // 계정이 가지고있는 권한의 목록을 리턴한다.
		Collection<GrantedAuthority> collect=new ArrayList<>();		
		
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {			

				return m.getM_type();  // DB에 설정해놓은 이 계정의 권한을 불러옴, USER,SELLER,ADMIN
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {//계정의 패스워드를 리턴		
		System.out.println(m.getM_pwd());
		return m.getM_pwd();
	}

	@Override
	public String getUsername() { // 계정의 이름을 리턴
		// TODO Auto-generated method stub
		return m.getM_email();
	}

	@Override
	public boolean isAccountNonExpired() { // 계정이 만료되지 않았는지를 리턴한다,true를 리턴하면 계정이 만료되지 않음
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() { // 계정이 잠겨있지 않은지를 리턴, true라면 계정이 잠겨있지 않음
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() { //계정의 패스워드가 만료되지 않았는지를 리턴한다, true라면 만료되지 않음
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() { // 계정이 사용가능한 계정인지를 리턴한다, true라면 계정이 사용가능,
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		
		return attributes;
	}

	@Override
	public String getName() {
		return null;
	}
	
	
}