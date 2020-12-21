package com.mraleksmay.projects.download_manager.plugin.youtube.model.plugin;

import com.mraleksmay.projects.download_manager.plugin.annotations.DMPlugin;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.category.CategoryMapper;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.category.SCategoryMapper;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.download.DownloadMapper;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.download.SDownloadMapper;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.group.GroupMapper;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.plugin.PluginMapper;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.group.Group;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;
import com.mraleksmay.projects.download_manager.plugin.youtube.communication.mapper.YoutubeSCategoryMapper;
import com.mraleksmay.projects.download_manager.plugin.youtube.communication.mapper.YoutubeSDownloadMapper;
import com.mraleksmay.projects.download_manager.plugin.youtube.io.mapper.category.YoutubeCategoryMapper;
import com.mraleksmay.projects.download_manager.plugin.youtube.io.mapper.download.YoutubeDownloadMapper;
import com.mraleksmay.projects.download_manager.plugin.youtube.io.mapper.group.YoutubeGroupMapper;
import com.mraleksmay.projects.download_manager.plugin.youtube.io.mapper.plugin.YoutubePluginMapper;
import com.mraleksmay.projects.download_manager.plugin.youtube.model.category.YoutubeCategory;
import com.mraleksmay.projects.download_manager.plugin.youtube.model.group.YoutubeGroup;
import com.mraleksmay.projects.download_manager.plugin.youtube.view.dialog.AddYoutubeDownloadDialog;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;


@DMPlugin
public class YoutubePlugin extends Plugin {
    {
        add("youtube.com");
        add("youtu.be");
        setPriority(10000);
    }


    public YoutubePlugin() {
        this("YOUTUBE", "YOUTUBE");
    }

    private YoutubePlugin(String id, String name) {
        super(id, name);
        setPSID("PLUGIN-YOUTUBE-" + id);
    }


    @Override
    public Plugin init() throws IOException {
        final Plugin plugin = this;
        plugin.setUserCanDownloadLater(false);

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

        return this;
    }

    @Override
    public boolean canDeserialize(String psid, String version) {
        return false;
    }

    @Override
    public boolean canDeserializeIgnoreVersion(String psid) {
        return false;
    }

    @Override
    public JDialog getAddDownloadDialogWindow(Class<?>[] constructorArgsType, Object[] constructorParams) throws Exception {
        final Class<? extends JDialog> dialogClass = this.getAddDownloadDialogWindowClass();
        final Constructor<? extends JDialog> dialogConstructor = dialogClass.getDeclaredConstructor(constructorArgsType);
        final JDialog dialog = dialogConstructor.newInstance(constructorParams);

        return dialog;
    }

    @Override
    public Class<? extends JDialog> getAddDownloadDialogWindowClass() {
        return AddYoutubeDownloadDialog.class;
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

    @Override
    public Class<? extends Plugin> getPluginClass() {
        return this.getClass();
    }

    @Override
    public PluginMapper getPluginMapper() {
        return new YoutubePluginMapper();
    }

    @Override
    public GroupMapper getGroupMapper() {
        return new YoutubeGroupMapper();
    }

    @Override
    public CategoryMapper getCategoryMapper() {
        return new YoutubeCategoryMapper();
    }

    @Override
    public DownloadMapper getDownloadMapper() {
        return new YoutubeDownloadMapper();
    }

    @Override
    public SDownloadMapper getSDownloadMapper() {
        return new YoutubeSDownloadMapper(new YoutubeSCategoryMapper());
    }

    @Override
    public SCategoryMapper getSCategoryMapper() {
        return new YoutubeSCategoryMapper();
    }

    @Override
    public Group getDefGroup() {
        return new YoutubeGroup();
    }

    @Override
    public Category getDefCategory() {
        return new YoutubeCategory();
    }
}
