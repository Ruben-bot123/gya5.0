package soundApp.codebehind;

import com.sun.javafx.logging.JFRInputEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SoundMain {
    //Form Controls
    private JButton buttonStart;
    private JButton buttonStop;
    private JLabel labelMessage;
    private JPanel panelMain;
    private JPanel panelPlotFrame;
    //Our canvas where we draw content
    private PlotCanvas plotCanvas;
    //Audio Source
    private InputSource inputSource;
    public SoundMain() {
        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                labelMessage.setText("Start...");
                inputSource.start();
            }
        });
        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                labelMessage.setText("Stop...");
                inputSource.stop();
            }

        });

        //The plot Canvas is where we plot the result
        plotCanvas = new PlotCanvas();
        plotCanvas.setSize(800, 400);
        //the inputsource handles audio input,
        //Start and stop capture
        //the plotcanvas that implements the AudiostreamReceiver is added to the canvas...
        //The inputsource sends audio data to the plotcanvas through the audiostreaminterface
        inputSource = new InputSource(plotCanvas);
        panelPlotFrame.add(plotCanvas);
        //plotCanvas = new PlotCanvas();
        //plotCanvas.setSize(800, 400);
        //canvasPanel = plotCanvas;
        //panelMain.add(plotCanvas);

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("SoundMain");

        frame.setContentPane(new SoundMain().panelMain);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(800,600);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
