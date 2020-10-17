package org.gsonformat.intellij.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import org.apache.http.util.TextUtils;
import org.gsonformat.intellij.ConvertBridge;
import org.gsonformat.intellij.common.PsiClassUtil;
import org.gsonformat.intellij.config.Config;
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
    private PsiClass cls;
    private PsiFile file;
    private Project project;
    private String errorInfo = null;
    private String currentClass = null;
    public static final String PLUGIN_NAME = "GsonFormat";


    public JsonDialog(PsiClass cls, PsiFile file, Project project) throws HeadlessException {
        this.cls = cls;
        this.file = file;
        this.project = project;
        setContentPane(contentPane2);
        setTitle(PLUGIN_NAME);
        getRootPane().setDefaultButton(okButton);
        this.setAlwaysOnTop(true);

        initGeneratePanel(file);
        initListener();
    }

    private void initListener() {

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String jsonStr = editTP.getText().trim();
                if (TextUtils.isEmpty(jsonStr)) {
                    editTP.requestFocusInWindow();
                } else {
                    onOK(jsonStr);
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
                    onOK();
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

    private void initGeneratePanel(PsiFile file) {
        this.generateClassEditable(false);

        generateClassTF.setBackground(errorLB.getBackground());

        currentClass = this.cls.getQualifiedName();
        // currentClass = ((PsiJavaFileImpl) file).getPackageName() + "." + file.getName().split("\\.")[0];
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
                String entityPackName = Config.getInstant().getEntityPackName();
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


    private void generateClassEditable(boolean editable) {
        generateClassLB.setEnabled(!editable);
        generateClassLB.setVisible(!editable);

        generateClassTF.setEnabled(editable);
        generateClassTF.setVisible(editable);
    }

    private void onOK() {
        String jsonStr = editTP.getText().trim();
        if (TextUtils.isEmpty(jsonStr)) {
            editTP.requestFocusInWindow();
        } else {
            onOK(jsonStr);
        }
    }

    private void onOK(String jsonStr) {
        this.setAlwaysOnTop(false);

        String generateClassName = generateClassTF.getText().replaceAll(" ", "").replaceAll(".java$", "");
        if (TextUtils.isEmpty(generateClassName) || generateClassName.endsWith(".")) {
            Toast.make(project, generateClassP, MessageType.ERROR, "the path is not allowed");
            return;
        }
        PsiClass generateClass;
        if (currentClass.equals(generateClassName)) {
            generateClass = cls;
        } else {
            generateClass = PsiClassUtil.exist(file, generateClassName);
        }

        new ConvertBridge(this, jsonStr, file, project, generateClass, cls, generateClassName).run();
    }

    private void onCancel() {
        dispose();
    }


    public PsiClass getPsiClass() {
        return cls;
    }

    public void setClass(PsiClass mClass) {
        this.cls = mClass;
    }

    public void setProject(Project mProject) {
        this.project = mProject;
    }

    public void setFile(PsiFile mFile) {
        this.file = mFile;
    }

    private void createUIComponents() {

    }

    public void openSettingDialog() {

        SettingDialog settingDialog = new SettingDialog(project);
        settingDialog.setSize(800, 720);
        settingDialog.setLocationRelativeTo(null);
//        settingDialog.setResizable(false);
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
