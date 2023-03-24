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

/**
 * 생성자 주입방식 사용이유
 * 불변성 보장, 순환참조를 방지, 에러를 런타임 시 알 수 있다.
 */
@RequiredArgsConstructor 
@Controller
@RequestMapping("/orders")
public class OrderController {

	private final OrderService oService;

	/**
	 * 장바구니에 상품 추가 -> INSERT
	 * principal -> 로그인을 한 정보를 가지고 있는 객체
	 * order - 장바구니에 넣으려고 하는 제품과 옵션의 정보를 담는다
	 */
	@ResponseBody // 비동기 방식으로 처리하기 위함.
	@PostMapping("/carts")
	public String AddCart(Principal principal, Order order) {
		String id = principal.getName(); // 로그인한 사용자의 ID를 담는다.

		/**
		 *  장바구니에 기존 상품이 있는지 검사
		 */
		int count = oService.countCart(order, id);
		if (count == 0) {
			oService.AddCart(id, order);  // 선택한 상품이 장바구니에 없으면 장바구니에 추가한다.
			return "장바구니에 추가 되었습니다";   //선택한 상품이 장바구니에 없으면 추가되었다는 문구 출력
		} else {
			return "이미 장바구니에 존재하는 상품입니다"; // 선택한 상품이 장바구니에 있으면 이미 존재하는 상품이라는 문구 출력
		}
	}
	
	/**
	 * 장바구니에 내역 뿌려주기 -> READ
	 * model -> View에 출력해줄 데이터를 담는 객체
	 * principal -> 로그인 정보를 가지고 있는 객체
	 */
	@GetMapping("/carts")
	public String findCart(Model model, Principal principal) {
		String m_nickname = principal.getName(); //로그인한 사용자의 ID를 담는다.
		ArrayList<Order> order = oService.findCart(m_nickname);  // 사용자의 정보를 토대로 장바구니에 담은 내역을 ArrayList order에 담는다.
		model.addAttribute("order", order); // 담은 내용을 View에 출력해주기 위해 model.Attribute에 담는다. 
		return "cart";
	}
	
	/**
	 * 장바구니에서 구매 시, 주문의 상태를 구매완료 처리 -> UPDATE
	 * o_id ->주문번호,
	 * o_quantity-> 주문수량을 파라미터로 담는다.
	 * 한 번에 여러 개의 상품을 주문할 수 있으므로 배열로 선언
	 */
	@PutMapping("/carts")
	public String cartBuyItem(int[] o_id, int[] o_quantity) {
		for (int i = 0; i < o_id.length; i++) {
			oService.cartBuyItem(o_id[i], o_quantity[i]);  // 장바구니에서 체크된 상품의 갯수와 수량만큼 반복문을 돌린다.
		}

		/**
		 *  default인 forward로 선언 시, 기존 업데이트가 중복 발생(새로고침 등) 되는 오류가 생길 수 있으므로
		 *  redirect로 선언하여 기존의 URL을 변경시켜 오류를 방지한다.  
		 */
		return "redirect:/orders/carts";
	}

	/**
	 * 장바구니에서 삭제 -> DELETE
	 * o_id -> 장바구니에 있는 제품의 Order 고유번호를 기준으로 삭제한다.
	 */
	@ResponseBody
	@DeleteMapping("/carts")
	public void delCartItem(int o_id) {
		oService.delCartItem(o_id);
	}

	/**
	 * 구매내역 & 페이징처리
	 * model -> View에 출력해줄 데이터를 담는 객체
	 * principal -> 로그인 정보를 가지고 있는 객체
	 * params-> 현재페이지 번호, 각 페이지별 출력할 갯수, 페이지 사이즈를 담고 있는 객체
	 * PagingResponse -> 페이징처리된 리스트와 페이지정보를 반환하기 위한 객체
	 * (리턴 값으로 <T> 제네릭을 사용한 이유는 어떤객체가 인자로 들어올지 모르고, 객체에 대한 재사용성을 높이기 위함.)
	 */
	@GetMapping("/buylists")
	public String buyList(Model model, Principal principal, SearchDto params) {
		String o_nickname = principal.getName(); //로그인한 사용자의 닉네임을 찾아온다.
		List<Product> product = new ArrayList<>(); //구매내역에 출력될 제품의 정보를 저장할 리스트
		List<Img> img = new ArrayList<>();  // 구매내역에 출력될 제품의 이미지를 저장할 리스트
		PagingResponse<Order> ordlist = oService.buyList(o_nickname, params); // 페이징처리를 하기 위한 기본 설정 값인 params에 데이터와 해당 사용자의 ID를 인자로 보내, 페이징처리를 하는 로직.
	
		/**
		 * 가져온 주문정보에 대한 리스트를 기반으로 view에 출력하기 편하도록 분할하기 위한 반복문
		 */
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

	// 배송조회 페이지로 전환하는 메소드
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