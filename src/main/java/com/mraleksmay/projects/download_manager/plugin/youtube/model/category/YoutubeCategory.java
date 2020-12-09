package com.mraleksmay.projects.download_manager.plugin.youtube.model.category;

import com.mraleksmay.projects.download_manager.common.model.category.Category;
import com.mraleksmay.projects.download_manager.common.model.plugin.Plugin;
import com.mraleksmay.projects.download_manager.common.model.user.User;
import com.mraleksmay.projects.download_manager.plugin.annotations.DMPlugin;

import java.io.File;
import java.io.IOException;

@DMPlugin
public class YoutubeCategory extends Category {

    public YoutubeCategory() {
    }

    public YoutubeCategory(String id, String name, String contentType, String extensions, File outputDir, Plugin parent) throws IOException {
        super(id, name, contentType, extensions, outputDir, parent);
        setSerializableId("PLUGIN-YOUTUBE-CATEGORY-YOUTUBE-" + id);
    }

    public YoutubeCategory(String id, String name, String contentType, String extensions, File outputDir, String comments, Plugin parent) throws IOException {
        super(id, name, contentType, extensions, outputDir, comments, parent);
        setSerializableId("PLUGIN-YOUTUBE-CATEGORY-YOUTUBE-" + id);
    }


    @Override
    public File getTmpDir() throws IOException {
        return User.getCurrentUser().getSettings().getDownloaderSettings().getTmpDir();
    }

    @Override
    public boolean isEditable() {
        return !getId().equals("ALL");
    }

    @Override
    public boolean isRemovable() {
        return !getId().equals("ALL");
    }
}
