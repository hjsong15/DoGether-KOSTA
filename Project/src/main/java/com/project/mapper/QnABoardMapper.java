package com.project.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.project.model.QnABoardModel;

@Mapper
public interface QnABoardMapper {
	
//	void selectSomething(QnABoardModel a);
	void AddQuestion(QnABoardModel qna);
	void AddAnswer(QnABoardModel qna);
	QnABoardModel FindQuestion(int q_id);
	List<Map<String, Object>> getQnAList(int p_id);
	
}
