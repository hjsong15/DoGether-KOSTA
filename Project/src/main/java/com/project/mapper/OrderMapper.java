package com.project.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.model.Order;

@Mapper
public interface OrderMapper {
	// 장바구니에 추가
	void AddCart(Order oid);

	// 장바루니 목록
	ArrayList<Order> findCart(int o_member_m_fk);

	int findMember(String m_email);

	// 장바구니에서 구매
	void cartBuyItem(@Param("o_id") int o_id, @Param("o_quantity") int o_quantity);

	// 장바구니에서 삭재ㅔ
	void delCartItem(int o_id);

	// 구매내역 & 페이징처리
	List<Order> buyList(Map<String, Object> map);

	int buyListCount(int o_member_m_fk);

	// 장바구니에 동일한 상품이 있는지 확인
	int countCart(Map<String, Object> map);

	void PostCodeAdd(Order o);

}