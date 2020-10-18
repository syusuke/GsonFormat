package org.gsonformat.intellij.process;

import com.intellij.psi.*;
import org.gsonformat.intellij.config.Constant;
import org.gsonformat.intellij.entity.ClassEntity;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Created by dim on 16/11/7.
 */
class JacksonProcessor extends Processor {

    private static final Pattern PATTERN = Pattern.compile("@.*?JsonIgnoreProperties");

    @Override
    protected void onStartGenerateClass(PsiElementFactory factory, ClassEntity classEntity, PsiClass parentClass, IProcessor visitor) {
        super.onStartGenerateClass(factory, classEntity, parentClass, visitor);
    }

    @Override
    public void onStarProcess(ClassEntity classEntity, PsiElementFactory factory, PsiClass cls, IProcessor visitor) {
        super.onStarProcess(classEntity, factory, cls, visitor);
        injectAnnotation(factory, cls);
    }

    @Override
    protected void onEndGenerateClass(PsiElementFactory factory, ClassEntity classEntity, PsiClass parentClass, PsiClass generateClass, IProcessor visitor) {
        super.onEndGenerateClass(factory, classEntity, parentClass, generateClass, visitor);
        injectAnnotation(factory, generateClass);
    }

    @NotNull
    @Override
    protected String getFullNameAnnotation() {
        return Constant.jackFullNameAnnotation;
    }

    private void injectAnnotation(PsiElementFactory factory, PsiClass generateClass) {
        if (factory == null || generateClass == null) {
            return;
        }
        PsiModifierList modifierList = generateClass.getModifierList();
        if (modifierList == null) {
            return;
        }
        PsiElement firstChild = modifierList.getFirstChild();
        if (firstChild != null && !PATTERN.matcher(firstChild.getText()).find()) {
            PsiAnnotation annotationFromText = factory.createAnnotationFromText("@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)", generateClass);
            modifierList.addBefore(annotationFromText, firstChild);
        }
    }
}
