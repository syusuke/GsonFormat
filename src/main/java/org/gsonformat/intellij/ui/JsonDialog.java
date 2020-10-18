package org.gsonformat.intellij.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;
import org.apache.http.util.TextUtils;
import org.gsonformat.intellij.ConvertBridge;
import org.gsonformat.intellij.common.PsiClassUtil;
import org.gsonformat.intellij.config.Config;
import org.gsonformat.intellij.config.Constants;
import org.gsonformat.intellij.config.EasyConfig;
import org.gsonformat.intellij.config.ProjectConfig;
import org.gsonformat.intellij.util.StringUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JsonDialog extends JFrame implements ConvertBridge.Operator {

    private JPanel contentPane2;
    private JButton okButton;
    private JButton cancelButton;
    private JLabel errorLB;
    private JTextPane editTP;
    private JButton settingButton;
    private JLabel generateClassLB;
    private JTextField generateClassTF;
    private JPanel generateClassP;
    private JButton formatBtn;
    private JRadioButton setterGetterRadioButton;
    private JRadioButton lombokRadioButton;
    private final PsiClass currentEditorClass;
    private final PsiJavaFile currentEditFile;
    private final Project project;
    private final String currentClass;
    private String errorInfo = null;

    public JsonDialog(PsiClass currentEditorClass, PsiJavaFile currentEditFile, Project project) throws HeadlessException {
        this.currentEditorClass = currentEditorClass;
        this.currentEditFile = currentEditFile;
        this.project = project;
        this.currentClass = this.currentEditorClass.getQualifiedName();
        // currentClass = ((PsiJavaFileImpl) file).getPackageName() + "." + file.getName().split("\\.")[0];

        setContentPane(contentPane2);
        setTitle(Constants.PLUGIN_NAME);
        getRootPane().setDefaultButton(okButton);
        this.setAlwaysOnTop(true);

        initGeneratePanel(currentEditFile);
        initListener();
    }

    private void initGeneratePanel(PsiJavaFile file) {
        this.generateClassEditable(false);

        generateClassTF.setBackground(errorLB.getBackground());

        generateClassTF.setFocusable(true);
        generateClassTF.setText(currentClass);
        generateClassLB.setText(currentClass);

        generateClassTF.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                String generateClassName = generateClassTF.getText().trim();
                if (!StringUtil.validateJavaIdentifier(generateClassName)) {
                    Toast.make(project, generateClassP, MessageType.ERROR, "Invalid class name");
                    generateClassTF.requestFocusInWindow();
                } else {
                    generateClassTF.setText(generateClassName);
                    generateClassEditable(false);
                    generateClassEditable(false);
                    generateClassLB.setText(generateClassName);
                }
            }
        });
        generateClassLB.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                String entityPackName = EasyConfig.getKeyEntityPackName(project);
                if (generateClassLB.getText().equals(currentClass) &&
                        !TextUtils.isEmpty(entityPackName)
                        && !"null".equals(entityPackName)) {
                    generateClassLB.setText(entityPackName);
                    generateClassTF.setText(entityPackName);
                }
                generateClassEditable(true);

                // edit 获取到焦点
                generateClassTF.requestFocusInWindow();
            }

        });
    }


    private void initListener() {

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String jsonStr = editTP.getText().trim();
                if (TextUtils.isEmpty(jsonStr)) {
                    editTP.requestFocusInWindow();
                } else {
                    onOk(jsonStr);
                }
            }
        });
        formatBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String json = editTP.getText();
                json = json.trim();
                if (json.startsWith("{")) {
                    JSONObject jsonObject = new JSONObject(json);
                    String formatJson = jsonObject.toString(4);
                    editTP.setText(formatJson);
                } else if (json.startsWith("[")) {
                    JSONArray jsonArray = new JSONArray(json);
                    String formatJson = jsonArray.toString(4);
                    editTP.setText(formatJson);
                }

            }
        });
        editTP.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                super.keyReleased(keyEvent);
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    onOk();
                }
            }
        });
        generateClassP.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                super.keyReleased(keyEvent);
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    editTP.requestFocus(true);
                }
            }
        });
        errorLB.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                if (errorInfo != null) {
                    ErrorDialog errorDialog = new ErrorDialog(errorInfo);
                    errorDialog.setSize(800, 600);
                    errorDialog.setLocationRelativeTo(null);
                    errorDialog.setVisible(true);
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        settingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSettingDialog();
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane2.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    private void generateClassEditable(boolean editable) {
        generateClassLB.setEnabled(!editable);
        generateClassLB.setVisible(!editable);

        generateClassTF.setEnabled(editable);
        generateClassTF.setVisible(editable);
    }

    private void onOk() {
        String jsonStr = editTP.getText().trim();
        if (TextUtils.isEmpty(jsonStr)) {
            editTP.requestFocusInWindow();
        } else {
            onOk(jsonStr);
        }
    }

    private void onOk(String jsonStr) {
        this.setAlwaysOnTop(false);

        String generateClassName = generateClassTF.getText().replaceAll(" ", "").replaceAll(".java$", "");
        if (TextUtils.isEmpty(generateClassName) || generateClassName.endsWith(".")) {
            Toast.make(project, generateClassP, MessageType.ERROR, "the path is not allowed");
            return;
        }
        PsiClass generateClass;
        if (currentClass.equals(generateClassName)) {
            // 在当前文件生成Bean
            generateClass = currentEditorClass;
        } else {
            // 非当前editor下的文件
            generateClass = PsiClassUtil.exist(currentEditFile, generateClassName);
        }
        new ConvertBridge(this, jsonStr, currentEditFile, project, generateClass, currentEditorClass, generateClassName).run();
    }

    private void onCancel() {
        dispose();
    }


    public PsiClass getPsiClass() {
        return currentEditorClass;
    }


    private void createUIComponents() {

    }

    public void openSettingDialog() {

        SettingDialog settingDialog = new SettingDialog(project);
        settingDialog.setSize(800, 720);
        settingDialog.setLocationRelativeTo(null);
        //settingDialog.setResizable(false);
        settingDialog.setVisible(true);
    }


    @Override
    public void cleanErrorInfo() {
        errorInfo = null;
    }

    @Override
    public void setErrorInfo(String error) {
        errorInfo = error;
    }

    @Override
    public void showError(ConvertBridge.Error err) {
        switch (err) {
            case DATA_ERROR:
                errorLB.setText("data err !!");
                if (Config.getInstant().isToastError()) {
                    Toast.make(project, errorLB, MessageType.ERROR, "click to see details");
                }
                break;
            case PARSE_ERROR:
                errorLB.setText("parse err !!");
                if (Config.getInstant().isToastError()) {
                    Toast.make(project, errorLB, MessageType.ERROR, "click to see details");
                }
                break;
            case PATH_ERROR:
                Toast.make(project, generateClassP, MessageType.ERROR, "the path is not allowed");
                break;
            default:
                break;
        }
    }

}
