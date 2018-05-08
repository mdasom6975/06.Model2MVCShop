package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseDao;

//==> 구매/판매관리 DAO CRUD 구현
@Repository("purchaseDaoImpl")
public class PurchaseDaoImpl implements PurchaseDao {
	
	///Field
		@Autowired
		@Qualifier("sqlSessionTemplate")
		private SqlSession sqlSession;
		public void setSqlSession(SqlSession sqlSession) {
			this.sqlSession = sqlSession;
		}

	///Constructor
	public PurchaseDaoImpl() {
		System.out.println(this.getClass());
	}

	@Override
	public void addPurchase(Purchase purchase) throws Exception {
		String[] splitDivyDate = purchase.getDivyDate().split("-");
		String divyDate="";
		for(String str : splitDivyDate) {
			divyDate +=str;
		}
		purchase.setDivyDate(divyDate);
		sqlSession.insert("PurchaseMapper.addPurchase", purchase);
	}

	@Override
	public Purchase getPurchase(int tranNo) throws Exception {
		return sqlSession.selectOne("PurchaseMapper.getPurchase", tranNo);
	}

	@Override
	public Purchase getPurchase2(int prodNo) throws Exception {
		return sqlSession.selectOne("PurchaseMapper.getPurchase2", prodNo);
	}

	@Override
	public List<Purchase> getPurchseList(Search search, String buyerId) throws Exception {
		Map<String , Object>  map = new HashMap<String, Object>();
		
		map.put("search", search);
		map.put("buyerId", buyerId);
	
		
		List<Purchase> list = sqlSession.selectList("PurchaseMapper.getPurchaseList", map); 
		
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setBuyer((User)sqlSession.selectOne("UserMapper.getUser", list.get(i).getBuyer().getUserId()));
			list.get(i).setPurchaseProd((Product)sqlSession.selectOne("ProductMapper.getProduct", list.get(i).getPurchaseProd().getProdNo()));
		}
		
		map.put("totalCount", sqlSession.selectOne("PurchaseMapper.getTotalCount", buyerId));

		map.put("list", list);

	return sqlSession.selectList("PurchaseMapper.getPurchaseList", map);
	}
	
//	@Override
//	public List<Purchase> getPurchseList(Search search, String buyerId) throws Exception {
//		//return sqlSession.selectList("PurchaseMapper.getPurchaseList", search);
//		//parameterType 은 다양하다. Join 하여 SQL 문 작성 시 
//		//다양한 정보를 담아 가져 오기 위해 map에 담아 넘긴다. 
//		// 다양한 parameterType 사용 1
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("search",search );
//				map.put("buyerId",buyerId);
//				return sqlSession.selectList("PurchaseMapper.getPurchaseList", map);
//				
//				// 다양한 parameterType 사용 2
//				//Map<Stirng, Object> map = new HashMap<String, Object>();
//				//map("object",search);
//				//return sqlSession.selectList("UserMapper.getUserList", map);
//	}

	@Override
	public void updatePurchase(Purchase purchase) throws Exception {
		sqlSession.update("PurchaseMapper.updatePurchase", purchase);
		
	}

	@Override
	public void updateTranCode(Purchase purchase) throws Exception {
		sqlSession.update("PurchaseMapper.updateTranCode", purchase);
		
	}

	@Override
	public int getTotalCount(Search search) throws Exception {
		return sqlSession.selectOne("PurchaseMapper.getTotalCount", search);
	}

	

}
