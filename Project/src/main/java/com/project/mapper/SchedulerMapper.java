package com.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.model.Order;
import com.project.model.Product;

@Mapper
public interface SchedulerMapper {
	public void EndPriceProduct(int p_endprice);

	public void EndPriceMember(@Param("p_endprice") int p_endprice,@Param("p_id")int p_id, @Param("m_id")int m_id,@Param("p_sell") int p_sell,@Param("p_price")int p_price);
	public void EndPriceSeller(Product p);

	public List<Product> FindProduct();
	public List<Order> getOrder(int o_product_p_fk);
}
