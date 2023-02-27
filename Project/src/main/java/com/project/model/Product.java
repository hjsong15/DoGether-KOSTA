package com.project.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
	private String p_category;
	private String p_nickname_m_fk;
	private String p_duedate;
	private String p_recruitdate;
	private String p_chk;
	private String p_name;
	private Timestamp p_createdate;
	private int p_id;
	private int p_price;
	private int p_sell;
	private int p_endprice;
	private ArrayList<Img> img;
	private ArrayList<Option> option;
	private ArrayList<Discount> discount;

	private List<MultipartFile> p_img; // request 용
	private List<MultipartFile> p_contentimg; // request 용
	private	List<Integer> p_discount_quan; // request 용
	private List<Integer> p_discount_count; // request 용

}
