package com.project.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.model.Member;
import com.project.model.Product;
import com.project.service.AdminService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
	// Authentication authentication =
	// SecurityContextHolder.getContext().getAuthentication();
	// User user = (User) authentication.getPrincipal();
	// user.getAuthorities();2

	private final AdminService aService;

	@GetMapping("")
	public String member(Model model) {
		List<Product> product = aService.ListProduct();
		List<Member> Member = aService.ListMember();
		List<String> date = new ArrayList<>();
		for (Member mem : Member) {
			String create = mem.getM_reg_date().toString();
			String[] da = create.split(" ");
			date.add(da[0]);
		}

		model.addAttribute("date", date);
		model.addAttribute("product", product);
		model.addAttribute("member", Member);

		return "admin";
	}

	@GetMapping("/result")
	public String result(Model model, String search, String searchText) {

		if (search.equals("p_name")) {
			List<Product> productresult = aService.FindProduct(searchText);
			model.addAttribute("productresult", productresult);
		} else if (search.equals("m_nickname")) {
			List<String> date = new ArrayList<>();
			List<Member> memberresult = aService.FindMember(searchText);
			for (Member mem : memberresult) {
				String create = mem.getM_reg_date().toString();
				String[] da = create.split(" ");
				date.add(da[0]);
			}

			model.addAttribute("date", date);
			model.addAttribute("memberresult", memberresult);

		}
		return "result";
	}

	@DeleteMapping("/product/{p_id}")
	public String pro_del(Model model,@PathVariable int p_id) {
		aService.DelProduct(p_id);
		return "redirect:/admin";
	}
	@DeleteMapping("/member/{m_id}")
	public String mem_del(Model model,@PathVariable int m_id) {
		aService.DelMember(m_id);
		System.out.println();		
		return "redirect:/admin";
	}

}
