package com.project.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.model.Chart;
import com.project.model.Product;

@Mapper
public interface ChartMapper {

	List<Chart> OneWeekChart(@Param("p_nickname_m_fk") String p_nickname_m_fk, @Param("Day") String Day);

	List<Chart> OneWeekSellPrice(@Param("p_nickname_m_fk") String p_nickname_m_fk, @Param("Day") String Day);

	List<Chart> OneMonthCategorySell(@Param("p_nickname_m_fk") String p_nickname_m_fk, @Param("Day") String Day);

	List<Chart> OneMonthFailedProduct(@Param("p_nickname_m_fk") String p_nickname_m_fk, @Param("Day") String Day);

	List<Map<String, Object>> NowAllSell(String p_nickname_m_fk);

	List<Map<String, Object>> NowAllProduct(String p_nickname_m_fk);

	List<Map<String, Object>> NowEndSellMoney(String p_nickname_m_fk);
	

}
