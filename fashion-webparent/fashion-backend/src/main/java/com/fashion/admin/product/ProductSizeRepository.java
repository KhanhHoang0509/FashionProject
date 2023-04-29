package com.fashion.admin.product;

import com.fashion.fashioncommon.entity.product.ProductSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProductSizeRepository extends PagingAndSortingRepository<ProductSize, Integer> {

    @Query("SELECT b FROM ProductSize b WHERE b.name LIKE %?1%")
    Page<ProductSize> findAll(String keyword, Pageable pageable);

    @Query("SELECT b FROM ProductSize b  WHERE b.parent.id is NULL")
    Iterable<ProductSize> findRootSizes();

    @Query("SELECT c FROM ProductSize c WHERE c.name LIKE %?1%")
    Page<ProductSize> search(String keyword, Pageable pageable);

    @Query("SELECT c FROM ProductSize c WHERE c.parent.id is NULL")
    Page<ProductSize> findRootProductSizes(Pageable pageable);
}
