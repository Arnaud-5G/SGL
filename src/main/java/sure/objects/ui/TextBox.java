package sure.objects.ui;

import sure.objects.GameObject;
import sure.objects.Rectangle;
import sure.renderers.Sprites.SpriteSheet;
import sure.utils.Assets;

import java.util.ArrayList;

public class TextBox extends GameObject {
    public final float HEIGHT = 20;
    public final float WIDTH = 20;

    SpriteSheet font;
    final ArrayList<Character> characters = new ArrayList<Character>();
    float scale = 1;

    public float x;
    public float y;
    public float zIndex;

    public TextBox(float x, float y, float zIndex) {
        this.font = Assets.getDefaultFont();
        this.x = x;
        this.y = y;
        this.zIndex = zIndex;
    }

    public TextBox(SpriteSheet font, float x, float y, float zIndex) {
        this.font = font;
        this.x = x;
        this.y = y;
        this.zIndex = zIndex;
    }

    public void scale(float scale) {
        this.scale = scale;
        for (Character character : characters) {
            character.height = HEIGHT * scale;
            character.width = WIDTH * scale;
        }
    }

    public void write(String text) {
        for(char c : text.toCharArray()) {
            characters.add(new Character(x + (WIDTH*scale)/2 + characters.size()*WIDTH*scale, y + (HEIGHT*scale)/2, HEIGHT*scale, WIDTH*scale, zIndex, c, font));
        }
    }

    public void write(char c) {
        characters.add(new Character(x + (WIDTH*scale)/2 + characters.size()*WIDTH*scale, y + (HEIGHT*scale)/2, HEIGHT*scale, WIDTH*scale, zIndex, c, font));
    }

    public void backspace() {
        if (characters.size() > 0) {
            characters.getLast().delete();
            characters.remove(characters.getLast());
        }
    }

    public void set(String text) {
        clear();
        write(text);
    }

    public void clear() {
        for (Character character : characters) {
            character.delete();
        }

        characters.clear();
    }

    private static class Character extends Rectangle {
        private Character(float x, float y, float height, float width, float zIndex, char character, SpriteSheet font) {
            super(x, y, height, width, zIndex, font.get(getFontIndex(character, font)));
        }

        private static int getFontIndex(char character, SpriteSheet font) {
            int index;
            try {
                index = character - ' ';
                font.get(index);
                return index;
            } catch (Exception e) {
                e.printStackTrace();
                return '`' - ' ';
            }
        }
    }

    @Override
    public void delete() {
        super.delete();
        clear();
    }
}
