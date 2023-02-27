package com.project.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.mapper.QnABoardMapper;
import com.project.model.QnABoardModel;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QnABoardService {
	@Autowired
	private QnABoardMapper qnaMapper;
	
	@Transactional
	public void AddQuestion(QnABoardModel qna) {
		qnaMapper.AddQuestion(qna);
	}
	
	public Map<String, Object> FindQuestion(int q_id) {
		Map<String, Object> map = new HashMap<>();
		QnABoardModel qnaModel = qnaMapper.FindQuestion(q_id);
		
		map.put("q_id", q_id);
		map.put("qnaModel", qnaModel);
		if (qnaModel.getQ_answer() != null) {
			map.put("q_answer", qnaModel.getQ_answer());
		}
		return map;
	}
	
	@Transactional
	public void AddAnswer(QnABoardModel qna) {
		qnaMapper.AddAnswer(qna);
	}
	
	public List<Map<String, Object>> getList(int p_id){
		return qnaMapper.getQnAList(p_id);
	};
	
}
