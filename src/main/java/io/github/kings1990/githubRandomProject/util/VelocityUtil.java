package io.github.kings1990.githubRandomProject.util;

import com.intellij.openapi.editor.colors.EditorColorsManager;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.jetbrains.java.generate.velocity.VelocityFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class VelocityUtil {
    public static String getTimeOutHtmlContent(String link,String title) {
        boolean darkEditor = EditorColorsManager.getInstance().isDarkEditor();
        String fileName = darkEditor ? "timeoutDark.vm" : "timeout.vm";
        VelocityEngine ve = VelocityFactory.getVelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        //得到模板文件
        ve.init();
        InputStream resourceAsStream = VelocityUtil.class.getClassLoader().getResourceAsStream(fileName);
        InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
        VelocityContext data = new VelocityContext();
        data.put("link", link);
        data.put("title", title);
        StringWriter sw = new StringWriter();
        ve.evaluate(data, sw, "ERROR", inputStreamReader);
        return sw.toString();
    }
}
