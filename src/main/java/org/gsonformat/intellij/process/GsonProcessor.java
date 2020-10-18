package org.gsonformat.intellij.process;


import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import org.gsonformat.intellij.config.Config;
import org.gsonformat.intellij.config.Constant;
import org.gsonformat.intellij.entity.ClassEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Created by dim on 16/11/7.
 */
class GsonProcessor extends Processor {

    @NotNull
    @Override
    protected String getFullNameAnnotation() {
        return Constant.gsonFullNameAnnotation;
    }

    @Override
    protected void generateConvertMethod(PsiElementFactory factory, PsiClass cls, ClassEntity classEntity) {
        if (cls == null || cls.getName() == null) {
            return;
        }
        if (config.isObjectFromData()) {
            createMethod(factory, Config.getInstant().getObjectFromDataStr().replace(CLASS_NAME_VAR, cls.getName()).trim(), cls);
        }
        if (config.isObjectFromData1()) {
            createMethod(factory, Config.getInstant().getObjectFromDataStr1().replace(CLASS_NAME_VAR, cls.getName()).trim(), cls);
        }
        if (config.isArrayFromData()) {
            createMethod(factory, Config.getInstant().getArrayFromDataStr().replace(CLASS_NAME_VAR, cls.getName()).trim(), cls);
        }
        if (config.isArrayFromData1()) {
            createMethod(factory, Config.getInstant().getArrayFromData1Str().replace(CLASS_NAME_VAR, cls.getName()).trim(), cls);
        }
    }
}
