package com.mraleksmay.projects.download_manager.plugin.youtube.io.mapper.plugin;

import com.mraleksmay.projects.download_manager.plugin.io.dto.group.GroupDto;
import com.mraleksmay.projects.download_manager.plugin.io.dto.plugin.PluginDto;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.plugin.PluginMapper;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;
import com.mraleksmay.projects.download_manager.plugin.youtube.io.dto.plugin.YoutubePluginDto;
import com.mraleksmay.projects.download_manager.plugin.youtube.io.mapper.group.YoutubeGroupMapper;

import java.util.List;
import java.util.stream.Collectors;

public class YoutubePluginMapper implements PluginMapper {
    @Override
    public PluginDto toDto(Plugin plugin) {
        final YoutubeGroupMapper baseGroupMapper = new YoutubeGroupMapper();
        final List<GroupDto> groupsDto = plugin.getGroups().stream()
                .map(g -> baseGroupMapper.toDto(g))
                .collect(Collectors.toUnmodifiableList());

        return new YoutubePluginDto()
                .setPSID(plugin.getPSID())
                .setVersion(plugin.getVersion())
                .setGroups(groupsDto);
    }

    @Override
    public Plugin fromDto(PluginDto pluginDto) {
        return null;
    }
}
