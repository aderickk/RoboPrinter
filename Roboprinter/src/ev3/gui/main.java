/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ev3.gui;

import ev3.parser.ElsNode;
import ev3.parser.SVGParser;
import java.awt.Dimension;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JFileChooser;
import org.w3c.dom.Document; 

public class main extends javax.swing.JFrame {

    final JFileChooser fc = new JFileChooser(); 
    SVGCanvas canvas = new SVGCanvas();
    
    public main() {
        menuWrapper = new javax.swing.JPanel();
        path = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        canvasWrapper = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(650, 400));
        setPreferredSize(new java.awt.Dimension(615, 400));
        setSize(new java.awt.Dimension(600, 400));

        menuWrapper.setBackground(new java.awt.Color(231, 76, 60));
        menuWrapper.setMinimumSize(new java.awt.Dimension(600, 50));
        menuWrapper.setPreferredSize(new java.awt.Dimension(600, 50));

        jButton1.setBackground(new java.awt.Color(150, 45, 34));
        jButton1.setForeground(java.awt.Color.white);
        jButton1.setText("SELECT A FILE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        
        GroupLayout jPanel2Layout = new GroupLayout(canvas);
        canvas.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 602, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 391, Short.MAX_VALUE)
        );
        canvasWrapper.setViewportView(canvas);

        GroupLayout menuWrapperLayout = new GroupLayout(menuWrapper);
        menuWrapper.setLayout(menuWrapperLayout);
        menuWrapperLayout.setHorizontalGroup(
            menuWrapperLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(menuWrapperLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(path, GroupLayout.PREFERRED_SIZE, 430, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        menuWrapperLayout.setVerticalGroup(
            menuWrapperLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(menuWrapperLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(menuWrapperLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(path)
                    .addComponent(jButton1, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );

        canvasWrapper.setBorder(null);
        canvasWrapper.setMinimumSize(new java.awt.Dimension(0, 0));
        canvasWrapper.setPreferredSize(new java.awt.Dimension(600, 350));

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(menuWrapper, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(canvasWrapper, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(menuWrapper, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(canvasWrapper, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();  
    }  

    

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            //This is where a real application would open the file.
            path.setText(file.getPath()); 
           
            SVGParser parser = new SVGParser();
            Document doc = parser.loadFile(file.getPath());  
            
            parser.convert(doc);
            canvas.setPreferredSize(new Dimension(parser.getSVGDimension()));
            
            for (ElsNode node: parser.getStatements()) {
                if(node.getFunction().startsWith("draw")){
                    canvas.paintInCanvas(node.getFunction(),node.getArgs());
                }
            }
            canvas.repaint();
            canvasWrapper.updateUI();
        }  
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) { 
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main().setVisible(true); 
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane canvasWrapper;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel menuWrapper;
    private javax.swing.JTextField path;
    // End of variables declaration//GEN-END:variables
}
