package com.project.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.project.mapper.IndexMapper;
import com.project.mapper.ReviewMapper;
import com.project.model.Img;
import com.project.model.Pagination;
import com.project.model.PagingResponse;
import com.project.model.Product;
import com.project.model.Review;
import com.project.model.SearchDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class IndexService {

	@Autowired
	private final IndexMapper pMapper;

	@Autowired
	private final ReviewMapper rMapper;

	@Value("${file.Upimg}")
	private String path;

	public ArrayList<Product> Productbest() {
		return pMapper.Productbest();
	}

	public ArrayList<Product> Productnew() {
		return pMapper.Productnew();
	}

	public PagingResponse<Product> Category(SearchDto params, String p_category) {
		int count = pMapper.category_count(p_category);

		if (count < 1) {
			return new PagingResponse<>(Collections.emptyList(), null);
		}

		Pagination pagination = new Pagination(count, params);

		params.setPagination(pagination);
		Map<String, Object> map = new HashMap<>();
		map.put("p_category", p_category);
		map.put("limitStart", pagination.getLimitStart());
		map.put("recordSize", params.getRecordSize());
		List<Product> list = pMapper.Category(map);
		System.out.println(list);
		return new PagingResponse<>(list, pagination);
	}

	public PagingResponse<Product> PagingList(SearchDto params) {
		int count = pMapper.count();
		if (count < 1) {
			return new PagingResponse<>(Collections.emptyList(), null);
		}
		Pagination pagination = new Pagination(count, params);
		params.setPagination(pagination);
		List<Product> list = pMapper.PagingList(params);
		return new PagingResponse<>(list, pagination);

	}

	public PagingResponse<Product> newlist(SearchDto params, String p_category) {
		int count;
		if (p_category == null) {
			count = pMapper.count();
		} else {
			count = pMapper.category_count(p_category);
		}
		if (count < 1) {
			return new PagingResponse<>(Collections.emptyList(), null);
		}
		Pagination pagination = new Pagination(count, params);
		params.setPagination(pagination);
		Map<String, Object> map = new HashMap<>();
		map.put("p_category", p_category);
		map.put("limitStart", params.getPagination().getLimitStart());
		map.put("recordSize", params.getRecordSize());
		List<Product> list = new ArrayList<>();
		if (p_category == null) {
			list = pMapper.newlist(params);
		} else {
			list = pMapper.CategoryNew(map);
		}
		return new PagingResponse<>(list, pagination);

	}

	public PagingResponse<Product> pricelist(SearchDto params, String p_category) {
		int count;
		if (p_category == null) {
			count = pMapper.count();
		} else {
			count = pMapper.category_count(p_category);
		}

		if (count < 1) {
			return new PagingResponse<>(Collections.emptyList(), null);
		}
		Pagination pagination = new Pagination(count, params);
		params.setPagination(pagination);
		Map<String, Object> map = new HashMap<>();
		map.put("p_category", p_category);
		map.put("limitStart", params.getPagination().getLimitStart());
		map.put("recordSize", params.getRecordSize());
		List<Product> list = new ArrayList<>();
		if (p_category == null) {
			list = pMapper.pricelist(params);
		} else {
			list = pMapper.CategoryPrice(map);
		}
		return new PagingResponse<>(list, pagination);

	}

	public PagingResponse<Product> pricelistdesc(SearchDto params, String p_category) {
		int count;
		if (p_category == null) {
			count = pMapper.count();
		} else {
			count = pMapper.category_count(p_category);
		}

		if (count < 1) {
			return new PagingResponse<>(Collections.emptyList(), null);
		}
		Pagination pagination = new Pagination(count, params);
		params.setPagination(pagination);
		Map<String, Object> map = new HashMap<>();
		map.put("p_category", p_category);
		map.put("limitStart", params.getPagination().getLimitStart());
		map.put("recordSize", params.getRecordSize());
		List<Product> list = new ArrayList<>();
		if (p_category == null) {
			list = pMapper.pricelistdesc(params);
		} else {
			list = pMapper.CategoryPriceDesc(map);
		}
		return new PagingResponse<>(list, pagination);

	}

	public PagingResponse<Product> bestlist(SearchDto params, String p_category) {
		int count;
		if (p_category == null) {
			count = pMapper.count();
		} else {
			count = pMapper.category_count(p_category);
		}

		if (count < 1) {
			return new PagingResponse<>(Collections.emptyList(), null);
		}
		Pagination pagination = new Pagination(count, params);
		params.setPagination(pagination);
		Map<String, Object> map = new HashMap<>();
		map.put("p_category", p_category);
		map.put("limitStart", params.getPagination().getLimitStart());
		map.put("recordSize", params.getRecordSize());
		List<Product> list = new ArrayList<>();
		if (p_category == null) {
			list = pMapper.bestlist(params);
		} else {
			list = pMapper.CategoryBest(map);
		}
		return new PagingResponse<>(list, pagination);
	}

	public PagingResponse<Product> Search(SearchDto params, String keyword, String search) {
		int count;
		if (keyword.equals("total")) {
			count = pMapper.SearchTotCount(search);
		} else {
			count = pMapper.SearchCount(keyword, search);
		}
		if (count < 1) {
			return new PagingResponse<>(Collections.emptyList(), null);
		}
		Pagination pagination = new Pagination(count, params);
		params.setPagination(pagination);
		Map<String, Object> map = new HashMap<>();
		map.put("search", search);
		map.put("keyword", keyword);
		map.put("limitStart", params.getPagination().getLimitStart());
		map.put("recordSize", params.getRecordSize());
		List<Product> list = null;
		if (keyword.equals("total")) {
			list = pMapper.SearchTotal(map);
		} else {
			list = pMapper.Search(map);
		}
		return new PagingResponse<>(list, pagination);
	}

	@Transactional
	public void ReviewAdd(Review r) throws Exception {
		rMapper.AddReview(r);
		for (MultipartFile img : r.getR_img()) {
			if (!img.isEmpty()) {
				AddImg(r.getR_img(), r.getR_id(), "r_img");
			}
		}

	}

	public PagingResponse<Review> AllReview(SearchDto params, String r_pnickname_m_fk) {
		int count = rMapper.Review_count(r_pnickname_m_fk);
		if (count < 1) {
			return new PagingResponse<>(Collections.emptyList(), null);
		}
		Pagination pagination = new Pagination(count, params);
		params.setPagination(pagination);
		Map<String, Object> map = new HashMap<>();
		map.put("r_pnickname_m_fk", r_pnickname_m_fk);
		map.put("limitStart", params.getPagination().getLimitStart());
		map.put("recordSize", params.getRecordSize());
		List<Review> list = rMapper.ReviewList(map);
		return new PagingResponse<>(list, pagination);
	}

	public String findnick(String m_email) {

		return rMapper.findnick(m_email);
	}

	public int reviewct(String r_pnickname_m_fk) {
		return rMapper.Review_count(r_pnickname_m_fk);
	}

	public double reviewStar(String r_pnickname_m_fk) {
		return rMapper.ReviewStar(r_pnickname_m_fk);
	}

	public void ReviewDel(int r_id) {
		rMapper.RemoveReview(r_id);
		delimg(r_id);
	}

	@Transactional // 이미지 생성 , 수정
	public void AddImg(List<MultipartFile> file, int r_id, String keyword) throws Exception {
		if (!CollectionUtils.isEmpty(file)) {
			for (MultipartFile imgfile : file) {
				String origName = imgfile.getOriginalFilename(); // 입력한 원본 파일의 이름
				String uuid = String.valueOf(UUID.randomUUID());// 문자+숫자의 랜덤한 파일명으로 변경
				String extension = origName.substring(origName.lastIndexOf(".")); // 원본파일의 파일확장자
				String savedName = uuid + extension; // 랜덤이름 + 확장자
				File converFile = new File(path, savedName); // path = 상품 이미지 파일의 저장 경로가 들어있는 프로퍼티 설정값
				if (!converFile.exists()) {
					converFile.mkdirs();
				}
				imgfile.transferTo(converFile); // --- 실제로 저장을 시켜주는 부분 , 해당 경로에 접근할 수 있는 권한이 없으면 에러 발생
				Img img = Img.builder().img_keyword(keyword).img_name(savedName).img_origname(origName)
						.img_rid_r_fk(r_id).build();
				rMapper.AddImg(img);
			}
		}
	}

	public void delimg(int img_rid_r_fk) {
		rMapper.RemoveImg(img_rid_r_fk);
	}

	public int checkComments(String m_email, int p_id) {
		 
		 Map<String, Object> map = new HashMap<>();
		 
		 map.put("m_email", m_email);
		 map.put("p_id", p_id);
		 
		 int chk = rMapper.CheckComments(map);
		 
		 return chk;
	 }
}
