package com.project.ouath2;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.project.mapper.MemberMapper;
import com.project.model.Member;
import com.project.service.PrincipalDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	private final MemberMapper memberMapper;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		String m_email = oAuth2User.getAttribute("email");
		String m_name = oAuth2User.getAttribute("name");
		String m_type = "ROLE_USER";
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String m_pwd = passwordEncoder.encode("비밀번호제작");
		Member newMember = Member.builder().m_email(m_email).m_name(m_name).m_type(m_type).m_pwd(m_pwd).build();
		Member memberCheck = memberMapper.FindID(m_email);
		if (memberCheck == null) {
			memberMapper.oAuthAdd(newMember);
		}else {
			newMember.setM_type(memberCheck.getM_type());
		}
		return new PrincipalDetails(newMember, oAuth2User.getAttributes());
	}

}
