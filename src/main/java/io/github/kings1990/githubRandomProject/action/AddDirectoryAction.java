package io.github.kings1990.githubRandomProject.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import io.github.kings1990.githubRandomProject.config.SavedProjectComponent;
import io.github.kings1990.githubRandomProject.model.SavedProject;
import io.github.kings1990.githubRandomProject.util.MyResourceBundleUtil;
import io.github.kings1990.githubRandomProject.util.ToolWindowUtil;
import io.github.kings1990.githubRandomProject.view.dialog.AddDirectoryView;
import org.jetbrains.annotations.NotNull;

public class AddDirectoryAction extends AnAction {
    public AddDirectoryAction() {
        super(MyResourceBundleUtil.getKey("AddFolder"));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        AddDirectoryView addDirectoryView = new AddDirectoryView(project);
        if (addDirectoryView.showAndGet()) {
            SavedProject directory = addDirectoryView.getDirectory();
            SavedProject parentDirectory = addDirectoryView.getParentDirectory();
            SavedProjectComponent.getInstance().getState().getSavedProjectList().add(0, directory);
            ToolWindowUtil.getGithubProjectToolWindow(project).refreshTree(parentDirectory);
        }
    }
}
