package org.gsonformat.intellij.process;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import org.gsonformat.intellij.config.ProjectConfig;
import org.gsonformat.intellij.entity.ClassEntity;
import org.gsonformat.intellij.entity.ConvertLibrary;


/**
 * Created by dim on 16/11/7.
 */
public class ClassProcessor {

    private PsiElementFactory factory;
    private PsiClass cls;
    private ProjectConfig config;

    public ClassProcessor(PsiElementFactory factory, PsiClass cls, ProjectConfig config) {
        this.factory = factory;
        this.cls = cls;
        this.config = config;

    }

    public void generate(ClassEntity classEntity, IProcessor visitor) {
        Processor processor = ProcessorHelper.getProcessor(ConvertLibrary.fromName(config.getConvertType()));
        if (processor != null) {
            processor.process(classEntity, factory, cls, visitor, config);
        }
    }
}
