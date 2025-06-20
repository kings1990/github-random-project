package io.github.kings1990.githubRandomProject.util;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;

public class NotificationUtil {

    public static Notification buildInfo(String title, String content) {
        return NotificationGroupManager.getInstance().getNotificationGroup("githubProject").createNotification(title, content, NotificationType.INFORMATION);
    }

    public static Notification buildInfo(String content) {
        return buildInfo("githubProject", content);
    }


    public static Notification buildUpdate(String content) {
        return NotificationGroupManager.getInstance().getNotificationGroup("githubProject").createNotification("GitHub Random Project", content, NotificationType.IDE_UPDATE);
    }

    public static Notification buildError(String title, String content) {
        return NotificationGroupManager.getInstance().getNotificationGroup("githubProject").createNotification(title, content, NotificationType.ERROR);
    }

    public static Notification buildError(String content) {
        return buildError("githubProject", content);
    }
}
