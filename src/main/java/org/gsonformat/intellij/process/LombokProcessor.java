package org.gsonformat.intellij.process;

import com.intellij.psi.*;
import org.apache.http.util.TextUtils;
import org.gsonformat.intellij.common.FieldHelper;
import org.gsonformat.intellij.common.Try;
import org.gsonformat.intellij.config.Config;
import org.gsonformat.intellij.config.Constant;
import org.gsonformat.intellij.entity.ClassEntity;
import org.gsonformat.intellij.entity.FieldEntity;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Created by dim on 16/11/7.
 */

@Deprecated
public class LombokProcessor extends Processor {

    private static final Pattern NO_ARGS_CONSTRUCTOR = Pattern.compile("@.*?NoArgsConstructor");
    private static final Pattern DATA_PATTERN = Pattern.compile("@.*?Data");

    @Override
    protected void onStarProcess(ClassEntity classEntity, PsiElementFactory factory, PsiClass cls, IProcessor visitor) {
        super.onStarProcess(classEntity, factory, cls, visitor);
        injectAnnotation(factory, cls);
    }

    private void injectAnnotation(PsiElementFactory factory, PsiClass generateClass) {
        if (factory == null || generateClass == null) {
            return;
        }
        PsiModifierList modifierList = generateClass.getModifierList();
        if (modifierList != null) {
            PsiElement firstChild = modifierList.getFirstChild();
            if (firstChild != null && !NO_ARGS_CONSTRUCTOR.matcher(firstChild.getText()).find()) {
                PsiAnnotation annotationFromText = factory.createAnnotationFromText("@lombok.NoArgsConstructor", generateClass);
                modifierList.addBefore(annotationFromText, firstChild);
            }
            if (firstChild != null && !DATA_PATTERN.matcher(firstChild.getText()).find()) {
                PsiAnnotation annotationFromText = factory.createAnnotationFromText("@lombok.Data", generateClass);
                modifierList.addBefore(annotationFromText, firstChild);
            }
        }
    }

    @Override
    protected void generateField(PsiElementFactory factory, FieldEntity fieldEntity, PsiClass cls, ClassEntity classEntity) {
        if (fieldEntity.isGenerate()) {
            Try.run(new Try.TryListener() {
                @Override
                public void run() {
                    cls.add(factory.createFieldFromText(generateLombokFieldText(classEntity, fieldEntity, null), cls));
                }

                @Override
                public void runAgain() {
                    fieldEntity.setFieldName(FieldHelper.generateLuckyFieldName(fieldEntity.getFieldName()));
                    cls.add(factory.createFieldFromText(generateLombokFieldText(classEntity, fieldEntity, Constant.FIXME), cls));
                }

                @Override
                public void error() {
                    cls.addBefore(factory.createCommentFromText("// FIXME generate failure  field " + fieldEntity.getFieldName(), cls), cls.getChildren()[0]);
                }
            });
        }
    }

    @NotNull
    @Override
    protected String getFullNameAnnotation() {
        return "";
    }

    @Override
    protected void createGetAndSetMethod(PsiElementFactory factory, PsiClass cls, FieldEntity field) {
    }

    @Override
    protected void onEndGenerateClass(PsiElementFactory factory, ClassEntity classEntity, PsiClass parentClass, PsiClass generateClass, IProcessor visitor) {
        super.onEndGenerateClass(factory, classEntity, parentClass, generateClass, visitor);
        injectAnnotation(factory, generateClass);
    }

    private String generateLombokFieldText(ClassEntity classEntity, FieldEntity fieldEntity, String fixme) {
        fixme = fixme == null ? "" : fixme;

        StringBuilder fieldSb = new StringBuilder();
        String filedName = fieldEntity.getGenerateFieldName();
        if (!TextUtils.isEmpty(classEntity.getExtra())) {
            fieldSb.append(classEntity.getExtra()).append("\n");
            classEntity.setExtra(null);
        }
        if (fieldEntity.getTargetClass() != null) {
            fieldEntity.getTargetClass().setGenerate(true);
        }

        if (config.isFieldPrivateMode()) {
            fieldSb.append("private  ").append(fieldEntity.getFullNameType()).append(" ").append(filedName).append(" ; ");
        } else {
            fieldSb.append("public  ").append(fieldEntity.getFullNameType()).append(" ").append(filedName).append(" ; ");
        }
        return fieldSb.append(fixme).toString();
    }
}
