package com.graphics;

import com.componentSystem.World;
import com.engine.items.DirectionalLight;
import com.componentSystem.components.PointLightComponent;
import com.componentSystem.components.SpotLightComponent;
import com.engine.items.Attenuation;
import com.engine.items.Light;
import com.maths.Matrix4f;
import com.maths.vectors.Vector2f;
import com.maths.vectors.Vector3f;
import com.maths.vectors.Vector4f;
import com.utils.DataUtils;
import com.utils.ReasourceLoader;
import org.apache.commons.math3.util.FastMath;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.utils.ReasourceLoader.loadFileAsString;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram implements IResource
{
    private static ShaderProgram BOUND_SHADER;

    private final int programId;
    private boolean disposed;
    private final String shaderProgramName;

    private final HashMap<String, Integer> uniforms;
    private final HashMap<String, HashMap<String, String>> structures;

    public ShaderProgram(String shaderProgramName, String vertexShaderName, String fragmentShaderName) throws Exception
    {
        this.shaderProgramName = shaderProgramName;
        this.programId = glCreateProgram();

        if (programId == 0)
        {
            throw new Exception(String.format("Could not create %s shader program", shaderProgramName));
        }

        final File vertexShaderFile = new File(String.format("./src/main/resources/shaders/vertex/%s.glsl", vertexShaderName));
        final File fragmentShaderFile = new File(String.format("./src/main/resources/shaders/fragment/%s.glsl", fragmentShaderName));

        if (!vertexShaderFile.exists())
        {
            throw new Exception(String.format("Could not find the %s vertex shader", vertexShaderName));
        }
        if (!fragmentShaderFile.exists())
        {
            throw new Exception(String.format("Could not find the %s fragment shader", fragmentShaderName));
        }

        final String vertexShaderCode = loadFileAsString(vertexShaderFile);
        final String fragmentShaderCode = loadFileAsString(fragmentShaderFile);

        int vertexShaderId = createShaderAttachment(vertexShaderCode, vertexShaderName, GL_VERTEX_SHADER);
        int fragmentShaderId = createShaderAttachment(fragmentShaderCode, fragmentShaderName, GL_FRAGMENT_SHADER);

        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0)
        {
            throw new Exception(String.format("Error linking %s Shader program: %s", shaderProgramName, glGetProgramInfoLog(programId, 1024)));
        }

        if (vertexShaderId != 0)
        {
            glDetachShader(programId, vertexShaderId);
            glDeleteShader(vertexShaderId);
        }
        if (fragmentShaderId != 0)
        {
            glDetachShader(programId, fragmentShaderId);
            glDeleteShader(fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0)
        {
            throw new Exception(String.format("Warning validating %s shader program: %s", shaderProgramName, glGetShaderInfoLog(programId, 1024)));
        }

        uniforms = new HashMap<>();
        structures = new HashMap<>();

        autoCreateUniforms(vertexShaderCode);
        autoCreateUniforms(fragmentShaderCode);
    }

    private int createShaderAttachment(String shaderCode, String shaderName, int shaderTarget) throws Exception
    {
        String shaderTypeName;

        switch (shaderTarget) {
            case GL_VERTEX_SHADER:
                shaderTypeName = "Vertex";
                break;
            case GL_FRAGMENT_SHADER:
                shaderTypeName = "Fragment";
                break;
            default:
                shaderTypeName = "Unknown";
                break;
        }

        int shaderId = glCreateShader(shaderTarget);
        if (shaderId == 0)
        {
            throw new Exception(String.format("Error creating %s %s shader", shaderName, shaderTypeName));
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0)
        {
            throw new Exception(String.format("Error compiling %s %s shader: %s", shaderName, shaderTypeName, glGetShaderInfoLog(shaderId, 1024)));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void bind()
    {
        if (BOUND_SHADER == this)
        {
            return;
        }

        if (disposed)
        {
            throw new RuntimeException(String.format("Shader program: %s (%d) has been disposed", shaderProgramName, programId));
        }

        glUseProgram(programId);

        BOUND_SHADER = this;
    }

    @Override
    public boolean equals(Object o)
    {
        return o != null && getId() == ((ShaderProgram) o).getId();
    }

    public boolean isDisposed()
    {
        return disposed;
    }

    public int getId()
    {
        return programId;
    }

    @Override
    public void dispose()
    {
        glUseProgram(0);

        if (disposed)
        {
            throw new RuntimeException(String.format("Shader program: %s (%d) has already been disposed", shaderProgramName, programId));
        }

        glDeleteProgram(programId);
        disposed = true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final Pattern structBodyPattern = Pattern.compile("struct\\s+\\D\\w*\\s*\\{(\\s*\\D\\w*\\s+\\D\\w*\\s*;\\s*)+}\\s*;"); //TODO: handle unexpected '\n's
    private static final Pattern uniformShaderLinePattern = Pattern.compile("uniform\\s+\\D\\w*(\\[\\w+])?\\s+\\D\\w+\\s*;");

    private void autoCreateUniforms(String shaderCode) throws Exception {
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

            for (String component : bodyComponents) {
                tokens = component.split("\\s");
                String dataType = tokens[0];
                String variableName = tokens[1];
                variableDataTypeMap.put(variableName, dataType);
            }

            structures.put(head, variableDataTypeMap);
        }

        matcher = uniformShaderLinePattern.matcher(shaderCode);

        while (matcher.find()) {
            String uniformShaderLine = matcher.group();
            uniformShaderLine = uniformShaderLine.replaceAll("uniform\\s+", ""); //remove head
            String[] tokens = uniformShaderLine.split("\\s+"); //split
            String dataTypeComponent = tokens[0];
            String uniformName = tokens[1].substring(0, tokens[1].length() - 1); //remove semicolon

            String[] subComponents = dataTypeComponent.split("[\\[\\]]");
            String dataType = subComponents[0];

            switch (subComponents.length) {
                case 1:
                    createUniform(dataType, uniformName);
                    break;
                case 2:
                    String arraySizePointerString = subComponents[1];
                    if (arraySizePointerString.matches("\\d+")) {
                        createUniformArray(dataType, uniformName, Integer.parseInt(arraySizePointerString));
                    } else {
                        createUniformArray(dataType, uniformName, ReasourceLoader.getGLSLConstIntValue(arraySizePointerString, shaderCode));
                    }
                    break;
                default:
                    throw new Exception("Unsupported uniform decleration");
            }
        }
    }

    private void createUniformArray(String dataType, String uniformName, int size) throws Exception {
        for (int i = 0; i < size; i++) {
            createUniform(dataType, String.format("%s[%d]", uniformName, i));
        }
    }

    private void createUniform(String dataType, String uniformName) throws Exception {
        switch (dataType) {
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
                if (structures.containsKey(dataType)) {
                    HashMap<String, String> attributeMap = structures.get(dataType);
                    for (String attribute : attributeMap.keySet()) {
                        createUniform(attributeMap.get(attribute), String.format("%s.%s", uniformName, attribute));
                    }
                } else {
                    throw new Exception(String.format("Cannot create the %s uniform type", dataType));
                }
                break;
        }
    }

    private void createUniform(String name) {
        final int uniformLocation = glGetUniformLocation(programId, name);

        if (uniformLocation == 0xFFFFFFFF) {
            throw new RuntimeException(String.format("The uniform: '%s' for the '%s' shader program is not used", name, shaderProgramName));
        } else {
            uniforms.put(name, uniformLocation);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private int getUniformId(String uniformName)
    {
        if (uniforms.containsKey(uniformName))
        {
            return uniforms.get(uniformName);
        }
        else
        {
            throw new RuntimeException(String.format("The %s shader does not contain the %s uniform", shaderProgramName, uniformName));
        }
    }

    public void setUniform(String uniformName, boolean value)
    {
        bind();
        glUniform1i(getUniformId(uniformName), value ? 1 : 0);
    }

    public void setUniform(String uniformName, int value)
    {
        bind();
        glUniform1i(getUniformId(uniformName), value);
    }

    public void setUniform(String uniformName, float value)
    {
        bind();
        glUniform1f(getUniformId(uniformName), value);
    }

    public void setUniform(String uniformName, Vector2f vec)
    {
        bind();
        glUniform2f(getUniformId(uniformName), vec.getX(), vec.getY());
    }

    public void setUniform(String uniformName, Vector3f vec)
    {
        bind();
        glUniform3f(getUniformId(uniformName), vec.getX(), vec.getY(), vec.getZ());
    }

    public void setUniform(String uniformName, Vector4f vec)
    {
        bind();
        glUniform4f(getUniformId(uniformName), vec.getX(), vec.getY(), vec.getZ(), vec.getW());
    }

    public void setUniform(String uniformName, Matrix4f mat)
    {
        bind();
        glUniformMatrix4fv(getUniformId(uniformName), false, mat.toFloatBuffer());
    }

    public void setUniform(String uniformName, Color colour)
    {
        setUniform(uniformName, DataUtils.toVector3f(colour));
    }

    public void setUniform(String uniformName, Attenuation attenuation)
    {
        setUniform(uniformName + ".constant", attenuation.getConstant());
        setUniform(uniformName + ".linear", attenuation.getLinear());
        setUniform(uniformName + ".exponent", attenuation.getExponent());
    }

    public void setUniform(String uniformName, Light light)
    {
        setUniform(uniformName + ".colour", light.getColor());
        setUniform(uniformName + ".intensity", light.getIntensity());
        setUniform(uniformName + ".enabled", light.isEnabled());
    }

    public void setUniform(String uniformName, DirectionalLight directionalLight)
    {
        setUniform(uniformName + ".light", directionalLight.getLight());
        setUniform(uniformName + ".direction", directionalLight.getDirection());
    }

    public void setUniform(String uniformName, PointLightComponent pointLight)
    {
        setUniform(uniformName + ".light", pointLight.getLight());
        setUniform(uniformName + ".position", pointLight.getPosition());
        setUniform(uniformName + ".attenuation", pointLight.getAttenuation());
        setUniform(uniformName + ".range", pointLight.getRange());
    }

    public void setUniform(String uniformName, PointLightComponent[] pointLights)
    {
        for (int i = 0; i < pointLights.length; i++)
        {
            setUniform(String.format("%s[%d]", uniformName, i), pointLights[i]);
        }
    }

    public void setUniform(String uniformName, SpotLightComponent spotLight)
    {
        setUniform(uniformName + ".light", spotLight.getLight());
        setUniform(uniformName + ".position", spotLight.getPosition());
        setUniform(uniformName + ".attenuation", spotLight.getAttenuation());
        setUniform(uniformName + ".coneDirection", Vector3f.Multiply(Vector3f.Negative(World.FORWARD_VECTOR), spotLight.getRotation()));
        setUniform(uniformName + ".innerCutOff", (float) FastMath.cos(FastMath.toRadians(spotLight.getInnerCutOff())));
        setUniform(uniformName + ".outerCutOff", (float) FastMath.cos(FastMath.toRadians(spotLight.getOuterCutOff())));
    }

    public void setUniform(String uniformName, SpotLightComponent[] spotLights)
    {
        for (int i = 0; i < spotLights.length; i++)
        {
            setUniform(String.format("%s[%d]", uniformName, i), spotLights[i]);
        }
    }
}
