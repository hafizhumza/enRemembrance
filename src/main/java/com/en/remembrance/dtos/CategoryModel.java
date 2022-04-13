package com.en.remembrance.dtos;

import com.en.remembrance.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryModel {

    private Long id;

    private String name;

    private String iconBase64;

    public CategoryModel(Category category) {
        id = category.getId();
        name = category.getName();
        iconBase64 = category.getIconBase64();
    }

}
