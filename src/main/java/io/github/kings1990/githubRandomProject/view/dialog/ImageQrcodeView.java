package io.github.kings1990.githubRandomProject.view.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import io.github.kings1990.githubRandomProject.model.ImageQrcode;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class ImageQrcodeView extends DialogWrapper {
    private final List<ImageQrcode>imageQrcodeList;
    

    public ImageQrcodeView(List<ImageQrcode> imageQrcodeList, String title) {
        super(false);
        this.imageQrcodeList = imageQrcodeList;
        init();
        setTitle(title);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel();
        for (ImageQrcode imageQrcode : imageQrcodeList) {
            JPanel innerPanel = new JPanel();
            innerPanel.setLayout(new BorderLayout());
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(imageQrcode.getPath())));
            JLabel label = new JLabel(icon);
            label.setVisible(true);
            JLabel jLabel = new JLabel(imageQrcode.getTitle(),JLabel.CENTER);
            jLabel.setBorder(JBUI.Borders.customLine(JBColor.DARK_GRAY));
            innerPanel.add(jLabel,BorderLayout.NORTH);
            innerPanel.add(label,BorderLayout.CENTER);
            panel.add(innerPanel);
        }
        
        return panel;
    }


}