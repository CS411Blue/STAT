/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mySTAT;

import java.awt.Color;
import javax.swing.JOptionPane;

/**
 *
 * @author Brian_2
 */
public class SecurityJDialog extends javax.swing.JDialog {
    
    private boolean encButtonState;
    private boolean defaultEncButtonState;
    private String password;
//    private int defaultPswdWidth;

    /**
     * Creates new form NewJDialog
     */
    public SecurityJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
    }
    
    public SecurityJDialog(java.awt.Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        encryptionButtonGroup = new javax.swing.ButtonGroup();
        encryptionPanel = new javax.swing.JPanel();
        encryptionLabel = new javax.swing.JLabel();
        onButton = new javax.swing.JRadioButton();
        offButton = new javax.swing.JRadioButton();
        passwordPanel = new javax.swing.JPanel();
        passLabel = new javax.swing.JLabel();
        passField = new javax.swing.JPasswordField();
        repassLabel = new javax.swing.JLabel();
        repassField = new javax.swing.JPasswordField();
        badPassLabel = new javax.swing.JLabel();
        confirmPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        encryptionPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        encryptionLabel.setText("Encryption");

        encryptionButtonGroup.add(onButton);
        onButton.setText("On");
        onButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onButtonActionPerformed(evt);
            }
        });

        encryptionButtonGroup.add(offButton);
        offButton.setText("Off");
        offButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                offButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout encryptionPanelLayout = new javax.swing.GroupLayout(encryptionPanel);
        encryptionPanel.setLayout(encryptionPanelLayout);
        encryptionPanelLayout.setHorizontalGroup(
            encryptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(encryptionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(encryptionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(onButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(offButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        encryptionPanelLayout.setVerticalGroup(
            encryptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(encryptionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(encryptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(encryptionLabel)
                    .addComponent(onButton)
                    .addComponent(offButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        passwordPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        passLabel.setText("Password");

        passField.setText("jPasswordField1");

        repassLabel.setText("Re-type Password");

        repassField.setText("jPasswordField2");

        badPassLabel.setText("Password's must match and contain at least one character");

        javax.swing.GroupLayout passwordPanelLayout = new javax.swing.GroupLayout(passwordPanel);
        passwordPanel.setLayout(passwordPanelLayout);
        passwordPanelLayout.setHorizontalGroup(
            passwordPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(passwordPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(passwordPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(passwordPanelLayout.createSequentialGroup()
                        .addGroup(passwordPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passLabel)
                            .addComponent(repassLabel))
                        .addGap(18, 18, 18)
                        .addGroup(passwordPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(passField)
                            .addComponent(repassField, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(badPassLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        passwordPanelLayout.setVerticalGroup(
            passwordPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(passwordPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(passwordPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passLabel)
                    .addComponent(passField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(passwordPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(repassLabel)
                    .addComponent(repassField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(badPassLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout confirmPanelLayout = new javax.swing.GroupLayout(confirmPanel);
        confirmPanel.setLayout(confirmPanelLayout);
        confirmPanelLayout.setHorizontalGroup(
            confirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(confirmPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(okButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton))
        );
        confirmPanelLayout.setVerticalGroup(
            confirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(confirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(okButton)
                .addComponent(cancelButton))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(encryptionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 144, Short.MAX_VALUE))
                    .addComponent(confirmPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(passwordPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(encryptionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(passwordPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(confirmPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void onButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onButtonActionPerformed
        encButtonState = true;
        encButtonChanged();
    }//GEN-LAST:event_onButtonActionPerformed

    private void offButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_offButtonActionPerformed
        encButtonState = false;
        encButtonChanged();
    }//GEN-LAST:event_offButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if(passwordAcceptable())
        {
            badPassLabel.setText("Password's must match and contain at least one character");
            badPassLabel.setForeground(Color.black);
            setVisible(false);
        }
        //OK button does nothing if password is not acceptable
        else{}
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
//        passField.setColumns(defaultPswdWidth);
//        repassField.setColumns(defaultPswdWidth);
        defaultInit(defaultEncButtonState);
        setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    
//sets up the default state of the window when it is opened
    public void defaultInit(boolean isEnc)
    {
        defaultEncButtonState = isEnc;
        encButtonState = isEnc;
        onButton.setSelected(isEnc);
        offButton.setSelected(!isEnc);
        encButtonChanged();
//        defaultPswdWidth = passField.getColumns();
    }
    
    public boolean getEncryptionChoice(){return encButtonState;}
    
    public String getPassword(){return password;}
    
    private void encButtonChanged()
    {
        passLabel.setEnabled(encButtonState);
        passField.setEnabled(encButtonState);
        repassLabel.setEnabled(encButtonState);
        repassField.setEnabled(encButtonState);
    }
    
    private boolean passwordAcceptable()
    {
        char[] pass1, pass2;
        try
        {
            pass1 = passField.getPassword();
            pass2 = repassField.getPassword();
        }
        catch (NullPointerException ex)
        {
            badPassLabel.setText("Password must have at least one character");
            badPassLabel.setForeground(Color.red);
//            JOptionPane.showMessageDialog(rootPane, 
//                    "Password must have at least one character", 
//                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(pass1.length != pass2.length)
        {
            badPassLabel.setText("Both passwords fields must match");
            badPassLabel.setForeground(Color.red);
            return false;
        }
        int length = pass1.length;
        if(length < 1)
        {
            badPassLabel.setText("Password must have at least one character");
            badPassLabel.setForeground(Color.red);
//            JOptionPane.showMessageDialog(null, 
//                    "Password must have at least one character", 
//                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        for(int i = 0; i < length; i++)
        {
            if(pass1[i] != pass2[i])
            {
                badPassLabel.setText("Both passwords fields must match");
                badPassLabel.setForeground(Color.red);
                return false;
            }
        }
        password = new String(pass1);
        for(int i = 0; i < length; i++)
        {
            pass1[i] = 0;
            pass2[i] = 0;
        }
        return true;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SecurityJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SecurityJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SecurityJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SecurityJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SecurityJDialog dialog = new SecurityJDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel badPassLabel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel confirmPanel;
    private javax.swing.ButtonGroup encryptionButtonGroup;
    private javax.swing.JLabel encryptionLabel;
    private javax.swing.JPanel encryptionPanel;
    private javax.swing.JRadioButton offButton;
    private javax.swing.JButton okButton;
    private javax.swing.JRadioButton onButton;
    private javax.swing.JPasswordField passField;
    private javax.swing.JLabel passLabel;
    private javax.swing.JPanel passwordPanel;
    private javax.swing.JPasswordField repassField;
    private javax.swing.JLabel repassLabel;
    // End of variables declaration//GEN-END:variables
}