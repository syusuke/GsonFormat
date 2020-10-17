package org.gsonformat.intellij.listeners;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.gsonformat.intellij.services.MyProjectService;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kerry on 2020/10/17
 */

public class MyProjectManagerListener implements ProjectManagerListener {
    @Override
    public void projectOpened(@NotNull Project project) {
        project.getService(MyProjectService.class);
    }
}
