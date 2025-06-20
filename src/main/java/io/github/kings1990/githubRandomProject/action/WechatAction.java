package io.github.kings1990.githubRandomProject.action;

import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import io.github.kings1990.githubRandomProject.model.ImageQrcode;
import io.github.kings1990.githubRandomProject.util.MyResourceBundleUtil;
import io.github.kings1990.githubRandomProject.view.dialog.ImageQrcodeView;
import org.jetbrains.annotations.NotNull;

public class WechatAction extends AnAction {

    public WechatAction() {
        super(MyResourceBundleUtil.getKey("WechatGroup"));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ImageQrcodeView qrcodeView = new ImageQrcodeView(Lists.newArrayList(new ImageQrcode("Github Project群","/img/wechatGroup.png")), 
                "提需求请扫码进群");
        qrcodeView.show();
    }
}
