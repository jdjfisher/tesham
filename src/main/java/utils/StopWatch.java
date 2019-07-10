package utils;


public class StopWatch
{
    private float startTime;
    private float endTime;
    private boolean running;

    public StopWatch()
    {
        running = false;
    }

    public void start()
    {
        running = true;
        endTime = 0;
        startTime = getTime();
    }

    public void stop()
    {
        endTime = getTime();
        running = false;
    }

    public void restart()
    {
        stop();
        start();
    }

    public float getTime()
    {
        return System.nanoTime() / 1000_000_000.0f;
    }

    public float getElapsedTime()
    {
        return (running ? getTime() : endTime) - startTime;
    }

    public double getStartTime()
    {
        return startTime;
    }
}