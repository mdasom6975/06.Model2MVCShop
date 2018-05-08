package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.user.UserService;

//==>판매관리 Controller
@Controller
public class ProductController {

	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;

	public ProductController() {
		System.out.println(this.getClass());
	}

	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;

	@Value("#{commonProperties['pageSize']}")
	int pageSize;

	@RequestMapping("/addProductView.do")
	public String addProduct() throws Exception {

		System.out.println("/addProductView.do");

		return "redirect:/product/addProductView.jsp";
	}

	@RequestMapping("/addProduct.do")
	public String addProduct(@ModelAttribute("product") Product product) throws Exception {

		System.out.println("/addProduct.do");
		// Business Logic
		productService.addProduct(product);

		return "forward:/product/addProduct.jsp";
	}

	@RequestMapping("getProduct.do")
	public String getProduct(@RequestParam("prodNo") String prodNo, Model model, HttpServletRequest request)
			throws Exception {

		System.out.println("/getProduct.do");
		// Business Logic
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		// Model과 View 연결
		model.addAttribute("product", product);

		if (request.getParameter("menu").equals("manage")) {
			System.out.println("menu :::::" + request.getParameter("menu"));
			return "forward:/product/updateProductView.jsp";
		} else {
			System.out.println("menu  ::::" + request.getParameter("menu"));
			return "forward:/product/getProduct.jsp";
		}
	}

	@RequestMapping("/updateProductView.do")
	public String updateProductView(@RequestParam("prodNo") String prodNo, Model model) throws Exception {

		System.out.println("/updateProductView.do");
		// Business Logic
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		// Model 과 View 연결
		model.addAttribute("product", product);

		return "forward:/product/updateProductView.jsp";
	}

	@RequestMapping("updateProduct.do")
	public String updateProduct(@ModelAttribute("product") Product product, Model model) throws Exception {

		System.out.println("updateProduct.do");
		// Business Logic
		productService.updateProduct(product);

		return "forward:/product/getProduct.jsp";
	}

	@RequestMapping("listProduct.do")
	public String listProduct(@ModelAttribute("search") Search search, Model model, HttpServletRequest request, HttpSession session)
			throws Exception {

		System.out.println("listProduct.do");

		if (search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		search.setSearchCondition(request.getParameter("searchCondition"));
		search.setSearchKeyword(request.getParameter("searchKeyword"));

		// Business logic 수행
		Map<String, Object> map = productService.getProductList(search);

		Page resultPage = new Page(search.getCurrentPage(), ((Integer) map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);


		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);

		if (request.getParameter("menu").equals("manage")) {
			System.out.println("menu :::::"+request.getParameter("menu"));
			return "forward:/product/managerProduct.jsp";
		} else {
			System.out.println("menu  ::::"+request.getParameter("menu"));
			return "forward:/product/listProduct.jsp";
		}
	}

}
