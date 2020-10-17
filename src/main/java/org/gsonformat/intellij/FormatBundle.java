package org.gsonformat.intellij;

import com.intellij.AbstractBundle;

/**
 * @author Kerry on 2020/10/17
 */

public class FormatBundle extends AbstractBundle {
    public static final String FORMAT_BUNDLE = "FormatBundle.properties";

    private static FormatBundle INSTANCE = new FormatBundle();

    protected FormatBundle() {
        super(FORMAT_BUNDLE);
    }

    public static String message(String key, Object... params) {
        return INSTANCE.getMessage(key, params);
    }

}
