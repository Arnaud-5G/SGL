package sure.utils;

import sure.renderers.Shader;
import sure.renderers.Sprites.Sprite;
import sure.renderers.Sprites.SpriteSheet;
import sure.renderers.Texture;

import java.util.HashMap;
import java.util.Map;

public class Assets {
    private static Map<String, Shader> shaders = new HashMap<String, Shader>();
    private static Map<String, SpriteSheet> textures = new HashMap<String, SpriteSheet>();

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

    public static Texture getTexture(String filepath, int index) {
        if (textures.containsKey(filepath)) {
            return textures.get(filepath).get(index);
        } else {
            return null;
        }
    }

    public static Sprite getSprite(String filepath) {
        if (textures.containsKey(filepath) && textures.get(filepath) instanceof Sprite) {
            return (Sprite) textures.get(filepath);
        } else {
            Sprite sprite = new Sprite(filepath);
            textures.put(filepath, sprite);
            return sprite;
        }
    }

    public static SpriteSheet getSpriteSheet(String filepath, int widthPerSprite, int heightPerSprite) {
        if (textures.containsKey(filepath)) {
            return textures.get(filepath);
        } else {
            SpriteSheet spriteSheet = new SpriteSheet(filepath, widthPerSprite, heightPerSprite);
            textures.put(filepath, spriteSheet);
            return spriteSheet;
        }
    }
}
