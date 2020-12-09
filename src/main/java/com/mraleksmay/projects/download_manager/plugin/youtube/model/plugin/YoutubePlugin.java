package com.mraleksmay.projects.download_manager.plugin.youtube.model.plugin;

import com.mraleksmay.projects.download_manager.common.model.category.Category;
import com.mraleksmay.projects.download_manager.common.model.group.Group;
import com.mraleksmay.projects.download_manager.common.model.plugin.Plugin;
import com.mraleksmay.projects.download_manager.plugin.annotations.DMPlugin;
import com.mraleksmay.projects.download_manager.plugin.youtube.model.category.YoutubeCategory;
import com.mraleksmay.projects.download_manager.plugin.youtube.model.group.YoutubeGroup;
import com.mraleksmay.projects.download_manager.plugin.youtube.view.dialog.AddYoutubeDownloadDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@DMPlugin
public class YoutubePlugin extends Plugin {
    {
        addSupportedWebSiteRegex("youtube.com");
        addSupportedWebSiteRegex("youtu.be");
        setPluginPriority(10000);
    }


    public YoutubePlugin() {
        this("YOUTUBE", "YOUTUBE");
    }

    private YoutubePlugin(String id, String name) {
        super(id, name);
        setSerializableId("PLUGIN-YOUTUBE-" + id);
    }


    @Override
    public void init() throws IOException {
        final Plugin plugin = this;
        plugin.setUserCanDownloadLater(false);
        plugin.setDownloadDialogClass(AddYoutubeDownloadDialog.class);

        final Group all = new YoutubeGroup("YOUTUBE", "Youtube", plugin);
        final String userHomeDir = System.getenv("HOME");

        plugin.add(all);

        final List<Category> categories = new ArrayList<>();

        String id, name, contentType, extensions;
        File tmpDir, outputDir;
        Category category;

        // CATEGORY ALL
        id = "ALL";
        name = "All";
        contentType = "all";
        extensions = "";
        tmpDir = new File("");
        outputDir = new File("");
        category = new YoutubeCategory(id, name, contentType, extensions, outputDir, plugin);
        category.setEditable(false);
        category.setRemovable(false);
        categories.add(category);

        // CATEGORY VIDEO
        id = "VIDEO";
        name = "Video";
        contentType = "video";
        extensions = "mp4";
        tmpDir = new File(userHomeDir + "/Downloader/Video/tmp");
        outputDir = new File(userHomeDir + "/Downloader/Video");
        category = new YoutubeCategory(id, name, contentType, extensions, outputDir, plugin);
        category.setEditable(true);
        category.setRemovable(true);
        categories.add(category);

        // CATEGORY MUSIC
        id = "AUDIO";
        name = "Audio";
        contentType = "audio";
        extensions = "mp3";
        tmpDir = new File(userHomeDir + "/Downloader/Music/tmp");
        outputDir = new File(userHomeDir + "/Downloader/Music");
        category = new YoutubeCategory(id, name, contentType, extensions, outputDir, plugin);
        category.setEditable(true);
        category.setRemovable(true);
        categories.add(category);

        // CATEGORY OTHER
        id = "OTHER";
        name = "Other";
        contentType = "*";
        extensions = "*";
        tmpDir = new File(userHomeDir + "/Downloader/Other/tmp");
        outputDir = new File(userHomeDir + "/Downloader/Other");
        category = new YoutubeCategory(id, name, contentType, extensions, outputDir, plugin);
        category.setEditable(false);
        category.setRemovable(false);
        categories.add(category);


        for (Category categ0ry : categories) {
            for (Group group : plugin.getGroups()) {
                group.add(categ0ry);
            }
        }
    }


    @Override
    public Category getDefaultCategory() {
        for (Group group : this.getGroups()) {
            if ("ALL".equalsIgnoreCase(group.getId())) {
                for (Category category : group.getCategories()) {
                    if ("OTHER".equalsIgnoreCase(category.getId())) {
                        return category;
                    }
                }
            }
        }

        return null;
    }
}
