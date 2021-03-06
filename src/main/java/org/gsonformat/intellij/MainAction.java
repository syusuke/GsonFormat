package org.gsonformat.intellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtilBase;
import org.gsonformat.intellij.ui.JsonDialog;
import org.gsonformat.intellij.util.StringUtil;

/**
 * User: dim
 * Date: 14-7-4
 * Time: 下午1:44
 */
public class MainAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        if (project == null || editor == null) {
            // not null
            return;
        }

        PsiFile currentEditPsiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
        if (currentEditPsiFile instanceof PsiJavaFile) {
            PsiJavaFile psiJavaFile = (PsiJavaFile) currentEditPsiFile;
            // 当前光标所在位置
            int offset = editor.getCaretModel().getOffset();
            PsiClass psiClass = getTargetClass(psiJavaFile, offset);
            if (psiClass == null) {
                return;
            }
            JsonDialog jsonD = new JsonDialog(psiClass, psiJavaFile, project);
            jsonD.setSize(600, 400);
            jsonD.setLocationRelativeTo(null);
            jsonD.setVisible(true);
        }

    }

    /**
     * 获取文件所在的类
     *
     * @param psiJavaFile
     * @param offset
     * @return
     */
    public PsiClass getTargetClass(PsiJavaFile psiJavaFile, int offset) {

        PsiElement elementAt = psiJavaFile.findElementAt(offset);
        PsiElement parent;
        while (elementAt != null) {
            parent = elementAt;
            elementAt = elementAt.getParent();

            if (parent instanceof PsiClass) {
                return (PsiClass) parent;
            }
        }

        // 当前Java文件包含的所有类
        PsiClass[] classes = psiJavaFile.getClasses();

        if (classes.length == 0) {
            // 当前Java文件没有类
            return null;
        }

        // find public
        for (PsiClass cls : classes) {
            PsiModifierList modifierList = cls.getModifierList();
            if (modifierList == null) {
                continue;
            }
            // 一个Java文件可能包含多个类,返回 public
            if (modifierList.hasModifierProperty(PsiModifier.PUBLIC)) {
                return cls;
            }
        }

        String packageName = psiJavaFile.getPackageName();

        String javaFileName = psiJavaFile.getName();
        javaFileName = javaFileName.substring(0, javaFileName.indexOf("."));
        // 当前文件包的Java类没有public,找到和文件名一样的类
        for (PsiClass cls : classes) {
            if (javaFileName.equals(cls.getName())) {
                return cls;
            }
        }
        // select first
        return classes[0];

    }

}
