package com.en.remembrance.services;

import com.en.remembrance.domain.Category;
import com.en.remembrance.dtos.CategoryModel;
import com.en.remembrance.exceptions.CategoryNotFoundException;
import com.en.remembrance.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category getByIdOrElseThrow(long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
    }

    public List<CategoryModel> getAllCategoryModels() {
        List<Category> categories = categoryRepository.findAll();

        if (!CollectionUtils.isEmpty(categories)) {
            return categories.stream().map(CategoryModel::new).collect(Collectors.toList());
        }

        return null;
    }

}
