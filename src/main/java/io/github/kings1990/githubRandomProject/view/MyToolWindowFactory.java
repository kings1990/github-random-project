package io.github.kings1990.githubRandomProject.view;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import io.github.kings1990.githubRandomProject.util.MyResourceBundleUtil;
import org.jetbrains.annotations.NotNull;

public class MyToolWindowFactory implements ToolWindowFactory, Disposable {

    public static final String IBLOG_CONTENT_NAME = MyResourceBundleUtil.getKey("Collection");

    /**
     * Create the tool window content.
     *
     * @param project    current project
     * @param toolWindow current tool window
     */
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ApplicationManager.getApplication().getService(ContentFactory.class);





        SearchToolWindow searchToolWindow = new SearchToolWindow(toolWindow, project);
        Content searchToolWindowContent = contentFactory.createContent(searchToolWindow, "Search", false);
        searchToolWindowContent.setCloseable(false);
        toolWindow.getContentManager().addContent(searchToolWindowContent);

        GithubProjectToolWindow browserToolWindow = new GithubProjectToolWindow(toolWindow, project);
        Content browser = contentFactory.createContent(browserToolWindow, IBLOG_CONTENT_NAME, false);
        browser.setCloseable(false);
        toolWindow.getContentManager().addContent(browser);
    }

    @Override
    public void dispose() {

    }
}