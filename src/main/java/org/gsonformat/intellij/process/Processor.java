package org.gsonformat.intellij.process;

import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import org.apache.http.util.TextUtils;
import org.gsonformat.intellij.common.FieldHelper;
import org.gsonformat.intellij.common.PsiClassUtil;
import org.gsonformat.intellij.common.Try;
import org.gsonformat.intellij.config.Config;
import org.gsonformat.intellij.config.Constant;
import org.gsonformat.intellij.config.ProjectConfig;
import org.gsonformat.intellij.entity.ClassEntity;
import org.gsonformat.intellij.entity.FieldEntity;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import static org.gsonformat.intellij.common.StringUtils.captureName;

/**
 * Created by dim on 16/11/7.
 */
public abstract class Processor {

    public static final String CLASS_NAME_VAR = "$ClassName$";

    protected ProjectConfig config;
    protected String mainPackage;

    public void process(ClassEntity classEntity, PsiElementFactory factory, PsiClass cls, IProcessor visitor, ProjectConfig projectConfig) {
        this.config = projectConfig;
        mainPackage = PsiClassUtil.getPackage(cls);
        onStarProcess(classEntity, factory, cls, visitor);

        for (FieldEntity fieldEntity : classEntity.getFields()) {
            generateField(factory, fieldEntity, cls, classEntity);
        }
        for (ClassEntity innerClass : classEntity.getInnerClasss()) {
            generateClass(factory, innerClass, cls, visitor);
        }

        if (this.config.isUseSetAndGet()) {
            generateGetterAndSetter(factory, cls, classEntity);
        }

        generateConvertMethod(factory, cls, classEntity);
        onEndProcess(classEntity, factory, cls, visitor);
    }

    private static final Pattern NO_ARGS_CONSTRUCTOR = Pattern.compile("@.*?NoArgsConstructor");
    private static final Pattern DATA_PATTERN = Pattern.compile("@.*?Data");

    private void injectLombokAnnotation(PsiElementFactory factory, PsiClass generateClass) {
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


    protected void onEndProcess(ClassEntity classEntity, PsiElementFactory factory, PsiClass cls, IProcessor visitor) {
        if (config.isUseLombok()) {
            injectLombokAnnotation(factory, cls);
        }
        if (visitor != null) {
            visitor.onEndProcess(classEntity, factory, cls);
        }
        formatJavCode(cls);
    }

    protected void formatJavCode(PsiClass cls) {
        if (cls == null) {
            return;
        }
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(cls.getProject());
        styleManager.optimizeImports(cls.getContainingFile());
        styleManager.shortenClassReferences(cls);
    }

    protected void onStarProcess(ClassEntity classEntity, PsiElementFactory factory, PsiClass cls, IProcessor visitor) {
        if (visitor != null) {
            visitor.onStarProcess(classEntity, factory, cls);
        }
    }

    protected void generateConvertMethod(PsiElementFactory factory, PsiClass cls, ClassEntity classEntity) {

    }

    protected void generateGetterAndSetter(PsiElementFactory factory, PsiClass cls, ClassEntity classEntity) {
        if (config.isFieldPrivateMode()) {
            for (FieldEntity field : classEntity.getFields()) {
                createGetAndSetMethod(factory, cls, field);
            }
        }
    }

    protected void createMethod(PsiElementFactory factory, String method, PsiClass cls) {
        Try.run(new Try.TryListener() {
            @Override
            public void run() {
                cls.add(factory.createMethodFromText(method, cls));
            }

            @Override
            public void runAgain() {

            }

            @Override
            public void error() {

            }
        });
    }


    protected void createGetAndSetMethod(PsiElementFactory factory, PsiClass cls, FieldEntity field) {
        if (field.isGenerate()) {
            String fieldName = field.getGenerateFieldName();
            String typeStr = field.getRealType();
            if (!TextUtils.isEmpty(config.getFiledNamePrefix())) {
                String temp = fieldName.replaceAll("^" + config.getFiledNamePrefix(), "");
                if (!TextUtils.isEmpty(temp)) {
                    fieldName = temp;
                }
            }
            if ("boolean".equals(typeStr) || "Boolean".equals(typeStr)) {
                String method = "public ".concat(typeStr).concat("   is").concat(
                        captureName(fieldName)).concat("() {   return ").concat(
                        field.getGenerateFieldName()).concat(" ;} ");
                cls.add(factory.createMethodFromText(method, cls));
            } else {
                String method = "public ".concat(typeStr).concat("   get").concat(
                        captureName(fieldName)).concat(
                        "() {   return ").concat(
                        field.getGenerateFieldName()).concat(" ;} ");
                cls.add(factory.createMethodFromText(method, cls));
            }

            String arg = fieldName;
            if (!TextUtils.isEmpty(config.getFiledNamePrefix())) {
                String temp = fieldName.replaceAll("^" + config.getFiledNamePrefix(), "");
                if (!TextUtils.isEmpty(temp)) {
                    fieldName = temp;
                    arg = fieldName;
                    if (arg.length() > 0) {

                        if (arg.length() > 1) {
                            arg = (arg.charAt(0) + "").toLowerCase() + arg.substring(1);
                        } else {
                            arg = arg.toLowerCase();
                        }
                    }
                }
            }

            String method = "public void  set".concat(captureName(fieldName)).concat("( ").concat(typeStr).concat(" ").concat(arg).concat(") {   ");
            if (field.getGenerateFieldName().equals(arg)) {
                method = method.concat("this.").concat(field.getGenerateFieldName()).concat(" = ").concat(arg).concat(";} ");
            } else {
                method = method.concat(field.getGenerateFieldName()).concat(" = ").concat(arg).concat(";} ");
            }

            String finalMethod = method;
            String finalFieldName = fieldName;
            Try.run(new Try.TryListener() {
                @Override
                public void run() {
                    cls.add(factory.createMethodFromText(finalMethod, cls));
                }

                @Override
                public void runAgain() {
                    cls.addBefore(factory.createCommentFromText("// FIXME generate failure  method  set and get " + captureName(finalFieldName), cls), cls.getChildren()[0]);

                }

                @Override
                public void error() {

                }
            });
        }
    }

    protected void generateClass(PsiElementFactory factory, ClassEntity classEntity, PsiClass parentClass, IProcessor visitor) {

        onStartGenerateClass(factory, classEntity, parentClass, visitor);
        PsiClass generateClass = null;
        if (classEntity.isGenerate()) {
            //// TODO: 16/11/9  待重构 
            if (config.isSplitGenerate()) {
                try {
                    generateClass = PsiClassUtil.getPsiClass(parentClass.getContainingFile(), parentClass.getProject(), classEntity.getQualifiedName());
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            } else {
                String classContent = "public static class " + classEntity.getClassName() + "{}";
                generateClass = factory.createClassFromText(classContent, null).getInnerClasses()[0];
            }

            if (generateClass != null) {

                for (ClassEntity innerClass : classEntity.getInnerClasss()) {
                    generateClass(factory, innerClass, generateClass, visitor);
                }
                if (!config.isSplitGenerate()) {
                    generateClass = (PsiClass) parentClass.add(generateClass);
                }
                for (FieldEntity fieldEntity : classEntity.getFields()) {
                    generateField(factory, fieldEntity, generateClass, classEntity);
                }
                if (config.isUseSetAndGet()) {
                    generateGetterAndSetter(factory, generateClass, classEntity);
                }
                generateConvertMethod(factory, generateClass, classEntity);
            }
        }
        onEndGenerateClass(factory, classEntity, parentClass, generateClass, visitor);
        if (config.isSplitGenerate()) {
            formatJavCode(generateClass);
        }
    }

    protected void onStartGenerateClass(PsiElementFactory factory, ClassEntity classEntity, PsiClass parentClass, IProcessor visitor) {
        if (visitor != null) {
            visitor.onStartGenerateClass(factory, classEntity, parentClass);
        }
    }

    protected void onEndGenerateClass(PsiElementFactory factory, ClassEntity classEntity, PsiClass parentClass, PsiClass generateClass, IProcessor visitor) {
        if (visitor != null) {
            visitor.onEndGenerateClass(factory, classEntity, parentClass, generateClass);
        }
    }

    protected void generateField(PsiElementFactory factory, FieldEntity fieldEntity, PsiClass cls, ClassEntity classEntity) {

        if (fieldEntity.isGenerate()) {
            Try.run(new Try.TryListener() {
                @Override
                public void run() {
                    cls.add(factory.createFieldFromText(generateFieldText(classEntity, fieldEntity, null), cls));

                }

                @Override
                public void runAgain() {
                    fieldEntity.setFieldName(FieldHelper.generateLuckyFieldName(fieldEntity.getFieldName()));
                    cls.add(factory.createFieldFromText(generateFieldText(classEntity, fieldEntity, Constant.FIXME), cls));
                }

                @Override
                public void error() {
                    cls.addBefore(factory.createCommentFromText("// FIXME generate failure  field " + fieldEntity.getFieldName(), cls), cls.getChildren()[0]);
                }
            });

        }

    }

    @NotNull
    protected abstract String getFullNameAnnotation();


    private String generateFieldText(ClassEntity classEntity, FieldEntity fieldEntity, String fixme) {
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
        if (!filedName.equals(fieldEntity.getKey()) || config.isUseSerializedName()) {
            fieldSb.append(getFullNameAnnotation().replaceAll("\\{filed}", fieldEntity.getKey()));
        }

        if (config.isFieldPrivateMode()) {
            fieldSb.append("private  ").append(fieldEntity.getFullNameType()).append(" ").append(filedName).append(" ; ");
        } else {
            fieldSb.append("public  ").append(fieldEntity.getFullNameType()).append(" ").append(filedName).append(" ; ");
        }
        return fieldSb.append(fixme).toString();
    }

}
