package com.project.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.model.Img;
import com.project.model.Order;
import com.project.model.PagingResponse;
import com.project.model.Product;
import com.project.model.SearchDto;
import com.project.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

	private final OrderService oService;

	@ResponseBody
	@PostMapping("/carts")
	public String AddCart(Principal principal, Order order) {
		String id = principal.getName();

		// 장바구니에 기존 상품이 있는지 검사
		int count = oService.countCart(order, id);
		if (count == 0) {
			oService.AddCart(principal, order);
			return "장바구니에 추가 되었습니다";
		} else {
			return "이미 장바구니에 존재하는 상품입니다";
		}

	}

	@GetMapping("/carts")
	public String findCart(Model model, Principal principal) {
		String m_nickname = principal.getName();
		ArrayList<Order> order = oService.findCart(m_nickname);
		for (Order ord : order) {
		}
		model.addAttribute("order", order);
		return "cart";
	}

	@PutMapping("/carts")
	public String cartBuyItem(int[] o_id, int[] o_quantity) {
		for (int i = 0; i < o_id.length; i++) {
			oService.cartBuyItem(o_id[i], o_quantity[i]);
		}
		return "redirect:/orders/carts";
	}

	@ResponseBody
	@DeleteMapping("/carts")
	public void delCartItem(int chk) {
		oService.delCartItem(chk);

	}

	// 구매내역
	@GetMapping("/buylists")
	public String buyList(Model model, Principal principal, SearchDto params) {
		String m_nickname = principal.getName();
		List<Product> product = new ArrayList<>();
		List<Img> img = new ArrayList<>();
		PagingResponse<Order> ordlist = oService.buyList(m_nickname, params);
		for (Order ord : ordlist.getList()) {
			product.add(ord.getProduct());
			img.add(ord.getImg());
		}
		model.addAttribute("img", img);
		model.addAttribute("product", product);
		model.addAttribute("params", params);
		model.addAttribute("ordlist", ordlist);

		return "buylist";
	}

	// 배송조회 페이지로 전환 하는 메소드
	@GetMapping("/delivery")
	public String delivery() {
		return "delivery";
	}

	@ResponseBody
	@PostMapping("/{o_id}/posts")
	public String PostCodeAdd(Order o) {
		oService.PostCodeAdd(o);
		return "추가완료";
	}
}