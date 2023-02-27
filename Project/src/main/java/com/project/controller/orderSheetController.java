package com.project.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.model.Order;
import com.project.service.ProductService;
import com.project.service.orderSheetService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ordersheet")
public class orderSheetController {

	@Autowired
	private orderSheetService orderSheetService;

	@Autowired
	private ProductService pService;

	@GetMapping("")
	public String addProduct(Model model, Order ord) {
		Map<String, Object> promap = pService.findProduct(ord.getO_product_p_fk());
		model.addAttribute("promap", promap);
		model.addAttribute("order", ord);
		return "ordersheet";
	}
	
	@PostMapping("")
	public String addOrder(Order order, String p_nickname_m_fk) {
		orderSheetService.AddOrder(order, p_nickname_m_fk);
		return "redirect:/products/"+order.getO_product_p_fk();
	}
	

	
	@PostMapping("/refund/{o_id}")
	public String refundProduct(@PathVariable int o_id, String o_reason) {
		
		orderSheetService.Refund(o_id, o_reason);
		return "redirect:/orders/buylists";
	}
}
