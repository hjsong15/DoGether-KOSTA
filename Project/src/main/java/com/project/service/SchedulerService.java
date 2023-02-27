package com.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.project.mapper.SchedulerMapper;
import com.project.model.Discount;
import com.project.model.Order;
import com.project.model.Product;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchedulerService {

	private final SchedulerMapper sMapper;

// */1
	@Scheduled(cron = "*/50 * * * * *")
	public void run() {
		List<Product> endProductList= sMapper.FindProduct(); // 공고가 종료된 제품들의 리스트
		List<Product> endProductPriceList = new ArrayList<>();
		if (endProductList.size() != 0) {
			for (Product End_Product : endProductList) {
				Product sellerReturn = new Product();
				Product buyerProduct = new Product();
				sellerReturn.setP_id(End_Product.getP_id());
				sellerReturn.setP_nickname_m_fk(End_Product.getP_nickname_m_fk());
				buyerProduct.setP_endprice(End_Product.getP_price());
				buyerProduct.setP_id(End_Product.getP_id());
				buyerProduct.setP_price(End_Product.getP_price());
				for (Discount EndPro_Dis : End_Product.getDiscount()) {
					if (End_Product.getP_sell() >= EndPro_Dis.getDis_quantity()) { // 판매량이 할인율기준 갯수보다 높거나 같다면 그 할인율을 적용, 할인율최저치보다 낮다면
						int p_endprice_txt = End_Product.getP_price() - ((End_Product.getP_price() / 100) * (EndPro_Dis.getDis_count()));
						sellerReturn.setP_endprice(p_endprice_txt);
						sellerReturn.setP_price((p_endprice_txt - (p_endprice_txt / 100 * 5)) * End_Product.getP_sell());
						sellerReturn.setP_sell(End_Product.getP_sell());
						buyerProduct.setP_endprice(End_Product.getP_price() - p_endprice_txt);
					} else {
						buyerProduct.setP_sell(0);
					}
				}
				if (sellerReturn.getP_endprice() != 0) {
					buyerProduct.setP_sell(1);
				}
				sMapper.EndPriceSeller(sellerReturn);
				endProductPriceList.add(buyerProduct);
			}
			for (Product BuyChk_Product : endProductPriceList) {
				List<Order> Product_Buyer_List = sMapper.getOrder(BuyChk_Product.getP_id());
				if (Product_Buyer_List.size() != 0) {
					for (Order Buy_Member : Product_Buyer_List) { // 제품번호에 해당하는 제품을 주문한 모든 주문자에게 적립금으로 반환,
						BuyChk_Product.setP_endprice(Buy_Member.getO_quantity()*BuyChk_Product.getP_endprice());
						BuyChk_Product.setP_price(Buy_Member.getO_quantity()*BuyChk_Product.getP_price());						
						sMapper.EndPriceMember(BuyChk_Product.getP_endprice(), BuyChk_Product.getP_id(), Buy_Member.getO_member_m_fk(),
								BuyChk_Product.getP_sell(), BuyChk_Product.getP_price());
					}
					Product_Buyer_List.clear();
				}
			}
		}

	}

}
