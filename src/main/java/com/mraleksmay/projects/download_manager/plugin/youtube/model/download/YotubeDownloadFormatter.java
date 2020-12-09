package com.mraleksmay.projects.download_manager.plugin.youtube.model.download;


import com.mraleksmay.projects.download_manager.common.model.download.Download;
import com.mraleksmay.projects.download_manager.common.model.download.DownloadFormatter;
import com.mraleksmay.projects.download_manager.plugin.annotations.DMPlugin;

@DMPlugin
public class YotubeDownloadFormatter extends DownloadFormatter {
    public YotubeDownloadFormatter() {
    }

    public YotubeDownloadFormatter(Download download) {
        super(download);
    }
}
