package io.github.kings1990.githubRandomProject.action;

import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import io.github.kings1990.githubRandomProject.config.SavedProjectComponent;
import io.github.kings1990.githubRandomProject.model.SavedProject;
import io.github.kings1990.githubRandomProject.tree.ApiTree;
import io.github.kings1990.githubRandomProject.tree.SavedProjectNode;
import io.github.kings1990.githubRandomProject.util.MyResourceBundleUtil;
import io.github.kings1990.githubRandomProject.util.NotificationUtil;
import io.github.kings1990.githubRandomProject.util.ToolWindowUtil;
import io.github.kings1990.githubRandomProject.view.GithubProjectToolWindow;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class RemoveDirectoryAction extends AnAction {

    public RemoveDirectoryAction() {
        super(MyResourceBundleUtil.getKey("RemoveFolder"));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        GithubProjectToolWindow toolWindow = ToolWindowUtil.getGithubProjectToolWindow(project);
        if (toolWindow == null) {
            return;
        }
        ApiTree apiTree = toolWindow.getApiTree();
        SavedProjectNode[] selectedNodes = apiTree.getSelectedNodes(SavedProjectNode.class, null);
        if (selectedNodes.length == 0) {

            return;
        }
        SavedProject selectBlog = selectedNodes[0].getSource();
        if (Lists.newArrayList("/", "/default").contains(selectBlog.getPath())) {
            NotificationUtil.buildError("该目录不能删除").notify(project);
            return;
        }
        int deleteFlag = Messages.showOkCancelDialog("Delete it?", "Delete", "Delete", "Cancel", Messages.getInformationIcon());
        if (deleteFlag == 0) {
            List<SavedProject> savedBlogList = SavedProjectComponent.getInstance().getState().getSavedProjectList();
            if (selectBlog.getType().equals(SavedProject.CATEGORY)) {
                List<SavedProject> childrenList = savedBlogList.stream().filter(q -> q.getPath().startsWith(selectBlog.getPath())).collect(Collectors.toList());
                if (!childrenList.isEmpty()) {
                    savedBlogList.removeAll(childrenList);
                }
            }
            savedBlogList.remove(selectBlog);
            SavedProject expandSavedProject = ((SavedProjectNode) selectedNodes[0].getParent()).getSource();
            toolWindow.refreshTree(expandSavedProject);

        }
    }
}
