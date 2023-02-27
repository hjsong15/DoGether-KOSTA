package com.project.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.project.model.Img;
import com.project.model.Order;
import com.project.model.Review;

@Mapper
public interface ReviewMapper {
	
	void AddReview(Review r);
	
	void RemoveReview(int r_id);
	
	List<Review> ReviewList(Map<String, Object> map);
	
	int Review_count(String r_pnickname_m_fk);
	
	String findnick(String m_email);
	
	double ReviewStar(String r_pnickname_m_fk);
	
	void AddImg(Img img);
	
	void RemoveImg(int img_rid_r_fk);
	
	int CheckComments(Map<String, Object> map);

}
