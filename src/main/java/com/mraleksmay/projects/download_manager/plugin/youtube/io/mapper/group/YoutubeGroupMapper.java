package com.mraleksmay.projects.download_manager.plugin.youtube.io.mapper.group;

import com.mraleksmay.projects.download_manager.plugin.io.dto.category.CategoryDto;
import com.mraleksmay.projects.download_manager.plugin.io.dto.group.GroupDto;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.group.GroupMapper;
import com.mraleksmay.projects.download_manager.plugin.model.group.Group;
import com.mraleksmay.projects.download_manager.plugin.youtube.io.dto.group.YoutubeGroupDto;
import com.mraleksmay.projects.download_manager.plugin.youtube.io.mapper.category.YoutubeCategoryMapper;

import java.util.List;
import java.util.stream.Collectors;

public class YoutubeGroupMapper implements GroupMapper {
    @Override
    public GroupDto toDto(Group group) {
        final YoutubeCategoryMapper baseCategoryMapper = new YoutubeCategoryMapper();
        final List<CategoryDto> categoriesList = group.getCategories().stream()
                .map(c -> baseCategoryMapper.toDto(c))
                .collect(Collectors.toUnmodifiableList());

        return new YoutubeGroupDto()
                .setGSID(group.getGSID())
                .setCategories(categoriesList);
    }

    @Override
    public Group fromDto(GroupDto groupDto) {
        return null;
    }
}
