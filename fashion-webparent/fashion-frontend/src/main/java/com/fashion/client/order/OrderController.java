package com.fashion.client.order;


import com.fashion.client.ControllerHelper;
import com.fashion.fashioncommon.entity.Customer;
import com.fashion.fashioncommon.entity.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private ControllerHelper controllerHelper;

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public String lístFirstPage(Model model, HttpServletRequest request) {
        return listOrdersByPage(model, request, 1, "orderTime", "desc", null);
    }

    @GetMapping("/orders/page/{pageNum}")
    private String listOrdersByPage(Model model, HttpServletRequest request,	@PathVariable(name = "pageNum") int pageNum, String sortField, String sortDir, String keyword) {
        Customer customer = controllerHelper.getAuthenticatedCustomer(request);

        Page<Order> page = orderService.listForCustomerByPage(customer, pageNum, sortField, sortDir, keyword);
        List<Order> listOrders = page.getContent();

        long startCount = (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
        long endCount = startCount + OrderService.ORDERS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listOrders", listOrders);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return "orders/orders_customer";
    }

    @GetMapping("/orders/detail/{id}")
    public String viewOrderDetails(Model model, @PathVariable(name = "id") Integer id, HttpServletRequest request) {
        Customer customer = controllerHelper.getAuthenticatedCustomer(request);
        Order order = orderService.getOrder(id, customer);

        model.addAttribute("order", order);

        return "orders/order_details_modal";
    }
}
