package org.gsonformat.intellij.process;

import org.jetbrains.annotations.NotNull;

/**
 * @author Kerry on 2020/10/18
 */

class NoneProcessor extends Processor {
    @NotNull
    @Override
    protected String getFullNameAnnotation() {
        return "";
    }
}
