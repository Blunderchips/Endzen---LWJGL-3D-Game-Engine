package game.util;

import engine.Crashform;
import java.awt.Component;

/**
 * basic SplashScreen screen.
 *
 * @author matthew 'siD' van der bijl
 */
public class SplashScreen extends javax.swing.JFrame {

    private final int duration = 2000;

    /**
     * Creates new form splash.
     *
     * @param parent the parent of the <code>SplashScreen</code>
     */
    public SplashScreen(Component parent) {
        super();
        try {
            super.setUndecorated(true);
            this.initComponents();

            super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            super.setLocationRelativeTo(parent);
            super.setResizable(false);
            super.setEnabled(true);
            super.pack();
            super.toFront();

            Thread.sleep(duration);
        } catch (NullPointerException npe) {
//            System.err.println(npe);
        } catch (Exception ex) {
            new Crashform(ex).setVisible(true);
            super.dispose();
        }
        { // destory splash:
            super.setVisible(false);
            super.dispose();
        } // --
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * <b>WARNING: Do NOT modify this code.</b> The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/game/res/gfx/splash.jpeg"))); // NOI18N
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                new SplashScreen(null).setVisible(true);
            }
        }).start();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables

}
