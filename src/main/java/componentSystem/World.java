package componentSystem;

import componentSystem.components.*;
import engine.input.Keyboard;
import engine.items.*;
import graphics.mesh.Mesh;
import maths.RNG;
import maths.vectors.Vector3f;
import utils.GenerateMesh;
import utils.functionalInterfaces.UniCallback;
import org.apache.commons.math3.util.FastMath;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_G;

public class World
{

    public static final Vector3f X_AXIS = new Vector3f(1, 0, 0);
    public static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
    public static final Vector3f Z_AXIS = new Vector3f(0, 0, 1);
    public static final Vector3f FORWARD_VECTOR = Z_AXIS;

    private final ArrayList<Entity> entities;
    private final Camera camera;
    private final Skybox skybox;
    private final DirectionalLight directionalLight;

    // TEMP

    private Entity pl1;
    private Entity spl1;
    private Mesh sphereMesh;
    private Mesh planeMesh;
    private Mesh semiSphereMesh;

    //

    public World()
    {
        entities = new ArrayList<>();

        camera = new Camera(new Vector3f(-1, 1, 6));
        camera.facePoint(0, 0, 0);

        skybox = new Skybox();

        directionalLight = new DirectionalLight(new Light(0.3f, Color.WHITE));
        directionalLight.getRotation().set(World.FORWARD_VECTOR, new Vector3f(0, 1, 0));
    }

    public void init() throws Exception
    {
        sphereMesh = GenerateMesh.sphere(1, 50);
        planeMesh = GenerateMesh.plane(50, 50);
        semiSphereMesh = GenerateMesh.semiSphere(0.4f, 20);

        int k = 4;

        pl1 = new Entity();
        pl1.transformComponent.getPosition().set(0, 0, 0);
        pl1.transformComponent.getScale().multiply(0.4f);
        pl1.addComponent(new PointLightComponent(new Light(10, Color.WHITE), new Attenuation(1.0f, 0.22f, 0.20f)));
        pl1.addComponent(new MeshComponent(sphereMesh));
        entities.add(pl1);

        spl1 = new Entity();
        spl1.addComponent(new SpotLightComponent(new Light(5, Color.WHITE), new Attenuation(1.0f, 0.027f, 0.0028f), 20f, 25f));
        spl1.addComponent(new MeshComponent(semiSphereMesh));
        entities.add(spl1);

        Entity ball1 = new Entity();
        ball1.transformComponent.getPosition().set(k, 0, 0);
        ball1.addComponent(new MaterialComponent(Color.RED, 16));
        ball1.addComponent(new MeshComponent(sphereMesh));
        entities.add(ball1);

        Entity ball2 = new Entity();
        ball2.transformComponent.getPosition().set(0, k, 0);
        ball2.addComponent(new MaterialComponent(Color.GREEN, 0));
        ball2.addComponent(new MeshComponent(sphereMesh));
        entities.add(ball2);

        Entity ball3 = new Entity();
        ball3.transformComponent.getPosition().set(0, 0, k);
        ball3.addComponent(new MaterialComponent(Color.BLUE, 128));
        ball3.addComponent(new MeshComponent(sphereMesh));
        entities.add(ball3);

        Entity plane = new Entity();
        plane.transformComponent.getPosition().set(0, -10, 0);
        plane.addComponent(new MaterialComponent(new Color(197, 197, 197), 128));
//        plane.addComponent(new MaterialComponent(new Color(197, 197, 197), 128, TextureLoader.load("/textures/Bricks/diffuse.jpg"), TextureLoader.load("/textures/Bricks/normalMap.jpg"), TextureLoader.load("/textures/Bricks/specularMap.jpg")));
        plane.addComponent(new MeshComponent(planeMesh));
        entities.add(plane);

        forEachEntity(e -> e.forEachComponent(c -> c.onInit()));
    }

    public void handleInput()
    {
        camera.handleInput();

        if (Keyboard.isKeyDown(GLFW_KEY_F))
        {
            spl1.transformComponent.getPosition().set(camera.getPosition());
            spl1.transformComponent.getRotation().set(camera.getRotation());
        }

        if (Keyboard.isKeyTapped(GLFW_KEY_G))
        {
            pl1.getComponent(PointLightComponent.class).getLight().setColour(RNG.Colour());
        }
    }


    float a;

    public void updateLogic(float interval)
    {
        camera.update(interval);

        forEachEntity(e -> e.forEachComponent(c -> c.onUpdate(interval)));

        a += interval;
        pl1.getComponent(PointLightComponent.class).getLight().setIntensity(Math.abs((float) (100 * FastMath.sin(0.2f * a))));
        float b = (float) (10 * FastMath.sin(a));
        pl1.transformComponent.getPosition().set(b, b, b);
    }

    public void preRender()
    {
        camera.preRender();
    }

    public void dispose()
    {
        forEachEntity(e -> e.destroy());
    }

    public Camera getCamera()
    {
        return camera;
    }

    public DirectionalLight getDirectionalLight()
    {
        return directionalLight;
    }

    /////////////////////////

    public void forEachEntity(UniCallback<Entity> callback)
    {
        for (Entity e : entities)
        {
            if (!e.isDestroyed())
            {
                callback.invoke(e);
            }
        }
    }

    public void forEachRootEntity(UniCallback<Entity> callback)
    {
        forEachEntityWithComponent(TransformComponent.class, e ->
        {
            if (e.getComponent(TransformComponent.class).getParent() != null)
            {
                callback.invoke(e);
            }
        });
    }

    public <T extends Component> void forEachEntityWithComponent(Class<T> klass, UniCallback<Entity> callback)
    {
        forEachEntity(e ->
        {
            if (e.hasComponent(klass))
            {
                callback.invoke(e);
            }
        });
    }

    public void forEachEntityWithFilteredComponents(ComponentTypeMask mask, UniCallback<Entity> callback)
    {
        forEachEntity(e ->
        {
            if (mask.test(e))
            {
                callback.invoke(e);
            }
        });
    }
}