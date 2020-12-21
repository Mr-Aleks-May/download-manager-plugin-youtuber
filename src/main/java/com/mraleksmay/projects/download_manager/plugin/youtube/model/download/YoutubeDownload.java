package com.mraleksmay.projects.download_manager.plugin.youtube.model.download;


import com.mraleksmay.projects.download_manager.common.annotation.NotNull;
import com.mraleksmay.projects.download_manager.plugin.annotations.DMPlugin;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.download.AuthenticationData;
import com.mraleksmay.projects.download_manager.plugin.model.download.Download;
import com.mraleksmay.projects.download_manager.plugin.model.download.DownloadFormatter;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@DMPlugin
public class YoutubeDownload extends Download {
    private DownloadFormatter formatter = new YoutubeDownloadFormatter(this);
    private AuthenticationData authData;

    {
        setExtension("mp4");
    }


    public YoutubeDownload() {
    }

    public YoutubeDownload(Download download) throws IOException {
        super(download);
    }

    public YoutubeDownload(String fileName,
                           URL url,
                           File outputDir,
                           @NotNull final String extension,
                           AuthenticationData authData,
                           Category category,
                           long fullSize) throws IOException {
        super(fileName, url, outputDir, extension, authData, category, fullSize);
    }

    @Override
    public DownloadFormatter getFormatter() {
        return formatter;
    }

    @Override
    public Download setFormatter(DownloadFormatter formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public AuthenticationData getAuthData() {
        return this.authData;
    }

    @Override
    public Download setAuthData(String login, String password) {
        this.authData = new Base64AuthenticationData(login, password);
        return this;
    }

    @Override
    public Download setAuthData(AuthenticationData authData) {
        if (authData == null) {
            return this;
        }

        this.authData = new Base64AuthenticationData(authData.getLogin(), authData.getPassword());
        return this;
    }
}
