package com.fashion.fashioncommon.entity.product;

import com.fashion.fashioncommon.entity.Category;
import com.fashion.fashioncommon.entity.IdBasedEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "sizes")
public class ProductSize extends IdBasedEntity {

    private String name;

    @ManyToMany(mappedBy = "sizes")
    private List<Product> products;


    @OneToOne//1-1
    @JoinColumn(name = "parent_id")
    private ProductSize parent;

    @Column(name = "all_parent_ids", length = 256, nullable = true)
    private String allParentIDs;

    @OneToMany(mappedBy = "parent")
    @OrderBy("name asc")
    private Set<ProductSize> children = new HashSet<>();


    public ProductSize() {
    }

    public ProductSize(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    public static ProductSize copyIdAndName(ProductSize productSize) {
        ProductSize copyProductSize = new ProductSize();
        copyProductSize.setId(productSize.getId());
        copyProductSize.setName(productSize.getName());


        return copyProductSize;
    }

    public static ProductSize copyIdAndName(Integer id, String name) {
        ProductSize copyProductSize = new ProductSize();
        copyProductSize.setId(id);
        copyProductSize.setName(name);

        return copyProductSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Set<ProductSize> getChildren() {

        return children;
    }

    public void setChildren(Set<ProductSize> children) {
        this.children = children;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    @Transient
    private boolean hasChildren;

    public String getAllParentIDs() {
        return allParentIDs;
    }

    public void setAllParentIDs(String allParentIDs) {
        this.allParentIDs = allParentIDs;
    }

    public ProductSize getParent() {
        return parent;
    }

    public void setParent(ProductSize parent) {
        this.parent = parent;
    }

    public static ProductSize copyFull(ProductSize productSize) {
        ProductSize copyProductSize = new ProductSize();
        copyProductSize.setId(productSize.getId());
        copyProductSize.setName(productSize.getName());
        copyProductSize.setHasChildren(productSize.getChildren().size() > 0);//nếu size > 0 -->có con


        return copyProductSize;
    }

    public static ProductSize copyFull(ProductSize productSize, String name) {
        ProductSize copyProductSize = ProductSize.copyFull(productSize);
        copyProductSize.setName(name);
        return copyProductSize;
    }

}
