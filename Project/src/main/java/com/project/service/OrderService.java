package com.project.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.mapper.OrderMapper;
import com.project.model.Order;
import com.project.model.Pagination;
import com.project.model.PagingResponse;
import com.project.model.SearchDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderMapper oMapper;
	
	/**
	 * 장바구니에 상품 추가 -> INSERT 메서드
	 * m_nickname - 로그인한 ID
	 * order - 장바구니에 넣으려고 하는 제품과 옵션의 정보를 담는다
	 */
	public void AddCart(String m_nickname, Order order) {
		int o_member_m_fk = oMapper.findMember(m_nickname); // mapper에서 email(유니크값)로 member의 PK를 가져온다.
		order.setO_member_m_fk(o_member_m_fk); // 가져온 PK 정보를 order에 담는다
		oMapper.AddCart(order);   // 이 Request 정보를 DB에 넣어주기 위해서 Insert 시킨다.
	}

	
	/**
	 * 장바구니에 담은 내역 출력해주는 것 -> READ 메서드
	 *  m_nickname - 로그인한 ID
	 */	
	public ArrayList<Order> findCart(String m_nickname) {
		int o_member_m_fk = oMapper.findMember(m_nickname);  //닉네임으로 member의 고유번호를 찾아온다.
		return oMapper.findCart(o_member_m_fk); //찾아온 고유번호 사용자의 정보를 장바구니에 담은 내역을 반환한다.
	}
	
	
	/**
	 * 장바구니에서 구매 시, 주문의 상태를 구매완료 처리 -> UPDATE
	 * o_id ->주문번호
	 * o_quantity-> 주문수량을 파라미터로 담는다.
	 */
	@Transactional
	public void cartBuyItem(int o_id, int o_quantity) {
		oMapper.cartBuyItem(o_id, o_quantity);

	}

	/**
	 * 장바구니 목록에서 삭제 -> DELETE
	 * o_id -> 장바구니에 있는 제품의 Order 고유번호를 기준으로 삭제한다.
	 */
	@Transactional
	public void delCartItem(int o_id) {
		oMapper.delCartItem(o_id);

	}
	

	/**
	 * 구매내역 & 페이징처리
	 * o_nickname -> 구매자의 ID
	 * params-> 현재페이지 번호, 각 페이지별 출력할 갯수, 페이지 사이즈를 담고 있는 객체
	 * PagingResponse -> 페이징처리된 리스트와 페이지정보를 반환하기 위한 객체
	 * (리턴 값으로 <T> 제네릭을 사용한 이유는 어떤객체가 인자로 들어올지 모르고, 객체에 대한 재사용성을 높이기 위함.)
	 */
	public PagingResponse<Order> buyList(String o_nickname, SearchDto params) {
		int o_member_m_fk = oMapper.findMember(o_nickname); //주문한 사람의 고유번호를 찾아오기 위함.
		int count = 0;  //구매한 제품들의 수량을 세기위한 변수
		Map<String, Object> map = new HashMap<>();   // 페이징 처리를 위해 여러 타입의 객체를 파라미터로 넘겨주기 위한 map 선언 (일회성이기 때문에 DTO를 만드는것이 아닌 Map 사용)
		count = oMapper.buyListCount(o_member_m_fk); // count에 구매한 제품의 갯수를 담는다
		if (count < 1) {
			return new PagingResponse<>(Collections.emptyList(), null);  // 카운트가 0이면 페이징 처리할게 없으니, 비어있는 리스트랑 null을 반환한다.
		}
		Pagination pagination = new Pagination(count, params);  //페이징처리를 위한 계산을 하는 객체 pagination 에게 계산에 필요한 데이터인 , count랑 params을 인자로 념겨준다
		map.put("o_member_m_fk", o_member_m_fk);
		map.put("limitstart", pagination.getLimitStart());
		map.put("recordsize", params.getRecordSize());     //map에 사용자의 고유번호, 찾아올 Limit의 시작점,한 페이지에 출력할 갯수를 담고 
		List<Order> list = oMapper.buyList(map);     // 위에서 만든 페이징 데이터와 해당 사용자의 고유번호를 담은 Map을 인자로 받아, 사용자의 구매내역을 받아온다.   
		return new PagingResponse<>(list, pagination);   // 앞서 만든 사용자의 구매내역과 페이징정보를 반환한다.
	}

	/**
	 *  동일한 상품이 장바구니 안에 있는지 확인
	 *  order -> 선택한 제품에 대한 정보
	 *  id - > 로그인한 사용자
	 */
	public int countCart(Order order, String id) {
		order.setO_member_m_fk(oMapper.findMember(id)); // 구매자 id의 고유번호를 넣어준다.
		return oMapper.countCart(order);   // 장바구니에 추가하려는 상품이 없으면 0, 있으면 1을 반환한다.

	}
	public void PostCodeAdd(Order o) {
		oMapper.PostCodeAdd(o);
	}

}