package sure.basepackage.renderers.Sprites;

import sure.basepackage.renderers.Texture;

public class Sprite extends SpriteSheet {
    public Sprite(String filepath) {
        super(filepath, -1, -1);
    }

    public Texture get() {
        return textures[0];
    }
}
