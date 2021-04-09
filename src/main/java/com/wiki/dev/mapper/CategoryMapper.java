package com.wiki.dev.mapper;

import com.wiki.dev.dto.CategoryDto;
import com.wiki.dev.entity.Category;
import com.wiki.dev.entity.Post;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "postCount", expression = "java(mapPosts(category.getPosts()))")
    CategoryDto mapCategoryToDto(Category category);

    default Integer mapPosts(List<Post> postCount) {
        return postCount.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Category mapDtoToCategory(CategoryDto categoryDto);
}
