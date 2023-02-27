package com.project.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Option {
	private int opt_id;
	private int opt_pid_p_fk;
	private String opt_option1;
	private String opt_option2;
	private int opt_quantity;
	
	private List<Integer> opt_quantity_list;
	private List<String> opt_opt1_list;
	private List<String> opt_opt2_list;
	
}
