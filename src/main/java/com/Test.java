package com;

import com.utils.functionalInterfaces.UniCallback;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.glGenTextures;

public class Test {

    public static void main(String[] args) {

        UniCallback<String> lamda = s -> System.out.println(s);

        lamda.invoke("lamda");

        UniCallback<String> mefref = System.out::println;

        mefref.invoke("mefref");


//        ArrayList<Integer> c = new ArrayList<>();
//        c.add(3);
//        c.add(6);
//        c.add(9);

//        int a = glGenTextures();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//        Matrix4f m1 = Matrix4f.Identity();
//        Matrix4f m2 = Matrix4f.Identity();
//
//        System.out.println(m1.equals(m2));
//
//        Matrix4f m3 = Matrix4f.Perspective(95, 4/3, 0.0001f, 4545);
//
//        System.out.println(m3.hashCode());
//        System.out.println(m3);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//        Entity e1 = new Entity();
//        e1.id = 5;
//
//        Entity e2 = new Entity();
//        e2.id = 5;
//
//
//        System.out.println(e1.equals(e2));
//
//        System.out.println(e1);
//        System.out.println(e2);
    }

}
