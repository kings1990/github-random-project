package io.github.kings1990.githubRandomProject.view;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.JBColor;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.SideBorder;
import com.intellij.ui.components.ActionLink;
import com.intellij.ui.components.IconLabelButton;
import icons.PluginIcons;
import io.github.kings1990.githubRandomProject.config.GithubProjectComponent;
import io.github.kings1990.githubRandomProject.config.SavedProjectComponent;
import io.github.kings1990.githubRandomProject.config.SearchConditionComponent;
import io.github.kings1990.githubRandomProject.model.MyProject;
import io.github.kings1990.githubRandomProject.model.SavedProject;
import io.github.kings1990.githubRandomProject.model.SearchCondition;
import io.github.kings1990.githubRandomProject.processor.GithubRepoFetchProcessor;
import io.github.kings1990.githubRandomProject.util.KeyGenerator;
import io.github.kings1990.githubRandomProject.util.MyResourceBundleUtil;
import io.github.kings1990.githubRandomProject.util.NotificationUtil;
import io.github.kings1990.githubRandomProject.util.ToolWindowUtil;
import io.github.kings1990.githubRandomProject.view.dialog.ChooseDirectoryView;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SearchToolWindow extends SimpleToolWindowPanel {
    private final Project myProject;
    private final JPanel mainPanel;
    private final ToolWindow toolWindow;
    private JPanel myPanel;
    private ComboBox<String> comboBoxLanguage;
    private ComboBox<String> comboBoxStars;
    private JTextField keywordsTextField;
    private JPanel projectPanel = new JPanel();
    
    private static final Logger LOGGER = Logger.getInstance(SearchToolWindow.class);
    

    public SearchToolWindow(ToolWindow toolWindow, Project project) {
        super(true, false);
        this.toolWindow = toolWindow;
        this.myProject = project;
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        this.setContent(mainPanel);

        renderingConsolePanel();
        mainPanel.add(myPanel, BorderLayout.CENTER);
        DefaultActionGroup group = new DefaultActionGroup();
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.TOOLWINDOW_CONTENT, group, true);
        actionToolbar.setTargetComponent(mainPanel);
        JComponent toolbarComponent = actionToolbar.getComponent();
        Border border = IdeBorderFactory.createBorder(SideBorder.BOTTOM);
        actionToolbar.getComponent().setBorder(border);
        setToolbar(toolbarComponent);


    }


    private void renderingConsolePanel() {
        myPanel = new JPanel();
        myPanel.setLayout(new BorderLayout());
        renderProjectPanel(new ArrayList<>());
        
        JScrollPane scrollPane = ScrollPaneFactory.createScrollPane(projectPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        
       

        comboBoxLanguage = new ComboBox<>(new String[]{"All", "Java", "Python", "JavaScript", "C++","Go", "Ruby", "PHP", "C#", "TypeScript",  "Kotlin", "Rust", "Scala", "Shell", "HTML", "CSS"});
        comboBoxStars = new ComboBox<>(new String[]{ ">10000", ">5000", ">1000", ">500", "<500"});
        keywordsTextField = new JTextField();
        JButton searchButton = new JButton("Search");
        searchButton.setMaximumSize(new Dimension(40,-1));
        JPanel conditionPanel = new JPanel();
        conditionPanel.setLayout(new GridLayout(1,5));
        conditionPanel.add(createLabelComboPanel(MyResourceBundleUtil.getKey("Keywords"), keywordsTextField));
        conditionPanel.add(createLabelComboPanel(MyResourceBundleUtil.getKey("ProgrammingLanguage"), comboBoxLanguage));
        conditionPanel.add(createLabelComboPanel(MyResourceBundleUtil.getKey("MinimumStars"), comboBoxStars));
        

        conditionPanel.add(searchButton);
        searchButton.addActionListener(e->{
            searchButton.setEnabled(false);
            String selectedLanguage = (String) comboBoxLanguage.getSelectedItem();
            String selectedStars = (String) comboBoxStars.getSelectedItem();
            String keywords = keywordsTextField.getText();
            List<MyProject> fetch = GithubRepoFetchProcessor.fetch(selectedLanguage, selectedStars, keywords);
            renderProjectPanel(fetch);

            projectPanel.revalidate();
            projectPanel.repaint();
            scrollPane.setViewportView(projectPanel);


            SearchCondition searchCondition = SearchConditionComponent.getInstance().getState();
            searchCondition.setLanguage(selectedLanguage);
            searchCondition.setStars(selectedStars);
            SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
            searchButton.setEnabled(true);
            
            
        });
        

        myPanel.add(conditionPanel, BorderLayout.NORTH);
        myPanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void renderProjectPanel(List<MyProject> projectList){
        projectPanel = new JPanel();
        projectPanel.setLayout(new GridLayout(projectList.size(),1));
        for (MyProject projectInfo : projectList) {
            JPanel projectItemPanel = new JPanel(new BorderLayout());
            ActionLink actionLink = new ActionLink(projectInfo.getTitle(), e -> {
                BrowserUtil.browse(projectInfo.getUrl());
            });
            actionLink.setExternalLinkIcon();
            actionLink.setFont(actionLink.getFont().deriveFont(18f));

            JTextArea descriptionLabel = new JTextArea();
            descriptionLabel.setLineWrap(true); // Enable line wrapping
            descriptionLabel.setWrapStyleWord(true); // Wrap at word boundaries
            descriptionLabel.setEditable(false); // Make it non-editable
            descriptionLabel.setOpaque(false); // Make the background transparent
            descriptionLabel.setBorder(null); // Remove border if needed
            descriptionLabel.setText(projectInfo.getDescription());
            descriptionLabel.setRows(projectInfo.getDescription() == null ? 1 : projectInfo.getDescription().length() / 100 + 1);

            JPanel infoPanel = new JPanel(new GridLayout(1, 3));
            JLabel starsLabel = new JLabel("Stars: " + projectInfo.getStars());
            JLabel forksLabel = new JLabel("Forks: " + projectInfo.getForks());
            JLabel languageLabel = new JLabel("Language: " + projectInfo.getLanguage());
            
            infoPanel.add(starsLabel);
            infoPanel.add(forksLabel);
            infoPanel.add(languageLabel);



            IconLabelButton iconLabelButton = new IconLabelButton(PluginIcons.mark,component -> {
                ChooseDirectoryView dialog = new ChooseDirectoryView(myProject);
                if (dialog.showAndGet()) {
                    
                    
                    SavedProject directory = dialog.getDirectory();

                    SavedProject savedProject = new SavedProject();
                    String key = KeyGenerator.generateKey();
                    savedProject.setOrigin(projectInfo.getOrigin());
                    savedProject.setKey(key);
                    savedProject.setType(SavedProject.PROJECT);
                    savedProject.setLevel(directory.getLevel() + 1);
                    savedProject.setPath(directory.getPath() + "/" + key);
                    savedProject.setOrigin(projectInfo.getOrigin());
                    savedProject.setName(projectInfo.getTitle());
                    savedProject.setTitle(projectInfo.getTitle());
                    savedProject.setDescription(projectInfo.getDescription());
                    savedProject.setUrl(projectInfo.getUrl());
                    savedProject.setStars(projectInfo.getStars());
                    savedProject.setForks(projectInfo.getForks());
                    savedProject.setLanguage(projectInfo.getLanguage());
                    SavedProjectComponent.getInstance().getState().getSavedProjectList().add(0, savedProject);
                    GithubProjectComponent.getInstance().getState().setLastChoosePath(directory.getPath());

                    ToolWindowUtil.getGithubProjectToolWindow(myProject).refreshTree(directory);
                    NotificationUtil.buildInfo(MyResourceBundleUtil.getKey("CollectedSuccessfully")).notify(myProject);
                }
                return null ;
            });
            
            
            JPanel titlePanel = new JPanel(new BorderLayout());
            titlePanel.add(actionLink, BorderLayout.CENTER);
            titlePanel.add(iconLabelButton, BorderLayout.WEST);
            
            projectItemPanel.add(titlePanel, BorderLayout.NORTH);
            projectItemPanel.add(descriptionLabel, BorderLayout.CENTER);
            projectItemPanel.add(infoPanel, BorderLayout.SOUTH);
            
            projectItemPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 5, 5, 5),
                    BorderFactory.createLineBorder(JBColor.GRAY,2, true)
            ));
            
            projectPanel.add(projectItemPanel);
        }
        
    }

    private JPanel createLabelComboPanel(String labelText, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JLabel label = new JLabel(labelText);
        panel.add(label);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(component);
        return panel;
    }




    public JComponent getContent() {
        return mainPanel;
    }

}
