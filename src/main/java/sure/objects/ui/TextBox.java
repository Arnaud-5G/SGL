package sure.objects.ui;

import sure.objects.GameObject;
import sure.objects.Rectangle;
import sure.renderers.Sprites.SpriteSheet;
import sure.utils.Assets;

import java.util.ArrayList;
import java.util.List;

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

    protected String text = "";

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

    public void scale(float scale) { // TODO: fix
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
                    newline(characters.size());
                    continue;
            }
            write(c);
        }
    }

    public void write(char c) {
        write(c, characters.size());
    }

    public void write(char c, int index) {
        if (c == '\n') {
            newline(index);
            return;
        }

        String temp = gatherAllCharsAfter(index);
        if (!characters.isEmpty() && index > 0) {
            characters.add(index, new Character(characters.get(index-1).x + WIDTH*scale, characters.get(index-1).y, HEIGHT*scale, WIDTH*scale, zIndex, c, font));
        } else {
            characters.add(new Character(x + (WIDTH*scale)/2, y + (HEIGHT*scale)/2, HEIGHT*scale, WIDTH*scale, zIndex, c, font));
        }

        text = text.substring(0, (index < characters.size() ? index : 0));
        text += c;
        write(temp);
    }

    public void backspace(int index) {
        if (!characters.isEmpty() && index > 0) {
            String temp = gatherAllCharsAfter(index);
            characters.get(index-1).delete();
            characters.remove(index-1);
            text = text.substring(0, index-1) + (index < text.length() ? text.substring(index) : "");
            write(temp);
        }
    }

    public void tab() {
        write("    ");
    }

    public void newline(int index) {
        String temp = gatherAllCharsAfter(index);
        if (!characters.isEmpty() && index > 0) {
            characters.add(index, new Character(x - WIDTH*scale/2, characters.getLast().y - HEIGHT*scale, HEIGHT*scale, 0, zIndex, '\n', font));

        } else {
            characters.add(new Character(x - WIDTH*scale/2, y - HEIGHT*scale/2, HEIGHT*scale, 0, zIndex, '\n', font));
        }

        text = text.substring(0, index) + '\n' + text.substring(index);
        write(temp);
    }

    /**
     * Removes and collects all Characters after the specified index.
     * @param index
     * @return a String containing all characters after a certain index in the list
     */
    protected String gatherAllCharsAfter(int index) {
        String temp = "";
        if (index < characters.size()) {
            temp = text.substring(index);
            List<Character> subList = characters.subList(index, characters.size());
            for (Character character : subList) {
                character.delete();
            }

            characters.removeAll(subList);
        }

        return temp;
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
        text = "";
    }

    public String getText() {
        return text;
    }

    protected static class Character extends Rectangle {
        protected final char c;

        protected Character(float x, float y, float height, float width, float zIndex, char character, SpriteSheet font) {
            super(x, y, height, width, zIndex, font.get(getFontIndex(character, font)));
            this.c = character;
        }

        protected static int getFontIndex(char character, SpriteSheet font) {
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
