package com.project.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
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

	// 장바구니에 아이템 추가 -> insert
	public void AddCart(Principal principal, Order order) {
		String m_nickname = principal.getName();
		int o_member_m_fk = oMapper.findMember(m_nickname);
		order.setO_member_m_fk(o_member_m_fk);
		oMapper.AddCart(order);
	}

	// 장바구니에 담은 내역 뿌려주는 것 -> read
	public ArrayList<Order> findCart(String m_nickname) {
		int o_member_m_fk = oMapper.findMember(m_nickname);
		return oMapper.findCart(o_member_m_fk);
	}

	// 장바구니에서 구매 -> update
	@Transactional
	public void cartBuyItem(int o_id, int o_quantity) {
		oMapper.cartBuyItem(o_id, o_quantity);

	}

	// 장바구니 목록에서 삭제 -> delete
	@Transactional
	public void delCartItem(int o_id) {
		oMapper.delCartItem(o_id);

	}

	// 구매내역
	public PagingResponse<Order> buyList(String o_nickname, SearchDto params) {
		int o_member_m_fk = oMapper.findMember(o_nickname);
		int count = 0;
		Map<String, Object> map = new HashMap<>();
		count = oMapper.buyListCount(o_member_m_fk);
		if (count < 1) {
			return new PagingResponse<>(Collections.emptyList(), null);
		}
		Pagination pagination = new Pagination(count, params);
		params.setPagination(pagination);
		map.put("o_member_m_fk", o_member_m_fk);
		map.put("limitstart", params.getPagination().getLimitStart());
		map.put("recordsize", params.getRecordSize());
		List<Order> list = oMapper.buyList(map);
		for (Order ord : list) {
		}
		return new PagingResponse<>(list, pagination);
	}

	// 동일한 상품이 장바구니 안에 있는지 확인
	public int countCart(Order order, String id) {
		int o_member_m_fk = oMapper.findMember(id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("o_product_p_fk", order.getO_product_p_fk());
		map.put("o_member_m_fk", o_member_m_fk);

		return oMapper.countCart(map);

	}
	public void PostCodeAdd(Order o) {
		oMapper.PostCodeAdd(o);
	}

}