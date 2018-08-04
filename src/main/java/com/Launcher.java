package com;

import com.engine.core.Engine;

public class Launcher {
    public static void main(String[] args) {
        try {
            Engine engine = new Engine();
            engine.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}
