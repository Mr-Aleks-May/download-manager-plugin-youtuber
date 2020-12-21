package com.mraleksmay.projects.download_manager.plugin.youtube.communication.mapper;

import com.mraleksmay.projects.download_manager.plugin.io.dto.category.SCategoryDto;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.category.SCategoryMapper;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;
import com.mraleksmay.projects.download_manager.plugin.youtube.communication.dto.YoutubeSCategoryDto;
import com.mraleksmay.projects.download_manager.plugin.youtube.model.category.YoutubeCategory;

public class YoutubeSCategoryMapper implements SCategoryMapper {
    @Override
    public SCategoryDto toDto(Category category) {
        return new YoutubeSCategoryDto()
                .setCategoryCSID(category.getCSID())
                .setName(category.getName())
                .setPluginPSID(category.getPlugin().getPSID());
    }

    @Override
    public Category fromDto(SCategoryDto categoryDto, Plugin plugin) {
        return new YoutubeCategory()
                .setCSID(categoryDto.getCategoryCSID())
                .setName(categoryDto.getName())
                .setPlugin(plugin);
    }
}
