package com.mraleksmay.projects.download_manager.plugin.youtube.io.mapper.download;

import com.mraleksmay.projects.download_manager.plugin.io.dto.category.CategoryDto;
import com.mraleksmay.projects.download_manager.plugin.io.dto.download.AuthenticationDataDto;
import com.mraleksmay.projects.download_manager.plugin.io.dto.download.DownloadDto;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.download.DownloadMapper;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.download.AuthenticationData;
import com.mraleksmay.projects.download_manager.plugin.model.download.Download;
import com.mraleksmay.projects.download_manager.plugin.youtube.io.dto.download.YoutubeDownloadDto;
import com.mraleksmay.projects.download_manager.plugin.youtube.io.mapper.category.YoutubeCategoryMapper;
import com.mraleksmay.projects.download_manager.plugin.youtube.model.download.YoutubeDownload;

import java.io.IOException;

public class YoutubeDownloadMapper implements DownloadMapper {
    @Override
    public DownloadDto toDto(Download download) {
        final CategoryDto categoryDto = new YoutubeCategoryMapper().toDto(download.getCategory());
        final AuthenticationDataDto authDataDto = new YoutubeAuthenticationDataMapper().toDto(download.getAuthData());

        try {
            return new YoutubeDownloadDto()
                    .setDSID(download.getDSID())
                    .setFileName(download.getFileName())
                    .setUrl(download.getUrl())
                    .setOutputDir(download.getOutputDir())
                    .setTempDir(download.getTempDir())
                    .setExtension(download.getExtension())
                    .setPluginPSID(download.getCategory().getPlugin().getPSID())
                    .setCategoryCSID(categoryDto)
                    .setAuthData(authDataDto)
                    .setFullSize(download.getFullSize())
                    .setCurrentSize(download.getCurrentSize())
                    .setCreationTime(download.getCreationTime());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Download fromDto(DownloadDto downloadDto, Category category) {
        final AuthenticationData authData = new YoutubeAuthenticationDataMapper().fromDto(downloadDto.getAuthData());

        try {
            return new YoutubeDownload()
                    .setDSID(downloadDto.getDSID())
                    .setFileName(downloadDto.getFileName())
                    .setUrl(downloadDto.getUrl())
                    .setOutputDir(downloadDto.getOutputDir())
                    .setTempDir(downloadDto.getTempDir())
                    .setExtension(downloadDto.getExtension())
                    .setCategory(category)
                    .setAuthData(authData)
                    .setFullSize(downloadDto.getFullSize())
                    .setCurrentSize(downloadDto.getCurrentSize())
                    .setCreationTime(downloadDto.getCreationTime());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
