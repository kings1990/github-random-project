package io.github.kings1990.githubRandomProject.model;

public class SearchCondition {
    private String language = "All";
    private String stars = ">10000";
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public String getStars() {
        return stars;
    }
    
    public void setStars(String stars) {
        this.stars = stars;
    }
}
