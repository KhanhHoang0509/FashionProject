package com.fashion.admin.shippingrate;


import com.fashion.admin.product.ProductRepository;
import com.fashion.fashioncommon.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ShippingRateService {
	
	private static final int DIM_DIVISOR = 139;	
	@Autowired private ProductRepository productRepo;
	
	public float calculateShippingCost(Integer productId) {
		Product product = productRepo.findById(productId).get();
		
		float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
		float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;
		float shippingCost = finalWeight * 1;
		
		return shippingCost;
	}
}
