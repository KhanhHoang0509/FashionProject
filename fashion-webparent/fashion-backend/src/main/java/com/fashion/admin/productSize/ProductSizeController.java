package com.fashion.admin.productSize;

import com.fashion.admin.category.CategoryPageInfo;
import com.fashion.admin.category.CategoryService;
import com.fashion.fashioncommon.entity.Category;
import com.fashion.fashioncommon.entity.product.ProductSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class ProductSizeController {

    private String defaultRedirectURL = "redirect:/productSizes/page/1?sortField=name&sortDir=asc&categoryId=0";

    @Autowired
    private ProductSizeService productSizeService;

    @GetMapping("/productSizes")
    public String listFirstPage() {
        return defaultRedirectURL;
    }

    @GetMapping("/productSizes/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, String sortDir, String keyword, Model model) {
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }

        ProductSizePageInfo pageInfo = new ProductSizePageInfo();//CategoryPageInfo là đối tượng dùng để lưu trữ totalPages và totalElements. Vì listByPage trả về List<Category> chứ ko trả về Page<Category>
        List<ProductSize> listProductSizes = productSizeService.listByPage(pageInfo, pageNum, sortDir, keyword);

        long startCount = (pageNum - 1) * productSizeService.PRODUCT_SIZE__PER_PAGE + 1;
        long endCount = startCount + productSizeService.PRODUCT_SIZE__PER_PAGE - 1;
        if (endCount > pageInfo.getTotalElements()) {
            endCount = pageInfo.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", pageInfo.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", pageInfo.getTotalElements());
        model.addAttribute("listProductSizes", listProductSizes);
        model.addAttribute("sortField", "name");
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return "productSizes/productSizes";
    }

    @GetMapping("/productSizes/new")
    public String newSize(Model model) {
        List<ProductSize> listProductSizes = productSizeService.listSizeUsedInForm();//lấy ra tất cả size, chỉ cần đổ size lên dropdown nên sẽ gọi phương thức listSizeUsedInForm()

        model.addAttribute("productSize", new ProductSize());
        model.addAttribute("listProductSizes", listProductSizes);
        model.addAttribute("pageTitle", "Create New Category");

        return "productSizes/productSizes_form";
    }

    @PostMapping("/productSizes/save")
    public String saveSize(ProductSize productSize, RedirectAttributes ra) throws IOException {

        productSizeService.save(productSize);

        ra.addFlashAttribute("message", "Size has been saved successfully.");
        return "redirect:/productSizes";
    }


    @GetMapping("/productSizes/edit/{id}")
    public String editSize(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra) {
        try {
            ProductSize productSize = productSizeService.get(id);
            List<ProductSize> listProductSizes = productSizeService.listSizeUsedInForm();

            model.addAttribute("productSize", productSize);
            model.addAttribute("listProductSizes", listProductSizes);
            model.addAttribute("pageTitle", "Edit size (ID: " + id + ")");

            return "productSizes/productSizes_form";
        } catch (SizeNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return defaultRedirectURL;
        }
    }

}
