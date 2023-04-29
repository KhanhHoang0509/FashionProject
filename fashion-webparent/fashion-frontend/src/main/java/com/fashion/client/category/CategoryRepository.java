package com.fashion.client.category;


import com.fashion.fashioncommon.entity.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

	@Query("SELECT c FROM Category c WHERE c.enabled = true ORDER BY c.name ASC")
	public List<Category> findAllEnabled();

	@Query("SELECT c FROM Category c WHERE c.enabled = true AND c.alias = ?1")
	public Category findByAliasEnabled(String alias);

	@Query("SELECT c FROM Category c WHERE c.parent.id is NULL")//sao sánh với NULL là is
	List<Category> findRootCategories(Sort sort);
}
