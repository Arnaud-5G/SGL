package sure.renderers.Sprites;

import org.lwjgl.BufferUtils;
import sure.renderers.Texture;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.stb.STBImage.*;

public class SpriteSheet {
    String filepath;
    int textureID;
    Texture[] textures;

    public SpriteSheet(String filepath, int widthPerSprite, int heightPerSprite) {
        this.filepath = filepath;

        this.textureID = glGenTextures()-1;
        System.out.println(textureID);
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        // pixelate when stretching and shrinking
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

        if (image == null) {
            throw new RuntimeException("Failed to load texture: '" + filepath + "'");
        }

        if (channels.get(0) == 3) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
        } else if (channels.get(0) == 4) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
        } else {
            throw new RuntimeException("Texture: '" + filepath + "' in unknown format. Number of color channels: " + channels.get(0));
        }

        if (widthPerSprite == -1 && heightPerSprite == -1) { // is a Sprite
            textures = new Texture[1];
            textures[0] = new Texture(textureID, new float[][]{
                    {0, 0},
                    {0, 1},
                    {1, 1},
                    {1, 0},
            });
        } else {
            generateTextures(width.get(0), widthPerSprite, height.get(0), heightPerSprite);
        }

        stbi_image_free(image);
    }

    private void generateTextures(int width, int widthPerSprite, int height, int heightPerSprite) {
        if (height % heightPerSprite != 0 || width % widthPerSprite != 0 || widthPerSprite <= 0 || heightPerSprite <= 0) {
            throw new IllegalStateException("SpriteSheet arguments are not valid");
        }
        int numOfRows = width / widthPerSprite;
        int numOfColumns = height / heightPerSprite;
        float uRange = (float) widthPerSprite / width;
        float vRange = (float) heightPerSprite / height;
        textures = new Texture[numOfRows * numOfColumns];
        for (int i = 0; i < numOfColumns; i++) {
            for (int j = 0; j < numOfRows; j++) {
                textures[(numOfColumns-1-i)*numOfRows + j] = new Texture(textureID, new float[][]{
                        {(j+0)*uRange, (0-i)*vRange},
                        {(j+0)*uRange, (1-i)*vRange},
                        {(j+1)*uRange, (1-i)*vRange},
                        {(j+1)*uRange, (0-i)*vRange},
                });
            }
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public Texture get(int index) {return textures[index];}
}
