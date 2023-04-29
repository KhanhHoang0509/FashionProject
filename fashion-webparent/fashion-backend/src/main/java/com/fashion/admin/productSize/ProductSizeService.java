package com.fashion.admin.productSize;

import com.fashion.admin.product.ProductSizeRepository;

import com.fashion.fashioncommon.entity.Category;
import com.fashion.fashioncommon.entity.product.ProductSize;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ProductSizeService {
    public static final int PRODUCT_SIZE__PER_PAGE = 10;

    @Autowired
    private ProductSizeRepository repo;

    public List<ProductSize> listAll() {
        return (List<ProductSize>) repo.findAll();
    }


    public List<ProductSize> listSizeUsedInForm() {//phương thức dùng cho form size, sẽ trả id và name của category
        List<ProductSize> productSizesUsedInForm = new ArrayList<>();

        Iterable<ProductSize> sizeInDB = repo.findRootSizes();

        for (ProductSize productSize : sizeInDB) {
            productSizesUsedInForm.add(ProductSize.copyIdAndName(productSize));

            Set<ProductSize> children = sortSubProductSizes(productSize.getChildren());

            for (ProductSize subProductSize : children) {
                String name = "--" + subProductSize.getName();
                productSizesUsedInForm.add(ProductSize.copyIdAndName(subProductSize.getId(), name));

                listSubProductSizesUsedInForm(productSizesUsedInForm, subProductSize, 1);
            }
        }
        return productSizesUsedInForm;
    }

    private void listSubProductSizesUsedInForm(List<ProductSize> productSizesUsedInForm, ProductSize parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<ProductSize> children = sortSubProductSizes(parent.getChildren());

        for (ProductSize subProductSize : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subProductSize.getName();

            productSizesUsedInForm.add(ProductSize.copyIdAndName(subProductSize.getId(), name));

            listSubProductSizesUsedInForm(productSizesUsedInForm, subProductSize, newSubLevel);
        }
    }

    private SortedSet<ProductSize> sortSubProductSizes(Set<ProductSize> children) {

        return sortSubProductSizes(children, "asc");
    }

    private SortedSet<ProductSize> sortSubProductSizes(Set<ProductSize> children, String sortDir) {
        SortedSet<ProductSize> sortedChildren = new TreeSet<>(new Comparator<ProductSize>() {
            @Override
            public int compare(ProductSize o1, ProductSize o2) {
                if (sortDir.equals("asc")) {
                    return o1.getName().compareTo(o2.getName());//name là String -->phải dùng compareTo để so sánh, vì compareTo trả về int(ko dùng .equals vì .equals trả về boolean)
                } else {
                    return o2.getName().compareTo(o1.getName());
                }
            }
        });
        sortedChildren.addAll(children);

        return sortedChildren;
    }


    public ProductSize save(ProductSize productSize) {
        ProductSize parent = productSize.getParent();

        if (parent != null) {
            String allParentIds = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
            allParentIds += String.valueOf(parent.getId()) + "-";
            productSize.setAllParentIDs(allParentIds);
        }

        return repo.save(productSize);
    }

    public ProductSize get(Integer id) throws SizeNotFoundException {
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new SizeNotFoundException("Could not find any size with ID " + id);
        }
    }

    public List<ProductSize> listByPage(ProductSizePageInfo pageInfo, int pageNum, String sortDir, String keyword) {
        Sort sort = Sort.by("name");

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_SIZE__PER_PAGE, sort);//1 trang sẽ có 4 root categories -->có nghĩa là 4 categories cao nhất(ko có cha)

        Page<ProductSize> pageProductSizes = null;

        if (keyword != null && !keyword.isEmpty()) {
            pageProductSizes = repo.search(keyword, pageable);//nếu có nhập keyword thì tìm theo keyword, lưu ý khi tìm theo keyword sẽ ko cần phân cấp theo thứ tự các categories
        } else {
            pageProductSizes = repo.findRootProductSizes(pageable);//ko nhập keyword thì tìm tất cả, sẽ phân cấp theo thứ tự các categories
        }

        List<ProductSize> rootProductSizes = pageProductSizes.getContent();

        pageInfo.setTotalElements(pageProductSizes.getTotalElements());
        pageInfo.setTotalPages(pageProductSizes.getTotalPages());

        if (keyword != null && !keyword.isEmpty()) {
            List<ProductSize> searchResult = pageProductSizes.getContent();//nếu có nhập keyword thì sẽ phân trang như users -->ko cần phân cấp theo thứ tự và ko cần thêm dấu -- vào mỗi cấp độ
            for (ProductSize productSize : searchResult) {
                productSize.setHasChildren(productSize.getChildren().size() > 0);//true -->có con, false -->ko có con
            }

            return searchResult;

        } else {
            return listHierarchicalProductSizes(rootProductSizes, sortDir);//ko nhập keyword thì tìm tất cả, phân cấp theo thứ tự và thêm dấu -- vào mỗi cấp độ
        }
    }

    private List<ProductSize> listHierarchicalProductSizes(List<ProductSize> rootProductSizes, String sortDir) {

        List<ProductSize> hierarchicalProductSizes = new ArrayList<>();

        for (ProductSize rootProductSize : rootProductSizes) {
            hierarchicalProductSizes.add(ProductSize.copyFull(rootProductSize));

            Set<ProductSize> children = sortSubProductSizes(rootProductSize.getChildren(), sortDir);

            for (ProductSize subProductSize : children) {
                String name = "--" + subProductSize.getName();
                hierarchicalProductSizes.add(ProductSize.copyFull(subProductSize, name));

                listSubHierarchicalProductSizes(hierarchicalProductSizes, subProductSize, 1, sortDir);
            }
        }
        return hierarchicalProductSizes;
    }

    private void listSubHierarchicalProductSizes(List<ProductSize> hierarchicalProductSizes, ProductSize parent, int subLevel, String sortDir) {

        Set<ProductSize> children = sortSubProductSizes(parent.getChildren(), sortDir);
        int newSubLevel = subLevel + 1;

        for (ProductSize subChildren : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subChildren.getName();

            hierarchicalProductSizes.add(ProductSize.copyFull(subChildren, name));

            listSubHierarchicalProductSizes(hierarchicalProductSizes, subChildren, newSubLevel, sortDir);
        }
    }
}
