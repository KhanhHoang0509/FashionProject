package com.fashion.client.fashion;

import com.fashion.fashioncommon.entity.CartItem;
import com.fashion.fashioncommon.entity.Customer;
import com.fashion.fashioncommon.entity.product.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartItemRepository extends CrudRepository<CartItem, Integer> {
    List<CartItem> findByCustomer(Customer customer);

    CartItem findByCustomerAndProduct(Customer customer, Product product);

    @Modifying
    @Query("update CartItem c set c.quantity = ?1 where c.quantity = ?2 and c.product.id = ?3")
    void updateQuantity(Integer quantity, Integer id, Integer productId);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.customer.id = ?1 AND c.product.id = ?2")
    void deleteByCustomerAndProduct(Integer id, Integer productId);

    @Modifying
    @Query("DELETE CartItem c WHERE c.customer.id = ?1")
    void deleteByCustomer(Integer id);
}
