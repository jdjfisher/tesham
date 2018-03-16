package com;

import com.componentSystem.Entity;
import com.maths.Matrix4f;
import com.maths.vectors.Vector3f;

public class Test {

    public static void main(String[] args) {

        Matrix4f m1 = Matrix4f.Identity();
        Matrix4f m2 = Matrix4f.Identity();

        System.out.println(m1.equals(m2));

        Matrix4f m3 = Matrix4f.Perspective(95, 4/3, 0.0001f, 4545);

        System.out.println(m3.hashCode());
        System.out.println(m3);

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
