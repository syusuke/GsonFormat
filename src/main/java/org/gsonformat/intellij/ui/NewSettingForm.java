package org.gsonformat.intellij.ui;

import org.gsonformat.intellij.types.ConvertLibrary;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author kerryzhang on 20/11/30
 */

public class NewSettingForm extends JFrame {
    public static final String TITLE = "Settings";
    private JPanel containerPanel;
    private JCheckBox virgoModeCheckBox;
    private JCheckBox generateCommentsCheckBox;
    private JCheckBox splitGenerateCheckBox;
    private JCheckBox reuseBeanCheckBox;
    private JTextField textField1;
    private JRadioButton privateFieldRadioButton;
    private JRadioButton protectedFieldRadioButton;
    private JRadioButton publicFieldRadioButton;
    private JCheckBox useSerializedNameCheckBox;
    private JCheckBox useWrapperClassOnCheckBox;
    private JCheckBox useSetAndGetCheckBox;
    private JCheckBox useLombokCheckBox;
    private JTextField textField2;
    private JComboBox cbConvertLib;
    private JTextField textField3;
    private JButton okButton;
    private JButton cancelButton;
    private JCheckBox checkBox1;
    private JButton button3;
    private JCheckBox checkBox2;
    private JButton button4;
    private JCheckBox checkBox3;
    private JButton button5;
    private JButton button7;
    private JCheckBox checkBox4;

    private ConvertLibrary convertLibrary;

    public NewSettingForm() {
        setExtendedState(JFrame.NORMAL);
        setContentPane(containerPanel);
        getRootPane().setDefaultButton(okButton);
        this.setAlwaysOnTop(true);
        this.setTitle(TITLE);
        setSize(560, 560);
        setLocationRelativeTo(null);
        setResizable(false);
        okButton.addActionListener(e -> onOk());
        cancelButton.addActionListener(e -> onCancel());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        containerPanel.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        preInitData();
        initListener();
    }

    private void initListener() {
        cbConvertLib.addActionListener(e -> {
            convertLibrary = ConvertLibrary.values()[cbConvertLib.getSelectedIndex()];
            System.out.println(convertLibrary);
        });
    }

    private void onCancel() {
        dispose();
    }

    private void onOk() {

    }

    private void preInitData() {
        for (ConvertLibrary convertLibrary : ConvertLibrary.values()) {
            //noinspection unchecked
            cbConvertLib.addItem(convertLibrary.getName());
        }

    }

}
