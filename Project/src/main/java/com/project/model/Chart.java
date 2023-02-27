package com.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chart {
	private String dateTxt;
	private String nickname;
	private String category;
	private int count;
	private int endprice;
	private int p_sell;
	
}
