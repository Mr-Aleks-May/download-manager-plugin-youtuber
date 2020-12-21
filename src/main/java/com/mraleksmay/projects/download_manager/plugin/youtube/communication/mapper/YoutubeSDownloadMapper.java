package com.mraleksmay.projects.download_manager.plugin.youtube.communication.mapper;


import com.mraleksmay.projects.download_manager.plugin.io.dto.download.SDownloadDto;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.download.SDownloadMapper;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.download.Download;
import com.mraleksmay.projects.download_manager.plugin.youtube.model.download.YoutubeDownload;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class YoutubeSDownloadMapper implements SDownloadMapper {
    private final YoutubeSCategoryMapper categoryMapper;


    public YoutubeSDownloadMapper(YoutubeSCategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }


    @Override
    public SDownloadDto toDto(Download download) {
        try {
            return new SDownloadDto()
                    .setDSID(download.getDSID())
                    .setFileName(download.getFileName())
                    .setExtension(download.getExtension())
                    .setTmpDir(download.getTempDir().getCanonicalPath())
                    .setOutputDir(download.getOutputDir().getCanonicalPath())
                    .setUrl(download.getUrl().toString())
                    .setCategoryDto(categoryMapper.toDto(download.getCategory()))
                    .setCreationTime(download.getCreationTime());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Download fromDto(SDownloadDto downloadDto, Category category) {
        try {
            return new YoutubeDownload()
                    .setDSID(downloadDto.getDSID())
                    .setFileName(downloadDto.getFileName())
                    .setExtension(downloadDto.getExtension())
                    .setTempDir(new File(downloadDto.getTmpDir()))
                    .setOutputDir(new File(downloadDto.getOutputDir()))
                    .setUrl(new URL(downloadDto.getUrl()))
                    .setCategory(category)
                    .setCreationTime(downloadDto.getCreationTime());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
