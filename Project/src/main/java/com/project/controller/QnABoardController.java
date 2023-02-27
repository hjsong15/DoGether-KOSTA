package com.project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.model.QnABoardModel;
import com.project.service.QnABoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/qnaboard")
public class QnABoardController {
	
	@Autowired
	private QnABoardService qnaService;
	
//	@GetMapping("/{p_id}")
//	public String form() {
//		return "question";
//	}
	
	
	@PostMapping("") 
	public String addQuestion(QnABoardModel qna) throws Exception{
		qna.setQ_id_p_fk(1234);
		qna.setQ_nickname_m_fk("exNickname");
		qnaService.AddQuestion(qna);
		return "redirect:/QnABoardModel";
	}
	
	@GetMapping("/{q_id}")
	public String readQuestion(@PathVariable int q_id, QnABoardModel qna, Model model) throws Exception{
		Map<String, Object> questionMap = qnaService.FindQuestion(q_id);
		model.addAttribute("questionMap", questionMap);
		return "qnaBoard";
	}
	
	@GetMapping("/answer/{q_id}")
	public String goAnswer(@PathVariable int q_id, QnABoardModel qna, Model model) throws Exception{
		Map<String, Object> questionMap = qnaService.FindQuestion(q_id);
		model.addAttribute("questionMap", questionMap);
		return "questionMap";
	}

	@PostMapping("/answer/{q_id}")
	public String addAnswer(@PathVariable int q_id, QnABoardModel qna, Model model) throws Exception{
		qnaService.AddAnswer(qna);
		return "redirect:/qnaboard/"+q_id;	
	}
	
	@GetMapping("/list/{p_id}")
	public String qnaList(QnABoardModel qna, Model model, @PathVariable int p_id) {
//		model.addAttribute("list", qnaService.getList());
//		ArrayList<QnABoardModel> qnaList = new ArrayList<QnABoardModel>();
		List<Map<String, Object>> qnaListMap = qnaService.getList(p_id);
			model.addAttribute("qnaListMap",qnaListMap);
//		qnaList.addAll(qnaListMap);
		return "qnaList";
	}
}
