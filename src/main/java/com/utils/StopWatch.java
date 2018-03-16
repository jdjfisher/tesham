package com.utils;


public class StopWatch {

    private double loopStartTime;
    private double loopEndTime;
    private double loopElapsedTime;
    private boolean running;

    public StopWatch(){
        running = false;
    }

    public void start() {
        running = true;
        loopEndTime = 0;
        loopStartTime = getTime();
    }

    public void stop() {
        loopEndTime = getTime();
        running = false;
    }

    public void restart(){
        stop();
        start();
    }

    public double getTime() {
        return System.nanoTime() / 1000_000_000.0;
    }

    public float getElapsedTime() {
        if(running){
            loopElapsedTime = getTime() - loopStartTime;
        }else {
            loopElapsedTime = loopEndTime - loopStartTime;
        }

        return (float)loopElapsedTime;
    }

    public double getLoopStartTime() {
        return loopStartTime;
    }
}