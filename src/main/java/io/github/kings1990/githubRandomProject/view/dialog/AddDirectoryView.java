package io.github.kings1990.githubRandomProject.view.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ComponentValidator;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.SimpleListCellRenderer;
import com.intellij.util.ui.UI;
import io.github.kings1990.githubRandomProject.config.SavedProjectComponent;
import io.github.kings1990.githubRandomProject.model.SavedProject;
import io.github.kings1990.githubRandomProject.tree.ApiTree;
import io.github.kings1990.githubRandomProject.tree.SavedProjectNode;
import io.github.kings1990.githubRandomProject.util.KeyGenerator;
import io.github.kings1990.githubRandomProject.util.MyResourceBundleUtil;
import io.github.kings1990.githubRandomProject.util.SavedProjectUtil;
import io.github.kings1990.githubRandomProject.util.ToolWindowUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class AddDirectoryView extends DialogWrapper {
    private ComboBox<SavedProject> comboBox;
    private JTextField folderTextField;
    private final Project project;

    public AddDirectoryView(@Nullable Project project) {
        super(project, false);
        this.project = project;
        init();
        setSize(400, 50);
        setTitle(MyResourceBundleUtil.getKey("AddFolder"));
    }


    public SavedProject getDirectory() {
        SavedProject parent = getParentDirectory();
        SavedProject current = new SavedProject();
        String text = folderTextField.getText();

        String path = parent.getPath().equals("/") ? "/" + text : parent.getPath() + "/" + text;
        current.setKey(KeyGenerator.generateKey());
        current.setType(SavedProject.CATEGORY);
        current.setLevel(parent.getLevel() + 1);
        current.setPath(path);
        current.setName(text);
        current.setTitle(text);
        current.setDescription(text);

        return current;
    }

    public SavedProject getParentDirectory() {
        return (SavedProject) comboBox.getSelectedItem();
    }


    @Override
    protected @Nullable JComponent createCenterPanel() {
        List<SavedProject> savedBlogList = SavedProjectComponent.getInstance().getState().getSavedProjectList();
        List<SavedProject> directoryList = savedBlogList.stream().filter(savedBlog -> savedBlog.getType().equals(SavedProject.CATEGORY)).collect(Collectors.toList());
        ComboBoxModel<SavedProject> comboBoxModel = new DefaultComboBoxModel<>(SavedProjectUtil.sortSavedProjectList(directoryList).toArray(new SavedProject[0]));
        comboBox = new ComboBox<>(comboBoxModel);
        chooseFolder();
        comboBox.setRenderer(new SimpleListCellRenderer<>() {

            @Override
            public void customize(JList list, SavedProject value, int index, boolean selected, boolean hasFocus) {
                setText(value.getPath());
            }
        });
        comboBox.setPreferredSize(new Dimension(400, 30));

        folderTextField = new JTextField();

        new ComponentValidator(this.myDisposable).withValidator(() -> {
            String text = folderTextField.getText();
            if (StringUtils.isEmpty(text)) {
                return new ValidationInfo(MyResourceBundleUtil.getKey("FolderEmptyWarn"), folderTextField).withOKEnabled();
            }
            return null;
        }).installOn(folderTextField);
        folderTextField.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent e) {
                ComponentValidator.getInstance(folderTextField)
                        .ifPresent(v -> v.revalidate());
                setOKActionEnabled(StringUtils.isNotBlank(folderTextField.getText()));
            }

        });

        setOKActionEnabled(false);

        return UI.PanelFactory.grid().splitColumns()
                .add(UI.PanelFactory.panel(folderTextField).withLabel(MyResourceBundleUtil.getKey("FolderName")))
                .add(UI.PanelFactory.panel(comboBox).withLabel(MyResourceBundleUtil.getKey("ParentFolder")))
                .createPanel();

    }

    private void chooseFolder() {
        ApiTree apiTree = ToolWindowUtil.getGithubProjectToolWindow(project).getApiTree();
        TreePath selectionPath = apiTree.getSelectionPath();
        if (selectionPath != null) {
            SavedProjectNode node = (SavedProjectNode) selectionPath.getLastPathComponent();
            SavedProject selectBlog = node.getSource();

            if (selectBlog.getType().equals(SavedProject.PROJECT)) {
                SavedProject blog = ((SavedProjectNode) node.getParent()).getSource();
                comboBox.setSelectedItem(blog);
            } else {
                comboBox.setSelectedItem(selectBlog);
            }
        }
    }
}
