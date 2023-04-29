package com.fashion.client.fashion;

import com.fashion.client.product.ProductRepository;
import com.fashion.fashioncommon.entity.CartItem;
import com.fashion.fashioncommon.entity.Customer;
import com.fashion.fashioncommon.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class FashionService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<CartItem> listCartItems(Customer customer) {
        return cartItemRepository.findByCustomer(customer);
    }

    public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws FashionException {

        Integer updatedQuantity = quantity;
        Product product = new Product(productId);

        CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer, product);

        if (cartItem != null) {
            updatedQuantity = cartItem.getQuantity() + quantity;
        }

        if (updatedQuantity > 5) {
            throw new FashionException("Could not add more " + quantity + " item(s)"
                    + " because there's already " + cartItem.getQuantity() + " item(s) "
                    + "in your cart. Maximum allowed quantity is 5.");
        } else {
            cartItem = new CartItem();
            cartItem.setCustomer(customer);
            cartItem.setProduct(product);
        }

        cartItem.setQuantity(updatedQuantity);

        cartItemRepository.save(cartItem);

        return updatedQuantity;
    }

    public float updateQuantity(Integer productId, Integer quantity, Customer customer) {

        cartItemRepository.updateQuantity(quantity, customer.getId(), productId);
        Product product = productRepository.findById(productId).get();

        float subtotal = product.getDiscountPrice() * quantity;

        return subtotal;
    }

    public void removeProduct(Integer productId, Customer customer) {
        cartItemRepository.deleteByCustomerAndProduct(customer.getId(), productId);
    }

    public void deleteByCustomer(Customer customer) {
        cartItemRepository.deleteByCustomer(customer.getId());
    }
}
