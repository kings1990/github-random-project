package io.github.kings1990.githubRandomProject.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import io.github.kings1990.githubRandomProject.view.GithubProjectToolWindow;
import io.github.kings1990.githubRandomProject.view.SearchToolWindow;

public class ToolWindowUtil {

    public static GithubProjectToolWindow getGithubProjectToolWindow(Project myProject) {
        final ToolWindow toolWindow = ToolWindowManager.getInstance(myProject).getToolWindow("GitHub Random Project");
        if (toolWindow != null) {
            final Content content = toolWindow.getContentManager().getContent(1);
            if (content != null) {
                return (GithubProjectToolWindow) content.getComponent();
            }
        }
        return null;
    }

    public static SearchToolWindow getSearchToolWindow(Project myProject) {
        final ToolWindow toolWindow = ToolWindowManager.getInstance(myProject).getToolWindow("GitHub Random Project");
        if (toolWindow != null) {
            final Content content = toolWindow.getContentManager().getContent(0);
            if (content != null) {
                return (SearchToolWindow) content.getComponent();
            }
        }
        return null;
    }
}
