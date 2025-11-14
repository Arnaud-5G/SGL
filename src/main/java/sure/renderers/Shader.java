package sure.renderers;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int shaderID;
    private boolean inUse = false;

    private String vertexShaderSrc;
    private String fragmentShaderSrc;

    private String filepath;

    public Shader(String filepath) {
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] shaders =  source.split("#type([a-zA-Z ]+)");

            int index = 0;
            int i = 1;
            while(source.indexOf("#type", index) != -1) {
                index = source.indexOf("#type", index) + "#type".length();
                int eol = source.indexOf("\r\n", index);
                String shaderName = source.substring(index, eol).trim();

                if (shaderName.equals("vertex")) {
                    vertexShaderSrc = shaders[i];
                } else if (shaderName.equals("fragment")) {
                    fragmentShaderSrc = shaders[i];
                } else { // TODO: change later
                    throw new IOException("Unexpected  shader name: " + shaderName);
                }

                index = eol;
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not open file for shader: '" + filepath + "'");
            System.exit(1);
        }
    }

    public void compile() {
        int vertexID,  fragmentID;

        // vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);

        // Check for errors
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int info = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tVertext Shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, info));
            System.exit(1);
        }

        // fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentShaderSrc);
        glCompileShader(fragmentID);

        // Check for errors
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int info = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tFragment Shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, info));
            System.exit(1);
        }

        // link shaders
        shaderID = glCreateProgram();
        glAttachShader(shaderID, vertexID);
        glAttachShader(shaderID, fragmentID);
        glLinkProgram(shaderID);

        // Check for errors
        success = glGetProgrami(shaderID, GL_LINK_STATUS);
        if (success == GL_FALSE){
            int info = glGetProgrami(shaderID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tShader Link failed.");
            System.out.println(glGetProgramInfoLog(shaderID, info));
            System.exit(1);
        }
    }

    public void use() {
        if (!inUse) {
            glUseProgram(shaderID);
            inUse = true;
        }
    }

    public void detach() {
        glUseProgram(0);
        inUse = false;
    }

    public void uploadMath4f(String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(shaderID, varName);
        this.use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMath3f(String varName, Matrix3f mat3) {
        int varLocation = glGetUniformLocation(shaderID, varName);
        this.use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void uploadVec4f(String varName, Vector4f vec4f) {
        int varLocation = glGetUniformLocation(shaderID, varName);
        this.use();
        glUniform4f(varLocation, vec4f.x, vec4f.y, vec4f.z, vec4f.w);
    }

    public void uploadVec3f(String varName, Vector3f vec3f) {
        int varLocation = glGetUniformLocation(shaderID, varName);
        this.use();
        glUniform3f(varLocation, vec3f.x, vec3f.y, vec3f.z);
    }

    public void uploadVec2f(String varName, Vector2f vec2f) {
        int varLocation = glGetUniformLocation(shaderID, varName);
        this.use();
        glUniform2f(varLocation, vec2f.x, vec2f.y);
    }

    public void uploadFloat(String varName, float val) {
        int varLocation = glGetUniformLocation(shaderID, varName);
        this.use();
        glUniform1f(varLocation, val);
    }

    public void uploadInt(String varName, int val) {
        int varLocation = glGetUniformLocation(shaderID, varName);
        this.use();
        glUniform1i(varLocation, val);
    }

    public void uploadTexture(String varName, int slot) {
        int varLocation = glGetUniformLocation(shaderID, varName);
        this.use();
        glUniform1i(varLocation, slot);
    }
}
