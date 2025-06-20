package io.github.kings1990.githubRandomProject.update;

import cn.hutool.core.thread.ThreadUtil;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.application.ex.ApplicationEx;
import com.intellij.openapi.application.ex.ApplicationManagerEx;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.updateSettings.impl.pluginsAdvertisement.PluginsAdvertiser;
import icons.PluginIcons;
import io.github.kings1990.githubRandomProject.util.MyResourceBundleUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class WhatsNewActivity implements StartupActivity {

    @Override
    public void runActivity(@NotNull Project project) {
        PluginId rfrPluginId = PluginId.getId("com.github.kings1990.githubRandomProject");
        IdeaPluginDescriptor plugin = PluginManagerCore.getPlugin(rfrPluginId);
        if (plugin != null) {
            String currentVersion = plugin.getVersion();
            ThreadUtil.execAsync(() -> {
                String onLineLastStableVersion = LastVersionUtil.getOnlineLastVersion();
                if (StringUtils.isNotBlank(onLineLastStableVersion)) {
                    boolean checkUpdateFlag = checkUpdate(currentVersion, onLineLastStableVersion);
                    if (checkUpdateFlag) {
                        ApplicationManager.getApplication().invokeLater(() -> {
                            String noticeContent = MyResourceBundleUtil.getKey("WhatsNew");
                            NotificationGroupManager.getInstance().getNotificationGroup("githubProject").createNotification(noticeContent, NotificationType.INFORMATION)
                                    .addAction(new NotificationAction(MyResourceBundleUtil.getKey("Upgrade")) {
                                        @Override
                                        public void actionPerformed(@NotNull AnActionEvent e, @NotNull Notification notification) {
                                            try {
                                                Set<PluginId> pluginIds = new HashSet<>();
                                                pluginIds.add(rfrPluginId);
                                                PluginsAdvertiser.installAndEnable(project, pluginIds, true, () -> {
                                                    // restart
                                                    ApplicationEx app = ApplicationManagerEx.getApplicationEx();
                                                    app.invokeLater(() -> app.restart(true), ModalityState.NON_MODAL);
                                                });
                                            } catch (Exception ee) {

                                            }
                                        }
                                    })
                                    .setIcon(PluginIcons.githubProject)
                                    .notify(project);
                        });
                    }
                }
            });

        }


    }

    public static boolean checkUpdate(String currentVersion, String onLineLastStableVersion) {
        boolean eapFlag = currentVersion.contains(".EAP");
        int compareTo;
        if (eapFlag) {
            compareTo = currentVersion.replaceAll(".EAP(-\\w)?", "").compareTo(onLineLastStableVersion);
        } else {
            compareTo = currentVersion.compareTo(onLineLastStableVersion);
        }
        return (eapFlag && compareTo <= 0) || (!eapFlag && compareTo < 0);
    }

}
