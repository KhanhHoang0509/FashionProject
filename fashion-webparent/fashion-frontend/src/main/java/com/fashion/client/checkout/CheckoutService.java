package com.fashion.client.checkout;

import com.fashion.fashioncommon.entity.CartItem;
import com.fashion.fashioncommon.entity.product.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutService {
    private static final int DIM_DIVISOR = 139;

    public CheckoutInfo prepareCheckout(List<CartItem> cartItems) {
        CheckoutInfo checkoutInfo = new CheckoutInfo();

        float productCost = calculateProductCost(cartItems);
        float productTotal = calculateProductTotal(cartItems);
        float shippingCostTotal = calculateShippingCost(cartItems);
        float paymentTotal = productTotal + shippingCostTotal;

        checkoutInfo.setProductCost(productCost);
        checkoutInfo.setProductTotal(productTotal);
        checkoutInfo.setShippingCostTotal(shippingCostTotal);
        checkoutInfo.setPaymentTotal(paymentTotal);

        return checkoutInfo;
    }

    private float calculateProductCost(List<CartItem> cartItems) {
        float cost = 0.0f;

        for (CartItem cartItem : cartItems) {
            cost += cartItem.getQuantity() + cartItem.getProduct().getCost();//gia mac dinh truoc khi ban
        }
        return cost;
    }

    private float calculateProductTotal(List<CartItem> cartItems) {
        float total = 0.0f;

        for (CartItem cartItem : cartItems) {
            total += cartItem.getSubtotal();
        }
        return total;
    }

    private float calculateShippingCost(List<CartItem> cartItems) {
        float shippingCostTotal = 0.0f;

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            float dimWeight = (product.getLength() * product.getHeight() * product.getWidth()) / DIM_DIVISOR;
            float finalWeight = Math.max(product.getWeight(), dimWeight);
            float shippingCost = finalWeight * cartItem.getQuantity();

            cartItem.setShippingCost(shippingCost);

            shippingCostTotal += shippingCost;
        }

        return shippingCostTotal;
    }
}
