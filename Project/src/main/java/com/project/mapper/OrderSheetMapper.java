package com.project.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.project.model.Order;

@Mapper
public interface OrderSheetMapper {
	void AddOrder(Order model);
	int getMember(String p_nickname_m_fk);
	void Refund(Order order);
	void Refund(Map<Integer, String> refundMap);
}
