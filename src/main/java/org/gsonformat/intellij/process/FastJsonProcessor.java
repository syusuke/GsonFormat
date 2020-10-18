package org.gsonformat.intellij.process;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import org.gsonformat.intellij.config.Constant;
import org.gsonformat.intellij.entity.ClassEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Created by dim on 16/11/7.
 */
class FastJsonProcessor extends Processor {

    @Override
    public void onStarProcess(ClassEntity classEntity, PsiElementFactory factory, PsiClass cls, IProcessor visitor) {
        super.onEndProcess(classEntity, factory, cls, visitor);
    }

    @NotNull
    @Override
    protected String getFullNameAnnotation() {
        return Constant.fastFullNameAnnotation;
    }
}
