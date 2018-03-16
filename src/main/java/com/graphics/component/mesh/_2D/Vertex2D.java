package com.graphics.component.mesh._2D;

import com.maths.vectors.Vector2f;

public class Vertex2D {
    public static final int SIZE = 4;

    private Vector2f position;
    private Vector2f textureCoord;

    public Vertex2D(Vector2f position){
        this(position, null);
    }

    public Vertex2D(Vector2f position, Vector2f textureCoord){
        this.position = position;
        this.textureCoord = textureCoord;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getTextureCoord() {
        return textureCoord;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void setTextureCoord(Vector2f textureCoord) {
        this.textureCoord = textureCoord;
    }

}
