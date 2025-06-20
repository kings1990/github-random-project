package io.github.kings1990.githubRandomProject.nomix;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.util.NlsActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class SuperBaseAction extends AnAction {
    

    public SuperBaseAction(@Nullable @NlsActions.ActionText String text,
                      @Nullable @NlsActions.ActionDescription String description,
                      @Nullable Icon icon) {
        super(text, description, icon);
    }
    
    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}