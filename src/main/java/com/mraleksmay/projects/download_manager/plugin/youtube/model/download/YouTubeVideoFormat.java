package com.mraleksmay.projects.download_manager.plugin.youtube.model.download;

import com.github.kiulian.downloader.model.formats.AudioFormat;
import com.github.kiulian.downloader.model.formats.AudioVideoFormat;
import com.github.kiulian.downloader.model.formats.Format;
import com.github.kiulian.downloader.model.formats.VideoFormat;
import com.mraleksmay.projects.download_manager.plugin.annotations.DMPlugin;

@DMPlugin
public class YouTubeVideoFormat {
    private Format format;


    public YouTubeVideoFormat(Format format) {
        this.format = format;
    }


    public Format getFormat() {
        return format;
    }

    @Override
    public String toString() {
        if (AudioVideoFormat.class.equals(getFormat().getClass())) {
            return ((AudioVideoFormat) getFormat()).qualityLabel() + " (video & audio)";
        }
        if (VideoFormat.class.equals(getFormat().getClass())) {
            return ((VideoFormat) getFormat()).qualityLabel() + " (only video)";
        }
        if (AudioFormat.class.equals(getFormat().getClass())) {
            return ((AudioFormat) getFormat()).audioSampleRate().toString() + " (only audio)";
        }

        return getFormat().bitrate().toString();
    }
}
