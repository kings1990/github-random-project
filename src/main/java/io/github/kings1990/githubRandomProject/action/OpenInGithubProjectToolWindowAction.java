package io.github.kings1990.githubRandomProject.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.impl.HTMLEditorProvider;
import icons.PluginIcons;
import io.github.kings1990.githubRandomProject.model.SavedProject;
import io.github.kings1990.githubRandomProject.nomix.SuperBaseAction;
import io.github.kings1990.githubRandomProject.tree.ApiTree;
import io.github.kings1990.githubRandomProject.tree.SavedProjectNode;
import io.github.kings1990.githubRandomProject.util.MyResourceBundleUtil;
import io.github.kings1990.githubRandomProject.util.ToolWindowUtil;
import io.github.kings1990.githubRandomProject.util.VelocityUtil;
import org.jetbrains.annotations.NotNull;

public class OpenInGithubProjectToolWindowAction extends SuperBaseAction {
    public OpenInGithubProjectToolWindowAction() {
        super(MyResourceBundleUtil.getKey("Open"), MyResourceBundleUtil.getKey("Open"), PluginIcons.openNewTab);
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ApiTree apiTree = ToolWindowUtil.getGithubProjectToolWindow(e.getProject()).getApiTree();
        SavedProjectNode[] selectedNodes = apiTree.getSelectedNodes(SavedProjectNode.class, null);
        if (selectedNodes.length == 0) {
            return;
        }
        SavedProject selectProject = selectedNodes[0].getSource();
        HTMLEditorProvider.openEditor(e.getProject(), selectProject.getTitle(), selectProject.getUrl(), VelocityUtil.getTimeOutHtmlContent(selectProject.getUrl(),selectProject.getTitle()));
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        ApiTree apiTree = ToolWindowUtil.getGithubProjectToolWindow(e.getProject()).getApiTree();
        SavedProjectNode[] selectedNodes = apiTree.getSelectedNodes(SavedProjectNode.class, null);
        e.getPresentation().setEnabled(selectedNodes.length == 1 && selectedNodes[0].getSource().getType().equals(SavedProject.PROJECT));
    }
}
