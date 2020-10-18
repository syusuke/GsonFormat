package org.gsonformat.intellij.ui;

import com.intellij.openapi.project.Project;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.codeStyle.VariableKind;
import org.apache.http.util.TextUtils;
import org.gsonformat.intellij.config.ProjectConfig;
import org.gsonformat.intellij.entity.ConvertLibrary;

import javax.swing.*;
import java.awt.event.*;

public class SettingDialog extends JFrame {

    private JPanel contentPane;
    private JRadioButton fieldPublicRadioButton;
    private JRadioButton fieldPrivateRadioButton;
    private JCheckBox useSerializedNameCheckBox;
    private JButton objectButton;
    private JButton object1Button;
    private JButton arrayButton;
    private JButton array1Button;
    private JTextField suffixEdit;
    private JCheckBox objectFromDataCB;
    private JCheckBox objectFromData1CB;
    private JCheckBox arrayFromDataCB;
    private JCheckBox arrayFromData1CB;
    private JCheckBox reuseEntityCB;
    private JButton cancelButton;
    private JButton okButton;
    private JTextField filedPrefixTF;
    private JRadioButton gsonJRB;
    private JRadioButton jackRB;
    private JRadioButton fastJsonRB;
    private JRadioButton otherRB;
    private JTextField annotationFT;
    private JCheckBox virgoModelCB;
    private JCheckBox generateCommentsCT;
    private JRadioButton loganSquareCB;
    private JRadioButton autoValueRadioButton;
    private JCheckBox splitGenerateMode;
    private String annotationStr;
    private JCheckBox useWrapperClassCB;
    private JLabel filedPrefixLB;
    private JCheckBox useSetAndGetCB;
    private JCheckBox useLombokCB;
    private JRadioButton noneJRB;

    private Project project;
    private ConvertLibrary convertType;


    public SettingDialog(Project project) {
        this.project = project;
        setContentPane(contentPane);
//        setModal(true);
        getRootPane().setDefaultButton(okButton);
        this.setAlwaysOnTop(true);
        setTitle("Settings");
        okButton.addActionListener(e -> onOk());
        cancelButton.addActionListener(e -> onCancel());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        ProjectConfig projectConfig = ProjectConfig.getConfig(project);

        initProjectConfig(projectConfig);

        initUiStatus();

        convertTypeListener();
        formDataListener();
        fromDataButtonListener();
    }

    private void initProjectConfig(ProjectConfig projectConfig) {

        objectButton.setEnabled(objectFromDataCB.isSelected());
        object1Button.setEnabled(objectFromData1CB.isSelected());
        arrayButton.setEnabled(arrayFromDataCB.isSelected());
        array1Button.setEnabled(arrayFromData1CB.isSelected());

        virgoModelCB.setSelected(projectConfig.isVirgoMode());
        generateCommentsCT.setSelected(projectConfig.isGenerateComments());
        splitGenerateMode.setSelected(projectConfig.isSplitGenerate());
        reuseEntityCB.setSelected(projectConfig.isReuseEntity());
        fieldPrivateRadioButton.setSelected(projectConfig.isFieldPrivateMode());
        useSerializedNameCheckBox.setSelected(projectConfig.isUseSerializedName());
        useWrapperClassCB.setSelected(projectConfig.isUseWrapperClass());
        useSetAndGetCB.setSelected(projectConfig.isUseSetAndGet());
        useLombokCB.setSelected(projectConfig.isUseLombok());

        suffixEdit.setText(projectConfig.getEntitySuffix());
        String filedNamePrefix = projectConfig.getFiledNamePrefix();
        //TODO
        if (TextUtils.isEmpty(filedNamePrefix)) {
            JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
            filedNamePrefix = styleManager.getPrefixByVariableKind(VariableKind.FIELD);
        }
        filedPrefixTF.setText(filedNamePrefix);

        convertType = ConvertLibrary.fromName(projectConfig.getConvertType());
        annotationStr = ConvertLibrary.getAnnotation(convertType);
        if (convertType == ConvertLibrary.OTHER) {
            annotationStr = projectConfig.getConvertOtherAnnotation();
        }
    }


    private void initUiStatus() {
        annotationFT.setText(annotationStr);

        // 初始状态
        switch (convertType) {
            case NONE:
                noneJRB.setSelected(true);
                annotationFT.setEnabled(false);
                enableFromData(false);
                enableFromDataButton(false);
                selectFromData(false);
                break;
            case GSON:
                gsonJRB.setSelected(true);
                annotationFT.setEnabled(false);
                enableFromData(true);
                enableFromDataButton(true);
                selectFromData(false);
                break;
            case JACKSON:
                jackRB.setSelected(true);
                annotationFT.setEnabled(false);
                enableFromData(false);
                enableFromDataButton(false);
                selectFromData(false);
                break;
            case FAST_JSON:
                fastJsonRB.setSelected(true);
                annotationFT.setEnabled(false);
                enableFromData(false);
                enableFromDataButton(false);
                selectFromData(false);
                break;
            case AUTO_VALUE:
                autoValueRadioButton.setSelected(true);
                annotationFT.setEnabled(false);
                enableFromData(false);
                enableFromDataButton(false);
                selectFromData(false);
                break;
            case LOGAN_SQUARE:
                loganSquareCB.setSelected(true);
                annotationFT.setEnabled(false);
                enableFromData(false);
                enableFromDataButton(false);
                selectFromData(false);
                break;
            case OTHER:
            default:
                otherRB.setSelected(true);
                annotationFT.setEnabled(true);
                enableFromData(false);
                enableFromDataButton(false);
                selectFromData(false);

                break;
        }
    }

    private String getAnnotationValue() {
        if (convertType == ConvertLibrary.OTHER) {
            return annotationFT.getText().trim();
        }
        return ConvertLibrary.getAnnotation(convertType);
    }

    private void convertTypeListener() {
        noneJRB.addActionListener(e -> {
            convertType = ConvertLibrary.NONE;
            annotationFT.setText(getAnnotationValue());
            annotationFT.setEnabled(false);
            enableFromData(false);
            enableFromDataButton(false);
            selectFromData(false);
        });
        jackRB.addActionListener(actionEvent -> {
            convertType = ConvertLibrary.JACKSON;
            annotationFT.setText(getAnnotationValue());
            annotationFT.setEnabled(false);
            enableFromData(false);
            selectFromData(false);
        });
        gsonJRB.addActionListener(actionEvent -> {
            convertType = ConvertLibrary.GSON;
            annotationFT.setText(getAnnotationValue());
            annotationFT.setEnabled(false);
            enableFromData(true);
            selectFromData(false);
        });
        fastJsonRB.addActionListener(actionEvent -> {
            convertType = ConvertLibrary.FAST_JSON;
            annotationFT.setText(getAnnotationValue());
            annotationFT.setEnabled(false);
            enableFromData(false);
            selectFromData(false);
        });
        loganSquareCB.addActionListener(actionEvent -> {
            convertType = ConvertLibrary.LOGAN_SQUARE;
            annotationFT.setText(getAnnotationValue());
            annotationFT.setEnabled(otherRB.isSelected());
            enableFromData(false);
            selectFromData(false);
        });
        autoValueRadioButton.addActionListener(e -> {
            convertType = ConvertLibrary.AUTO_VALUE;
            annotationFT.setText(getAnnotationValue());
            annotationFT.setEnabled(false);
            enableFromData(false);
            selectFromData(false);
        });
        otherRB.addActionListener(actionEvent -> {
            convertType = ConvertLibrary.OTHER;
            annotationFT.setText(getAnnotationValue());
            annotationFT.setEnabled(true);
            enableFromData(false);
            selectFromData(false);
        });
    }

    private void fromDataButtonListener() {

        objectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EditDialog editDialog = new EditDialog(EditDialog.Type.OBJECT_FROM_DATA);
                editDialog.setSize(600, 360);
                editDialog.setLocationRelativeTo(null);
                editDialog.setResizable(false);
                editDialog.setVisible(true);
            }
        });
        object1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EditDialog editDialog = new EditDialog(EditDialog.Type.OBJECT_FROM_DATA1);
                editDialog.setSize(600, 360);
                editDialog.setLocationRelativeTo(null);
                editDialog.setResizable(false);
                editDialog.setVisible(true);
            }
        });
        arrayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EditDialog editDialog = new EditDialog(EditDialog.Type.ARRAY_FROM_DATA);
                editDialog.setSize(600, 600);
                editDialog.setLocationRelativeTo(null);
                editDialog.setResizable(false);
                editDialog.setVisible(true);
            }
        });
        array1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EditDialog editDialog = new EditDialog(EditDialog.Type.ARRAY_FROM_DATA1);
                editDialog.setSize(600, 600);
                editDialog.setLocationRelativeTo(null);
                editDialog.setResizable(false);
                editDialog.setVisible(true);
            }
        });
    }

    private void formDataListener() {
        objectFromDataCB.addActionListener(actionEvent -> objectButton.setEnabled(objectFromDataCB.isSelected()));
        objectFromData1CB.addActionListener(actionEvent -> object1Button.setEnabled(objectFromData1CB.isSelected()));
        arrayFromDataCB.addActionListener(actionEvent -> arrayButton.setEnabled(arrayFromDataCB.isSelected()));
        arrayFromData1CB.addActionListener(actionEvent -> array1Button.setEnabled(arrayFromData1CB.isSelected()));
    }


    private void enableFromData(boolean enable) {
        objectFromDataCB.setEnabled(enable);
        objectFromData1CB.setEnabled(enable);
        arrayFromDataCB.setEnabled(enable);
        arrayFromData1CB.setEnabled(enable);

    }

    private void enableFromDataButton(boolean enable) {
        objectButton.setEnabled(enable);
        object1Button.setEnabled(enable);
        arrayButton.setEnabled(enable);
        array1Button.setEnabled(enable);
    }

    private void selectFromData(boolean select) {
        objectFromDataCB.setSelected(select);
        objectFromData1CB.setSelected(select);
        arrayFromDataCB.setSelected(select);
        arrayFromData1CB.setSelected(select);
    }

    private void onOk() {
        ProjectConfig projectConfig = ProjectConfig.getConfig(project);

        projectConfig.setObjectFromData(objectFromDataCB.isSelected());
        projectConfig.setObjectFromData1(objectFromData1CB.isSelected());
        projectConfig.setArrayFromData(arrayFromDataCB.isSelected());
        projectConfig.setArrayFromData1(arrayFromData1CB.isSelected());

        projectConfig.setVirgoMode(virgoModelCB.isSelected());
        projectConfig.setGenerateComments(generateCommentsCT.isSelected());
        projectConfig.setSplitGenerate(splitGenerateMode.isSelected());
        projectConfig.setReuseEntity(reuseEntityCB.isSelected());
        projectConfig.setFieldPrivateMode(fieldPrivateRadioButton.isSelected());
        projectConfig.setUseSerializedName(useSerializedNameCheckBox.isSelected());
        projectConfig.setUseWrapperClass(useWrapperClassCB.isSelected());
        projectConfig.setUseSetAndGet(useSetAndGetCB.isSelected());
        projectConfig.setUseLombok(useLombokCB.isSelected());

        projectConfig.setEntitySuffix(suffixEdit.getText().trim());
        projectConfig.setFiledNamePrefix(filedPrefixTF.getText().trim());
        projectConfig.setConvertType(convertType.name());

        if (otherRB.isSelected()) {
            String text = annotationFT.getText().trim();
            projectConfig.setConvertOtherAnnotation(text);
        }

        projectConfig.save();

        dispose();

    }

    private void createUIComponents() {
    }


    private void onCancel() {
        dispose();
    }


}
