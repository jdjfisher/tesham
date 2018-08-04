/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.componentSystem.components;

import com.componentSystem.Component;
import com.maths.Matrix4f;
import com.maths.Quaternion;
import com.maths.vectors.Vector3f;

/**
 * @author Sri Harsha Chilakapati
 */
public class TransformComponent extends Component
{
    private final Vector3f scale;
    private final Vector3f position;
    private final Quaternion rotation;

    private final Matrix4f localTransform = new Matrix4f();
    private final Matrix4f worldTransform = new Matrix4f();

    private TransformComponent parent = null;

    private boolean hasChanged = false;
    private boolean changed    = true;

    public TransformComponent()
    {
        scale = new Vector3f(1,1,1);
        position = new Vector3f();
        rotation = new Quaternion();
    }

    protected void reComputeTransforms()
    {
        hasChanged = false;

        if (changed)
        {
            localTransform.identity();
            localTransform.rotate(this.rotation);
            localTransform.stretch(this.scale);
            localTransform.translate(this.position);

            hasChanged = true;
            changed = false;
        }

        if (parent != null)
        {
            worldTransform.set(localTransform);
            worldTransform.multiply(parent.getWorldTransform());
        }
    }

    public Vector3f getScale() {
        return scale;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    @Override
    protected void onUpdate(float elapsedTime)
    {
        changed = true; //remove
        reComputeTransforms();
    }

    public Matrix4f getLocalTransform()
    {
        return this.localTransform;
    }

    public TransformComponent getParent()
    {
        return parent;
    }

    public void setParent(TransformComponent parent)
    {
        this.parent = parent;
    }

    public Matrix4f getWorldTransform()
    {
        return parent == null ? localTransform : worldTransform;
    }

    public boolean hasChanged()
    {
        return hasChanged || (parent != null && parent.hasChanged());
    }
}
