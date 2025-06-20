package icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public interface PluginIcons {

    Icon save = IconLoader.getIcon("/icon/save.svg", PluginIcons.class);

    Icon openNewTab = IconLoader.getIcon("/icon/openNewTab.svg", PluginIcons.class);

    Icon folder = IconLoader.getIcon("/icon/folder.svg", PluginIcons.class);

    Icon mark = IconLoader.getIcon("/icon/mark.svg", PluginIcons.class);
    
    Icon newFolder = IconLoader.getIcon("/icon/newFolder.svg", PluginIcons.class);

    Icon delete = IconLoader.getIcon("/icon/delete.svg", PluginIcons.class);

    Icon expandAll = IconLoader.getIcon("/icon/expandAll.svg", PluginIcons.class);

    Icon collapseAll = IconLoader.getIcon("/icon/collapseAll.svg", PluginIcons.class);

    Icon web = IconLoader.getIcon("/icon/web.svg", PluginIcons.class);

    Icon config = IconLoader.getIcon("/icon/config.svg", PluginIcons.class);

    Icon wechat = IconLoader.getIcon("/icon/wechat.svg", PluginIcons.class);
    
    Icon githubProject = IconLoader.getIcon("/icon/github.svg", PluginIcons.class);
}
