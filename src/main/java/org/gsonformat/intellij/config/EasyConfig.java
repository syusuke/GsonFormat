package org.gsonformat.intellij.config;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;

/**
 * @author Kerry on 2020/10/18
 */

public class EasyConfig {
    /**
     * json dialog 使用
     */
    public static final String KEY_ENTITY_PACK_NAME = "entityPackName";

    public static String getKeyEntityPackName(Project project) {
        return PropertiesComponent.getInstance(project).getValue(KEY_ENTITY_PACK_NAME);
    }

    public static void saveCurrentEntityPackage(Project project, String entityPackName) {
        if (entityPackName == null) {
            return;
        }
        PropertiesComponent.getInstance(project).setValue(KEY_ENTITY_PACK_NAME, entityPackName);
    }

}
