package com.fashion.client.category;


import com.fashion.fashioncommon.entity.Category;
import com.fashion.fashioncommon.entity.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public List<Category> listNoChildrenCategories() {
		List<Category> listNoChildrenCategories = new ArrayList<>();

		List<Category> listEnabledCategories = categoryRepository.findAllEnabled();

		for (Category category : listEnabledCategories) {
			Set<Category> children = category.getChildren();
			if (children == null || children.size() == 0) {
				listNoChildrenCategories.add(category);
			}
		}
		return listNoChildrenCategories;
	}

	public Category getCategory(String alias) throws CategoryNotFoundException {
		Category category = categoryRepository.findByAliasEnabled(alias);
		if (category == null) {
			throw new CategoryNotFoundException("Could not find any categories with alias " + alias);
		}
		return category;
	}

	public List<Category> getCategoryParents(Category child) {
		List<Category> listParents = new ArrayList<>();
		Category parent = child.getParent();

		while (parent != null) {
			listParents.add(0, parent);
			parent = parent.getParent();
		}

		listParents.add(child);
		return listParents;
	}












	public List<Category> listCategoriesUsedInForm() {//phương thức dùng cho form category, sẽ trả id và name của category
		List<Category> categoriesUsedInForm = new ArrayList<>();

		Iterable<Category> categoriesInDB = categoryRepository.findRootCategories(Sort.by("name").ascending());

		for (Category category : categoriesInDB) {
			categoriesUsedInForm.add(Category.copyIdAndName(category));//chỉ copy thuộc tính id và name -->vì chỉ cần hiển thị lên dropdown

			Set<Category> children = sortSubCategories(category.getChildren());

			for (Category subCategory : children) {
				String name = "--" + subCategory.getName();
				categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

				listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
			}
		}

		return categoriesUsedInForm;
	}

	private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = sortSubCategories(parent.getChildren());

		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();

			categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

			listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
		}
	}

	private SortedSet<Category> sortSubCategories(Set<Category> children) {
		return sortSubCategories(children, "asc");
	}

	private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
		SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {//Anonymous function, vì đây là TreeSet nên phải khai báo tiêu chí sắp xếp các đối tượng category cho nó, nếu ko sẽ báo lỗi vì ko biết sắp xếp theo tiêu chí gì
			@Override
			public int compare(Category cat1, Category cat2) {//phương thức compare trả về int
				if (sortDir.equals("asc")) {
					return cat1.getName().compareTo(cat2.getName());//name là String -->phải dùng compareTo để so sánh, vì compareTo trả về int(ko dùng .equals vì .equals trả về boolean)
				} else {
					return cat2.getName().compareTo(cat1.getName());
				}
			}
		});

		sortedChildren.addAll(children);

		return sortedChildren;
	}
}
