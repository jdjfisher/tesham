package com;

import engine.core.Engine;

public class Launcher
{
    public static void main(String[] args)
    {
        try
        {
            Engine engine = new Engine();
            engine.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
