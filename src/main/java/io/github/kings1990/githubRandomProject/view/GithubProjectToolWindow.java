package io.github.kings1990.githubRandomProject.view;

import com.google.common.collect.Lists;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.impl.HTMLEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.*;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.speedSearch.SpeedSearchUtil;
import com.intellij.util.ui.StatusText;
import icons.PluginIcons;
import io.github.kings1990.githubRandomProject.config.SavedProjectComponent;
import io.github.kings1990.githubRandomProject.model.SavedProject;
import io.github.kings1990.githubRandomProject.tree.ApiTree;
import io.github.kings1990.githubRandomProject.tree.SavedProjectNode;
import io.github.kings1990.githubRandomProject.util.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GithubProjectToolWindow extends SimpleToolWindowPanel implements Disposable {
    private final Project myProject;
    private final JPanel mainPanel;
    private JPanel iBlogPanel;
    private static final Logger LOGGER = Logger.getInstance(GithubProjectToolWindow.class);
    private ApiTree apiTree;
    private String selectNodeKey;
    private SearchTextField searchTextField;

    public void setSelectNodeKey(String selectNodeKey) {
        this.selectNodeKey = selectNodeKey;
    }

    public ApiTree getApiTree() {
        return apiTree;
    }

    public GithubProjectToolWindow(ToolWindow toolWindow, Project project) {
        super(true, false);
        this.myProject = project;
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        this.setContent(mainPanel);
        renderingConsolePanel();
        mainPanel.add(iBlogPanel, BorderLayout.CENTER);
        DefaultActionGroup group = new DefaultActionGroup();
        group.add(ActionManager.getInstance().getAction("githubProject.AddDirectoryAction"));
        group.add(ActionManager.getInstance().getAction("githubProject.RemoveDirectoryAction"));
        group.add(ActionManager.getInstance().getAction("githubProject.OpenInGithubProjectToolWindowAction"));
        
        group.add(ActionManager.getInstance().getAction("githubProject.ExpandAllAction"));
        group.add(ActionManager.getInstance().getAction("githubProject.CollapseAllAction"));


        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.TOOLWINDOW_CONTENT, group, true);
        actionToolbar.setTargetComponent(mainPanel);
        JComponent toolbarComponent = actionToolbar.getComponent();
        Border border = IdeBorderFactory.createBorder(SideBorder.BOTTOM);
        actionToolbar.getComponent().setBorder(border);
        setToolbar(toolbarComponent);
    }


    private void renderingConsolePanel() {
        iBlogPanel = new JPanel();
        iBlogPanel.setLayout(new BorderLayout());

        apiTree = new ApiTree();
        apiTree.setRootVisible(true);
        apiTree.setCellRenderer(new MyCellRenderer());
        CustomMenuItemRenderer customMenuItemRenderer = new CustomMenuItemRenderer();
        apiTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                SavedProjectNode[] selectedNodes = apiTree.getSelectedNodes(SavedProjectNode.class, null);
                if (selectedNodes.length == 0) {
                    return;
                }
                SavedProject selectBlog = selectedNodes[0].getSource();
                if (selectBlog.getType().equals(SavedProject.PROJECT)) {
                    if (event.getClickCount() == 2 && event.getButton() == MouseEvent.BUTTON1) {
                        HTMLEditorProvider.openEditor(myProject, selectBlog.getTitle(), selectBlog.getUrl(), VelocityUtil.getTimeOutHtmlContent(selectBlog.getUrl(),selectBlog.getTitle()));
                    } else if (SwingUtilities.isRightMouseButton(event)) {
                        JBMenuItem openInBrowserItem = new JBMenuItem("浏览器中打开", PluginIcons.web);
                        openInBrowserItem.addActionListener(e -> {
                            final Desktop dp = Desktop.getDesktop();
                            String url = selectBlog.getUrl();
                            if (dp.isSupported(Desktop.Action.BROWSE)) {
                                try {
                                    dp.browse(URI.create(url));
                                } catch (IOException ex) {
                                    LOGGER.error("open url fail:%s", ex, url);
                                }
                            }
                        });
                        List<JMenuItem> menuItemList = Lists.newArrayList(openInBrowserItem);
                        JBPopupFactory.getInstance().createPopupChooserBuilder(menuItemList)
                                .setItemsChosenCallback(selectedValues -> {
                                    for (JMenuItem selectedValue : selectedValues) {
                                        selectedValue.doClick();
                                    }
                                })
                                .setResizable(false)
                                .setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
                                .setRenderer(customMenuItemRenderer)
                                .createPopup()
                                .show(new RelativePoint(event));
                    }
                }

            }
        });

        refreshTree(null);

        JScrollPane scrollPane = ScrollPaneFactory.createScrollPane(apiTree);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        searchTextField = new SearchTextField(true);
        JBTextField searchTextFieldTextEditor = searchTextField.getTextEditor();
        StatusText emptyText = searchTextFieldTextEditor.getEmptyText();
        JBColor grayColor = JBColor.namedColor("Label.infoForeground", new JBColor(Gray._120, Gray._135));
        emptyText.appendText(MyResourceBundleUtil.getKey("TypeKeywordsToSearch"), new SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, grayColor));

        searchTextFieldTextEditor.getDocument().addDocumentListener(new DelayedDocumentListener());

        iBlogPanel.add(searchTextField, BorderLayout.NORTH);
        iBlogPanel.add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void dispose() {
        searchTextField.reset();
    }

    private class DelayedDocumentListener implements DocumentListener {

        private final Timer timer;

        public DelayedDocumentListener() {
            timer = new Timer(500, e -> filterRequest());
            timer.setRepeats(false);
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            timer.restart();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            timer.restart();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            timer.restart();
        }

    }

    private void filterRequest() {
        String text = searchTextField.getText().trim();
        List<SavedProject> savedBlogList = SavedProjectComponent.getInstance().getState().getSavedProjectList();
        List<SavedProject> filterList = savedBlogList.stream().filter(
                q -> (StringUtils.isNotBlank(q.getTitle()) &&
                        q.getTitle().toLowerCase().contains(text.toLowerCase()))
                        || q.getType().equals(SavedProject.CATEGORY)
        ).collect(Collectors.toList());
        refreshTree(filterList, null);
        if (StringUtils.isNotBlank(text)) {
            apiTree.expandAll();
        }
    }

    private static class CustomMenuItemRenderer extends ColoredListCellRenderer<JMenuItem> {

        @Override
        protected void customizeCellRenderer(@NotNull JList<? extends JMenuItem> list, JMenuItem value, int index, boolean selected, boolean hasFocus) {
            setIcon(value.getIcon());
            append(value.getText());
        }
    }


    private void buildNode(LinkedHashMap<String, SavedProjectNode> savedBlogNodeMap, List<SavedProject> sortedList) {
        for (SavedProject savedBlog : sortedList) {
            String subPath = savedBlog.getPath().substring(0, savedBlog.getPath().lastIndexOf("/"));
            String parentPath = StringUtils.isBlank(subPath) ? "/" : subPath;
            SavedProjectNode parentNode = savedBlogNodeMap.get(parentPath);
            SavedProjectNode savedBlogNode = new SavedProjectNode(savedBlog);
            if (SavedProject.CATEGORY.equals(savedBlog.getType())) {
                savedBlogNodeMap.put(savedBlog.getPath(), savedBlogNode);
            }
            parentNode.add(savedBlogNode);
            TreeNode parent = parentNode.getParent();
            if (parent != null) {
                savedBlogNodeMap.put(parentPath, parentNode);
            }
        }

    }


    private class MyCellRenderer extends ColoredTreeCellRenderer {
        public MyCellRenderer() {
        }

        @Override
        public void customizeCellRenderer(@NotNull JTree tree, Object target, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            SavedProjectNode node = null;
            if (target instanceof SavedProjectNode) {
                node = (SavedProjectNode) target;
            }
            if (node == null) {
                return;
            }
            SavedProject savedBlog = node.getSource();

            append(savedBlog.getName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
            if (SavedProject.CATEGORY.equals(savedBlog.getType())) {
                setIcon(PluginIcons.folder);
            } else {
                setIcon(IconUtil.getIcon(savedBlog.getOrigin()));
            }
            if (StringUtils.isNotBlank(selectNodeKey) && selectNodeKey.equals(savedBlog.getKey())) {
                setForeground(MyColor.green);
            }
            SpeedSearchUtil.applySpeedSearchHighlighting(this, this, false, true);
        }
    }

    public JComponent getContent() {
        return mainPanel;
    }

    public void refreshTree(List<SavedProject> savedProjectList, SavedProject expandNodeSource) {
        //savedBlogList根据 level 排序，
        List<SavedProject> sortedList = SavedProjectUtil.sortSavedProjectList(savedProjectList);

        LinkedHashMap<String, SavedProjectNode> savedProjectNodeMap = new LinkedHashMap<>();
        SavedProject rootSavedProject = sortedList.get(0);
        SavedProjectNode root = new SavedProjectNode(rootSavedProject);
        savedProjectNodeMap.put(rootSavedProject.getKey(), root);

        List<SavedProject> childList = sortedList.subList(1, savedProjectList.size());

        buildNode(savedProjectNodeMap, childList);
        apiTree.setModel(new DefaultTreeModel(root));

        if (expandNodeSource != null) {
            apiTree.expandPath(findPathByUserObject(apiTree, expandNodeSource));
        }
    }


    public void refreshTree(SavedProject expandNodeSource) {
        refreshTree(SavedProjectComponent.getInstance().getState().getSavedProjectList(), expandNodeSource);
    }

    public TreePath findPathByUserObject(JTree tree, SavedProject userObject) {
        TreeModel model = tree.getModel();
        return findPathByUserObjectRecursive(model, new TreePath(model.getRoot()), userObject);
    }

    private TreePath findPathByUserObjectRecursive(TreeModel model, TreePath currentPath, SavedProject userObject) {
        SavedProjectNode node = (SavedProjectNode) currentPath.getLastPathComponent();
        if (node.getSource().equals(userObject)) {
            return currentPath;
        } else {
            for (int i = 0; i < model.getChildCount(node); i++) {
                Object child = model.getChild(node, i);
                TreePath childPath = currentPath.pathByAddingChild(child);
                TreePath result = findPathByUserObjectRecursive(model, childPath, userObject);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

}
