package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;

@Controller
public class PurchaseController {

	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;

	public PurchaseController() {
		System.out.println(this.getClass());
	}

	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;

	@Value("#{commonProperties['pageSize']}")
	int pageSize;

	@RequestMapping("/addPurchaseView.do")
	public ModelAndView addPurchase(@RequestParam("prodNo") String prodNo)  throws Exception {
		System.out.println("addPurchaseView.do");
				
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/purchase/addPurchaseView.jsp");
		modelAndView.addObject("product", product);
		return modelAndView;
	}

	@RequestMapping("/addPurchase.do")
	public ModelAndView addPurchase(@ModelAttribute("purchase") Purchase purchase, @RequestParam("prodNo") String prodNo, HttpSession session) throws Exception {

		System.out.println("/addPurchase.do");
		
//		purchase.setPurchaseProd(product);
//		purchase.setBuyer((User)session.getAttribute("user"));
		
		User user =(User)session.getAttribute("user");
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		purchase.setPurchaseProd(product);
		purchase.setBuyer(user);
		System.out.println("DB 전 Purchase"+purchase);
		purchaseService.addPurchase(purchase);
		
		System.out.println("DB 후 다녀 왔나 Purchase");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/purchase/addPurchase.jsp");
		modelAndView.addObject("purchase", purchase);

		return modelAndView;
	}

	@RequestMapping("getPurchase.do")
	public ModelAndView getPurchase(@RequestParam("tranNo") String tranNo) throws Exception {

		System.out.println("getPurchase.do");
		Purchase purchase = purchaseService.getPurchase(Integer.parseInt(tranNo));
//		purchase.setPurchaseProd(product);
//		purchase.setBuyer((User)session.getAttribute("user"));
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/purchase/getPurchase.jsp");
		modelAndView.addObject("purchase", purchase);
		

		return modelAndView;
	}
	
	@RequestMapping("listPurchase.do")
	public ModelAndView listPurchase(@ModelAttribute("search") Search search, HttpSession session) throws Exception{
		
		System.out.println("listPurchase.do");
		
		User user=(User)session.getAttribute("user");
		
		if (search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String, Object> map = purchaseService.getPurchaseList(search, user.getUserId());
		
		Page resultPage = new Page(search.getCurrentPage(), ((Integer) map.get("totalCount")).intValue(), pageUnit, pageSize);
		
	
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/purchase/listPurchase.jsp");
		modelAndView.addObject("list", map.get("list"));
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.addObject("search", search);

		
		System.out.println("list"+map.get("list"));
		return modelAndView;
	}
	
	@RequestMapping("/updatePurchaseView.do")
	public ModelAndView updatePurchaseView(@RequestParam("tranNo") String tranNo) throws Exception{
		
		System.out.println("updatePurchaseView.do");
		Purchase purchase = purchaseService.getPurchase(Integer.parseInt(tranNo));
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/purchase/updatePurchaseView.jsp");
		modelAndView.addObject("purchase", purchase);

		return modelAndView;
		
	}
	
	@RequestMapping("/updateTranCode.do")
	public ModelAndView updateTranCode(@RequestParam(value = "prodNo", required=false) String prodNo, @RequestParam(value="tranNo", required=false) String tranNo,
			@RequestParam("tranCode") String tranCode, HttpSession session) throws Exception{
		
		int tranCodeUp = Integer.parseInt(tranCode);
		System.out.println("/purchase/updateTranCode GET");
		
		String role = "";
		User user = (User)session.getAttribute("user");
		
		if(user != null) {
			role = user.getRole();	
		}
		Purchase purchase = null;
		ModelAndView modelAndView = new ModelAndView();
		

		if(role.equals("admin")) {
			modelAndView.setViewName("/listProduct.do?menu=manage");
			purchase = purchaseService.getPurchase2(Integer.parseInt(prodNo));			
		} 
		else if(role.equals("user")) {
			modelAndView.setViewName("/listPurchase.do");
			purchase = purchaseService.getPurchase(Integer.parseInt(tranNo));
		}
		System.out.println(tranCodeUp);
		tranCodeUp++;
		System.out.println(tranCodeUp);
		
		purchase.setTranCode(String.valueOf(tranCodeUp));
		purchaseService.updateTranCode(purchase);
	
		return modelAndView;
	}

}
