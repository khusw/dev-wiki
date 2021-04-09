package com.wiki.dev.mapper;

import com.wiki.dev.dto.PostRequest;
import com.wiki.dev.dto.PostResponse;
import com.wiki.dev.entity.Category;
import com.wiki.dev.entity.Post;
import com.wiki.dev.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "postName", source = "postRequest.postName")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "description", source = "postRequest.description")
    Post map(PostRequest postRequest, Category category, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "postName", source = "postName")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "userName", source = "user.username")
    PostResponse mapToDto(Post post);
}
