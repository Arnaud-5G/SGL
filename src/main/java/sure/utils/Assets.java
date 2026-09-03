package sure.utils;

import sure.renderers.Shader;
import sure.renderers.Sprites.Sprite;
import sure.renderers.Sprites.SpriteSheet;
import sure.sound.Sound;

import java.util.HashMap;
import java.util.Map;

public class Assets {
    private static Map<String, Shader> shaders = new HashMap<String, Shader>();
    private static Map<String, SpriteSheet> spriteSheets = new HashMap<String, SpriteSheet>();
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
        return spriteSheets.get("src/main/java/sure/assets/default_font.png");
    }

    public static Sprite getSprite(String filepath) {
        if (spriteSheets.containsKey(filepath) && spriteSheets.get(filepath) instanceof Sprite) {
            return (Sprite) spriteSheets.get(filepath);
        } else {
            Sprite sprite = new Sprite(filepath);
            spriteSheets.put(filepath, sprite);
            return sprite;
        }
    }

    public static SpriteSheet getSpriteSheet(String filepath, int widthPerSprite, int heightPerSprite) {
        if (spriteSheets.containsKey(filepath)) {
            return spriteSheets.get(filepath);
        } else {
            SpriteSheet spriteSheet = new SpriteSheet(filepath, widthPerSprite, heightPerSprite);
            spriteSheets.put(filepath, spriteSheet);
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
