package io.github.kings1990.githubRandomProject.model;


public class SavedProject extends MyProject {
    private String key;
    private String name;
    private String type;
    private Integer level;
    private String path;


    public static final String CATEGORY = "category";
    public static final String PROJECT = "project";

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
