package com.project.service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.project.mapper.ProductMapper;
import com.project.model.Discount;
import com.project.model.Img;
import com.project.model.Option;
import com.project.model.Order;
import com.project.model.Pagination;
import com.project.model.PagingResponse;
import com.project.model.Product;
import com.project.model.SearchDto;
import com.project.model.SetEnum;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductMapper productMapper;
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	@Value("${file.Upimg}")
	private String path;

	@Transactional // 트랜잭션 처리로 하위에 INSERT들이 진행도중 오류가 생긴다면 RollBack이 된다 (에외의 종류에 따라서 안될수도 있음)
	public void createProduct(Product productRequest) throws Exception {
		productMapper.AddProduct(productRequest);
		createDiscount(productRequest, SetEnum.ADD.name());
		createImg(productRequest.getP_img(), productRequest.getP_id(), SetEnum.p_img.name());
		createImg(productRequest.getP_contentimg(), productRequest.getP_id(), SetEnum.p_contentimg.name());
		createSqlEvent(productRequest, SetEnum.ADD.name());
	}

	@Transactional
	public void modifyProduct(Product productRequest) throws Exception {
		productMapper.UpdateProduct(productRequest);
		createDiscount(productRequest, SetEnum.UPDATE.name());
		createSqlEvent(productRequest, SetEnum.UPDATE.name());
		modifyImg(productRequest.getP_img(), productRequest.getP_id(), SetEnum.p_img.name());
		modifyImg(productRequest.getP_contentimg(), productRequest.getP_id(), SetEnum.p_contentimg.name());
	}

	@Transactional // 할인율 생성 ,수정
	public void createDiscount(Product productRequest, String Type) {
		if (Type.equals(SetEnum.UPDATE.name())) {
			productMapper.DeleteDiscount(productRequest.getP_id());
		}
		for (int i = 0; i < productRequest.getP_discount_count().size(); i++) {
			Discount dis = Discount.builder().dis_count(productRequest.getP_discount_count().get(i))
					.dis_quantity(productRequest.getP_discount_quan().get(i)).dis_pid_p_fk(productRequest.getP_id())
					.build();
			productMapper.AddDiscount(dis);
		}
	}

	@Transactional // 이미지 생성 , 수정
	public void modifyImg(List<MultipartFile> file, int p_id, String keyword) throws Exception {
		Img imgParameter = Img.builder().img_keyword(keyword).img_pid_p_fk(p_id).build();
		List<Img> beforeImgList = productMapper.img_length(imgParameter);
		for (int j = 0; j < file.size(); j++) {
			if (!file.get(j).isEmpty()) {
				String origName = file.get(j).getOriginalFilename(); // 입력한 원본 파일의 이름
				String uuid = String.valueOf(UUID.randomUUID()); // toString 보다는 valueOf를 추천 , NPE에러 예방,
				String extension = origName.substring(origName.lastIndexOf(".")); // 원본파일의 파일확장자
				String savedName = uuid + extension; // 랜덤이름 + 확장자
				File converFile = new File(path, savedName); // path = 상품 이미지 파일의 저장 경로가 들어있는 프로퍼티 설정값
				if (!converFile.exists()) {
					converFile.mkdirs();
				}
				file.get(j).transferTo(converFile); // --- 실제로 저장을 시켜주는 부분 , 해당 경로에 접근할 수 있는 권한이 없으면 에러 발생
				if (beforeImgList.size() > j) {
					deleteImg(beforeImgList.get(j));
					Img img = Img.builder().img_keyword(keyword).img_name(savedName).img_origname(origName)
							.img_pid_p_fk(p_id).img_id(beforeImgList.get(j).getImg_id()).build();
					productMapper.UpdateImg(img);
				} else {
					Img img = Img.builder().img_keyword(keyword).img_name(savedName).img_origname(origName)
							.img_pid_p_fk(p_id).build();
					productMapper.AddImg(img);
				}
			}
		}
	}

	@Transactional // 이미지 생성 , 수정
	public void createImg(List<MultipartFile> file, int p_id, String keyword) throws Exception {
		if (!CollectionUtils.isEmpty(file)) {
			for (MultipartFile imgFile : file) {
				String origName = imgFile.getOriginalFilename(); // 입력한 원본 파일의 이름
				String uuid = String.valueOf(UUID.randomUUID());// 문자+숫자의 랜덤한 파일명으로 변경
				String extension = origName.substring(origName.lastIndexOf(".")); // 원본파일의 파일확장자
				String savedName = uuid + extension; // 랜덤이름 + 확장자
				File converFile = new File(path, savedName); // path = 상품 이미지 파일의 저장 경로가 들어있는 프로퍼티 설정값
				if (!converFile.exists()) {
					converFile.mkdirs();
				}
				imgFile.transferTo(converFile); // --- 실제로 저장을 시켜주는 부분 , 해당 경로에 접근할 수 있는 권한이 없으면 에러 발생
				Img img = Img.builder().img_keyword(keyword).img_name(savedName).img_origname(origName)
						.img_pid_p_fk(p_id).build();
				productMapper.AddImg(img);
			}
		}
	}

	public void deleteImg(Img i) {
		String deletePath = path + i.getImg_name();
		File file = new File(deletePath);
		file.delete();
	}

	public void createSqlEvent(Product productRequest, String type) {
		String value = "";
		if (type.equals(SetEnum.UPDATE.name())) {
			value = "DROP EVENT " + productRequest.getP_id() + "_start";
			productMapper.CreateNewEvent(value);
			value = "DROP EVENT " + productRequest.getP_id() + "_end";
			productMapper.CreateNewEvent(value);
		}
		value = "CREATE EVENT IF NOT EXISTS " + productRequest.getP_id() + "_start ON SCHEDULE AT '"
				+ productRequest.getP_recruitdate()
				+ "' ON COMPLETION NOT PRESERVE ENABLE COMMENT 'CHECK' DO UPDATE product set p_chk='start' WHERE p_id="
				+ productRequest.getP_id();
		productMapper.CreateNewEvent(value);
		value = "CREATE EVENT IF NOT EXISTS " + productRequest.getP_id() + "_end ON SCHEDULE AT '"
				+ productRequest.getP_duedate()
				+ "' ON COMPLETION NOT PRESERVE ENABLE COMMENT 'CHECK' DO UPDATE product set p_chk='end' WHERE p_id="
				+ productRequest.getP_id();
		productMapper.CreateNewEvent(value);
	}

	@Transactional
	public void createOption(Option optionRequest) {
		productMapper.AddOption(optionRequest);
	}

	public Map<String, Object> findProduct(int p_id) {

		Product findProduct = productMapper.FindProduct(p_id);
		int nowDiscount = 0;
		int nextDiscountSell = findProduct.getDiscount().get(0).getDis_quantity();
		int discountPrice = findProduct.getP_price(); // 할인이 적용된 가격을 넣으려고 만든거,
		int index = 0;
		for (Discount discount : findProduct.getDiscount()) {
			if (discount.getDis_quantity() <= findProduct.getP_sell()) {
				discountPrice = findProduct.getP_price()
						- ((findProduct.getP_price() / 100) * (discount.getDis_count()));
				nowDiscount = discount.getDis_count();
				nextDiscountSell = findProduct.getDiscount().get(index).getDis_quantity();
				if (index < findProduct.getDiscount().size() - 1) {
					nextDiscountSell = findProduct.getDiscount().get(index + 1).getDis_quantity();
				}

			}
			index++;
		}
		LocalDateTime p_recruitdate = LocalDateTime.parse(findProduct.getP_recruitdate(), formatter);
		LocalDateTime p_duedate = LocalDateTime.parse(findProduct.getP_duedate(), formatter);
		List<Map<String, Object>> categoryBest = productMapper.CategoryBestProduct(findProduct.getP_nickname_m_fk(),
				findProduct.getP_category());
		List<Map<String, Object>> sellerBest = productMapper.SellerBestProduct(findProduct.getP_nickname_m_fk());
		Map<String, Object> map = Map.of("categoryBest", categoryBest, "sellerBest", sellerBest, "nowDiscount",
				nowDiscount, "nextDiscountSell", nextDiscountSell, "discountPrice", discountPrice, "p_recruitdate",
				p_recruitdate, "p_duedate", p_duedate, "findProduct", findProduct);
		return map;
	}

	public List<Option> findOptionTwo(String opt_option1, int p_id) {
		List<Option> option_list = productMapper.Option_List(p_id);
		List<Option> FindOption = new ArrayList<>();
		for (Option opt : option_list) {
			if (opt.getOpt_option1().equals(opt_option1)) {
				FindOption.add(opt);
			}
		}
		return FindOption;
	}

	public PagingResponse<Product> findSellerProducts(String p_nickname_m_fk, SearchDto params, String keyword) {
		int elementCount = 0;
		Map<String, Object> parameterMap = new HashMap<>();
		List<Product> list = new ArrayList<>();
		if (params.getSearching() != null) {
			elementCount = productMapper.SearchSellerCount(p_nickname_m_fk, params.getSearching(), keyword);
			parameterMap.put("search", params.getSearching());
		} else {
			elementCount = productMapper.WriterProductlistCount(p_nickname_m_fk, keyword);
		}
		if (elementCount < 1) {
			return new PagingResponse<>(Collections.emptyList(), null);
		}

		Pagination pagination = new Pagination(elementCount, params);
		params.setPagination(pagination);
		parameterMap.put("p_nickname_m_fk", p_nickname_m_fk);
		parameterMap.put("limitstart", params.getPagination().getLimitStart());
		parameterMap.put("recordsize", params.getRecordSize());
		parameterMap.put("keyword", keyword);

		if (params.getSearching() != null) {
			list = productMapper.SearchSeller(parameterMap);
		} else {
			list = productMapper.WriterProductlist(parameterMap);
		}
		return new PagingResponse<>(list, pagination);
	}

	public PagingResponse<Order> findProductBuyers(String p_nickname_m_fk, SearchDto params, String keyword) {
		int elementCount = 0;
		elementCount = productMapper.BuyProductCount(p_nickname_m_fk, keyword);
		if (elementCount < 1) {
			return new PagingResponse<>(Collections.emptyList(), null);
		}
		Pagination pagination = new Pagination(elementCount, params);
		params.setPagination(pagination);

		Map<String, Object> parameterMap = new HashMap<>() {
			{
				put("p_nickname_m_fk", p_nickname_m_fk);
				put("limitstart", params.getPagination().getLimitStart());
				put("recordsize", params.getRecordSize());
				put("sort", params.getSort());
				put("keyword", keyword);
			}
		};

		List<Order> list = productMapper.BuyProduct(parameterMap);
		return new PagingResponse<>(list, pagination);
	}

	@Transactional
	public void deleteEvent(int p_id) {
		String value = "";
		Product findCalender = productMapper.FindCalender(p_id);
		String p_recruitdate_str = findCalender.getP_recruitdate();
		String p_duedate_str = findCalender.getP_recruitdate();
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime p_recruitdate = LocalDateTime.parse(p_recruitdate_str, formatter);
		LocalDateTime p_duedate = LocalDateTime.parse(p_duedate_str, formatter);
		if (now.isBefore(p_recruitdate)) {
			value = "DROP EVENT " + p_id + "_start";
			productMapper.CreateNewEvent(value);
			value = "DROP EVENT " + p_id + "_end";
			productMapper.CreateNewEvent(value);
		} else if (now.isBefore(p_duedate)) {
			value = "DROP EVENT " + p_id + "_end";
		}
		productMapper.removeProduct(p_id);

	}

	public Map<String, Object> findOptions(int p_id) {
		List<Option> optionList = productMapper.Option_List(p_id);
		List<String> newList = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		if (optionList.size() != 0 && optionList.get(0).getOpt_option2() != null) {
			for (Option option1 : optionList) {
				newList.add(option1.getOpt_option1());
			}
			List<String> opt1 = newList.stream().distinct().collect(Collectors.toList());
			map.put("opt1", opt1);
		}
		map.put("optionList", optionList);
		return map;
	}

	public Map<String, Object> findSellPrices(String p_nickname_m_fk) { // 총 판매액 계산
		List<Map<String, Object>> allSell = productMapper.Sell_chart(p_nickname_m_fk);
		int sellMoney = 0;
		int totalSell = 0;
		for (Map<String, Object> map : allSell) {
			sellMoney += Integer.parseInt(map.get("p_endprice").toString())
					* Integer.parseInt(map.get("p_sell").toString());
			totalSell += Integer.parseInt(map.get("p_sell").toString());
		}
		Map<String, Object> create_map = new HashMap<>();
		create_map.put("sellCount", allSell.size());
		create_map.put("totalSell", totalSell);
		create_map.put("sellMoney", sellMoney);

		return create_map;
	}

	@Transactional
	public void deleteOption(String opt_option1, int opt_pid_p_fk) {
		Option opt = Option.builder().opt_option1(opt_option1).opt_pid_p_fk(opt_pid_p_fk).build();
		productMapper.OptionRemove(opt);
	}

	@Transactional
	public void deleteOneOption(int opt_id) {
		productMapper.OneOptionRemove(opt_id);
	}

	@Transactional
	public void deleteProductOptions(int p_id) {
		productMapper.OptionRemoveProduct(p_id);
	}

	public Option hasOption(int opt_pid_p_fk) {
		List<Option> pid_All_OptionList = productMapper.Option_List(opt_pid_p_fk);
		if (pid_All_OptionList.size() > 0) {
			return pid_All_OptionList.get(0);
		}
		return null;
	}

	public List<Option> findAllOptions(int p_id) {
		return productMapper.Option_List(p_id);
	}

	public void modifyQuantity(int[] opt_id, int[] opt_quantity) {
		for (int i = 0; i < opt_id.length; i++) {
			Option modifyOption = Option.builder().opt_id(opt_id[i]).opt_quantity(opt_quantity[i]).build();
			productMapper.modifyQuantity(modifyOption);
		}

	}

}
