package org.gsonformat.intellij.process;

import org.jetbrains.annotations.NotNull;

/**
 * Created by dim on 16/11/7.
 */
public class OtherProcessor extends Processor {
    @NotNull
    @Override
    protected String getFullNameAnnotation() {
        return "@{filed}".replaceAll("\\(", "(").replaceAll("\\)", ")").replaceAll("\\s\\*", "");
    }
}
