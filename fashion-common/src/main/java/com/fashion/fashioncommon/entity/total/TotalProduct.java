package com.fashion.fashioncommon.entity.total;

import com.fashion.fashioncommon.entity.IdBasedEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "total_product")
public class TotalProduct extends IdBasedEntity {

    private int totalProduct;

    public TotalProduct() {
    }

    public TotalProduct(int totalProduct) {
        this.totalProduct = totalProduct;
    }

    public int getTotalProduct() {
        return totalProduct;
    }

    public void setTotalProduct(int totalProduct) {
        this.totalProduct = totalProduct;
    }
}
