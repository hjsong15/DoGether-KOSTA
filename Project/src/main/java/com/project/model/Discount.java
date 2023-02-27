package com.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Discount {
	private int dis_count;
	private int dis_pid_p_fk;
	private int dis_quantity;
	private int dis_id;
}
