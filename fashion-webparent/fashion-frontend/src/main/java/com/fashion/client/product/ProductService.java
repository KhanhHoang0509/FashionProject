package com.fashion.client.product;


import com.fashion.fashioncommon.entity.exception.ProductNotFoundException;
import com.fashion.fashioncommon.entity.product.Product;
import com.fashion.fashioncommon.entity.product.ProductSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

	public static final int PRODUCTS_PER_PAGE = 6;
	public static final int SEARCH_RESULTS_PER_PAGE = 10;

	@Autowired
	private ProductRepository repo;


	public Page<Product> listByCategory(int pageNum, Integer categoryId) {
		String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);

		return repo.listByCategory(categoryId, categoryIdMatch, pageable);
	}

	public Product getProduct(String alias) throws ProductNotFoundException {
		Product product = repo.findByAlias(alias);
		if (product == null) {
			throw new ProductNotFoundException("Could not find any product with alias " + alias);
		}

		return product;
	}

	public Page<Product> search(String keyword, int pageNum) {
		Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULTS_PER_PAGE);
		return repo.search(keyword, pageable);

	}

	public List<Product> listHotProducts() {
		return repo.findAllHotProducts();
	}

    public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword, Integer categoryId) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);

        Page<Product> page = null;

        if (keyword != null && !keyword.isEmpty()) {
            if (categoryId != null && categoryId > 0) {
                String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
                page = repo.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);//lấy tất cả product theo categoryId và keyword
            } else {
                page = repo.findAll(keyword, pageable);//lấy tất cả product theo keyword
            }
        } else {
            if (categoryId != null && categoryId > 0) {
                String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
                page = repo.findAllInCategory(categoryId, categoryIdMatch, pageable);//lấy tất cả product theo categoryId
            } else {
                page = repo.findAll(pageable);//lấy tất cả product
            }
        }

        return page;
    }

    public List<ProductSize> getProductSize() {
        return repo.findAllProductSize();
    }
}
