package com.fashion.client.fashion;

import com.fashion.client.ControllerHelper;
import com.fashion.fashioncommon.entity.CartItem;
import com.fashion.fashioncommon.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class FashionController {

    @Autowired
    private ControllerHelper controllerHelper;

    @Autowired
    private FashionService fashionService;

    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest httpServletRequest) {
        Customer customer = controllerHelper.getAuthenticatedCustomer(httpServletRequest);

        List<CartItem> cartItems = fashionService.listCartItems(customer);

        float estimatedTotal = 0.0F;

        for (CartItem cartItem : cartItems) {
            estimatedTotal += cartItem.getSubtotal();
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("estimatedTotal", estimatedTotal);

        return "cart/shopping_cart";
    }
}
