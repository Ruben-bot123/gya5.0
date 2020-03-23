package soundApp.codebehind;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

public class PlotCanvas extends JPanel implements Runnable, AudioStreamReceiver{
    Thread displayThread;
    float[] data = new float[8192];
    float[] spec = new float[8192];
    FastFourierTransform fft = new FastFourierTransform();

    public PlotCanvas(){

    }

    public void start(){
        if(displayThread == null){
            displayThread = new Thread(this);
            displayThread.start();
        }
    }

    public void run(){}

    public void stop(){
        displayThread = null;
    }

    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(Color.black);
        g.fillRect(0,0,800,400);
        g.setColor(Color.white);
        g.drawString("JSpect FFT: N=8192, FR=5.4Hz",20,20);



        // draw horizontal(frequency) scale
        g.drawLine(30,360,660,360);
        g.drawLine(30,360,30,355);
        g.drawString("16Hz",32,357);
        g.drawLine(90,360,90,355);
        g.drawString("32Hz",92,357);
        g.drawLine(150,360,150,355);
        g.drawString("64Hz",152,357);
        g.drawLine(210,360,210,355);
        g.drawString("128Hz",212,357);
        g.drawLine(270,360,270,355);
        g.drawString("256Hz",272,357);
        g.drawLine(330,360,330,355);
        g.drawString("512kHz",332,357);
        g.drawLine(390,360,390,355);
        g.drawString("1kHz",392,357);
        g.drawLine(450,360,450,355);
        g.drawString("2kHz",452,357);
        g.drawLine(510,360,510,355);
        g.drawString("4kHz",512,357);
        g.drawLine(570,360,570,355);
        g.drawString("8kHz",572,357);
        g.drawLine(630,360,630,355);
        g.drawString("16kHz",632,357);
        g.drawLine(660,360,660,355);

        g.setColor(Color.green);

        g.drawLine(30,343,660,343);

        // y scale multiplier/divisor, size adjuster
        int mult = 16;

        //Frequency = index * samplerate / size of fft
        //our example: frequency = index * 44100 / 8192
        //We have zero indexed arrays so for spec[i] the index = i+1
        //example
        // i = 2, index = 3 => 3 * 44100 / 8192 = 16.14990  aprox = 16Hz
        // i =
        // draw frequency information (slow!)
        g.fillRect(30,340-(int)spec[2]/mult,20,3); //16Hz
        g.fillRect(50,340-(int)spec[3]/mult,20,3);
        g.fillRect(70,340-(int)spec[4]/mult,20,3);
        g.fillRect(90,340-(int)spec[5]/mult,10,3); //32Hz
        g.fillRect(100,340-(int)spec[6]/mult,10,3);
        g.fillRect(110,340-(int)spec[7]/mult,10,3);
        g.fillRect(120,340-(int)spec[8]/mult,10,3);
        g.fillRect(130,340-(int)spec[9]/mult,10,3);
        g.fillRect(140,340-(int)spec[10]/mult,10,3);
        g.fillRect(150,340-(int)spec[11]/mult,10,3);
        g.fillRect(160,340-(int)spec[12]/mult,10,3); //64Hz
        g.fillRect(170,340-(int)spec[14]/mult,10,3);
        g.fillRect(180,340-(int)spec[16]/mult,10,3);
        g.fillRect(190,340-(int)spec[18]/mult,10,3);
        g.fillRect(200,340-(int)spec[20]/mult,10,3);
        g.fillRect(210,340-(int)spec[24]/mult,10,3);
        g.fillRect(220,340-(int)spec[26]/mult,10,3); //128Hz
        g.fillRect(230,340-(int)spec[30]/mult,10,3);
        g.fillRect(240,340-(int)spec[34]/mult,10,3);
        g.fillRect(250,340-(int)spec[38]/mult,10,3);
        g.fillRect(260,340-(int)spec[42]/mult,10,3);
        g.fillRect(270,340-(int)spec[48]/mult,10,3); //256Hz
        g.fillRect(280,340-(int)spec[56]/mult,10,3);
        g.fillRect(290,340-(int)spec[64]/mult,10,3);
        g.fillRect(300,340-(int)spec[72]/mult,10,3);
        g.fillRect(310,340-(int)spec[80]/mult,10,3);
        g.fillRect(320,340-(int)spec[88]/mult,10,3);
        g.fillRect(330,340-(int)spec[96]/mult,10,3); //512Hz
        g.fillRect(340,340-(int)spec[112]/mult,10,3);
        g.fillRect(350,340-(int)spec[128]/mult,10,3);
        g.fillRect(360,340-(int)spec[144]/mult,10,3);
        g.fillRect(370,340-(int)spec[160]/mult,10,3);
        g.fillRect(380,340-(int)spec[176]/mult,10,3);
        g.fillRect(390,340-(int)spec[192]/mult,10,3); //1kHz
        g.fillRect(400,340-(int)spec[224]/mult,10,3);
        g.fillRect(410,340-(int)spec[256]/mult,10,3);
        g.fillRect(420,340-(int)spec[288]/mult,10,3);
        g.fillRect(430,340-(int)spec[320]/mult,10,3);
        g.fillRect(440,340-(int)spec[352]/mult,10,3);
        g.fillRect(450,340-(int)spec[382]/mult,10,3); //2k
        g.fillRect(460,340-(int)spec[446]/mult,10,3);
        g.fillRect(470,340-(int)spec[510]/mult,10,3);
        g.fillRect(480,340-(int)spec[574]/mult,10,3);
        g.fillRect(490,340-(int)spec[638]/mult,10,3);
        g.fillRect(500,340-(int)spec[702]/mult,10,3);
        g.fillRect(510,340-(int)spec[766]/mult,10,3); //4k
        g.fillRect(520,340-(int)spec[894]/mult,10,3);
        g.fillRect(530,340-(int)spec[1022]/mult,10,3);
        g.fillRect(540,340-(int)spec[1150]/mult,10,3);
        g.fillRect(550,340-(int)spec[1278]/mult,10,3);
        g.fillRect(560,340-(int)spec[1406]/mult,10,3);
        g.fillRect(570,340-(int)spec[1534]/mult,10,3); //8k
        g.fillRect(580,340-(int)spec[1790]/mult,10,3);
        g.fillRect(590,340-(int)spec[2046]/mult,10,3);
        g.fillRect(600,340-(int)spec[2302]/mult,10,3);
        g.fillRect(610,340-(int)spec[2558]/mult,10,3);
        g.fillRect(620,340-(int)spec[2814]/mult,10,3);
        g.fillRect(630,340-(int)spec[3070]/mult,10,3); //16k
        g.fillRect(640,340-(int)spec[3582]/mult,10,3);
        g.fillRect(650,340-(int)spec[4094]/mult,10,3);

        //draw a level-meter, the average of all the amplitudes
        g.drawString(new Integer(levelSpec()).toString(),40,40);
    }

    /**
     * levelSpec() - returns an int computed by averaging the spec vector
     * 	i.e. the average amplitude across the frequency info
     * 	returned by the fft
     *
     */
    public int levelSpec(){
        int avg=0;
        for(int i=0;i<spec.length;i++){
            avg += (int)spec[i];
        }
        return avg/8192;
    }

    /**
     * sendData() - computes fft on the float array and repaints the
     * 	display
     *
     */
    public void sendData(float[] b, int len){
        data = b;
        //PCM Audio IN
        //Fast FOurier Transform (Universitetsmatematik)
        //SKip
        spec = fft.fftMag(b);
        //Spec r privat array, rita om
        repaint();
        /*
        ANDREAS LYTTER
        testaSkit() körs ifrån klassen Board istället
         */
    }

    /*
    ANDREAS LYTTER
    Detta är Ellas version av koden.
     */
    public int högstaton = 0;
    public int testaSkit() {
        float högstvolym = 0;

        for (int i = 23; i < 47; i++) {
            if (spec[i] > högstvolym)
                högstvolym = spec[i];
        }
        for (int i = 23; i < 47; i++) {
            if (högstvolym == spec[i])
                högstaton = i;
        }
        System.out.println(högstaton + " " + högstvolym);
        return högstaton;
    }
}
