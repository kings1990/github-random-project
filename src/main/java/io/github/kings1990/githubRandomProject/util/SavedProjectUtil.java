package io.github.kings1990.githubRandomProject.util;

import io.github.kings1990.githubRandomProject.model.SavedProject;

import java.util.List;
import java.util.stream.Collectors;

public class SavedProjectUtil {
    public static List<SavedProject> sortSavedProjectList(List<SavedProject> savedBlogList) {
        return savedBlogList.stream().sorted(
                (o1, o2) -> {
                    // 如果 path 为 "/"，则排在最前面
                    if (o1.getPath().equals("/")) {
                        return -1;
                    }
                    if (o2.getPath().equals("/")) {
                        return 1;
                    }

                    // 如果 path 为 "/default"，则排在 "/" 之后
                    if (o1.getPath().equals("/default")) {
                        return -1;
                    }
                    if (o2.getPath().equals("/default")) {
                        return 1;
                    }
                    if (o1.getLevel().equals(o2.getLevel())) {
                        // 如果 type 为 "category"，则排在前面
                        if (o1.getType().equals(o2.getType())) {
                            return o2.getKey().compareTo(o1.getKey());
                        } else {
                            if (o1.getType().equals("category")) {
                                return -1;
                            }
                            if (o2.getType().equals("category")) {
                                return 1;
                            }
                            return o2.getKey().compareTo(o1.getKey());
                        }


                    } else {
                        return o1.getLevel().compareTo(o2.getLevel());
                    }

                }
        ).collect(Collectors.toList());
    }
}
