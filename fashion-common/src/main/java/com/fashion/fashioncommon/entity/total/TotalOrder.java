package com.fashion.fashioncommon.entity.total;

import com.fashion.fashioncommon.entity.IdBasedEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "total_order")
public class TotalOrder extends IdBasedEntity {

    private int totalOrder;

    public TotalOrder() {
    }

    public TotalOrder(int totalOrder) {
        this.totalOrder = totalOrder;
    }

    public int getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(int totalOrder) {
        this.totalOrder = totalOrder;
    }
}
