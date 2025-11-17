package sure.utils;

import sure.renderers.Shader;
import sure.renderers.Texture;

import java.util.HashMap;
import java.util.Map;

public class Assets {
    private static Map<String, Shader> shaders = new HashMap<String, Shader>();
    private static Map<String, Texture> textures = new HashMap<String, Texture>();

    public static Shader getShader(String filepath) {
        if (shaders.containsKey(filepath)) {
            return shaders.get(filepath);
        } else {
            Shader shader = new Shader(filepath);
            shader.compile();
            shaders.put(filepath, shader);
            return shader;
        }
    }

    public static Texture getTexture(String filepath) {
        if (textures.containsKey(filepath)) {
            return textures.get(filepath);
        } else {
            Texture texture = new Texture(filepath);
            textures.put(filepath, texture);
            return texture;
        }
    }
}
