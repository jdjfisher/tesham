package com.engine.items;

import com.graphics.opengl.Texture;
import com.graphics.opengl.mesh._3D.FaceMI;
import com.graphics.opengl.mesh._3D.Mesh3D;
import com.graphics.opengl.mesh._3D.MultiIndexMeshData;
import com.maths.TransformationSet;
import com.maths.vectors.Vector2f;
import com.maths.vectors.Vector3f;
import org.apache.commons.math3.util.FastMath;

import static com.engine.core.Options.getViewDistanceFar;

/**
 * Created by Jordan Fisher on 29/06/2017.
 */
public class Skybox {
    public static final Mesh3D mesh = createSkyboxMesh();
    private final TransformationSet transformationSet;
    private Texture texture;

    public Skybox(){
        transformationSet = new TransformationSet();
    }

    public void update(Vector3f cameraPosition){
        transformationSet.getPosition().set(cameraPosition);
        transformationSet.setScale(2 * (float)FastMath.sqrt(FastMath.pow(getViewDistanceFar(), 2) / 3) - 1);
    }

    public void setTexture(Texture texture){
        this.texture = texture;
    }

    public boolean hasTexture(){
        return texture != null;
    }

    public Texture getTexture() {
        return texture;
    }

    public Mesh3D getMesh(){
        return mesh;
    }

    public TransformationSet getTransformationSet() {
        return transformationSet;
    }

    private static Mesh3D createSkyboxMesh(){
        float var = 0.5f;

        Vector3f[] vertices = new Vector3f[]{
                new Vector3f(-var,  var,  var),//V0
                new Vector3f(-var,  var, -var),//V1
                new Vector3f( var,  var, -var),//V2
                new Vector3f( var,  var,  var),//V3
                new Vector3f(-var, -var,  var),//V4
                new Vector3f(-var, -var, -var),//V5
                new Vector3f( var, -var, -var),//V6
                new Vector3f( var, -var,  var) //V7
        };

        Vector2f[] textureCoords = new Vector2f[]{
                new Vector2f(1f / 4,1f),     //T0
                new Vector2f(2f / 4,1f),     //T1
                new Vector2f(0f    ,3f / 4), //T2
                new Vector2f(1f / 4,3f / 4), //T3
                new Vector2f(2f / 4,3f / 4), //T4
                new Vector2f(3f / 4,3f / 4), //T5
                new Vector2f(0f    ,2f / 4), //T6
                new Vector2f(1f / 4,2f / 4), //T7
                new Vector2f(2f / 4,2f / 4), //T8
                new Vector2f(3f / 4,2f / 4), //T9
                new Vector2f(1f / 4,1f / 4), //T10
                new Vector2f(2f / 4,1f / 4), //T11
                new Vector2f(1f / 4,0f),     //T12
                new Vector2f(2f / 4,0f)      //T13
        };

        FaceMI[] faces = new FaceMI[]{
                //front
                new FaceMI(
                        0, 3,
                        3, 4,
                        7, 8,
                        4, 7

                ),

                //left
                new FaceMI(
                        1, 2,
                        0, 3,
                        4, 7,
                        5, 6
                ),

                //right
                new FaceMI(
                        3, 4,
                        2, 5,
                        6, 9,
                        7, 8
                ),

                //back
                new FaceMI(
                        2, 13,
                        1, 12,
                        5, 10,
                        6, 11
                ),

                //top
                new FaceMI(
                        1, 0,
                        2, 1,
                        3, 4,
                        0, 3
                ),

                //bottom
                new FaceMI(
                        4, 7,
                        7, 8,
                        6, 11,
                        5, 10
                )
        };

        return new Mesh3D(new MultiIndexMeshData(vertices, null, textureCoords, faces));
    }
}
