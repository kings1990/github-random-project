package io.github.kings1990.githubRandomProject.idea;

import com.intellij.ide.AppLifecycleListener;
import com.intellij.ide.plugins.DynamicPluginListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionManagerImpl;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.ui.GotItTooltip;
import io.github.kings1990.githubRandomProject.util.MyResourceBundleUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class TitleBarActionRegistrar implements AppLifecycleListener, DynamicPluginListener {
    private static final Logger LOGGER = Logger.getInstance(TitleBarActionRegistrar.class);

    @Override
    public void appFrameCreated(@NotNull List<String> commandLineArgs) {
        if (isNewUI()) {
            registerAction();
        }
    }

    @Override
    public void pluginLoaded(@NotNull IdeaPluginDescriptor pluginDescriptor) {
        if (isNewUI() && pluginDescriptor.getPluginId().getIdString().equals("io.github.kings1990.githubRandomProject")) {
            registerAction();
        }
    }

    private boolean isNewUI() {
        int baselineVersion = ApplicationInfo.getInstance().getBuild().getBaselineVersion();
        return baselineVersion >= 223 && Registry.is("ide.experimental.ui");
    }


    private void registerAction() {
        try {

            int baselineVersion = ApplicationInfo.getInstance().getBuild().getBaselineVersion();
            Constraints constraints;
            String groupId;
            if (baselineVersion >= 223) {
                constraints = new Constraints(Anchor.BEFORE, "SettingsEntryPoint");
                groupId = "MainToolbarRight";
            } else {
                constraints = Constraints.LAST;
                groupId = "ExperimentalToolbarActions";
            }

            ActionManagerImpl actionManager = (ActionManagerImpl) ActionManager.getInstance();
            AnAction action = actionManager.getAction("githubProject.GithubProjectRandomAction");
            assert action != null;
            DefaultActionGroup group = (DefaultActionGroup) ActionManager.getInstance().getAction(groupId);

            List<AnAction> actionList = Arrays.asList(group.getChildActionsOrStubs());

            if (!actionList.contains(action)) {
                actionManager.addToGroup(group, action, constraints);
            }
            new GotItTooltip("GithubProjectRandomAction", MyResourceBundleUtil.getKey("FetchProject"), null)
                    .assignTo(ActionManager.getInstance().getAction("githubProject.GithubProjectRandomAction").getTemplatePresentation(), GotItTooltip.BOTTOM_MIDDLE);
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }
}
