package com.karlotoy.perfectune.thread;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;

public class TuneThread extends Thread
{

    private volatile boolean isPlaying;
    private final int sampleRate = 44100;
    private volatile double tuneFreq = 440;
    private volatile int volume;
    private AudioTrack audioTrack;

    @Override
    public void run()
    {
        super.run();

        isPlaying = true;
        final int buffsize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        // create an audiotrack object
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, buffsize,
                                    AudioTrack.MODE_STREAM);

        short samples[] = new short[buffsize];
        int amp = 10000;
        double twopi = 8. * Math.atan(1.);
        double ph = 0.0;

        // start audio
        audioTrack.play();

        // synthesis loop
        while (isPlaying)
        {
            double fr = tuneFreq;
            for (int i = 0; i < buffsize; i++)
            {
                samples[i] = (short) (amp * Math.sin(ph));
                ph += twopi * fr / sampleRate;
            }

            final float gain = (float) volume / 100.0f;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                audioTrack.setVolume(gain);
            else
                audioTrack.setStereoVolume(gain, gain);

            audioTrack.write(samples, 0, buffsize);
        }

        // pause() appears to be more snappy in audio cutoff than stop()
        audioTrack.pause();
        audioTrack.flush();
        audioTrack.release();
        audioTrack = null;
    }

    public double getTuneFreq()
    {
        return tuneFreq;
    }

    public void setTuneFreq(double tuneFreq)
    {
        this.tuneFreq = tuneFreq;
    }

    public boolean isPlaying()
    {
        return isPlaying;
    }

    public int getVolume()
    {
        return volume;
    }

    public void setVolume(int volume)
    {
        this.volume = volume;
    }

    public void stopTune()
    {
        isPlaying = false;

        try
        {
            this.join();
            this.interrupt();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}

