package com.fashion.client;

import com.fashion.client.brand.BrandService;
import com.fashion.client.category.CategoryService;
import com.fashion.client.product.ProductService;
import com.fashion.fashioncommon.entity.Brand;
import com.fashion.fashioncommon.entity.Category;
import com.fashion.fashioncommon.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class MainController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private BrandService brandService;

	
	@GetMapping("")
	public String viewHomePage(Model model) {
		List<Product> listHotProducts = productService.listHotProducts();
		List<Brand> listBrands = brandService.listAll();
		List<Category> listCategories = categoryService.listNoChildrenCategories();

		model.addAttribute("listHotProducts", listHotProducts);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("listCategories", listCategories);

		return "index";
	}
	
	@GetMapping("/login")
	public String viewLoginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}
		
		return "redirect:/";
	}	
}
