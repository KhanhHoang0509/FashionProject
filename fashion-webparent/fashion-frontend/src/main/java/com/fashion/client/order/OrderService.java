package com.fashion.client.order;

import com.fashion.client.checkout.CheckoutInfo;
import com.fashion.fashioncommon.entity.CartItem;
import com.fashion.fashioncommon.entity.Customer;
import com.fashion.fashioncommon.entity.order.Order;
import com.fashion.fashioncommon.entity.order.OrderDetail;
import com.fashion.fashioncommon.entity.order.PaymentMethod;
import com.fashion.fashioncommon.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    public static final int ORDERS_PER_PAGE = 5;

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Customer customer, List<CartItem> cartItems, PaymentMethod paymentMethod, CheckoutInfo checkoutInfo) {
        Order newOrder = new Order();

        newOrder.setOrderTime(new Date());
        newOrder.setCustomer(customer);
        newOrder.setProductCost(checkoutInfo.getProductCost());
        newOrder.setSubtotal(checkoutInfo.getProductTotal());
        newOrder.setShippingCost(checkoutInfo.getShippingCostTotal());
        newOrder.setTax(0.0f);
        newOrder.setTotal(checkoutInfo.getPaymentTotal());
        newOrder.setPaymentMethod(paymentMethod);

        newOrder.copyAddressFromCustomer();

        Set<OrderDetail> orderDetails = newOrder.getOrderDetails();

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            OrderDetail orderDetail = new OrderDetail();//tạo orderDetail để lưu các thông tin của từng cartItem
            orderDetail.setOrder(newOrder);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setUnitPrice(product.getDiscountPrice());
            orderDetail.setProductCost(product.getCost() * cartItem.getQuantity());
            orderDetail.setSubtotal(cartItem.getSubtotal());
            orderDetail.setShippingCost(cartItem.getShippingCost());

            orderDetails.add(orderDetail);
        }
        return orderRepository.save(newOrder);
    }

    public Page<Order> listForCustomerByPage(Customer customer, int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);

        if (keyword != null) {
            return orderRepository.findAll(keyword, customer.getId(), pageable);
        }

        return orderRepository.findAll(customer.getId(), pageable);
    }

    public Order getOrder(Integer id, Customer customer) {
        return orderRepository.findByIdAndCustomer(id, customer);
    }
}
