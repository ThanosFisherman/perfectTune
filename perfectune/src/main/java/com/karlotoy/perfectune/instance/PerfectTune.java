package com.karlotoy.perfectune.instance;

import com.karlotoy.perfectune.thread.TuneThread;

public class PerfectTune
{

    private TuneThread tuneThread;
    private double tuneFreq = 0;
    private int volume;

    public void playTune()
    {
        if (tuneThread == null)
        {
            tuneThread = new TuneThread();
            tuneThread.setTuneFreq(tuneFreq);
            tuneThread.setVolume(volume);
            tuneThread.start();
        }
    }

    public void setTuneFreq(double tuneFreq)
    {
        this.tuneFreq = tuneFreq;
        if (tuneThread != null)
            tuneThread.setTuneFreq(tuneFreq);
    }

    public int getVolume()
    {
        if (tuneThread != null)
            return tuneThread.getVolume();
        return 0;
    }

    public void setVolume(int volume)
    {
        this.volume = volume;
        if (tuneThread != null)
            tuneThread.setVolume(volume);
    }

    public double getTuneFreq()
    {
        return tuneFreq;
    }

    public void stopTune()
    {
        if (tuneThread != null)
        {
            tuneThread.stopTune();
            tuneThread = null;
        }
    }

}
