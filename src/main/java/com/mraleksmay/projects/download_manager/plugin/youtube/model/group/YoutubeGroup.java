package com.mraleksmay.projects.download_manager.plugin.youtube.model.group;


import com.mraleksmay.projects.download_manager.plugin.annotations.DMPlugin;
import com.mraleksmay.projects.download_manager.plugin.model.group.Group;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;

@DMPlugin
public class YoutubeGroup extends Group {
    public YoutubeGroup() {
    }

    public YoutubeGroup(String id, String name, Plugin parent) {
        super(id, name, parent);
        setGSID("PLUGIN-YOUTUBE-GROUP-YOUTUBE-" + id);
    }
}
