package io.github.kings1990.githubRandomProject.config;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import io.github.kings1990.githubRandomProject.model.SearchCondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@State(name = "searchCondition", storages = {@Storage("githubProject/searchCondition.xml")})
public class SearchConditionComponent implements PersistentStateComponent<SearchCondition> {
    private SearchCondition searchCondition;

    @Override
    public @Nullable SearchCondition getState() {
        if (searchCondition == null) {
            searchCondition = new SearchCondition();
        }
        return searchCondition;
    }

    @Override
    public void loadState(@NotNull SearchCondition state) {
        XmlSerializerUtil.copyBean(state, Objects.requireNonNull(getState()));
    }

    public static SearchConditionComponent getInstance() {
        return ApplicationManager.getApplication().getService(SearchConditionComponent.class);
    }
}
