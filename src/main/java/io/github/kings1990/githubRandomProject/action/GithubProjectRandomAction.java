package io.github.kings1990.githubRandomProject.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.impl.HTMLEditorProvider;
import com.intellij.openapi.project.Project;
import io.github.kings1990.githubRandomProject.config.SearchConditionComponent;
import io.github.kings1990.githubRandomProject.model.MyProject;
import io.github.kings1990.githubRandomProject.model.SearchCondition;
import io.github.kings1990.githubRandomProject.processor.GithubRepoFetchProcessor;
import io.github.kings1990.githubRandomProject.util.VelocityUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GithubProjectRandomAction extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }
        SearchCondition searchCondition = SearchConditionComponent.getInstance().getState();
        List<MyProject> fetch = GithubRepoFetchProcessor.fetch(searchCondition.getLanguage(), searchCondition.getStars(), "");
        if(!fetch.isEmpty()){
            MyProject projectInfo = fetch.get(0);
            HTMLEditorProvider.openEditor(e.getProject(), projectInfo.getTitle(), projectInfo.getUrl(), VelocityUtil.getTimeOutHtmlContent(projectInfo.getUrl(),projectInfo.getTitle()));
        }
    }
}
