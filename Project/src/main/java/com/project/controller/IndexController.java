package com.project.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.model.Discount;
import com.project.model.Img;
import com.project.model.PagingResponse;
import com.project.model.Product;
import com.project.model.SearchDto;
import com.project.service.IndexService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {
	private final IndexService iService;

/*   -------------------------메인페이지-----------------------------	 */
	
	@GetMapping({"/index","/"}) 
	public String mainPage(Model model) {
		
		List<Product> bestlist = iService.Productbest();
		List<Product> newlist = iService.Productnew();
		
		model.addAttribute("bestlist", bestlist);
		model.addAttribute("newlist", newlist);
		
		
		return "index";
	}

	
	/*   -------------------------페이징-----------------------------	 */
							
	@GetMapping(value = {"/index/newlist/{category}","/index/newlist"})
	public String newlist(@ModelAttribute("params") SearchDto params, Model model, @PathVariable(name="category",required = false) String p_category) {
		PagingResponse<Product> plist = iService.newlist(params, p_category);
		List<Img> img_name = new ArrayList<>();
		for (Product img : plist.getList()) {
			img_name.addAll(img.getImg());
		}
		List<Discount>dis = new ArrayList<>();
		for(Product discount: plist.getList()) {
			dis.addAll(discount.getDiscount());
		}
		
		model.addAttribute("dis", dis);
		
		
		model.addAttribute("img", img_name);
		model.addAttribute("plist", plist);
		model.addAttribute("category", p_category);
		
		return "prodlist";
	}
	@GetMapping(value = {"/index/pricelist/{category}","/index/pricelist"})
	public String pricelist(@ModelAttribute("params") SearchDto params, Model model, @PathVariable(name="category",required = false) String p_category) {

		PagingResponse<Product> plist = iService.pricelist(params, p_category);
		
		List<Img> img_name = new ArrayList<>();
		for (Product img : plist.getList()) {
			img_name.addAll(img.getImg());
		}
		List<Discount>dis = new ArrayList<>();
		for(Product discount: plist.getList()) {
			dis.addAll(discount.getDiscount());
		}
		
		model.addAttribute("dis", dis);
		
		model.addAttribute("img", img_name);
		model.addAttribute("plist", plist);
		model.addAttribute("category", p_category);
		
		return "prodlist";
	}
	@GetMapping(value = {"/index/pricelistdesc/{category}", "/index/pricelistdesc"})
	public String pricelistdesc(@ModelAttribute("params") SearchDto params, Model model, @PathVariable(name="category",required = false) String p_category) {
		PagingResponse<Product> plist = iService.pricelistdesc(params, p_category);
		
		List<Img> img_name = new ArrayList<>();
		for (Product img : plist.getList()) {
			img_name.addAll(img.getImg());
		}
		List<Discount>dis = new ArrayList<>();
		for(Product discount: plist.getList()) {
			dis.addAll(discount.getDiscount());
		}
		
		model.addAttribute("dis", dis);
		
		model.addAttribute("img", img_name);
		model.addAttribute("plist", plist);
		model.addAttribute("category", p_category);
		
		return "prodlist";
	}
	@GetMapping(value = {"/index/bestlist/{category}", "/index/bestlist"})
	public String bestlist(@ModelAttribute("params") SearchDto params, Model model, @PathVariable(name="category",required = false) String p_category) {
		PagingResponse<Product> plist = iService.bestlist(params, p_category);
		
		List<Img> img_name = new ArrayList<>();
		for (Product img : plist.getList()) {
			img_name.addAll(img.getImg());
		}
		List<Discount>dis = new ArrayList<>();
		for(Product discount: plist.getList()) {
			dis.addAll(discount.getDiscount());
		}
		
		model.addAttribute("dis", dis);
		
		model.addAttribute("img", img_name);
		model.addAttribute("plist", plist);
		model.addAttribute("category", p_category);
		
		return "prodlist";
	}
	@GetMapping("/index/list")
	public String PagingList(@ModelAttribute("params") SearchDto params, Model model) {
		PagingResponse<Product> plist = iService.PagingList(params);
		
		List<Img> img_name = new ArrayList<>();
		for (Product img : plist.getList()) {
			img_name.addAll(img.getImg());
			System.out.println("3213213213213"+img.getImg());
		}
		
		List<Discount>dis = new ArrayList<>();
		for(Product discount: plist.getList()) {
			dis.addAll(discount.getDiscount());
			System.out.println("asdsadasd"+discount.getDiscount());
		}
		
		model.addAttribute("dis", dis);
		model.addAttribute("img", img_name);
		model.addAttribute("plist", plist);
		
		return "prodlist";
	}
	
	
	@GetMapping("/index/category")
	public String category(@ModelAttribute("params") SearchDto params, String p_category, Model model) {
		
		PagingResponse<Product> plist = iService.Category(params, p_category);
		
		List<Img> img_name = new ArrayList<>();
		for (Product img : plist.getList()) {
			img_name.addAll(img.getImg());
		}
		
		List<Discount>dis = new ArrayList<>();
		for(Product discount: plist.getList()) {
			dis.addAll(discount.getDiscount());
		}
		
		model.addAttribute("dis", dis);
		
		
		model.addAttribute("img", img_name);
		model.addAttribute("plist", plist);
		model.addAttribute("category", p_category);
		return "prodlist";
	}
	
	@GetMapping("/index/search")
	public String Search(@ModelAttribute("params") SearchDto params, Model model,
			@RequestParam("keyword") String keyword, @RequestParam("search") String search) {
		PagingResponse<Product> plist = iService.Search(params, keyword, search);
		
		List<Img> img_name = new ArrayList<>();
		for (Product img : plist.getList()) {
			img_name.addAll(img.getImg());
		}
		List<Discount>dis = new ArrayList<>();
		for(Product discount: plist.getList()) {
			dis.addAll(discount.getDiscount());
		}
		
		model.addAttribute("dis", dis);
		
		model.addAttribute("img", img_name);
		model.addAttribute("plist", plist);
		model.addAttribute("keyword", keyword);		
		return "prodlist";
	}
	
	/* ---------------------------- Review ------------------------*/
	
	
}
