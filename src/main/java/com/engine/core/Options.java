package com.engine.core;

import static com.engine.core.Engine.prefs;

/**
 * Created by Jordan Fisher on 08/07/2017.
 */
public  class Options {
    public static void loadOptions(){
        debug = prefs.getBoolean("debug", debug_DEFAULT);
        ambientOcclusion = prefs.getBoolean("ambientOcclusion", ambientOcclusion_DEFAULT);
        mappedNormals = prefs.getBoolean("mappedNormals", mappedNormals_DEFAULT);
        specularMapping = prefs.getBoolean("specularMapping", specularMapping_DEFAULT);
        HUDvisible = prefs.getBoolean("HUDvisible", HUDvisible_DEFAULT);
        frustumCull = prefs.getBoolean("frustumCull", frustumCull_DEFAULT);
        cullFaces = prefs.getBoolean("cullFaces", cullFaces_DEFAULT);
        wireframeMode = prefs.getBoolean("wireframeMode", wireframeMode_DEFAULT);
        vSync = prefs.getBoolean("vSync", vSync_DEFAULT);
        antiAliasing = prefs.getBoolean("antiAliasing", antiAliasing_DEFAULT);
        freeze = prefs.getBoolean("freeze", freeze_DEFAULT);
        axisVisible = prefs.getBoolean("axisVisible", axisVisible_DEFAULT);
        skyboxVisible = prefs.getBoolean("skyboxVisible", skyboxVisible_DEFAULT);
        FOV = prefs.getInt("FOV", FOV_DEFAULT);
        viewDistanceNear = prefs.getFloat("viewDistanceNear", viewDistanceNear_DEFAULT);
        viewDistanceFar = prefs.getInt("viewDistanceFar", viewDistanceFar_DEFAULT);
        ambientLightBrightness = prefs.getFloat("ambientLightBrightness", ambientLightBrightness_DEFAULT);
        exposure = prefs.getFloat("exposure", exposure_DEFAULT);
        gamma = prefs.getFloat("gamma", gamma_DEFAULT);
    }

    public static void saveOptions(){
        prefs.putBoolean("debug", debug);
        prefs.putBoolean("ambientOcclusion", ambientOcclusion);
        prefs.putBoolean("mappedNormals", mappedNormals);
        prefs.putBoolean("specularMapping", specularMapping);
        prefs.putBoolean("HUDvisible", HUDvisible);
        prefs.putBoolean("frustumCull", frustumCull);
        prefs.putBoolean("cullFaces", cullFaces);
        prefs.putBoolean("wireframeMode", wireframeMode);
        prefs.putBoolean("vSync", vSync);
        prefs.putBoolean("antiAliasing", antiAliasing);
        prefs.putBoolean("freeze", freeze);
        prefs.putBoolean("axisVisible", axisVisible);
        prefs.putBoolean("skyboxVisible", skyboxVisible);
        prefs.putInt("FOV", FOV);
        prefs.putFloat("viewDistanceNear", viewDistanceNear);
        prefs.putInt("viewDistanceFar", viewDistanceFar);
        prefs.putFloat("ambientLightBrightness", ambientLightBrightness);
        prefs.putFloat("exposure", exposure);
        prefs.putFloat("gamma", gamma);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean debug;
    private static final boolean debug_DEFAULT = false;

    public static void setDebugMode(boolean state){
        debug = state;
    }

    public static void toggleDebugMode(){
        debug = !debug;
    }

    public static boolean isDebugging() {
        return debug;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean ambientOcclusion;
    private static final boolean ambientOcclusion_DEFAULT = false;

    public static void setAmbientOcclusion(boolean state){
        ambientOcclusion = state;
    }

    public static void toggleAmbientOcclusion(){
        ambientOcclusion = !ambientOcclusion;
    }

    public static boolean isAmbientOcclusionActive() {
        return ambientOcclusion;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean mappedNormals;
    private static final boolean mappedNormals_DEFAULT = true;

    public static void setMappedNormals(boolean state){
        mappedNormals = state;
    }

    public static void toggleMappedNormals(){
        mappedNormals = !mappedNormals;
    }

    public static boolean isUsingMappedNormals() {
        return mappedNormals;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean specularMapping;
    private static final boolean specularMapping_DEFAULT = true;

    public static void setSpecularMapping(boolean state){
        specularMapping = state;
    }

    public static void toggleSpecularMapping(){
        specularMapping = !specularMapping;
    }

    public static boolean isUsingSpecularMaps() {
        return specularMapping;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean HUDvisible;
    private static final boolean HUDvisible_DEFAULT = true;

    public static void setHUDvisibility(boolean state){
        HUDvisible = state;
    }

    public static void toggleHUDvisibility(){
        HUDvisible = !HUDvisible;
    }

    public static boolean isHUDvisible() {
        return HUDvisible;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean frustumCull;
    private static final boolean frustumCull_DEFAULT = true;

    public static void setFrustumCull(boolean state){
        frustumCull = state;
    }

    public static void toggleFrustumCull(){
        frustumCull = !frustumCull;
    }

    public static boolean isFrustumCullingEnabled() {
        return frustumCull;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean cullFaces;
    private static final boolean cullFaces_DEFAULT = true;
    
    public static void setCullFaces(boolean state){
        cullFaces = state;
    }
    
    public static void toggleCullFaces(){
        cullFaces = !cullFaces;
    }

    public static boolean isCullingFacesEnabled() {
        return cullFaces;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean wireframeMode;
    private static final boolean wireframeMode_DEFAULT = false;

    public static void setWireframeMode(boolean state){
        wireframeMode = state;
    }

    public static void toggleWireframeMode(){
        wireframeMode = !wireframeMode;
    }

    public static boolean isWireframeMode() {
        return wireframeMode;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean vSync;
    private static final boolean vSync_DEFAULT = true;

    public static void setVSync(boolean state){
        vSync = state;
    }

    public static void toggleVSync(){
        vSync = !vSync;
    }

    public static boolean isVSyncEnabled() {
        return vSync;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean antiAliasing;
    private static final boolean antiAliasing_DEFAULT = true;

    public static void setAntiAliasing(boolean state){
        antiAliasing = state;
    }

    public static void toggleAntiAliasing(){
        antiAliasing = !antiAliasing;
    }

    public static boolean isAntiAliasingEnabled() {
        return antiAliasing;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean freeze;
    private static final boolean freeze_DEFAULT = false;

    public static void setFreeze(boolean state){
        freeze = state;
    }

    public static void toggleFreeze(){
        freeze = !freeze;
    }

    public static boolean isFrozen() {
        return freeze;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean axisVisible;
    private static final boolean axisVisible_DEFAULT = false;

    public static void setAxisVisibility(boolean state){
        axisVisible = state;
    }

    public static void toggleAxisVisibility(){
        axisVisible = !axisVisible;
    }

    public static boolean isAxisVisible() {
        return axisVisible;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean skyboxVisible;
    private static final boolean skyboxVisible_DEFAULT = true;

    public static void setSkyboxVisibility(boolean state){
        skyboxVisible = state;
    }

    public static void toggleSkyboxVisibility(){
        skyboxVisible = !skyboxVisible;
    }

    public static boolean isSkyboxVisible() {
        return skyboxVisible;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static int FOV;
    private static final int FOV_DEFAULT = 70;
    public static final int FOV_MINIMUM = 20;
    public static final int FOV_MAXIMUM = 160;
    
    public static void setFOV(int value){
        if(value < FOV_MINIMUM){
            FOV = FOV_MINIMUM;
        }else if(value > FOV_MAXIMUM){
            FOV = FOV_MAXIMUM;
        }else {
            FOV = value;
        }
    }
    
    public static void changeFOV(int value){
        setFOV(getFOV() + value);
    }
    
    public static int getFOV(){
        return FOV;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static float viewDistanceNear;
    private static final float viewDistanceNear_DEFAULT = 0.01f;
    private static final float viewDistanceNear_MINIMUM = 0.001f;
    private static final float viewDistanceNear_MAXIMUM = 10f;

    public static void setViewDistanceNear(float value){
        if(value < viewDistanceNear_MINIMUM){
            viewDistanceNear = viewDistanceNear_MINIMUM;
        }else if(value > viewDistanceNear_MAXIMUM){
            viewDistanceNear = viewDistanceNear_MAXIMUM;
        }else {
            viewDistanceNear = value;
        }
    }

    public static void changeViewDistanceNear(float value){
        setViewDistanceNear(getViewDistanceNear() + value);
    }

    public static float getViewDistanceNear(){
        return viewDistanceNear;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static int viewDistanceFar;
    private static final int viewDistanceFar_DEFAULT = 5000;
    public static final int viewDistanceFar_MINIMUM = 200;
    public static final int viewDistanceFar_MAXIMUM = 10200;

    public static void setViewDistanceFar(int value){
        if(value < viewDistanceFar_MINIMUM){
            viewDistanceFar = viewDistanceFar_MINIMUM;
        }else if(value > viewDistanceFar_MAXIMUM){
            viewDistanceFar = viewDistanceFar_MAXIMUM;
        }else {
            viewDistanceFar = value;
        }
    }

    public static void changeViewDistanceFar(int value){
        setViewDistanceFar(getViewDistanceFar() + value);
    }

    public static int getViewDistanceFar(){
        return viewDistanceFar;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static float ambientLightBrightness;
    private static final float ambientLightBrightness_DEFAULT = 0.2f;
    public static final float ambientLightBrightness_MINIMUM = 0f;
    public static final float ambientLightBrightness_MAXIMUM = 1f;

    public static void setAmbientLightBrightness(float value){
        if(value < ambientLightBrightness_MINIMUM){
            ambientLightBrightness = ambientLightBrightness_MINIMUM;
        }else if(value > ambientLightBrightness_MAXIMUM){
            ambientLightBrightness = ambientLightBrightness_MAXIMUM;
        }else {
            ambientLightBrightness = value;
        }
    }

    public static void changeAmbientLightBrightness(float deltaValue){
        setAmbientLightBrightness(getAmbientLightBrightness() + deltaValue);
    }

    public static float getAmbientLightBrightness(){
        return ambientLightBrightness;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static float exposure;
    private static final float exposure_DEFAULT = 0.8f;
    public static final float exposure_MINIMUM = 0f;
    public static final float exposure_MAXIMUM = 1f;

    public static void setExposure(float value){
        if(value < exposure_MINIMUM){
            exposure = exposure_MINIMUM;
        }else if(value > exposure_MAXIMUM){
            exposure = exposure_MAXIMUM;
        }else {
            exposure = value;
        }
    }

    public static void changeExposure(float deltaValue){
        setExposure(getExposure() + deltaValue);
    }

    public static float getExposure(){
        return exposure;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static float gamma;
    private static final float gamma_DEFAULT = 2.2f;
    public static final float gamma_MINIMUM = 0f;
    public static final float gamma_MAXIMUM = 5f;

    public static void setGamma(float value){
        if(value < gamma_MINIMUM){
            gamma = gamma_MINIMUM;
        }else if(value > gamma_MAXIMUM){
            gamma = gamma_MAXIMUM;
        }else {
            gamma = value;
        }
    }

    public static void changeGamma(float deltaValue){
        setGamma(getGamma() + deltaValue);
    }

    public static float getGamma(){
        return gamma;
    }
}