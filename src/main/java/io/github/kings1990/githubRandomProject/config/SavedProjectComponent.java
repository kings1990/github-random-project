package io.github.kings1990.githubRandomProject.config;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import io.github.kings1990.githubRandomProject.model.SavedProject;
import io.github.kings1990.githubRandomProject.model.SavedProjectData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@State(name = "savedProject", storages = {@Storage("githubProject/savedProject.xml")})
public class SavedProjectComponent implements PersistentStateComponent<SavedProjectData> {
    private SavedProjectData config;

    @Override
    public @Nullable SavedProjectData getState() {
        if (config == null) {
            config = new SavedProjectData();
            List<SavedProject> savedBlogList = config.getSavedProjectList();

            SavedProject root = new SavedProject();
            root.setKey("/");
            root.setName("/");
            root.setType(SavedProject.CATEGORY);
            root.setLevel(0);
            root.setPath("/");
            root.setTitle("/");
            savedBlogList.add(root);

            SavedProject defaultDir = new SavedProject();
            defaultDir.setKey("default");
            defaultDir.setType(SavedProject.CATEGORY);
            defaultDir.setName("default");
            defaultDir.setPath("/default");
            defaultDir.setLevel(1);
            defaultDir.setTitle("default");

            savedBlogList.add(defaultDir);
        }
        return config;
    }

    @Override
    public void loadState(@NotNull SavedProjectData state) {
        XmlSerializerUtil.copyBean(state, Objects.requireNonNull(getState()));
    }

    public static SavedProjectComponent getInstance() {
        return ApplicationManager.getApplication().getService(SavedProjectComponent.class);
    }
}
