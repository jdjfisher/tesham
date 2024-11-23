package utils;

import graphics.mesh.FaceMI;
import graphics.mesh.FaceMI.IndexSet;
import graphics.mesh.Mesh;
import graphics.mesh.MultiIndexMeshData;
import maths.vectors.Vector2f;
import maths.vectors.Vector3f;
import org.apache.commons.math3.util.FastMath;

import java.util.ArrayList;

/**
 * Created by Jordan Fisher on 20/05/2017.
 */
public class MeshFactory {
        private MeshFactory() {
        }

        public static Mesh plane(float width, float length) {
                float halfWidth = FastMath.abs(width / 2);
                float halfLength = FastMath.abs(length / 2);

                Vector3f[] vertexPositions = new Vector3f[] {
                                new Vector3f(halfWidth, 0, -halfLength),
                                new Vector3f(-halfWidth, 0, -halfLength),
                                new Vector3f(-halfWidth, 0, halfLength),
                                new Vector3f(halfWidth, 0, halfLength)
                };

                Vector2f[] textureCoordinates = new Vector2f[] {
                                new Vector2f(1, 0),
                                new Vector2f(0, 0),
                                new Vector2f(0, 1),
                                new Vector2f(1, 1)
                };

                FaceMI[] faces = new FaceMI[] {
                                new FaceMI(
                                                0, 0,
                                                1, 1,
                                                2, 2,
                                                3, 3)
                };

                return new Mesh(new MultiIndexMeshData(vertexPositions, null, textureCoordinates, faces));
        }

        public static Mesh tetrahedron(float length) {
                length = FastMath.abs(length);

                float v = length / (float) FastMath.sqrt(2);

                Vector3f[] vertexPositions = new Vector3f[] {
                                new Vector3f(0, length, v), // V0
                                new Vector3f(-length, 0, -v), // V1
                                new Vector3f(0, -length, v), // V2
                                new Vector3f(length, 0, -v), // V3
                };

                FaceMI[] faces = new FaceMI[] {
                                new FaceMI(// base
                                                1,
                                                3,
                                                2),
                                new FaceMI(// back
                                                0,
                                                3,
                                                1),
                                new FaceMI(// frontLeft
                                                0,
                                                1,
                                                2),
                                new FaceMI(// frontright
                                                0,
                                                2,
                                                3)
                };

                return new Mesh(new MultiIndexMeshData(vertexPositions, null, null, faces));
        }

        public static Mesh cube(float sideLength) {
                sideLength = FastMath.abs(sideLength);
                return cuboid(sideLength, sideLength, sideLength);
        }

        // public static Mesh thing(int n){
        //// Vector3f[] veretxPositions = new Vector3f[n * n * n];
        //
        // float l = 1.0f;
        // float step = l / n;
        // float hl = l / 2f;
        //
        //// for(int i = 0; i < n; i++) {
        //// for(int j = 0; j < n; j++) {
        //// for(int k = 0; k < n; k++) {
        //// veretxPositions[i * n * n + j * n + k] = new Vector3f(i * step, j * step, k
        // * step);
        //// }
        //// }
        //// }
        //
        // ArrayList<Vector3f> vertexPositions = new ArrayList<>();
        //
        // //front
        //
        // for (float x = -hl; x <= hl; x += step){
        // for (float y = -hl; y <= hl; y += step) {
        // vertexPositions.add(new Vector3f(x, y, hl));
        // }
        // }
        //
        // for (int i = 0; i < n; i++) {
        //
        // }
        //
        // FaceMI[] faces = new FaceMI[0];
        //
        // return new Mesh(new MultiIndexMeshData(vertexPositions, null, null, faces));
        // }

        public static Mesh cuboid(float width, float height, float length) {
                float halfWidth = FastMath.abs(width / 2);
                float halfHeight = FastMath.abs(height / 2);
                float halfLength = FastMath.abs(length / 2);

                Vector3f[] vertexPositions = new Vector3f[] {
                                new Vector3f(-halfWidth, halfHeight, halfLength), // V0
                                new Vector3f(-halfWidth, halfHeight, -halfLength), // V1
                                new Vector3f(halfWidth, halfHeight, -halfLength), // V2
                                new Vector3f(halfWidth, halfHeight, halfLength), // V3
                                new Vector3f(-halfWidth, -halfHeight, halfLength), // V4
                                new Vector3f(-halfWidth, -halfHeight, -halfLength), // V5
                                new Vector3f(halfWidth, -halfHeight, -halfLength), // V6
                                new Vector3f(halfWidth, -halfHeight, halfLength) // V7
                };

                Vector2f[] textureCoordinates = new Vector2f[] {
                                new Vector2f(1f / 4, 1f), // T0
                                new Vector2f(2f / 4, 1f), // T1
                                new Vector2f(0f, 3f / 4), // T2
                                new Vector2f(1f / 4, 3f / 4), // T3
                                new Vector2f(2f / 4, 3f / 4), // T4
                                new Vector2f(3f / 4, 3f / 4), // T5
                                new Vector2f(0f, 2f / 4), // T6
                                new Vector2f(1f / 4, 2f / 4), // T7
                                new Vector2f(2f / 4, 2f / 4), // T8
                                new Vector2f(3f / 4, 2f / 4), // T9
                                new Vector2f(1f / 4, 1f / 4), // T10
                                new Vector2f(2f / 4, 1f / 4), // T11
                                new Vector2f(1f / 4, 0f), // T12
                                new Vector2f(2f / 4, 0f) // T13
                };

                FaceMI[] faces = new FaceMI[] {
                                new FaceMI(// front
                                                3, 4,
                                                0, 3,
                                                4, 7,
                                                7, 8),
                                new FaceMI(// left
                                                0, 3,
                                                1, 2,
                                                5, 6,
                                                4, 7),
                                new FaceMI(// right
                                                2, 5,
                                                3, 4,
                                                7, 8,
                                                6, 9),
                                new FaceMI(// back
                                                1, 12,
                                                2, 13,
                                                6, 11,
                                                5, 10),
                                new FaceMI(// top
                                                2, 1,
                                                1, 0,
                                                0, 3,
                                                3, 4),
                                new FaceMI(// bottom
                                                7, 8,
                                                4, 7,
                                                5, 10,
                                                6, 11),
                };

                return new Mesh(new MultiIndexMeshData(vertexPositions, null, textureCoordinates, faces));
        }

        public static Mesh corner(float width, float height, float length) {
                float halfWidth = FastMath.abs(width / 2);
                float halfLength = FastMath.abs(length / 2);

                Vector3f[] vertexPositions = new Vector3f[] {
                                new Vector3f(-halfWidth, height, -halfLength), // V0
                                new Vector3f(halfWidth, height, -halfLength), // V1
                                new Vector3f(-halfWidth, height, halfLength), // V2
                                new Vector3f(-halfWidth, 0, halfLength), // V3
                                new Vector3f(-halfWidth, 0, -halfLength), // V4
                                new Vector3f(halfWidth, 0, -halfLength), // V5
                                new Vector3f(halfWidth, 0, halfLength) // V6
                };

                FaceMI[] faces = new FaceMI[] {
                                new FaceMI(// left
                                                0,
                                                2,
                                                3,
                                                4),
                                new FaceMI(// back
                                                4,
                                                5,
                                                1,
                                                0),
                                new FaceMI(// bottom
                                                5,
                                                4,
                                                3,
                                                6)
                };

                return new Mesh(new MultiIndexMeshData(vertexPositions, null, null, faces));
        }

        public static Mesh cylinder(float radius, int resolution, float length) {
                resolution = resolution < 3 ? 3 : resolution;
                radius = FastMath.abs(radius);
                float halfLength = FastMath.abs(length / 2);

                Vector3f[] vertexPositions = new Vector3f[resolution * 2];

                double deltaTheta = 2 * FastMath.PI / (resolution);
                double theta = 0;

                for (int i = 0; i < resolution; i++) {
                        float rSinTheta = radius * (float) FastMath.sin(theta);
                        float rCosTheta = radius * (float) FastMath.cos(theta);
                        vertexPositions[i] = new Vector3f(rCosTheta, halfLength, rSinTheta);
                        vertexPositions[resolution + i] = new Vector3f(rCosTheta, -halfLength, rSinTheta);
                        theta -= deltaTheta; // beacause faces defined anticlockwise
                }

                ArrayList<FaceMI> faces = new ArrayList<>();

                IndexSet[] topFaceIndexSets = new IndexSet[resolution];
                IndexSet[] bottomFaceIndexSets = new IndexSet[resolution];

                for (int i = 0; i < resolution; i++) {
                        topFaceIndexSets[i] = new IndexSet(i);
                        bottomFaceIndexSets[i] = new IndexSet((2 * resolution) - i - 1);
                }

                // top face
                faces.add(new FaceMI(topFaceIndexSets));

                // bottom face
                faces.add(new FaceMI(bottomFaceIndexSets));

                for (int i = 0; i < resolution - 1; i++) {
                        faces.add(new FaceMI(
                                        i + 1,
                                        i + 0,
                                        i + resolution,
                                        i + 1 + resolution));
                }

                faces.add(new FaceMI(
                                0,
                                resolution - 1,
                                (resolution * 2) - 1,
                                resolution + 0));

                return new Mesh(new MultiIndexMeshData(vertexPositions, null, null,
                                faces.toArray(new FaceMI[faces.size()])));
        }

        public static Mesh tube(float outerRadius, float innerRadius, int resolution, float length) {
                resolution = resolution < 3 ? 3 : resolution;
                innerRadius = FastMath.abs(innerRadius);
                outerRadius = FastMath.abs(outerRadius);
                float halfLength = FastMath.abs(length / 2);

                if (innerRadius > outerRadius) {
                        float temp = innerRadius;
                        innerRadius = outerRadius;
                        outerRadius = temp;
                } else if (innerRadius == outerRadius) {
                        innerRadius = outerRadius - 0.01f;
                }

                Vector3f[] vertexPositions = new Vector3f[resolution * 4];

                double deltaTheta = 2 * FastMath.PI / (resolution);
                double theta = 0;

                final int outerBotVerticesOffset = resolution;
                final int innerTopVerticesOffset = resolution * 2;
                final int innerBotVerticesOffset = resolution * 3;

                for (int i = 0; i < resolution; i++) {
                        float SinTheta = (float) FastMath.sin(theta);
                        float CosTheta = (float) FastMath.cos(theta);
                        vertexPositions[i] = new Vector3f(outerRadius * CosTheta, halfLength, outerRadius * SinTheta);
                        vertexPositions[resolution + i] = new Vector3f(outerRadius * CosTheta, -halfLength,
                                        outerRadius * SinTheta);
                        vertexPositions[resolution * 2 + i] = new Vector3f(innerRadius * CosTheta, halfLength,
                                        innerRadius * SinTheta);
                        vertexPositions[resolution * 3 + i] = new Vector3f(innerRadius * CosTheta, -halfLength,
                                        innerRadius * SinTheta);
                        theta -= deltaTheta; // beacause faces defined anticlockwise
                }

                ArrayList<FaceMI> faces = new ArrayList<>();

                for (int i = 0; i < resolution - 1; i++) {
                        // top face fragments
                        faces.add(new FaceMI(
                                        innerTopVerticesOffset + i + 1,
                                        innerTopVerticesOffset + i,
                                        i + 0,
                                        i + 1));

                        // bottom face fragments
                        faces.add(new FaceMI(
                                        innerBotVerticesOffset + i,
                                        innerBotVerticesOffset + i + 1,
                                        outerBotVerticesOffset + i + 1,
                                        outerBotVerticesOffset + i));

                        // outer side face fragment
                        faces.add(new FaceMI(
                                        i + 1,
                                        i + 0,
                                        outerBotVerticesOffset + i,
                                        outerBotVerticesOffset + i + 1));

                        // inner side face fragment
                        faces.add(new FaceMI(
                                        innerTopVerticesOffset + i,
                                        innerTopVerticesOffset + i + 1,
                                        innerBotVerticesOffset + i + 1,
                                        innerBotVerticesOffset + i));
                }

                // top face filler fragments
                faces.add(new FaceMI(
                                innerTopVerticesOffset + 0,
                                innerTopVerticesOffset + resolution - 1,
                                resolution - 1,
                                0));

                // bottom face filler fragments
                faces.add(new FaceMI(
                                innerBotVerticesOffset + resolution - 1,
                                innerBotVerticesOffset + 0,
                                outerBotVerticesOffset + 0,
                                outerBotVerticesOffset + resolution - 1));

                // outer side face filler fragment
                faces.add(new FaceMI(
                                0,
                                resolution - 1,
                                outerBotVerticesOffset + resolution - 1,
                                outerBotVerticesOffset + 0));

                // inner side face filler fragment
                faces.add(new FaceMI(
                                innerTopVerticesOffset + resolution - 1,
                                innerTopVerticesOffset + 0,
                                innerBotVerticesOffset + 0,
                                innerBotVerticesOffset + resolution - 1));

                return new Mesh(new MultiIndexMeshData(vertexPositions, null, null,
                                faces.toArray(new FaceMI[faces.size()])));
        }

        public static Mesh cone(float radius, int resolution, float height) {
                resolution = resolution < 3 ? 3 : resolution;
                radius = FastMath.abs(radius);
                float halfHeight = FastMath.abs(height / 2);

                Vector3f[] vertexPositions = new Vector3f[resolution + 1];

                final int peakIndex = resolution;
                vertexPositions[peakIndex] = new Vector3f(0, halfHeight, 0);

                double deltaTheta = 2 * FastMath.PI / (resolution);
                double theta = 0;

                for (int i = 0; i < resolution; i++) {
                        float rSinTheta = radius * (float) FastMath.sin(theta);
                        float rCosTheta = radius * (float) FastMath.cos(theta);
                        vertexPositions[i] = new Vector3f(rCosTheta, -halfHeight, rSinTheta);
                        theta -= deltaTheta;
                }

                ArrayList<FaceMI> faces = new ArrayList<>();

                IndexSet[] baseFaceIndexSets = new IndexSet[resolution];

                for (int i = 0; i < resolution; i++) {
                        baseFaceIndexSets[i] = new IndexSet(resolution - i - 1);
                }

                // base face
                faces.add(new FaceMI(baseFaceIndexSets));

                for (int i = 0; i < resolution - 1; i++) {
                        // side faces
                        faces.add(new FaceMI(
                                        peakIndex + 0,
                                        i + 0,
                                        i + 1));
                }

                // filler face
                faces.add(new FaceMI(
                                peakIndex + 0,
                                resolution - 1,
                                0));

                return new Mesh(new MultiIndexMeshData(vertexPositions, null, null,
                                faces.toArray(new FaceMI[faces.size()])));
        }

        public static Mesh torus(float majorRadius, float minorRadius, int resolution) {
                Vector3f[] vertexPositions = new Vector3f[resolution * resolution];

                final int ringCount = resolution;
                final int meridianCount = resolution;

                final double deltaAlpha = 2 * FastMath.PI / resolution;
                final double deltaTheta = 2 * FastMath.PI / resolution; // xz plane
                double alpha = 0;
                double theta;

                for (int ring = 0; ring < ringCount; ring++) {
                        alpha -= deltaAlpha;
                        theta = 0;
                        for (int meridian = 0; meridian < meridianCount; meridian++) {
                                float sinTheta = (float) FastMath.sin(theta);
                                float cosTheta = (float) FastMath.cos(theta);
                                float sinPhi = (float) FastMath.sin(alpha);
                                float cosPhi = (float) FastMath.cos(alpha);

                                vertexPositions[ring * meridianCount + meridian] = new Vector3f(
                                                (majorRadius + (minorRadius * cosPhi)) * cosTheta,
                                                minorRadius * sinPhi,
                                                (majorRadius + (minorRadius * cosPhi)) * sinTheta);
                                theta -= deltaTheta;
                        }
                }

                ArrayList<FaceMI> faces = new ArrayList<>();

                for (int ring = 0; ring < ringCount - 1; ring++) {

                        final int currentRingIndexOffset = ring * meridianCount;
                        final int nextRingIndexOffset = (ring + 1) * meridianCount;

                        for (int meridian = 0; meridian < meridianCount - 1; meridian++) {

                                // Main faces
                                faces.add(new FaceMI(
                                                currentRingIndexOffset + meridian + 1,
                                                currentRingIndexOffset + meridian,
                                                nextRingIndexOffset + meridian,
                                                nextRingIndexOffset + meridian + 1));
                        }
                }

                final int bottomRingIndexOffset = (ringCount - 1) * meridianCount;

                for (int i = 0; i < resolution - 1; i++) {

                        // Ring filler faces
                        faces.add(new FaceMI(
                                        bottomRingIndexOffset + i + 1,
                                        bottomRingIndexOffset + i,
                                        0 + i,
                                        0 + i + 1));

                        final int currentRingIndexOffset = i * meridianCount;
                        final int nextRingIndexOffset = (i + 1) * meridianCount;

                        // Meridan filler faces
                        faces.add(new FaceMI(
                                        currentRingIndexOffset + 0,
                                        currentRingIndexOffset + meridianCount - 1,
                                        nextRingIndexOffset + meridianCount - 1,
                                        nextRingIndexOffset + 0));
                }

                // Final filler face
                faces.add(new FaceMI(
                                bottomRingIndexOffset + 0,
                                bottomRingIndexOffset + meridianCount - 1,
                                0 + meridianCount - 1,
                                0 + 0));

                return new Mesh(new MultiIndexMeshData(vertexPositions, null, null,
                                faces.toArray(new FaceMI[faces.size()])));
        }

        public static Mesh sphere(float radius, int resolution) {
                resolution = resolution < 3 ? 3 : resolution;
                radius = FastMath.abs(radius);

                final int ringCount = resolution - 1;
                final int meridianCount = resolution;

                Vector3f[] vertexPositions = new Vector3f[ringCount * meridianCount + 2];

                final int northPoleIndex = vertexPositions.length - 2;
                final int southPoleIndex = vertexPositions.length - 1;

                vertexPositions[northPoleIndex] = new Vector3f(0, radius, 0);
                vertexPositions[southPoleIndex] = new Vector3f(0, -radius, 0);

                double deltaAlpha = FastMath.PI / resolution;
                double deltaTheta = 2 * FastMath.PI / resolution;
                double alpha = 0;
                double theta;

                for (int ring = 0; ring < ringCount; ring++) {
                        alpha -= deltaAlpha;
                        theta = 0;
                        for (int meridian = 0; meridian < meridianCount; meridian++) {
                                float sinTheta = (float) FastMath.sin(theta);
                                float cosTheta = (float) FastMath.cos(theta);
                                float sinPhi = (float) FastMath.sin(alpha);
                                float cosPhi = (float) FastMath.cos(alpha);

                                vertexPositions[(ring * meridianCount) + meridian] = new Vector3f(
                                                radius * sinPhi * cosTheta,
                                                radius * cosPhi,
                                                radius * sinPhi * sinTheta);
                                theta -= deltaTheta;
                        }
                }

                ArrayList<FaceMI> faces = new ArrayList<>();

                for (int ring = 0; ring < ringCount - 1; ring++) {

                        final int currentRingIndexOffset = ring * meridianCount;
                        final int nextRingIndexOffset = (ring + 1) * meridianCount;

                        for (int meridian = 0; meridian < meridianCount - 1; meridian++) {

                                // faces
                                faces.add(new FaceMI(
                                                currentRingIndexOffset + meridian + 1,
                                                currentRingIndexOffset + meridian,
                                                nextRingIndexOffset + meridian,
                                                nextRingIndexOffset + meridian + 1));
                        }

                        // filler faces
                        faces.add(new FaceMI(
                                        currentRingIndexOffset + 0,
                                        currentRingIndexOffset + meridianCount - 1,
                                        nextRingIndexOffset + meridianCount - 1,
                                        nextRingIndexOffset + 0));
                }

                final int bottomRingIndexOffset = (ringCount - 1) * meridianCount;

                for (int meridian = 0; meridian < resolution - 1; meridian++) {
                        // top faces
                        faces.add(new FaceMI(
                                        northPoleIndex,
                                        meridian,
                                        meridian + 1));

                        // bottom faces
                        faces.add(new FaceMI(
                                        southPoleIndex,
                                        bottomRingIndexOffset + meridian + 1,
                                        bottomRingIndexOffset + meridian));
                }

                // top faces filler
                faces.add(new FaceMI(
                                northPoleIndex,
                                meridianCount - 1,
                                0));

                // bottom faces filler
                faces.add(new FaceMI(
                                southPoleIndex,
                                bottomRingIndexOffset,
                                bottomRingIndexOffset + meridianCount - 1));

                return new Mesh(new MultiIndexMeshData(vertexPositions, null, null,
                                faces.toArray(new FaceMI[faces.size()])));
        }

        public static Mesh semiSphere(float radius, int resolution) {
                resolution = resolution < 3 ? 3 : resolution;

                radius = FastMath.abs(radius);

                final int ringCount = resolution / 2;
                final int sectorCount = resolution;

                Vector3f[] vertexPositions = new Vector3f[sectorCount * ringCount + 1];

                final int peakIndex = vertexPositions.length - 1;

                vertexPositions[peakIndex] = new Vector3f();

                double deltaRingTheta = FastMath.PI / (2 * ringCount);
                double deltaSectorTheta = 2 * FastMath.PI / sectorCount;
                double ringTheta = 0;
                double sectorTheta;

                for (int ring = 0; ring < ringCount; ring++) {
                        sectorTheta = 0;
                        for (int sector = 0; sector < sectorCount; sector++) {
                                float sinSTheta = (float) FastMath.sin(sectorTheta);
                                float cosSTheta = (float) FastMath.cos(sectorTheta);
                                float sinRTheta = (float) FastMath.sin(ringTheta);
                                float cosRTheta = (float) FastMath.cos(ringTheta);

                                vertexPositions[(ring * sectorCount) + sector] = new Vector3f(
                                                radius * sinSTheta * cosRTheta,
                                                radius * cosRTheta * cosSTheta,
                                                radius * (1 - sinRTheta));
                                sectorTheta += deltaSectorTheta;
                        }
                        ringTheta += deltaRingTheta;
                }

                ArrayList<FaceMI> faces = new ArrayList<>();

                for (int ring = 0; ring < ringCount - 1; ring++) {

                        final int currentRingIndexOffset = ring * sectorCount;
                        final int nextRingIndexOffset = (ring + 1) * sectorCount;

                        for (int sector = 0; sector < sectorCount - 1; sector++) {

                                // faces
                                faces.add(new FaceMI(
                                                currentRingIndexOffset + sector,
                                                currentRingIndexOffset + sector + 1,
                                                nextRingIndexOffset + sector + 1,
                                                nextRingIndexOffset + sector));
                        }

                        // filler faces
                        faces.add(new FaceMI(
                                        currentRingIndexOffset + sectorCount - 1,
                                        currentRingIndexOffset + 0,
                                        nextRingIndexOffset + 0,
                                        nextRingIndexOffset + sectorCount - 1));
                }

                final int backRingIndexOffset = sectorCount - 1;

                IndexSet[] backFaceIndexSets = new IndexSet[sectorCount];

                for (int i = 0; i < sectorCount; i++) {
                        backFaceIndexSets[i] = new IndexSet(backRingIndexOffset - i);
                }

                // back face
                faces.add(new FaceMI(backFaceIndexSets));

                final int frontRingIndexOffset = (ringCount - 1) * sectorCount;

                for (int sector = frontRingIndexOffset; sector < vertexPositions.length - 1; sector++) {
                        // front faces
                        faces.add(new FaceMI(
                                        peakIndex + 0,
                                        sector + 0,
                                        sector + 1));
                }

                // front faces filler
                faces.add(new FaceMI(
                                peakIndex,
                                vertexPositions.length - 2,
                                frontRingIndexOffset));

                return new Mesh(new MultiIndexMeshData(vertexPositions, null, null,
                                faces.toArray(new FaceMI[faces.size()])));
        }
}
