//package com.game;
//
//import com.engine.core.AbstractGame;
//import com.engine.core.Window;
//import com.engine.input.Cursor;
//import com.engine.input.Keyboard;
//import com.engine.input.MouseButtons;
//import com.engine.input.MouseWheel;
//import com.engine.items.*;
//import com.game.guis.Guiv2;
//import com.graphics.RendererEngine;
//import com.graphics.component.Material;
//import com.graphics.component.Texture;
//import com.graphics.component.mesh._3D.Mesh3D;
//import com.graphics.lighting.Attenuation;
//import com.graphics.lighting.PointLight;
//import com.graphics.lighting.SpotLight;
//import com.maths.RNG;
//import com.maths.vectors.Vector2f;
//import com.maths.vectors.Vector3f;
//import com.utils.*;
//
//
//import java.awt.*;
//
//import static com.engine.core.Options.*;
//import static com.engine.items.Scene.X_AXIS;
//import static com.engine.items.Scene.Z_AXIS;
//import static org.lwjgl.glfw.GLFW.*;
//
//
//public class Game extends AbstractGame {
//    private Guiv2 gui;
//
//    private static final float ENTITY_MOVEMENT_SPEED = 5f;
//
//    private static final float ENTITY_SCALE_STEP = 0.05f;
//
//    private int deltaScroll;
//
//    private Vector3f deltaCamPos;
//
//    private Vector3f deltaEntityPos;
//
//    private float deltaEntityScale;
//
//    public Game() {}
//
//    @Override
//    public void init() throws Exception{
//        currentScene = new Scene("Test Scene");
//        currentCamera = new Camera(new Vector3f(-1,1,6));
//        deltaCamPos = new Vector3f();
//        deltaEntityPos = new Vector3f();
//        deltaEntityScale = 0;
//
//        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//        final Texture compassTexture = TextureLoader.load("/textures/compass.png");
//        final Texture asteroidTexture = TextureLoader.load("/textures/asteroid.png");
//        final Texture blueskyboxTexture = TextureLoader.load("/textures/skybox/blue2.png");
//        final Texture redskyboxTexture = TextureLoader.load("/textures/skybox/red1.png");
//        final Texture brick_texture_diffuse = TextureLoader.load("/textures/Bricks/diffuse.jpg");
//        final Texture brick_texture_normal = TextureLoader.load("/textures/Bricks/normalMap.jpg");
//        final Texture brick_texture_specular = TextureLoader.load("/textures/Bricks/specular.jpg");
//
//        ////////////////http://www.ogre3d.org/tikiwiki/tiki-index.php?page=-Point+Light+Attenuation/////////////////////
//
//        final Attenuation attenuation3250m = new Attenuation(1.0f, 0.0014f, 0.000007f);
//        final Attenuation attenuation600m = new Attenuation(1.0f, 0.007f, 0.0002f);
//        final Attenuation attenuation325m = new Attenuation(1.0f, 0.014f, 0.0007f);
//        final Attenuation attenuation200m = new Attenuation(1.0f, 0.022f, 0.0019f);
//        final Attenuation attenuation160m = new Attenuation(1.0f, 0.027f, 0.0028f);
//        final Attenuation attenuation100m = new Attenuation(1.0f, 0.045f, 0.0075f);
//        final Attenuation attenuation65m = new Attenuation(1.0f, 0.07f, 0.017f);
//        final Attenuation attenuation50m = new Attenuation(1.0f, 0.09f, 0.032f);
//        final Attenuation attenuation32m = new Attenuation(1.0f, 0.14f, 0.07f);
//        final Attenuation attenuation20m = new Attenuation(1.0f, 0.22f, 0.20f);
//        final Attenuation attenuation13m = new Attenuation(1.0f, 0.35f, 0.44f);
//        final Attenuation attenuation7m = new Attenuation(1.0f, 0.7f, 1.8f);
//
//        //////////////////////////http://devernay.free.fr/cours/opengl/materials.html///////////////////////////////////
//
//        final Material emerald = new Material(new Color(0.07568f, 0.61424f, 0.07568f), 76.8f);
//        final Material jade = new Material(new Color(0.54f, 0.89f, 0.63f), 32);
//        final Material obsidian = new Material(new Color(0.18275f, 0.17f, 0.22525f), 32);
//        final Material pearl = new Material(new Color(0f, 0.829f, 0.829f), 32);
//        final Material ruby = new Material(new Color(0.61424f, 0.04136f, 0.04136f), 76.8f);
//        final Material turquoise = new Material(new Color(0.396f, 0.74151f, 0.69102f), 32);
//        final Material brass = new Material(new Color(0.780392f, 0.568627f, 0.113725f), 32);
//        final Material bronze = new Material(new Color(0.714f, 0.4284f, 0.18144f), 32);
//        final Material chrome = new Material(new Color(0.4f, 0.4f, 0.4f), 32);
//        final Material copper = new Material(new Color(0.7038f, 0.27048f, 0.0828f), 32);
//        final Material gold = new Material(new Color(0.75164f, 0.60648f, 0.22648f), 51.2f);
//        final Material silver = new Material(new Color(0.50754f, 0.50754f, 0.50754f), 51.2f);
//        final Material blackPlastic = new Material(new Color(0.01f, 0.01f, 0.01f),	32f);
//        final Material cyanPlastic = new Material(new Color(0.0f, 0.50980392f, 0.50980392f), 32f);
//        final Material greenPlastic = new Material(new Color(0.1f, 0.35f, 0.1f), 32f);
//        final Material redPlastic = new Material(new Color(0.5f, 0.0f, 0.0f), 32f);
//        final Material whitePlastic = new Material(new Color(0.55f, 0.55f, 0.55f), 32f);
//        final Material yellowPlastic = new Material(new Color(0.5f, 0.5f, 0.0f), 32f);
//        final Material blackRubber = new Material(new Color(0.01f, 0.01f, 0.01f), 100f);
//        final Material cyanRubber = new Material(new Color(0.4f, 0.5f, 0.5f), 100f);
//        final Material greenRubber = new Material(new Color(0.4f, 0.5f, 0.4f), 100f);
//        final Material redRubber = new Material(new Color(0.5f, 0.4f, 0.4f),100f);
//        final Material whiteRubber = new Material(new Color(0.5f, 0.5f, 0.5f),	100f);
//        final Material yellowRubber = new Material(new Color(0.5f, 0.5f, 0.4f), 100);
//
//        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
////        SceneEntity plane = new SceneEntity("plane", GenerateMesh.plane(10000, 10000), new Material(Color.white));
////        plane.getTransformationSet().getPosition().setY(-15);
////        currentScene.addEntity(plane);
//
//        SceneEntity tetrahedron = new SceneEntity("tetrahedron", GenerateMesh.tetrahedron(0.5f));
//        tetrahedron.getTransformationSet().getPosition().set(-6,4,-2);
//        tetrahedron.getTransformationSet().getRotation().set(45, 0, 0);
//        currentScene.addEntity(tetrahedron);
//
//        SceneEntity cube = new SceneEntity("cube", GenerateMesh.cube(1));
//        cube.getTransformationSet().getPosition().set(4,0,-4);
//        currentScene.addEntity(cube);
//
//        SceneEntity sphere = new SceneEntity("sphere", GenerateMesh.sphere(0.5f,50), gold);
//        sphere.getTransformationSet().getPosition().set(-3,2,3);
//        currentScene.addEntity(sphere);
//
//        SceneEntity cylinder = new SceneEntity("cylinder", GenerateMesh.cylinder(0.3f, 50, 1));
//        cylinder.getTransformationSet().getPosition().set(1,5,3);
//        currentScene.addEntity(cylinder);
//
//        SceneEntity cone = new SceneEntity("cone", GenerateMesh.cone(0.3f, 50, 1));
//        cone.getTransformationSet().getPosition().set(-2,-1,-4);
//        currentScene.addEntity(cone);
//
//        SceneEntity tube = new SceneEntity("tube", GenerateMesh.tube(0.7f,0.6f,50,1.5f));
//        tube.getTransformationSet().getPosition().set(0,7,0);
//        currentScene.addEntity(tube);
//
//        SceneEntity torus = new SceneEntity("torus", GenerateMesh.torus(2,1,50), emerald);
//        torus.getTransformationSet().getPosition().set(0,-7,0);
//        currentScene.addEntity(torus);
//
////        SceneEntity corner = new SceneEntity("corner", GenerateMesh.corner(10,10,10));
////        corner.getTransformationSet().getPosition().set(0,20,0);
////        currentScene.addEntity(corner);
//
//        SceneEntity brickWall = new SceneEntity("brickWall", GenerateMesh.plane(1,1), new Material(brick_texture_diffuse, brick_texture_normal, brick_texture_specular));
//        brickWall.getTransformationSet().getPosition().set(0, 0, 5);
//        brickWall.getTransformationSet().getRotation().set(45, X_AXIS);
//        currentScene.addEntity(brickWall);
//
//        SceneEntity lymanRifle = new SceneEntity("lymanRifle", MeshLoader.loadMesh("/meshs/Lyman_Rifle.obj"), new Material(TextureLoader.load("/textures/Lyman_Rifle.png"), null, TextureLoader.load("/textures/Lyman_Rifle_specular.png")));
//        lymanRifle.getTransformationSet().getPosition().setY(3);
//        currentScene.addEntity(lymanRifle);
//
////        SceneEntity room = new SceneEntity("room", MeshLoader.loadMesh("/meshs/room.obj"), new Material(Color.WHITE));
////        room.getTransformationSet().getPosition().set(100,-3,0);
////        room.getTransformationSet().setScale(0.1f);
////        currentScene.addEntity(room);
//
////        SceneEntity mayaNormals = new SceneEntity("mayaNormals", ModelLoader.loadModels("mayaNormals"));
////        mayaNormals.getTransformationSet().getPosition().set(-1.5f,20,0);
////        mayaNormals.getTransformationSet().setScale(2);
////        currentScene.addEntity(mayaNormals);
//////
////        SceneEntity mayaNoNormals = new SceneEntity("mayaNoNormals", ModelLoader.loadModels("mayaNoNormals"));
////        mayaNoNormals.getTransformationSet().getPosition().set(1.5f,20,0);
////        mayaNoNormals.getTransformationSet().setScale(2);
////        currentScene.addEntity(mayaNoNormals);
//
////        SceneEntity interior = new SceneEntity("interior", ModelLoader.loadModels("interior"));
////        interior.getTransformationSet().getPosition().set(100,0,30);
////        interior.getTransformationSet().setScale(0.1f);
////        currentScene.addEntity(interior);
//
////        SceneEntity falcon = new SceneEntity("falcon", ModelLoader.loadModels("millenium-falcon"));
////        falcon.getTransformationSet().getPosition().set(0,25,0);
////        falcon.getTransformationSet().setScale(0.1f);
////        currentScene.addEntity(falcon);
//
////        SceneEntity lambo = new SceneEntity("lambo", ModelLoader.loadModels("lambo"));
////        lambo.getTransformationSet().getPosition().set(20,10,0);
////        currentScene.addEntity(lambo);
//
////        SceneEntity nanosuit = new SceneEntity("nanosuit", ModelLoader.loadModels("nanosuit"));
////        nanosuit.getTransformationSet().getPosition().set(10,-10.5f,0);
////        nanosuit.getTransformationSet().getRotation().set(90, Y_AXIS);
////        currentScene.addEntity(nanosuit);
//
//        SceneEntity sponza = new SceneEntity("sponza", ModelLoader.loadModels("sponza"));
//        sponza.getTransformationSet().setScale(0.1f);
//        sponza.getTransformationSet().getPosition().set(0,-10,6);
//        currentScene.addEntity(sponza);
//
//        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//        PointLight originPointLight = new PointLight("originPointLight", new Color(233, 99, 22), 10, attenuation20m);
//        originPointLight.getTransformationSet().setScale(0.3f);
//        currentScene.addPointLight(originPointLight);
//
//        SpotLight flashLight = new SpotLight("flashLight", Color.WHITE, 5, 25f, 30f, attenuation160m);
//        flashLight.getTransformationSet().getPosition().set(75,10,75);
//        currentScene.addSpotLight(flashLight);
//
//        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//        currentScene.getSkybox().setTexture(blueskyboxTexture);
//
//        gui = new Guiv2(currentScene);
//    }
//
//    @Override
//    public void handleInput() {
//        if(Keyboard.isKeyDown(GLFW_KEY_W)){
//            deltaCamPos.setZ(-1);
//        } else if(Keyboard.isKeyDown(GLFW_KEY_S)){
//            deltaCamPos.setZ(1);
//        } else{
//            deltaCamPos.setZ(0);
//        }
//
//        if(Keyboard.isKeyDown(GLFW_KEY_A)){
//            deltaCamPos.setX(-1);
//        } else if(Keyboard.isKeyDown(GLFW_KEY_D)){
//            deltaCamPos.setX(1);
//        } else{
//            deltaCamPos.setX(0);
//        }
//
//        if(MouseButtons.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
//            deltaCamPos.setY(1);
//        } else if(MouseButtons.isButtonDown(GLFW_MOUSE_BUTTON_RIGHT)){
//            deltaCamPos.setY(-1);
//        } else{
//            deltaCamPos.setY(0);
//        }
//
//
//        System.out.println(currentCamera);
//
//        if(Keyboard.isKeyDown(GLFW_KEY_SPACE)){
//            deltaCamPos.multiply(20);
//        }else if(Keyboard.isKeyDown(GLFW_KEY_LEFT_ALT)) {
//            deltaCamPos.multiply(0.1f);
//        }
//
//
//        if(Cursor.inCameraMode()) {
//            Vector2f deltaCursorPos = Cursor.getDeltaPosition();
//            deltaCursorPos.multiply(currentCamera.getMouseSensitivity());
//            currentCamera.changeTilt(-deltaCursorPos.getY());
//            currentCamera.changePan(deltaCursorPos.getX());
//        }
//
//        if(MouseWheel.isScrolled()){
//            deltaScroll = MouseWheel.getDeltaScroll();
//        }else {
//            deltaScroll = 0;
//        }
//
//        if(Keyboard.isKeyTapped(GLFW_KEY_RIGHT_CONTROL) && !isWindowFullScreen()) {
//            if (Cursor.inCameraMode()) {
//                Cursor.toggleCameraMode();
//            }
//            gui.setVisibility(true);
//        }
//
//        if(Keyboard.isKeyTapped(GLFW_KEY_F)){
//            toggleFreeze();
//        }
//
//        if(Keyboard.isKeyTapped(GLFW_KEY_F2)){
//            toggleDebugMode();
//        }
//
//        if(Keyboard.isKeyTapped(GLFW_KEY_N)){
//            toggleMappedNormals();
//        }
//
//        if(Keyboard.isKeyTapped(GLFW_KEY_L)){
//            toggleSpecularMapping();
//        }
//
//        if(Keyboard.isKeyTapped(GLFW_KEY_O)){
//            toggleAmbientOcclusion();
//        }
//
//        if(Keyboard.isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
//            Attenuation attenuation = new Attenuation(1.0f, 0.35f, 0.44f);
//            for(int i = 0; i < 99; i++){
//                PointLight pointLight = new PointLight(String.format("testPointlight: %d", i), 10, attenuation);
//                pointLight.getTransformationSet().getPosition().set(RNG.Float(-100, 100), RNG.Float(-10, 100), RNG.Float(-20, 20));
//                currentScene.addPointLight(pointLight);
//            }
//        }
//
//        if(Keyboard.isKeyTapped(GLFW_KEY_RIGHT_SHIFT)) {
//            final Mesh3D sphereMesh = GenerateMesh.sphere(1, 20);
//            final float v = 3f;
//            final float a = 5f;
//            for(int i = 0; i < 5000; i++){
//                SceneEntity e = new SceneEntity(String.format("sphere id: %d", i), sphereMesh);
//                e.getTransformationSet().setScale(RNG.Float(1, 5));
//                e.getTransformationSet().getRotation().set(RNG.Float(0, 360), RNG.Float(0, 360), RNG.Float(0, 360));
//                e.getVelocity().set(RNG.Float(-v, v), RNG.Float(-v, v), RNG.Float(-v, v));
//                e.getAcceleration().set(RNG.Float(-a, a), RNG.Float(-a, a), RNG.Float(-a, a));
//                currentScene.addEntity(e);
//            }
//        }
//
//        if (Keyboard.isKeyDown(GLFW_KEY_UP)) {
//            deltaEntityPos.setZ(-1);
//        } else if (Keyboard.isKeyDown(GLFW_KEY_DOWN)) {
//            deltaEntityPos.setZ(1);
//        } else {
//            deltaEntityPos.setZ(0);
//        }
//        if (Keyboard.isKeyDown(GLFW_KEY_LEFT)) {
//            deltaEntityPos.setX(-1);
//        } else if (Keyboard.isKeyDown(GLFW_KEY_RIGHT)) {
//            deltaEntityPos.setX(1);
//        } else {
//            deltaEntityPos.setX(0);
//        }
//        if (Keyboard.isKeyDown(GLFW_KEY_PAGE_UP)) {
//            deltaEntityPos.setY(1);
//        } else if (Keyboard.isKeyDown(GLFW_KEY_PAGE_DOWN)) {
//            deltaEntityPos.setY(-1);
//        } else {
//            deltaEntityPos.setY(0);
//        }
//
//        if (Keyboard.isKeyDown(GLFW_KEY_KP_ADD)) {
//            deltaEntityScale = ENTITY_SCALE_STEP;
//        } else if (Keyboard.isKeyDown(GLFW_KEY_KP_SUBTRACT)) {
//            deltaEntityScale = -ENTITY_SCALE_STEP;
//        }else {
//            deltaEntityScale = 0;
//        }
//
//        if(Keyboard.isKeyTapped(GLFW_KEY_1)){
//            PointLight pointLight = currentScene.getPointLight("originPointLight");
//            pointLight.setColour(RNG.Colour());
//            System.out.println("///////////////////////////////////////////////////////////");
//            System.out.println("Attenuation: " + pointLight.getAttenuation());
//            System.out.println("Colour: " + pointLight.getColor());
//            System.out.println("Component: " + pointLight.getIntensity() * DataTypeUtils.toVector3f(pointLight.getColor()).getLargestComponent());
//            System.out.println("Range:  " + pointLight.getRange());
//        }
//
//        if(Keyboard.isKeyTapped(GLFW_KEY_2)){
//            currentScene.getPointLight("originPointLight").toggleActive();
//        }
//
//        if(Keyboard.isKeyDown(GLFW_KEY_3)){
//            currentScene.getDirectionalLight().getRotation().multiply(0.1f, Z_AXIS);
//        }else if(Keyboard.isKeyDown(GLFW_KEY_4)){
//            currentScene.getDirectionalLight().getRotation().multiply(-0.1f, Z_AXIS);
//        }
//
//        if(Keyboard.isKeyDown(GLFW_KEY_5)){
//            currentScene.getDirectionalLight().getRotation().multiply(0.1f, X_AXIS);
//        }else if(Keyboard.isKeyDown(GLFW_KEY_6)){
//            currentScene.getDirectionalLight().getRotation().multiply(-0.1f, X_AXIS);
//        }
//    }
//
//    @Override
//    public void updateLogic(float interval) {
//        gui.update();
//        changeFOV(-deltaScroll);
//        currentScene.getSkybox().update(currentCamera.getPosition());
//        currentCamera.changePositionRelativeToOrientation(Vector3f.Multiply(deltaCamPos, currentCamera.getCameraMovementSpeed() * interval));
//
//        if(!isFrozen()) {
//            currentScene.update(interval);
//
//            if (currentScene.hasSpotLight("flashLight")) {
//                currentScene.getSpotLight("flashLight").getTransformationSet().getRotation().set(currentCamera.getRotation());
//                currentScene.getSpotLight("flashLight").getTransformationSet().getPosition().set(currentCamera.getPosition());
//            }
//
//            if (currentScene.hasEntity("tube")) {
//                currentScene.getEntity("tube").getTransformationSet().getRotation().multiply(0.1f, 0.1f, 0.1f);
//            }
//
//            if (currentScene.hasEntity("cylinder")) {
//                currentScene.getEntity("cylinder").getTransformationSet().getRotation().multiply(1, RNG.Vector3f());
//            }
//        }
//
//        if (currentHUD.hasEntity("compass")) {
//            currentHUD.getEntity("compass").setRotation(currentCamera.getPan());
//        }
//    }
//
//    @Override
//    public void render(Window window, RendererEngine renderer) throws Exception {
//        renderer.render(window, currentCamera, currentScene, currentHUD);
//    }
//
//    @Override
//    public void dispose() {
//        currentScene.dispose();
//    }
//}