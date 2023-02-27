package com.project.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
	private String r_nickname_m_fk, r_content,r_datetxt,r_pnickname_m_fk;
	private Timestamp r_date;
	private double r_rating;
	private int r_id;
	
	private ArrayList<Img> img;
	private List<MultipartFile> r_img; // request 용

}
