package org.gsonformat.intellij.types;

/**
 * @author kerryzhang on 20/11/30
 */

public enum ConvertLibrary {
    /**
     * NONE
     */
    NONE("None", ""),
    /**
     * jackson
     */
    JACKSON("Jackson", ""),
    /**
     * gson
     */
    GSON("Gson", ""),
    /**
     * alibaba fast json
     */
    FAST_JSON("FastJson", ""),
    /**
     * logan
     */
    LOGAN_SQUARE("LoganSquare", ""),
    /**
     * auth value
     */
    AUTO_VALUE("AutoValue", ""),
    /**
     * Moshi
     */
    MOSHI("Moshi", ""),
    /**
     * other
     */
    OTHER("Other", "");

    private final String name;
    private final String annotation;

    ConvertLibrary(String name, String annotation) {
        this.name = name;
        this.annotation = annotation;
    }

    public String getName() {
        return name;
    }

    public String getAnnotation() {
        return annotation;
    }


    public static ConvertLibrary fromName(String name) {
        for (ConvertLibrary value : values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        // default
        return NONE;
    }

}
