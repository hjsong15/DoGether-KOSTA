package com.project.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SearchDto {

    private int page;                 // 현재 페이지 번호
    private int recordSize;           // 페이지당 출력할 데이터 개수
    private int pageSize;             // 화면 하단에 출력할 페이지 사이즈
    private Pagination pagination;    // 페이지네이션 정보
    private String searching;
    private String sort;
    private String item;
    public SearchDto() {
        this.page = 1;
        this.recordSize = 10;
        this.pageSize = 10;
    }

}