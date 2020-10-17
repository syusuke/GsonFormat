package org.gsonformat.intellij.util;

import java.util.regex.Pattern;

/**
 * @author Kerry on 2020/10/17
 */

public class StringUtil {
    private static final String ID_PATTERN = "\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*";
    private static final Pattern JAVA_NAME_PATTERN = Pattern.compile(ID_PATTERN + "(\\." + ID_PATTERN + ")*");

    /**
     * 是否为正确的Java全限定类名
     * <p>
     * https://stackoverflow.com/questions/5205339/regular-expression-matching-fully-qualified-class-names
     *
     * @param identifier
     * @return
     */
    public static boolean validateJavaIdentifier(String identifier) {
        if (identifier == null || identifier.length() == 0) {
            return false;
        }
        return JAVA_NAME_PATTERN.matcher(identifier).matches();
    }
}
