package com.graphics.mesh;

import com.maths.vectors.Vector2f;
import com.maths.vectors.Vector3f;

public class Vertex {
    public static final int SIZE = 14;
    private Vector3f position;
    private Vector3f normal;
    private Vector2f textureCoord;
    private Vector3f tangent;
    private Vector3f bitangent;

    public Vertex(Vector3f position){
        this(position, null, null, null, null);
    }

    public Vertex(Vector3f position, Vector3f normal){
        this(position, normal, null, null, null);
    }

    public Vertex(Vector3f position, Vector2f textureCoord){
        this(position, null, textureCoord, null, null);
    }

    public Vertex(Vector3f position, Vector3f normal, Vector2f textureCoord){
        this(position, normal, textureCoord, null, null);
    }

    public Vertex(Vector3f position, Vector2f textureCoord, Vector3f tangent, Vector3f bitangent){
        this(position, null, textureCoord, tangent, bitangent);
    }

    public Vertex(Vector3f position, Vector3f normal, Vector2f textureCoord, Vector3f tangent, Vector3f bitangent){
        this.position = position;
        this.normal = normal;
        this.textureCoord = textureCoord;
        this.tangent = tangent;
        this.bitangent = bitangent;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getNormal() {
        return normal;
    }

    public Vector2f getTextureCoord() {
        return textureCoord;
    }

    public Vector3f getTangent() {
        return tangent;
    }

    public Vector3f getBitangent() {
        return bitangent;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setNormal(Vector3f normal) {
        this.normal = normal;
    }

    public void setTextureCoord(Vector2f textureCoord) {
        this.textureCoord = textureCoord;
    }

    public void setTangent(Vector3f tangent) {
        this.tangent = tangent;
    }

    public void setBitangent(Vector3f bitangent) {
        this.bitangent = bitangent;
    }
}
