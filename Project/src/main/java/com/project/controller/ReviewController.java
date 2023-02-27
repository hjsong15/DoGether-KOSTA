package com.project.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.model.Img;
import com.project.model.PagingResponse;
import com.project.model.Review;
import com.project.model.SearchDto;
import com.project.service.IndexService;
import com.project.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
	
	private final ReviewService rService;

	@ResponseBody
	@GetMapping("")
	public Map<String, Object> Reviewlist(@ModelAttribute("params") SearchDto params, String r_pnickname_m_fk, Principal prin){// 페이징 처리를 위한 SearchDTO를 가져옴,어떤 상품의 리뷰를 가져올지 체크하기 위한 제품번호
		
		PagingResponse<Review> prolist = rService.AllReview(params, r_pnickname_m_fk);
		
		Map<String, Object> result = new HashMap<String, Object>(); // 리턴시켜야 하는 값의 자료형이 여러개이기 때문에 HashMap안에 담아서 KEY값으로 한번에 모아서 처리,
		
		int reviewct = rService.reviewct(r_pnickname_m_fk);
		double reviewstar = rService.reviewStar(r_pnickname_m_fk);
		reviewstar = Math.round(((reviewstar/reviewct)*10)/10.0);
		
		List<Img> img_name = new ArrayList<>();
		
		for (Review img : prolist.getList()) {
			img_name.addAll(img.getImg());
		}
		String m_nickname="";
		if(prin!=null) {
			m_nickname = rService.findnick(prin.getName());
		}else {
			m_nickname="비회원";
			
		}
		
		result.put("params", params);
		result.put("prolist", prolist.getList());
		result.put("pagination", prolist.getPagination());
		result.put("reviewct", reviewct);
		result.put("reviewstar", reviewstar);
		result.put("img", img_name);
		result.put("m_nickname", m_nickname);
		
		
		return result;
	}
	
	@ResponseBody
	@PostMapping("")
	public void addReview(Review r, Principal prin) throws Exception {
		
		
		String nick = rService.findnick(prin.getName());
		if(nick == null || nick == "") {
			nick = prin.getName();
		}
		r.setR_nickname_m_fk(nick);
		
		rService.ReviewAdd(r);

		
	}

	@ResponseBody
	@DeleteMapping("")
	public void DelReview(@RequestParam int r_id) {
		
		rService.ReviewDel(r_id);
		
		
	}
	
	@ResponseBody
	@GetMapping("/comment")
	public boolean checkcomment(Principal prin,@RequestParam int p_id) {
		boolean check = false;
		
		int index = 0;
		
		if(prin!=null) {
			index = rService.checkComments(prin.getName(), p_id);
		}
		
		if(index>0) {
			check = true;
		}
		
		return check;
	}
}
