package io.github.kings1990.githubRandomProject.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import io.github.kings1990.githubRandomProject.util.MyResourceBundleUtil;
import io.github.kings1990.githubRandomProject.util.ToolWindowUtil;
import org.jetbrains.annotations.NotNull;

public class CollapseAllAction extends AnAction {

    public CollapseAllAction() {
        super(MyResourceBundleUtil.getKey("CollapseAll"));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        ToolWindowUtil.getGithubProjectToolWindow(project).getApiTree().collapseAll();
    }
}
