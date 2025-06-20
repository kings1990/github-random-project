package io.github.kings1990.githubRandomProject.processor;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.intellij.openapi.diagnostic.Logger;
import io.github.kings1990.githubRandomProject.model.MyProject;

import java.util.ArrayList;
import java.util.List;

public class GithubRepoFetchProcessor {
    private static final Logger LOGGER = Logger.getInstance(GithubRepoFetchProcessor.class);

    public static List<MyProject> fetch(String language, String stars, String keywords) {
        UrlBuilder urlBuilder = UrlBuilder.of("https://api.github.com/search/repositories").addQuery("q", "%s+stars:%s language:%s".formatted(keywords,stars, language)).addQuery("order", "desc").addQuery("page", 1).addQuery("sort", "star").addQuery("per_page", 1000);
        List<MyProject> result = new ArrayList<>();
        try (HttpResponse response = HttpUtil.createGet(urlBuilder.build()).timeout(5000).setConnectionTimeout(5000).execute()){
            JSONObject jsonObject;
            jsonObject = JSONUtil.parseObj(response.body());
            JSONArray items = jsonObject.getJSONArray("items");
            
            List<Integer> randomNumbers = new ArrayList<>();
            
            if(items.size() >=20){
                while (randomNumbers.size() < 20) {
                    int randomNumber = RandomUtil.randomInt(items.size());
                    if (!randomNumbers.contains(randomNumber)) {
                        randomNumbers.add(randomNumber);
                    }
                }
            } else {
                for (int i = 0; i < items.size(); i++) {
                    randomNumbers.add(i);
                }
            }

            JSONArray itemsFiltered = new JSONArray();
            for (Integer index : randomNumbers) {
                itemsFiltered.add(items.get(index));
            }
            
            
            for (Object dataObject : itemsFiltered) {
                JSONObject data = (JSONObject) dataObject;
                MyProject myProject = new MyProject();
                myProject.setOrigin("GitHub");
                myProject.setTitle(data.getStr("name"));
                myProject.setDescription(data.getStr("description") == null ? "" : data.getStr("description"));
                myProject.setUrl(data.getStr("html_url"));
                myProject.setStars(data.getInt("stargazers_count"));
                myProject.setForks(data.getInt("forks_count"));
                myProject.setLanguage(data.getStr("language"));
                result.add(myProject);
            }
            return result;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
