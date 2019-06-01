package Game.Graphics;


import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;

import static jdk.xml.internal.SecuritySupport.getResourceAsStream;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_BGR;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Texture {
    private int id;
    private int width;
    private int height;

    public Texture() {
        id = 0;
    }
    public Texture(int id) {
        this.id = id;
    }

    public static Texture genTexture(String fileName) {
        //load png file
        PNGDecoder decoder = null;
        try {
            decoder = new PNGDecoder(new FileInputStream(new File(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create a byte buffer big enough to store RGBA values
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());

        //decode
        try {
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //flip the buffer so its ready to read
        buffer.flip();

        //create a texture
        int id = glGenTextures();

        //bind the texture
        glBindTexture(GL_TEXTURE_2D, id);

        //tell opengl how to unpack bytes
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        //set the texture parameters, can be GL_LINEAR or GL_NEAREST
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        //upload texture
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        // Generate Mip Map
        glGenerateMipmap(GL_TEXTURE_2D);

        return new Texture(id);
    }

    @Deprecated
    public Texture(String fileName) {
        BufferedImage bi;
        try {
            bi = ImageIO.read(new File(fileName));
            width = bi.getWidth();
            height = bi.getHeight();
            ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

            for (int x = 0; x <width ; x++) {
                for (int y = 0; y < height; y++) {
                    Color c = new Color(bi.getRGB(x, y), true);
                    pixels.put((byte) (c.getRed() - 128));
                    pixels.put((byte) (c.getGreen() - 128));
                    pixels.put((byte) (c.getBlue() - 128));
                    pixels.put((byte) (c.getAlpha() - 128));
                }
            }

            pixels.flip();

            id = glGenTextures();

            glBindTexture(GL_TEXTURE_2D, id);

            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_BYTE, pixels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
