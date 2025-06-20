package io.github.kings1990.githubRandomProject.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecentProject implements Serializable {
    private List<MyProject> projectList = new ArrayList<>();

    public List<MyProject> getProjectList() {
        return projectList;
    }

    public void setBlogList(List<MyProject> projectList) {
        this.projectList = projectList;
    }
}
