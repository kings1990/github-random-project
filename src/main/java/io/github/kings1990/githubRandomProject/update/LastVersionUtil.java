package io.github.kings1990.githubRandomProject.update;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;


public class LastVersionUtil {

    //todo kings
    private static final String URL = "https://plugins.jetbrains.com/api/plugins/24920/updates?size=10&channel=&page=1";

    public static String getOnlineLastVersion() {
        try {
            HttpRequest httpRequest = HttpUtil.createGet(URL);
            httpRequest.setConnectionTimeout(1500);
            httpRequest.setReadTimeout(1500);
            HttpResponse response = httpRequest.execute();
            if (response.isOk()) {
                String body = response.body();
                JSONArray jsonArray = JSONUtil.parseArray(body);
                for (Object o : jsonArray) {
                    JSONObject jsonObject = JSONUtil.parseObj(o.toString());
                    Boolean approve = jsonObject.getBool("approve");
                    Boolean listed = jsonObject.getBool("listed");
                    if (approve && listed) {
                        return jsonObject.getStr("version");
                    }
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }

    }

}
