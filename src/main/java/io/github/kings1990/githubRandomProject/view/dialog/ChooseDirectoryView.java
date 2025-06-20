package io.github.kings1990.githubRandomProject.view.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.SimpleListCellRenderer;
import io.github.kings1990.githubRandomProject.config.GithubProjectComponent;
import io.github.kings1990.githubRandomProject.config.SavedProjectComponent;
import io.github.kings1990.githubRandomProject.model.SavedProject;
import io.github.kings1990.githubRandomProject.util.MyResourceBundleUtil;
import io.github.kings1990.githubRandomProject.util.SavedProjectUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ChooseDirectoryView extends DialogWrapper {
    private ComboBox<SavedProject> comboBox;

    public ChooseDirectoryView(@Nullable Project project) {
        super(project, false);
        init();
        setSize(400, 30);
        setTitle(MyResourceBundleUtil.getKey("ChooseFolder"));
    }

    public SavedProject getDirectory() {
        return (SavedProject) comboBox.getSelectedItem();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        List<SavedProject> savedBlogList = SavedProjectComponent.getInstance().getState().getSavedProjectList();
        List<SavedProject> directoryList = savedBlogList.stream().filter(savedBlog -> savedBlog.getType().equals(SavedProject.CATEGORY)).collect(Collectors.toList());
        ComboBoxModel<SavedProject> comboBoxModel = new DefaultComboBoxModel<>(SavedProjectUtil.sortSavedProjectList(directoryList).toArray(new SavedProject[0]));
        comboBox = new ComboBox<>(comboBoxModel);


        String lastChoosePath = GithubProjectComponent.getInstance().getState().getLastChoosePath();
        directoryList.stream().filter(q -> q.getPath().equals(lastChoosePath)).findFirst().ifPresent(lastSelectDirectory -> comboBox.setSelectedItem(lastSelectDirectory));


        comboBox.setRenderer(new SimpleListCellRenderer<>() {

            @Override
            public void customize(JList list, SavedProject value, int index, boolean selected, boolean hasFocus) {
                setText(value.getPath());
            }
        });
        comboBox.setPreferredSize(new Dimension(400, 30));
        return comboBox;

    }
}
