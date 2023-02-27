package com.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.mapper.AdminMapper;
import com.project.model.Member;
import com.project.model.Product;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	
	private final AdminMapper AdMapper;

	
	public List<Product> FindProduct(String search) {

		return AdMapper.FindProduct(search);
	}

	public List<Product> ListProduct() {

		return AdMapper.ListProduct();
	}

	public void DelProduct(int p_id) {
		AdMapper.DelProduct(p_id);		
	}
	
	public List<Member> ListMember() {

		return AdMapper.ListMember();
	}
	public List<Member> FindMember(String search) {

		return AdMapper.FindMember(search);
	}
	public void DelMember(int m_id) {
		AdMapper.DelMember(m_id);	
		
	}
}
