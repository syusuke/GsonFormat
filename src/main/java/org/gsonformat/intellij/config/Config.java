package org.gsonformat.intellij.config;

import com.intellij.ide.util.PropertiesComponent;

/**
 * Created by dim on 15/5/31.
 */
public class Config {

    private static Config config;


    private String objectFromDataStr;
    private String objectFromDataStr1;
    private String arrayFromDataStr;
    private String arrayFromData1Str;

    /**
     * 错误次数,前两次提醒哪里查看错误日志.
     */
    private int errorCount;


    private Config() {

    }

    public void save() {

        PropertiesComponent.getInstance().setValue("objectFromDataStr", objectFromDataStr + "");
        PropertiesComponent.getInstance().setValue("objectFromDataStr1", objectFromDataStr1 + "");
        PropertiesComponent.getInstance().setValue("arrayFromDataStr", arrayFromDataStr + "");
        PropertiesComponent.getInstance().setValue("arrayFromData1Str", arrayFromData1Str + "");

        PropertiesComponent.getInstance().setValue("errorCount", errorCount + "");

    }

    public static Config getInstant() {

        if (config == null) {
            config = new Config();

            config.setObjectFromDataStr(PropertiesComponent.getInstance().getValue("objectFromDataStr", Constant.objectFromObject));
            config.setObjectFromDataStr1(PropertiesComponent.getInstance().getValue("objectFromDataStr1", Constant.objectFromObject1));
            config.setArrayFromDataStr(PropertiesComponent.getInstance().getValue("arrayFromDataStr", Constant.arrayFromData));
            config.setArrayFromData1Str(PropertiesComponent.getInstance().getValue("arrayFromData1Str", Constant.arrayFromData1));

            config.setErrorCount(PropertiesComponent.getInstance().getInt("errorCount", 0));

        }
        return config;
    }


    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }


    public void setObjectFromDataStr(String objectFromDataStr) {
        this.objectFromDataStr = objectFromDataStr;
    }

    public void setObjectFromDataStr1(String objectFromDataStr1) {
        this.objectFromDataStr1 = objectFromDataStr1;
    }

    public void setArrayFromDataStr(String arrayFromDataStr) {
        this.arrayFromDataStr = arrayFromDataStr;
    }

    public void setArrayFromData1Str(String arrayFromData1Str) {
        this.arrayFromData1Str = arrayFromData1Str;
    }

    public String getObjectFromDataStr() {
        return objectFromDataStr;
    }

    public String getObjectFromDataStr1() {
        return objectFromDataStr1;
    }

    public String getArrayFromDataStr() {
        return arrayFromDataStr;
    }

    public String getArrayFromData1Str() {
        return arrayFromData1Str;
    }


    public void saveObjectFromDataStr(String objectFromDataStr) {
        this.objectFromDataStr = objectFromDataStr;
        PropertiesComponent.getInstance().setValue("objectFromDataStr", objectFromDataStr + "");
    }

    public void saveObjectFromDataStr1(String objectFromDataStr1) {
        this.objectFromDataStr1 = objectFromDataStr1;
        PropertiesComponent.getInstance().setValue("objectFromDataStr1", objectFromDataStr1 + "");
    }

    public void saveArrayFromDataStr(String arrayFromDataStr) {
        this.arrayFromDataStr = arrayFromDataStr;
        PropertiesComponent.getInstance().setValue("arrayFromDataStr", arrayFromDataStr + "");
    }

    public void saveArrayFromData1Str(String arrayFromData1Str) {
        this.arrayFromData1Str = arrayFromData1Str;
        PropertiesComponent.getInstance().setValue("arrayFromData1Str", arrayFromData1Str + "");
    }

    public boolean isToastError() {
        if (Config.getInstant().getErrorCount() < 3) {
            Config.getInstant().setErrorCount(Config.getInstant().getErrorCount() + 1);
            Config.getInstant().save();
            return true;
        }
        return false;
    }
}
