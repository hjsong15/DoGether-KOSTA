package com.project.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.model.Chart;
import com.project.model.Img;
import com.project.model.Option;
import com.project.model.Order;
import com.project.model.PagingResponse;
import com.project.model.Product;
import com.project.model.SearchDto;
import com.project.service.ChartService;
import com.project.service.IndexService;
import com.project.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
	private final ProductService productService;
	private final ChartService chartService;
	private final IndexService indexService;
	@Value("${chart.OneWeek}")
	private String oneWeek;

	/* ==================================================================== */
	@GetMapping("")
	public String loadProductAddForm(Principal prin, Model model) {
		String memberEmail = prin.getName();
		model.addAttribute("memberEmail", memberEmail);
		return "productadd";
	}

	@PostMapping("")
	public String createProduct(Product productRequest, RedirectAttributes reDirect) throws Exception {
		productService.createProduct(productRequest);
		reDirect.addAttribute("p_id", productRequest.getP_id());
		return "redirect:/products/options";
	}

	/* ==================================================================== */
	@GetMapping("/{p_id}/info")
	public String loadProductUpdateForm(@PathVariable int p_id, Model model) {
		Map<String, Object> findProductMap = productService.findProduct(p_id);
		model.addAttribute("findProductMap", findProductMap);
		return "productupdate";
	}

	@PutMapping("/{p_id}/info")
	public String modifyProduct(@PathVariable int p_id, Model model, Product productRequest) throws Exception {
		productService.modifyProduct(productRequest);
		
		return "redirect:/products/options/" + p_id + "/info";
	}

	@DeleteMapping("/{p_id}/info")
	public String deleteProduct  (@PathVariable int p_id) throws Exception {
		productService.deleteEvent(p_id);
		return "redirect:/products/add/lists";

	}

	@GetMapping("/{p_id}")
	public String findProductDetail(@PathVariable int p_id, Model model, String r_pnickname_m_fk) {
		Map<String, Object> findProductMap = productService.findProduct(p_id);
		int reviewCount = indexService.reviewct(r_pnickname_m_fk);
		double reviewStar = indexService.reviewStar(r_pnickname_m_fk);
		reviewStar = Math.round(((reviewStar / reviewCount) * 10) / 10.0);
		model.addAttribute("findProductMap", findProductMap);
		model.addAttribute("p_id", p_id);
		model.addAttribute("reviewCount", reviewCount);
		model.addAttribute("reviewStar", reviewStar);
		return "productdetail";
	}



	@ResponseBody
	@GetMapping("/options/{p_id}")
	public List<Option> findOptionTwo(String opt_option1, @PathVariable int p_id) {
		return productService.findOptionTwo(opt_option1, p_id);
	}

	@ResponseBody
	@PostMapping("/options")
	public String createOption(Option optionRequest, int opt_pid_p_fk) {
		if (optionRequest.getOpt_opt1_list() == null) {
			for (int i = 0; i < optionRequest.getOpt_opt2_list().size(); i++) {
				if (!optionRequest.getOpt_opt2_list().get(i).isEmpty()) {
					Option option = Option.builder().opt_pid_p_fk(opt_pid_p_fk)
							.opt_option1(optionRequest.getOpt_option1())
							.opt_option2(optionRequest.getOpt_opt2_list().get(i))
							.opt_quantity(optionRequest.getOpt_quantity_list().get(i)).build();
					productService.createOption(option);
				}
			}
			return "AllOptionAdd";
		} else {
			for (int i = 0; i < optionRequest.getOpt_opt1_list().size(); i++) {
				Option option = Option.builder().opt_pid_p_fk(opt_pid_p_fk)
						.opt_option1(optionRequest.getOpt_opt1_list().get(i))
						.opt_quantity(optionRequest.getOpt_quantity_list().get(i)).build();
				productService.createOption(option);
			}
		}
		return "OneOptionAdd";
	}

	@GetMapping("/{keyword}/lists")
	@ExceptionHandler(NullPointerException.class)
	public String findSellerProducts(Principal principal, Model model, @ModelAttribute("params") SearchDto params,
			@PathVariable String keyword) {
		String memberEmail = principal.getName();
		System.out.println(keyword);
		PagingResponse<Product> sellerProductList = productService.findSellerProducts(memberEmail, params, keyword);
		List<Img> imgList = new ArrayList<>();
		for (Product img : sellerProductList.getList()) {
			imgList.addAll(img.getImg());
		}
		Map<String, Object> sellerTotalCount = productService.findSellPrices(memberEmail);
		System.out.println(sellerProductList.getList());
		List<Chart> chart = chartService.OneWeekChart(memberEmail, oneWeek);
		model.addAttribute("img", imgList);
		model.addAttribute("chart", chart);
		model.addAttribute("sellerTotalCount", sellerTotalCount);
		model.addAttribute("sellerProductList", sellerProductList);
		model.addAttribute("keyword", keyword);
		return "mypage"; 
	}

	@GetMapping("/{keyword}/lists/buy")
	public String findSellerProductBuyers(Principal principal, Model model, @ModelAttribute("params") SearchDto params,
			@PathVariable String keyword) {
		String memberEmail = principal.getName();
		PagingResponse<Order> sellerBuyerList = productService.findProductBuyers(memberEmail, params, keyword);
		System.out.println(sellerBuyerList);
		Map<String, Object> sellerTotalCount = productService.findSellPrices(memberEmail);
		model.addAttribute("sellerTotalCount", sellerTotalCount);
		model.addAttribute("sellerBuyerList", sellerBuyerList);
		model.addAttribute("keyword", keyword);
		model.addAttribute("sortSelect", params.getSort());
		return "buymember";
	}

	@GetMapping("/charts/{day}/{month}")
	public String findChart(@PathVariable(required = false) String day, @PathVariable(required = false) String month,
			Principal principal, Model model) {
		String memberEmail = principal.getName();
		
		Map<String, Object> allChart = chartService.AllChartList(memberEmail, day, month);
		model.addAttribute("allChart", allChart);
		return "chart";
	}

	
	
	@ResponseBody
	@GetMapping("/options/chk")
	public Option hasOptionAdd(int opt_pid_p_fk) {
		Option optionCheck = productService.hasOption(opt_pid_p_fk);
		return optionCheck;
	}
	@ResponseBody
	@GetMapping("/options/lists")
	public List<Option> findOptions(int p_id){
		return productService.findAllOptions(p_id);
	}
	
	@ResponseBody
	@PutMapping("/options/lists")
	public void modifyQuantity(int[] opt_id,int[] opt_quantity) {
		productService.modifyQuantity(opt_id, opt_quantity);
	}
	@ResponseBody
	@DeleteMapping("/options")
	public String deleteProductOptionCheck(int opt_pid_p_fk) {
		Option productOptionCheck = productService.hasOption(opt_pid_p_fk);
		if (productOptionCheck == null) {
			productService.deleteProductOptions(opt_pid_p_fk);
			return "옵션이 존재하지 않아 해당 제품은 삭제 처리되었습니다.";
		} else {
			return "옵션 저장 및 제품 추가 완료";
		}
	}

	@GetMapping("/options")
	public String loadOptionAddForm(Model model, int p_id) {
		Option productOptionCheck = productService.hasOption(p_id);
		model.addAttribute("productOptionCheck", productOptionCheck);
		model.addAttribute("p_id", p_id);
		return "option";
	}

	@GetMapping("/options/{p_id}/info")
	public String loadOptionEditForm(@PathVariable int p_id, Model model) {
		Map<String, Object> productFindOption = productService.findOptions(p_id);
		model.addAttribute("p_id",p_id);
		model.addAttribute("productFindOption", productFindOption);
		return "optionUpdate";
	}

	@DeleteMapping("/options/{opt_option1}/info/{opt_pid_p_fk}")
	public String deleteOption(@PathVariable String opt_option1, @PathVariable int opt_pid_p_fk) {
		productService.deleteOption(opt_option1, opt_pid_p_fk);
		return "redirect:/products/options/" + opt_pid_p_fk + "/info";
	}

	@DeleteMapping("/options/{opt_id}/info")
	public String deleteOneOption(@PathVariable int opt_id, int opt_pid_p_fk) {
		productService.deleteOneOption(opt_id);
		return "redirect:/products/options/" + opt_pid_p_fk + "/info";
	}
}
