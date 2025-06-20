package io.github.kings1990.githubRandomProject.util;

import icons.PluginIcons;

import javax.swing.*;
import java.util.LinkedHashMap;

public class IconUtil {
    public static LinkedHashMap<String, Icon> iconMap = new LinkedHashMap<>();

    static {
        iconMap.put("GitHub", PluginIcons.githubProject);
    }

    public static Icon getIcon(String iconName) {
        return iconMap.get(iconName);
    }
}
