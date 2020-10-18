package org.gsonformat.intellij.entity;

import org.gsonformat.intellij.config.Constant;

/**
 * Created by didm on 16/11/7.
 */
public enum ConvertLibrary {
    /**
     * none
     */
    NONE,
    /**
     * google gson
     */
    GSON,
    /**
     * jackson
     */
    JACKSON,
    /**
     * fastjson
     */
    FAST_JSON,
    /**
     * logan
     */
    LOGAN_SQUARE,
    /**
     * auto Value
     */
    AUTO_VALUE,
    /**
     * other
     */
    OTHER;

    public static ConvertLibrary fromName(String name) {
        for (ConvertLibrary value : values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return ConvertLibrary.NONE;
    }

    public static String getAnnotation(ConvertLibrary type) {
        switch (type) {
            case GSON:
                return Constant.gsonAnnotation;
            case JACKSON:
                return Constant.jackAnnotation;
            case FAST_JSON:
                return Constant.fastAnnotation;
            case AUTO_VALUE:
                return Constant.autoValueAnnotation;
            case LOGAN_SQUARE:
                return Constant.loganSquareAnnotation;
            case OTHER:
                return Constant.DEFAULT_OTHER_ANNOTATION;
            default:
                break;
        }
        return "";
    }


    public static ConvertLibrary from(String annotation) {
        if (annotation == null) {
            return NONE;
        }
        switch (annotation) {
            case Constant.gsonAnnotation:
                return GSON;
            case Constant.jackAnnotation:
                return JACKSON;
            case Constant.fastAnnotation:
                return FAST_JSON;
            case Constant.loganSquareAnnotation:
                return LOGAN_SQUARE;
            case Constant.autoValueAnnotation:
                return AUTO_VALUE;
            default:
                return OTHER;
        }
    }
}
