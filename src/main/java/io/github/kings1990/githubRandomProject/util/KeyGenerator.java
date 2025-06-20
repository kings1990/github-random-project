package io.github.kings1990.githubRandomProject.util;

import cn.hutool.core.util.RandomUtil;

public class KeyGenerator {
    public static String generateKey() {
        return System.currentTimeMillis() + "" + RandomUtil.randomInt(1000, 9999);
    }

}
