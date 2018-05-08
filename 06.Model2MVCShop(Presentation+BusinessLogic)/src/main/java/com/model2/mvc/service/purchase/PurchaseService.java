package com.model2.mvc.service.purchase;

import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;

//==> 판매관리에서 서비스할 내용 추상화/캡슐화한 Service  Interface Definition  
public interface PurchaseService {
	
		// 구매/판매
		public void addPurchase(Purchase purchase) throws Exception;
		
		// 구매정보확인
		public Purchase getPurchase(int tranNo) throws Exception;
		
		// 구매정보확인
		public Purchase getPurchase2(int prodNo) throws Exception;
		
		// 구매정보리스트
		//public List<Purchase> getPurchseList(Search search) throws Exception;
		public Map<String, Object> getPurchaseList(Search search, String buyerId) throws Exception;
		
		// 구매정보수정
		public void updatePurchase(Purchase purchase) throws Exception;
		
		// 구매/판매 상태수정
		public void updateTranCode(Purchase purchase) throws Exception;

}
