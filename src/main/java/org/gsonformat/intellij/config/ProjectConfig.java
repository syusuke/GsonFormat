package org.gsonformat.intellij.config;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import org.gsonformat.intellij.entity.ConvertLibrary;

/**
 * @author Kerry on 2020/10/17
 */

public class ProjectConfig {

    public static final String KEY_OBJECT_FROM_DATA = "objectFromData";
    public static final String KEY_OBJECT_FROM_DATA_1 = "objectFromData1";
    public static final String KEY_ARRAY_FROM_DATA = "arrayFromData";
    public static final String KEY_ARRAY_FROM_DATA_1 = "arrayFromData1";

    public static final String KEY_USE_LOMBOK = "useLombok";
    public static final String KEY_USE_SET_AND_GET = "useSetAndGet";
    public static final String KEY_USE_WRAPPER_CLASS = "useWrapperClass";
    public static final String KEY_FIELD_PRIVATE_MODE = "fieldPrivateMode";
    public static final String KEY_USE_SERIALIZED_NAME = "useSerializedName";
    public static final String KEY_GENERATE_COMMENTS = "generateComments";
    public static final String KEY_VIRGO_MODE = "virgoMode";
    public static final String KEY_REUSE_ENTITY = "reuseEntity";
    public static final String KEY_SPLIT_GENERATE = "splitGenerate";
    public static final String KEY_FILED_NAME_PREFIX = "filedNamePrefix";
    public static final String KEY_ENTITY_SUFFIX = "entitySuffix";
    public static final String KEY_CONVERT_TYPE = "convertType";
    public static final String KEY_CONVERT_OTHER_ANNOTATION = "convertOtherAnnotation";


    private static ProjectConfig projectConfig;

    public void initConfig(Project project) {
        getConfig(project);
    }

    public static ProjectConfig getConfig(Project project) {
        if (projectConfig == null) {
            projectConfig = new ProjectConfig(project);
        } else {
            if (projectConfig.getProject() == project) {
                return projectConfig;
            } else {
                projectConfig = null;
                projectConfig = new ProjectConfig(project);
            }
        }
        return projectConfig;
    }

    private final Project project;

    public Project getProject() {
        return project;
    }


    private boolean objectFromData;
    private boolean objectFromData1;
    private boolean arrayFromData;
    private boolean arrayFromData1;

    private boolean useLombok;
    private boolean useSetAndGet;
    /**
     * 是否使用包装类来替代基本类型
     */
    private boolean useWrapperClass;
    /**
     * 字段使用 private default true
     */
    private boolean fieldPrivateMode;
    /**
     * true  Json属性名和类字段名一样时,也用注解
     */
    private boolean useSerializedName;
    /**
     * 生成Json注释
     */
    private boolean generateComments;
    /**
     * 处女座模式
     */
    private boolean virgoMode;
    /**
     * 是否重用
     */
    private boolean reuseEntity;
    /**
     * true: 不使用内部类
     */
    private boolean splitGenerate;

    /**
     * 字段前缀
     */
    private String filedNamePrefix;

    /**
     * 实体类后续
     */
    private String entitySuffix;

    private String convertType;
    private String convertOtherAnnotation;

    public ProjectConfig(Project project) {
        this.project = project;
        init();
    }

    public void init() {
        PropertiesComponent instance = PropertiesComponent.getInstance(project);

        objectFromData = instance.getBoolean(KEY_OBJECT_FROM_DATA, false);
        objectFromData1 = instance.getBoolean(KEY_OBJECT_FROM_DATA_1, false);
        arrayFromData = instance.getBoolean(KEY_ARRAY_FROM_DATA, false);
        arrayFromData1 = instance.getBoolean(KEY_ARRAY_FROM_DATA_1, false);

        useLombok = instance.getBoolean(KEY_USE_LOMBOK, false);
        useSetAndGet = instance.getBoolean(KEY_USE_SET_AND_GET, true);
        useWrapperClass = instance.getBoolean(KEY_USE_WRAPPER_CLASS, true);
        fieldPrivateMode = instance.getBoolean(KEY_FIELD_PRIVATE_MODE, true);
        useSerializedName = instance.getBoolean(KEY_USE_SERIALIZED_NAME, false);
        generateComments = instance.getBoolean(KEY_GENERATE_COMMENTS, true);
        virgoMode = instance.getBoolean(KEY_VIRGO_MODE, true);
        reuseEntity = instance.getBoolean(KEY_REUSE_ENTITY, false);
        splitGenerate = instance.getBoolean(KEY_SPLIT_GENERATE, false);
        filedNamePrefix = instance.getValue(KEY_FILED_NAME_PREFIX, "");
        entitySuffix = instance.getValue(KEY_ENTITY_SUFFIX, "");
        convertType = instance.getValue(KEY_CONVERT_TYPE, ConvertLibrary.NONE.name());
        convertOtherAnnotation = instance.getValue(KEY_CONVERT_OTHER_ANNOTATION, Constant.DEFAULT_OTHER_ANNOTATION);
    }

    public void save() {
        PropertiesComponent instance = PropertiesComponent.getInstance(project);
        instance.setValue(KEY_OBJECT_FROM_DATA, objectFromData);
        instance.setValue(KEY_OBJECT_FROM_DATA_1, objectFromData1);
        instance.setValue(KEY_ARRAY_FROM_DATA, arrayFromData);
        instance.setValue(KEY_ARRAY_FROM_DATA_1, arrayFromData1);

        instance.setValue(KEY_USE_LOMBOK, useLombok);
        instance.setValue(KEY_USE_SET_AND_GET, useSetAndGet);
        instance.setValue(KEY_USE_WRAPPER_CLASS, useWrapperClass);
        instance.setValue(KEY_FIELD_PRIVATE_MODE, fieldPrivateMode);
        instance.setValue(KEY_USE_SERIALIZED_NAME, useSerializedName);
        instance.setValue(KEY_GENERATE_COMMENTS, generateComments);
        instance.setValue(KEY_VIRGO_MODE, virgoMode);
        instance.setValue(KEY_REUSE_ENTITY, reuseEntity);
        instance.setValue(KEY_SPLIT_GENERATE, splitGenerate);
        instance.setValue(KEY_FILED_NAME_PREFIX, filedNamePrefix);
        instance.setValue(KEY_ENTITY_SUFFIX, entitySuffix);
        instance.setValue(KEY_CONVERT_TYPE, convertType);
        instance.setValue(KEY_CONVERT_OTHER_ANNOTATION, convertOtherAnnotation);
    }


    public boolean isUseLombok() {
        return useLombok;
    }

    public boolean isUseSetAndGet() {
        return useSetAndGet;
    }

    public boolean isUseWrapperClass() {
        return useWrapperClass;
    }

    public boolean isFieldPrivateMode() {
        return fieldPrivateMode;
    }

    public boolean isUseSerializedName() {
        return useSerializedName;
    }

    public boolean isGenerateComments() {
        return generateComments;
    }

    public boolean isVirgoMode() {
        return virgoMode;
    }

    public boolean isReuseEntity() {
        return reuseEntity;
    }

    public boolean isSplitGenerate() {
        return splitGenerate;
    }

    public String getFiledNamePrefix() {
        return filedNamePrefix;
    }

    public String getEntitySuffix() {
        return entitySuffix;
    }

    public ProjectConfig setUseLombok(boolean useLombok) {
        this.useLombok = useLombok;
        return this;
    }

    public ProjectConfig setUseSetAndGet(boolean useSetAndGet) {
        this.useSetAndGet = useSetAndGet;
        return this;
    }

    public ProjectConfig setUseWrapperClass(boolean useWrapperClass) {
        this.useWrapperClass = useWrapperClass;
        return this;
    }

    public ProjectConfig setFieldPrivateMode(boolean fieldPrivateMode) {
        this.fieldPrivateMode = fieldPrivateMode;
        return this;
    }

    public ProjectConfig setUseSerializedName(boolean useSerializedName) {
        this.useSerializedName = useSerializedName;
        return this;
    }

    public ProjectConfig setGenerateComments(boolean generateComments) {
        this.generateComments = generateComments;
        return this;
    }

    public ProjectConfig setVirgoMode(boolean virgoMode) {
        this.virgoMode = virgoMode;
        return this;
    }

    public ProjectConfig setReuseEntity(boolean reuseEntity) {
        this.reuseEntity = reuseEntity;
        return this;
    }

    public ProjectConfig setSplitGenerate(boolean splitGenerate) {
        this.splitGenerate = splitGenerate;
        return this;
    }

    public ProjectConfig setFiledNamePrefix(String filedNamePrefix) {
        this.filedNamePrefix = filedNamePrefix;
        return this;
    }

    public ProjectConfig setEntitySuffix(String entitySuffix) {
        this.entitySuffix = entitySuffix;
        return this;
    }

    public String getConvertType() {
        return convertType;
    }

    public ProjectConfig setConvertType(String convertType) {
        this.convertType = convertType;
        return this;
    }

    public String getConvertOtherAnnotation() {
        return convertOtherAnnotation;
    }

    public ProjectConfig setConvertOtherAnnotation(String convertOtherAnnotation) {
        this.convertOtherAnnotation = convertOtherAnnotation;
        return this;
    }

    public boolean isObjectFromData() {
        return objectFromData;
    }

    public ProjectConfig setObjectFromData(boolean objectFromData) {
        this.objectFromData = objectFromData;
        return this;
    }

    public boolean isObjectFromData1() {
        return objectFromData1;
    }

    public ProjectConfig setObjectFromData1(boolean objectFromData1) {
        this.objectFromData1 = objectFromData1;
        return this;
    }

    public boolean isArrayFromData() {
        return arrayFromData;
    }

    public ProjectConfig setArrayFromData(boolean arrayFromData) {
        this.arrayFromData = arrayFromData;
        return this;
    }

    public boolean isArrayFromData1() {
        return arrayFromData1;
    }

    public ProjectConfig setArrayFromData1(boolean arrayFromData1) {
        this.arrayFromData1 = arrayFromData1;
        return this;
    }

}
