package org.gsonformat.intellij.action;

import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import org.gsonformat.intellij.config.Constants;
import org.gsonformat.intellij.config.ProjectConfig;
import org.gsonformat.intellij.entity.ClassEntity;
import org.gsonformat.intellij.process.ClassProcessor;
import org.gsonformat.intellij.process.IProcessor;
import org.gsonformat.intellij.ui.Toast;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kerry on 2020/10/17
 */

public class DataWriteAction implements Runnable {

    private PsiClass cls;
    private PsiElementFactory factory;
    private Project project;
    private PsiFile file;
    private ClassEntity targetClass = null;
    private List<String> generateClassList = new ArrayList<>();

    public DataWriteAction(PsiFile file, Project project, PsiClass cls) {

        factory = JavaPsiFacade.getElementFactory(project);
        this.file = file;
        this.project = project;
        this.cls = cls;
    }


    public void execute(ClassEntity targetClass) {
        this.targetClass = targetClass;
        ProgressManager.getInstance().run(new Task.Backgroundable(project, Constants.PLUGIN_NAME) {

            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                progressIndicator.setIndeterminate(true);
                long currentTimeMillis = System.currentTimeMillis();
                WriteCommandAction.runWriteCommandAction(project, DataWriteAction.this);
                progressIndicator.setIndeterminate(false);
                progressIndicator.setFraction(1.0);

                Toast.make(project, MessageType.INFO, Constants.PLUGIN_NAME + " [" + (System.currentTimeMillis() - currentTimeMillis) + " ms]\n");
            }
        });

    }


    @Override
    public void run() {
        if (targetClass == null) {
            return;
        }
        ProjectConfig config = ProjectConfig.getConfig(project);
        generateClassList.clear();
        new ClassProcessor(factory, cls, config).generate(targetClass, new IProcessor() {
            @Override
            public void onStarProcess(ClassEntity classEntity, PsiElementFactory factory, PsiClass cls) {
                generateClassList.add(cls.getQualifiedName());
            }

            @Override
            public void onEndProcess(ClassEntity classEntity, PsiElementFactory factory, PsiClass cls) {

            }

            @Override
            public void onStartGenerateClass(PsiElementFactory factory, ClassEntity classEntity, PsiClass parentClass) {

            }

            @Override
            public void onEndGenerateClass(PsiElementFactory factory, ClassEntity classEntity, PsiClass parentClass, PsiClass generateClass) {
                generateClassList.add(generateClass.getQualifiedName());

            }
        });
    }
}
