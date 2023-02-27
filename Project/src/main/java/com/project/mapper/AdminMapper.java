package com.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.project.model.Member;
import com.project.model.Product;

///////////DAO
@Mapper
public interface AdminMapper{
	
	
	List<Product> ListProduct();
	List<Product>FindProduct(String search);
	void DelProduct (int p_id);
	
	void DelMember (int m_id);
	List<Member> ListMember();
	List<Member>FindMember(String search);	
	
	
}
 







