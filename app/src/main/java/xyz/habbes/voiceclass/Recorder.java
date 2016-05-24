package xyz.habbes.voiceclass;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Habbes on 24/05/2016.
 */
public class Recorder {

    public final static int SAMPLE_RATE = 44100;
    public final static int CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    public final static int ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    public final static int SAMPLE_WIDTH = 2; // 2 bytes = 16bit
    public final static int BUFFER_SIZE =
            AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNELS, ENCODING);
    public final static String FILE_PATH =
            Environment.getExternalStorageDirectory().getPath() + "/VoiceClassRecording.pcm";
    private static AudioRecord recorder;
    private static Thread recordingThread;
    private static boolean isRecording = false;
    private static byte[] buffer = new byte[BUFFER_SIZE];


    public static void startRecording(){
        if(isRecording){
            return;
        }
        recorder = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                CHANNELS,
                ENCODING,
                BUFFER_SIZE
        );

        recorder.startRecording();
        isRecording = true;
        recordingThread = new Thread(new Runnable(){
            public void run(){
                writeAudioToFile();
            }
        }, "Audio Recorder Thread");

        recordingThread.start();
    }

    public static void stopRecording(){
        if(null != recorder && isRecording){
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;
            recordingThread = null;
        }
    }

    public static File getRecordedFile(){
        return new File(FILE_PATH);
    }

    public static byte[] readRecordedData() throws IOException {
        int read;
        int bufSize = 1024;
        byte[] buffer = new byte[bufSize];
        InputStream is = new BufferedInputStream(new FileInputStream(FILE_PATH));
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        while( (read = is.read(buffer, 0, bufSize)) != -1 ){
            os.write(buffer, 0, read);
        }

        return os.toByteArray();
    }

    public static boolean isRecording(){
        return isRecording;
    }

    private static void writeAudioToFile() {

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(FILE_PATH);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while(isRecording){
            int bytesRead = recorder.read(buffer, 0, BUFFER_SIZE);
            try {
                stream.write(buffer, 0, bytesRead);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
