package io.github.kings1990.githubRandomProject.model;

import java.util.ArrayList;
import java.util.List;

public class SavedProjectData {
    private List<SavedProject> savedProjectList = new ArrayList<>();

    public List<SavedProject> getSavedProjectList() {
        return savedProjectList;
    }

    public void setSavedProjectList(List<SavedProject> savedProjectList) {
        this.savedProjectList = savedProjectList;
    }
}
