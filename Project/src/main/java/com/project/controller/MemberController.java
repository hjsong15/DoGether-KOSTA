package com.project.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.catalina.filters.ExpiresFilter.XServletOutputStream;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.model.Member;
import com.project.service.EmailService;
import com.project.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/members") // member
public class MemberController {

	@Autowired
	private final MemberService mService;

	@Autowired
	private final EmailService eService;


	@GetMapping("") 
	public String loginForm(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "exception", required = false) String exception,Model model) {  
		if(error!=null) {
			model.addAttribute("exception",exception);
		}
		return "loginForm";
	}
	@GetMapping("/users")  //members/user/info
	public String joinForm() {
		return "joinForm";
	}

	@PostMapping("/users")
	public String join(Member member) {
		mService.MemberAdd(member);
		return "redirect:/member/index";
	}


	@GetMapping("/put") // "members/{m_id}"
	public String updateForm(Principal principal, Model model) {
		Member member = mService.FindID(principal.getName());
		model.addAttribute("member", member);
		String address = mService.FindAddr(principal.getName());
		if (address != null) {
			String[] addr = address.split("/");

			switch (addr.length) {
			case 2:
				model.addAttribute("addr1", addr[0]);
				model.addAttribute("addr2", addr[1]);
				break;
			case 3:
				model.addAttribute("addr1", addr[0]);
				model.addAttribute("addr2", addr[1]);
				model.addAttribute("addr3", addr[2]);
				break;
			}
		}
		return "memberUpdate";
	}

	@PutMapping("/put")
	public String update(Member member, @RequestParam("addr1") String addr1, @RequestParam("addr2") String addr2,
			@RequestParam(required = false) String addr3) {
		String address = addr1 + "/" + addr2 + "/" + addr3;
		member.setM_address(address);
		mService.editMember(member);
//		return "redirect:/index";  
		return "index";
	}

	//@PutMapping("/delete") // members/
	@DeleteMapping("/info")
	public String delete(Member m) {
		System.out.println(m);
		mService.delete(m);
		return "index";
	}

	@GetMapping("/info") // "/members/user"///find\"
	public String findPwdForm() {
		return "findPwd";
	}

	@PutMapping("/info") ///"/findpwd"//members/user"
	public String pwdUpDate(Member member, @RequestParam("m_email") String m_eamil) {
		mService.changePwd(member);
		System.out.println(member);
		return "loginForm";
	}

	@ResponseBody
	@PostMapping("/info") //members/user"//findpwd"
	public String findPwd(@RequestParam("m_email") String m_email, @RequestParam("m_name") String m_name) {
		System.out.println(m_email);
		System.out.println(m_name);
		String user = mService.FindPwd(m_email, m_name);
		return user;
	}

	/* 이메일 중복확인 */
	@ResponseBody
	@PostMapping("/info/email") //members/user/info"     /emailCheck"
	public int emailCheck(@RequestParam("m_email") String m_email) {
		int cnt = mService.emailCheck(m_email);
		return cnt;
	}

	/* 닉네임 중복확인 */
	@ResponseBody  
	@PostMapping("/info/nick")  //members/user/info2  ///nickcheck\"
	public int nickCheck(@RequestParam("m_nickname") String m_nickname) {
		int cnt2 = mService.nickCheck(m_nickname);
		return cnt2;
	}

//	@ResponseBody
//	@GetMapping("/email")
//	public String email(String m_email) { // 이메일인증의 시작점, 아이디 찾기에서 이메일 입력시 이곳으로 넘어와서
//		ArrayList<Member> m = mService.AllMember(); // email입력값이 DB에 존재하는 이메일인지 체크, 업다면 NULL
//		String chk = null; // 있다면 SUCCESS를 반환
//		for (int i = 0; i < m.size(); i++) {
//			if (m.get(i).getM_email().equals(m_email)) {
//				chk = "SUCCESS";
//			}
//		}
//		return chk;
//	}

	/* 인증번호 발송 */
	@ResponseBody  //members/user/info3		
	@PostMapping("/info/send") /* 자바설계규칙 *///"/sendEmail"
	public String EmailCheck(String m_email, Model model) throws Exception {
		String code = eService.sendSimpleMessage(m_email); // EmailService의 메일을 보내는 메서드를 작동.(리턴값으로 인증번호를 받아옴)
//		 mService.EmailNum(code, m_email); // Email에 해당하는 DB의 컬럼에 code에 인증번호를 저장시켜준다.
		model.addAttribute("email", m_email); // 인증번호를 보낸 email의 값을 인증 체크시 사용하기 위해 View로 전송 (Hidden으로 값을 저장해둠)
		return code;
	}

}
