package com.mraleksmay.projects.download_manager.plugin.youtube.model.download;

import com.mraleksmay.projects.download_manager.plugin.annotations.DMPlugin;
import com.mraleksmay.projects.download_manager.plugin.model.download.Download;
import com.mraleksmay.projects.download_manager.plugin.model.download.DownloadFormatter;

@DMPlugin
public class YoutubeDownloadFormatter extends DownloadFormatter {
    public YoutubeDownloadFormatter() {
    }

    public YoutubeDownloadFormatter(Download download) {
        super(download);
    }
}
