package com.mraleksmay.projects.download_manager.plugin.youtube.model.group;


import com.mraleksmay.projects.download_manager.common.model.group.Group;
import com.mraleksmay.projects.download_manager.common.model.plugin.Plugin;
import com.mraleksmay.projects.download_manager.plugin.annotations.DMPlugin;

@DMPlugin
public class YoutubeGroup extends Group {
    public YoutubeGroup() {
    }

    public YoutubeGroup(String id, String name, Plugin parent) {
        super(id, name, parent);
        setSerializableId("PLUGIN-YOUTUBE-GROUP-YOUTUBE-" + id);
    }
}
