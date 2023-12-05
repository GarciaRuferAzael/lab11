package it.unibo.oop.reactivegui03;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Third experiment with reactive gui.
 */
@SuppressWarnings("PMD.AvoidPrintStackTrace")
public final class AnotherConcurrentGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final double HEIGHT_PERC = 0.2;
    private static final double WIDTH_PERC = 0.1;

    private final CounterAgent agent;
    private final JLabel display = new JLabel();
    private final JButton up = new JButton("up");
    private final JButton down = new JButton("down");
    private final JButton stop = new JButton("stop");

    public AnotherConcurrentGUI(){
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        final JPanel panel = new JPanel();
        panel.add(display);
        panel.add(up);
        panel.add(down);
        panel.add(stop);
        this.getContentPane().add(panel);
        this.setVisible(true);

        this.agent = new CounterAgent();
    }


}

private class CounterAgent implements Runnable{

    private volatile boolean stop;
    private volatile boolean countUp = true;
    private int counter;

    @Override
    public void run() {
        while(!this.stop){
            try {
                final var nextText = Integer.toString(this.counter);
                SwingUtilities.invokeAndWait(() -> AnotherConcurrentGUI.this.display.setText(nextText));
                if (this.countUp) {
                    this.counter++;
                } else {
                    this.counter--;
                }
                Thread.sleep(100);
            } catch (InvocationTargetException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopCounting(){
        this.stop = true;
    }

    public void setCountUp(final boolean up){
        this.countUp = up;
        AnotherConcurrentGUI.this.up.setEnabled(!up);
        AnotherConcurrentGUI.this.up.setEnabled(up);
    }
}
