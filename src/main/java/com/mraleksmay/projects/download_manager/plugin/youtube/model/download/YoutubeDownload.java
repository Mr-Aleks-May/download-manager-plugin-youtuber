package com.mraleksmay.projects.download_manager.plugin.youtube.model.download;


import com.mraleksmay.projects.download_manager.common.annotation.NotNull;
import com.mraleksmay.projects.download_manager.common.model.category.Category;
import com.mraleksmay.projects.download_manager.common.model.download.AuthenticationData;
import com.mraleksmay.projects.download_manager.common.model.download.Download;
import com.mraleksmay.projects.download_manager.plugin.annotations.DMPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@DMPlugin
public class YoutubeDownload extends Download {
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
}
