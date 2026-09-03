package sure.objects.ui;

import sure.objects.GameObject;
import sure.objects.Rectangle;
import sure.renderers.Sprites.SpriteSheet;
import sure.utils.Assets;

import java.util.ArrayList;

public class TextBox extends GameObject {
    public final float HEIGHT = 20;
    public final float WIDTH = 20;

    public static final char NULL_CHAR = '`' - ' ';

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
            switch (c) {
                case '\t' :
                    tab();
                    continue;
                case '\n' :
                    newline();
                    continue;
            }
            write(c);
        }
    }

    public void write(char c) {
        if (characters.size() > 0) {
            characters.add(new Character(characters.getLast().x + WIDTH*scale, characters.getLast().y, HEIGHT*scale, WIDTH*scale, zIndex, c, font));
        } else {
            characters.add(new Character(x + (WIDTH*scale)/2, y + (HEIGHT*scale)/2, HEIGHT*scale, WIDTH*scale, zIndex, c, font));
        }
    }

    public void backspace() {
        if (characters.size() > 0) {
            characters.getLast().delete();
            characters.remove(characters.getLast());
        }
    }

    public void tab() {
        write("    ");
    }

    public void newline() {
        if (characters.size() > 0) {
            characters.add(new Character(x - WIDTH*scale/2, characters.getLast().y - HEIGHT*scale, HEIGHT*scale, 0, zIndex, '\n', font));
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
        private final char c;

        private Character(float x, float y, float height, float width, float zIndex, char character, SpriteSheet font) {
            super(x, y, height, width, zIndex, font.get(getFontIndex(character, font)));
            this.c = character;
        }

        private static int getFontIndex(char character, SpriteSheet font) {
            int index;
            try {
                if (character == '\n') {
                    return NULL_CHAR;
                }
                index = character - ' ';
                font.get(index);
                return index;
            } catch (Exception e) {
                e.printStackTrace();
                return NULL_CHAR;
            }
        }
    }

    @Override
    public void delete() {
        super.delete();
        clear();
    }
}
