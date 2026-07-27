package sure.utils;

import sure.renderers.Shader;
import sure.renderers.Sprites.Sprite;
import sure.renderers.Sprites.SpriteSheet;
import sure.renderers.Texture;
import sure.sound.Sound;

import java.util.HashMap;
import java.util.Map;

public class Assets {
    private static Map<String, Shader> shaders = new HashMap<String, Shader>();
    private static Map<String, SpriteSheet> textures = new HashMap<String, SpriteSheet>();
    private static Map<String, Sound> sounds = new HashMap<String, Sound>();

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

    public static SpriteSheet getDefaultFont() {
        return textures.get("src/main/java/sure/assets/default_font.png");
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

    public static Sound getSound(String filepath) {
        return getSound(filepath, false);
    }

    public static Sound getSound(String filepath, boolean looping) {
        if (sounds.containsKey(filepath)) {
            return sounds.get(filepath);
        }  else {
            Sound sound = new Sound(filepath, looping);
            sounds.put(filepath, sound);
            return sound;
        }
    }
}
