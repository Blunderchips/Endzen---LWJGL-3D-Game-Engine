package engine;

import javax.swing.JFrame;
import java.awt.Component;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import static java.lang.System.err;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * Crashes are unexpected shutdowns of the program. When the program crashes, it
 * typically closes immediately, though it may show an error report marking the
 * location of the exception which caused the crash.
 *
 * @author Matthew 'siD' Van der Bijl
 *
 * @see javax.swing.JFrame
 * @see java.lang.Exception
 * @see java.lang.Throwable
 */
public class Crashform extends JFrame {

    private final JTextArea txtDisplay;
    private final JScrollPane scrollPane;

    /**
     * Creates new form <code>Crashform</code> with a specified
     * <code>Throwable</code> as the cause.
     *
     * @see java.awt.Component
     * @see java.lang.Throwable
     *
     * @param cause the cause of the error
     * @param parent the parent of the window
     */
    public Crashform(Throwable cause, Component parent) {
        super(cause.getClass().getName());

        System.out.println("");
        cause.printStackTrace(err);

        this.txtDisplay = new javax.swing.JTextArea();
        this.scrollPane = new javax.swing.JScrollPane();

        this.txtDisplay.setRows(5);
        this.txtDisplay.setColumns(20);
        this.txtDisplay.setEditable(false);
        this.txtDisplay.setWrapStyleWord(false);
        this.scrollPane.setViewportView(txtDisplay);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
                getContentPane()
        );
        super.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                        layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(scrollPane,
                                javax.swing.GroupLayout.DEFAULT_SIZE, 676,
                                Short.MAX_VALUE)
                        .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                        layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(scrollPane,
                                javax.swing.GroupLayout.DEFAULT_SIZE, 426,
                                Short.MAX_VALUE)
                        .addContainerGap()));
        super.pack();

        super.toFront();
        super.setLocationRelativeTo(parent);
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setEnabled(true);
        //super.setVisible(true);

        this.setDisplay(cause);
    }

    /**
     * Creates new form <code>Crashform</code> with a specified
     * <code>Throwable</code> as the cause.
     *
     * @see java.lang.Throwable
     *
     * @param cause the cause of the error
     */
    public Crashform(Throwable cause) {
        this(cause, null);
    }

    public JTextArea getDisplay() {
        return this.txtDisplay;
    }

    /**
     * @param cause the cause of the crash
     */
    private void setDisplay(Throwable cause) {
        String msg = " ---Crash Report--" + "\n";

        // Crash comment:
        msg += " " + Random.crashComment().toLowerCase() + "\n \n";

        // System details:
        msg += " LWJGL: " + org.lwjgl.Sys.getVersion() + "\n";
        msg += " OS: " + System.getProperty("os.name")
                + " (" + System.getProperty("os.arch") + ")"
                + " version " + System.getProperty("os.version") + "\n";
        msg += " Java: " + System.getProperty("java.version") + " "
                + System.getProperty("java.vendor")
                + " (" + System.getProperty("java.vendor.url") + ")" + "\n";
        msg += " Date: " + Time.getTime() + " " + Time.getDate() + "" + "\n" + "\n";

        // Exception details:
        msg += " " + cause.toString() + "\n";
        for (StackTraceElement element : cause.getStackTrace()) {
            msg += "\t " + element + "\n";
        }

        this.txtDisplay.setText(msg);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                new Crashform(new java.lang.Exception("Crashform - Test")).setVisible(true);
            }
        }).start();
    }
}
