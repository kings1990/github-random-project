package io.github.kings1990.githubRandomProject.config;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import io.github.kings1990.githubRandomProject.model.GithubProjectConfigConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@State(name = "config", storages = {@Storage("githubProject/config.xml")})
public class GithubProjectComponent implements PersistentStateComponent<GithubProjectConfigConfig> {
    private GithubProjectConfigConfig config;

    @Override
    public @Nullable GithubProjectConfigConfig getState() {
        if (config == null) {
            config = new GithubProjectConfigConfig();
        }
        return config;
    }

    @Override
    public void loadState(@NotNull GithubProjectConfigConfig state) {
        XmlSerializerUtil.copyBean(state, Objects.requireNonNull(getState()));
    }

    public static GithubProjectComponent getInstance() {
        return ApplicationManager.getApplication().getService(GithubProjectComponent.class);
    }
}
