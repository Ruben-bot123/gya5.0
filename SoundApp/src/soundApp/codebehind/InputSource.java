package soundApp.codebehind;

import javax.sound.sampled.*;


public class InputSource implements Runnable{

    Thread inputThread;
    AudioStreamReceiver rt;
    TargetDataLine line;


    byte[] dataVector = new byte[16384];

    public InputSource(AudioStreamReceiver r){
        rt = r;

    }

    public void start(){

        if(inputThread == null){
            inputThread = new Thread(this);
            inputThread.start();
        }
    }
    public void stop(){
        if(line != null) {
            line.stop();
        }
        inputThread = null;
    }

    /***********
     * run() - when thread is "started", this function sets up
     *	available audio input 44.1kHz, 16-bit from the
     *	standard mic input. Then, in an endless loop, the thread
     *	reads 8192 samples from the audio line and then sends the
     *	vector of samples to the display object rt for analysis
     *	and visualization.
     *
     *****************/
    public void run(){

        AudioFormat.Encoding en = AudioFormat.Encoding.PCM_SIGNED;
        AudioFormat format = new AudioFormat(en,
                44100,16,1,2,44100,true);

        DataLine.Info info = new DataLine.Info(TargetDataLine.class,
                format); // format is an AudioFormat object

        if (!AudioSystem.isLineSupported(info)) {
            // Handle the error ...
            System.out.println("No Support");
        }



        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format,line.getBufferSize());
        } catch (LineUnavailableException ex) {
            // Handle the error ...
        }

        System.out.println(line.getFormat().toString());


        line.start();

        try{

            int av;
            int num;
            int sample;
            float[] sampleVector = new float[8192];

            while(true){
                if((av = line.available()) > 0){
                    num = line.read(dataVector,0,16384);
                    for(int i=0;i<8192;i++){
                        sampleVector[i] =
                                dataVector[i*2+1] & (dataVector[i*2]<< 8);
                    }
                    rt.sendData(sampleVector,0);
                }
            }

        }catch(Exception e){}
    }
}
