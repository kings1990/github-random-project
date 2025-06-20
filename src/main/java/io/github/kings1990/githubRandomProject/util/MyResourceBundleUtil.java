package io.github.kings1990.githubRandomProject.util;

import java.util.ResourceBundle;

public class MyResourceBundleUtil {

    public static String getKey(String key) {
        try {
            return ResourceBundle.getBundle("messages/githubRandomProject").getString(key);
        } catch (Exception e) {
            return "";
        }
    }
}