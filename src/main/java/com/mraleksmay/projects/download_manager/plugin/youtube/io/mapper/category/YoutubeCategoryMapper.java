package com.mraleksmay.projects.download_manager.plugin.youtube.io.mapper.category;

import com.mraleksmay.projects.download_manager.plugin.io.dto.category.CategoryDto;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.category.CategoryMapper;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.youtube.io.dto.category.YoutubeCategoryDto;
import com.mraleksmay.projects.download_manager.plugin.youtube.model.category.YoutubeCategory;

public class YoutubeCategoryMapper implements CategoryMapper {

    @Override
    public CategoryDto toDto(Category category) {
        return new YoutubeCategoryDto()
                .setCSID(category.getCSID())
                .setId(category.getId())
                .setName(category.getName())
                .setContentType(category.getContentType())
                .setOutputDir(category.getOutputDir())
                .setContentType(category.getComments());
    }

    @Override
    public Category fromDto(CategoryDto categoryDto) {
        return new YoutubeCategory()
                .setCSID(categoryDto.getCSID())
                .setId(categoryDto.getId())
                .setName(categoryDto.getName())
                .setContentType(categoryDto.getContentType())
                .setOutputDir(categoryDto.getOutputDir())
                .setContentType(categoryDto.getComments());
    }
}
