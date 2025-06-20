package io.github.kings1990.githubRandomProject.config;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import io.github.kings1990.githubRandomProject.model.RecentProject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@State(name = "recentProject", storages = {@Storage("githubProject/recentProject.xml")})
public class RecentProjectComponent implements PersistentStateComponent<RecentProject> {
    private RecentProject config;

    @Override
    public @Nullable RecentProject getState() {
        if (config == null) {
            config = new RecentProject();
        }
        return config;
    }

    @Override
    public void loadState(@NotNull RecentProject state) {
        XmlSerializerUtil.copyBean(state, Objects.requireNonNull(getState()));
    }

    public static RecentProjectComponent getInstance() {
        return ApplicationManager.getApplication().getService(RecentProjectComponent.class);
    }
}
