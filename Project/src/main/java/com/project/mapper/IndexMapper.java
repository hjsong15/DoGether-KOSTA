package com.project.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.model.Product;
import com.project.model.SearchDto;

@Mapper
public interface IndexMapper {
	
	ArrayList<Product> Productbest();

	ArrayList<Product> Productnew();
	
	List<Product> Search(Map<String, Object> map);

	List<Product> SearchTotal(Map<String, Object> map);
   
	List<Product> Category(Map<String, Object> map);
	
	List<Product> CategoryNew(Map<String, Object> map);
	
	List<Product> CategoryPrice(Map<String, Object> map);
	
	List<Product> CategoryPriceDesc(Map<String, Object> map);
	
	List<Product> CategoryBest(Map<String, Object> map);

	List<Product> newlist(SearchDto params);
	
	List<Product> pricelist(SearchDto params);
	
	List<Product> pricelistdesc(SearchDto params);
	
	List<Product> bestlist(SearchDto params);
	
	List<Product> PagingList(SearchDto params);
	
	int count();

	int category_count(String params);
	
	int SearchCount(@Param("keyword") String keyword, @Param("search") String search);

	int SearchTotCount(@Param("search") String search);

}
