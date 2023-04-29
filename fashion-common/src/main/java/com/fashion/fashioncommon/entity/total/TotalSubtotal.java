package com.fashion.fashioncommon.entity.total;

import com.fashion.fashioncommon.entity.IdBasedEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "total_subtotal")
public class TotalSubtotal extends IdBasedEntity {

    private int totalSubtotal;

    public TotalSubtotal() {
    }

    public TotalSubtotal(int totalSubtotal) {
        this.totalSubtotal = totalSubtotal;
    }

    public int getTotalSubtotal() {
        return totalSubtotal;
    }

    public void setTotalSubtotal(int totalSubtotal) {
        this.totalSubtotal = totalSubtotal;
    }
}
