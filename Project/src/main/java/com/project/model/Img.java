package com.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Img {
	private String img_origname;
	private String img_keyword;
	private String img_name;
	private int img_pid_p_fk;
	private int img_rid_r_fk;
	private int img_id;

}
