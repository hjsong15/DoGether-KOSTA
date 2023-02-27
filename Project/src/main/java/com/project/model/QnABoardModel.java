package com.project.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QnABoardModel {
	private int q_id;
	private int q_id_p_fk;
	private String q_title;
	private String q_content;
	private String q_nickname_m_fk;
	private Timestamp q_date;
	private String q_answer;
	private String q_deleteCheck;
}
