package com.graphics;

import com.graphics.lighting.*;
import com.maths.Matrix4f;
import com.maths.vectors.Vector2f;
import com.maths.vectors.Vector3f;
import com.maths.vectors.Vector4f;
import com.utils.DataUtils;
import com.utils.ReasourceLoader;
import org.apache.commons.math3.util.FastMath;

import java.awt.*;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.engine.items.World.WORLD_FORWARD_VECTOR;
import static com.graphics.ShaderProgram.ShaderType.*;
import static com.utils.ReasourceLoader.loadFileAsString;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public class ShaderProgram {

    private static final int NULL = 0;

    private final String shaderName;
    private final ShaderType shaderType;

    private final int programId;
    private int vertexShaderId;
    private int geometryShaderId;
    private int fragmentShaderId;

    private final HashMap<String, Integer> uniforms;
    private final HashMap<String, HashMap<String, String>> structs;

    public ShaderProgram(String shaderName) throws Exception {
        this.shaderName = shaderName;
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception(String.format("Could not create %s Shader", shaderName));
        }

        final File vertexShaderFile   = new File(String.format("./src/main/resources/shaders/%s/vertex.vs",   shaderName));
        final File geometryShaderFile = new File(String.format("./src/main/resources/shaders/%s/geometry.gs", shaderName));
        final File fragmentShaderFile = new File(String.format("./src/main/resources/shaders/%s/fragment.fs", shaderName));

        if(!vertexShaderFile.exists()){ throw new Exception(String.format("Could not find the %s vertex shader", shaderName)); }
        final boolean hasGeometryShader = geometryShaderFile.exists();
        if(!fragmentShaderFile.exists()){ throw new Exception(String.format("Could not find the %s fragment shader", shaderName)); }

        String vertexShaderCode   = loadFileAsString(String.format("./src/main/resources/shaders/%s/vertex.vs", shaderName));
        String geometryShaderCode = hasGeometryShader ? loadFileAsString(String.format("./src/main/resources/shaders/%s/geometry.gs", shaderName)) : null;
        String fragmentShaderCode = loadFileAsString(String.format("./src/main/resources/shaders/%s/fragment.fs", shaderName));

        createVertexShader(vertexShaderCode);
        if(hasGeometryShader){createGeometryShader(geometryShaderCode);}
        createFragmentShader(fragmentShaderCode);

        linkProgram();

        this.shaderType = getShaderType(vertexShaderCode);

        uniforms = new HashMap<>();
        structs = new HashMap<>();

        autoCreateUniforms(vertexShaderCode);
        if(hasGeometryShader){autoCreateUniforms(geometryShaderCode);}
        autoCreateUniforms(fragmentShaderCode);
    }

    private void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShaderAttachment(shaderCode, GL_VERTEX_SHADER);
    }

    private void createGeometryShader(String shaderCode) throws Exception {
        geometryShaderId = createShaderAttachment(shaderCode, GL_GEOMETRY_SHADER);
    }

    private void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShaderAttachment(shaderCode, GL_FRAGMENT_SHADER);
    }

    private int createShaderAttachment(String shaderCode, int shaderTarget) throws Exception {
        String shaderTypeName;
        switch (shaderTarget){
            case GL_VERTEX_SHADER:
                shaderTypeName = "Vertex";
                break;
            case GL_GEOMETRY_SHADER:
                shaderTypeName = "Geometry";
                break;
            case GL_FRAGMENT_SHADER:
                shaderTypeName = "Fragment";
                break;
            default:
                shaderTypeName = "Unknown";
                break;
        }

        int shaderId = glCreateShader(shaderTarget);
        if (shaderId == NULL) {
            throw new Exception(String.format("Error creating %s %s Shader", shaderName, shaderTypeName));
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == NULL) {
            throw new Exception(String.format("Error compiling %s %s Shader: %s", shaderName, shaderTypeName, glGetShaderInfoLog(shaderId, 1024)));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    private void linkProgram() throws Exception {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == NULL) {
            throw new Exception(String.format("Error linking %s Shader code: %s", shaderName, glGetProgramInfoLog(programId, 1024)));
        }

        if (vertexShaderId != NULL) {
            glDetachShader(programId, vertexShaderId);
            glDeleteShader(vertexShaderId);
        }
        if (geometryShaderId != NULL){
            glDetachShader(programId, geometryShaderId);
            glDeleteShader(geometryShaderId);
        }
        if (fragmentShaderId != NULL) {
            glDetachShader(programId, fragmentShaderId);
            glDeleteShader(fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == NULL) {
            throw new Exception(String.format("Warning validating %s Shader code: %s", shaderName, glGetShaderInfoLog(programId, 1024)));
        }
    }

    public void bind() {
        glUseProgram(programId);
        BoundShaderType = shaderType;
    }

    public void unbind() {
        glUseProgram(NULL);
        BoundShaderType = sNULL;
    }

    public void dispose() {
        unbind();
        if (programId != NULL) {
            glDeleteProgram(programId);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public enum ShaderType {
        s2D,
        s3D,
        sNULL
    }

    private static ShaderType BoundShaderType = sNULL;

    public static ShaderType getBoundShaderType() {
        return BoundShaderType;
    }

    private static final Pattern vec2AttributeShaderLinePattern = Pattern.compile("layout\\s+\\(location = 0\\)\\s+in\\s+vec2\\s+\\D\\w*\\s*;");
    private static final Pattern vec3AttributeShaderLinePattern = Pattern.compile("layout\\s+\\(location = 0\\)\\s+in\\s+vec3\\s+\\D\\w*\\s*;");

    private ShaderType getShaderType(String vertexShaderCode) {
        Matcher vec2Matcher = vec2AttributeShaderLinePattern.matcher(vertexShaderCode);
        Matcher vec3Matcher = vec3AttributeShaderLinePattern.matcher(vertexShaderCode);

        if (vec2Matcher.find()) {
            return s2D;
        } else if (vec3Matcher.find()) {
            return s3D;
        } else {
            return sNULL;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final Pattern structBodyPattern = Pattern.compile("struct\\s+\\D\\w*\\s*\\{(\\s*\\D\\w*\\s+\\D\\w*\\s*;\\s*)+}\\s*;"); //TODO: handle unexpected '\n's
    private static final Pattern uniformShaderLinePattern = Pattern.compile("uniform\\s+\\D\\w*(\\[\\w+])?\\s+\\D\\w+\\s*;");

    private void autoCreateUniforms(String shaderCode) throws Exception{
        Matcher matcher = structBodyPattern.matcher(shaderCode);

        while (matcher.find()) {
            String structCode = matcher.group();

            String[] tokens = structCode.split("[{}]"); // splits in to body, head, tail
            String head = tokens[0].replaceAll("struct|\\s", "");
            String body = tokens[1];

            body = body.replaceAll("[\\s\n]+", " ");
            body = body.replaceAll("(?<!\\w)\\s", ""); // negative lookbehind: (?<!a)b, which is b not preceeded by a
            String[] bodyComponents = body.split(";");

            HashMap<String, String> variableDataTypeMap = new HashMap<>();

            for(String component : bodyComponents){
                tokens = component.split("\\s");
                String dataType = tokens[0];
                String variableName = tokens[1];
                variableDataTypeMap.put(variableName, dataType);
            }

            structs.put(head, variableDataTypeMap);
        }

        matcher = uniformShaderLinePattern.matcher(shaderCode);

        while (matcher.find()) {
            String uniformShaderLine = matcher.group();
            uniformShaderLine = uniformShaderLine.replaceAll("uniform\\s+",""); //remove head
            String[] tokens = uniformShaderLine.split("\\s+"); //split
            String dataTypeComponent = tokens[0];
            String uniformName = tokens[1].substring(0, tokens[1].length() - 1); //remove semicolon

            String[] subComponents = dataTypeComponent.split("[\\[\\]]");
            String dataType = subComponents[0];

            switch (subComponents.length){
                case 1:
                    createUniform(dataType, uniformName);
                    break;
                case 2:
                    String arraySizePointerString = subComponents[1];
                    if(arraySizePointerString.matches("\\d+")){
                        createUniformArray(dataType, uniformName, Integer.parseInt(arraySizePointerString));
                    }else {
                        createUniformArray(dataType, uniformName, ReasourceLoader.getGLSLConstIntValue(arraySizePointerString, shaderCode));
                    }
                    break;
                default:
                    throw new Exception("Unsupported uniform decleration");
            }
        }
    }

    private void createUniformArray(String dataType, String uniformName, int size) throws Exception{
        for(int i = 0; i < size; i++){
            createUniform(dataType, String.format("%s[%d]", uniformName, i));
        }
    }
    
    private void createUniform(String dataType, String uniformName) throws Exception{
        switch (dataType){
            case "bool":
            case "int":
            case "float":
            case "vec2":
            case "vec3":
            case "vec4":
            case "mat4":
            case "sampler2D":
                createUniform(uniformName);
                break;
            default:
                if(structs.containsKey(dataType)){
                    HashMap<String, String> attributeMap = structs.get(dataType);
                    for(String attribute : attributeMap.keySet()){
                        createUniform(attributeMap.get(attribute), String.format("%s.%s", uniformName, attribute));
                    }
                }else {
                    throw new Exception(String.format("Cannot create the %s uniform type", dataType));
                }
            break;
        }
    }

    private void createUniform(String name) throws Exception {
        final int uniformLocation = glGetUniformLocation(programId, name);

        if(uniformLocation == 0xFFFFFFFF){
            throw new Exception(String.format("The uniform: '%s' for the '%s' shader program is not used", name, shaderName));
        }else {
            uniforms.put(name, uniformLocation);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private int getUniform(String uniformName) throws Exception{
        if(!uniforms.containsKey(uniformName)){
            throw new Exception(String.format("The %s shader does not contain the %s uniform", shaderName, uniformName));
        }else {
            return uniforms.get(uniformName);
        }
    }

    public void setColourRGBUniform(String uniformName, Color colour) throws Exception{
        setVector3fUniform(uniformName, DataUtils.toVector3f(colour));
    }

    public void setMatrix4fUniform(String uniformName, Matrix4f matrix) throws Exception{
        glUniformMatrix4fv(getUniform(uniformName),false, matrix.toFloatBuffer());
    }

    public void setVector2fUniform(String uniformName, Vector2f vector) throws Exception{
        glUniform2f(getUniform(uniformName), vector.getX(), vector.getY());
    }

    public void setVector3fUniform(String uniformName, Vector3f vector) throws Exception{
        glUniform3f(getUniform(uniformName), vector.getX(), vector.getY(), vector.getZ());
    }

    public void setVector4fUniform(String uniformName, Vector4f vector) throws Exception{
        glUniform4f(getUniform(uniformName), vector.getX(), vector.getY(), vector.getZ(), vector.getW());
    }

    public void setIntUniform(String uniformName, int value) throws Exception{
        glUniform1i(getUniform(uniformName), value);
    }

    public void setFloatUniform(String uniformName, float value) throws Exception{
        glUniform1f(getUniform(uniformName), value);
    }

    public void setBooleanUniform(String uniformName, boolean state) throws Exception{
        setIntUniform(uniformName, state ? 1 : 0);
    }

    public void setSampler2DUniform(String uniformName, int textureUnit) throws Exception{
        if(0 <= textureUnit && textureUnit <=  glGetInteger(GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS)){
            setIntUniform(uniformName, textureUnit);
        }
    }

    public void setAttenuationUniform(String uniformName, Attenuation attenuation) throws Exception {
        setFloatUniform(uniformName + ".constant", attenuation.getConstant());
        setFloatUniform(uniformName + ".linear", attenuation.getLinear());
        setFloatUniform(uniformName + ".exponent", attenuation.getExponent());
    }

    public void setLightUniform(String uniformName, Light light) throws Exception{
        setColourRGBUniform(uniformName + ".colour", light.getColor());
        setFloatUniform(uniformName + ".intensity", light.getIntensity());
        setBooleanUniform(uniformName + ".active", light.isActive());
        }

    public void setDirectionalLightUniform(String uniformName, DirectionalLight directionalLight) throws Exception{
        setLightUniform(uniformName + ".light", directionalLight);
        setVector3fUniform(uniformName + ".direction", directionalLight.getDirection());
    }

    public void setPointLightsUniform(String uniformName, Collection<PointLight> pointLights) throws Exception{
        setPointLightsUniform(uniformName, pointLights.toArray(new PointLight[pointLights.size()]));
    }

    public void setPointLightsUniform(String uniformName, PointLight[] pointLights) throws Exception{
        for(int i = 0; i < pointLights.length; i++){
            setPointLightUniform(String.format("%s[%d]", uniformName, i), pointLights[i]);
        }
    }

    public void setPointLightUniform(String uniformName, PointLight pointLight) throws Exception{
        setLightUniform(uniformName + ".light", pointLight);
        setVector3fUniform(uniformName + ".position", pointLight.getTransformationSet().getPosition());
        setAttenuationUniform(uniformName + ".attenuation", pointLight.getAttenuation());
        setFloatUniform(uniformName + ".range", pointLight.getRange());
    }

    public void setSpotLightsUniform(String uniformName, Collection<SpotLight> spotLights) throws Exception{
        setSpotLightsUniform(uniformName, spotLights.toArray(new SpotLight[spotLights.size()]));
    }

    public void setSpotLightsUniform(String uniformName, SpotLight[] spotLights) throws Exception{
        for(int i = 0; i < spotLights.length; i++){
            setSpotLightUniform(String.format("%s[%d]", uniformName, i), spotLights[i]);
        }
    }

    public void setSpotLightUniform(String uniformName, SpotLight spotLight) throws Exception{
        setLightUniform(uniformName + ".light", spotLight);
        setVector3fUniform(uniformName + ".position", spotLight.getTransformationSet().getPosition());
        setAttenuationUniform(uniformName + ".attenuation", spotLight.getAttenuation());
        setVector3fUniform(uniformName + ".coneDirection", Vector3f.Multiply(Vector3f.Negative(WORLD_FORWARD_VECTOR), spotLight.getTransformationSet().getRotation()));
        setFloatUniform(uniformName + ".cutOff", (float)FastMath.cos(FastMath.toRadians(spotLight.getCutOff())));
        setFloatUniform(uniformName + ".outerCutOff", (float)FastMath.cos(FastMath.toRadians(spotLight.getOuterCutOff())));
    }
}
