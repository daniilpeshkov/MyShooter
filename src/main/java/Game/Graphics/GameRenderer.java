package Game.Graphics;

import Game.Logic.TexturedEntity;
import org.joml.Matrix3f;
import org.joml.Vector3f;
import java.io.File;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class GameRenderer {

   public static Map<Integer, Texture> textureMap;

   public static void loadTextures() {
      File texturePath = new File("src\\main\\resources");
      textureMap = new HashMap();
      for (File a : texturePath.listFiles()) {
         String name = a.getName();
         int number = Integer.valueOf(name.substring(0, name.indexOf('.')));
         textureMap.put(number, Texture.genTexture(a.getAbsolutePath()));
         System.out.println("Texture " + number + " loaded");
      }
   }

   public static void bindTexture(int id) {
      textureMap.get(id).bind();
   }

   public static void renderEntity(TexturedEntity entity, Vector3f cameraPos) {
      Matrix3f rotation_matrix = new Matrix3f();
      rotation_matrix.rotate(entity.getFi(), 0, 0, 1, rotation_matrix);
      Vector3f pos = entity.getPos();

      Vector3f p1 = Camera.transform(entity.getPos(), cameraPos, new Vector3f(pos.x - entity.getR() / 2.0f, pos.y + entity.getR() / 2.0f, 0), rotation_matrix);
      Vector3f p2 = Camera.transform(entity.getPos(), cameraPos, new Vector3f(pos.x + entity.getR() / 2.0f, pos.y + entity.getR() / 2.0f, 0), rotation_matrix);
      Vector3f p3 = Camera.transform(entity.getPos(), cameraPos, new Vector3f(pos.x + entity.getR() / 2.0f, pos.y - entity.getR() / 2.0f, 0), rotation_matrix);
      Vector3f p4 = Camera.transform(entity.getPos(), cameraPos, new Vector3f(pos.x - entity.getR() / 2.0f, pos.y - entity.getR() / 2.0f, 0), rotation_matrix);

      glShadeModel(GL_SMOOTH);

      glColor3f(1f, 1.0f, 1f);

      glEnable(GL_TEXTURE_2D);
      bindTexture(entity.getTextureID());
      glBegin(GL_QUADS);

      glTexCoord2f(0, 0);
      glVertex2f(p1.x, p1.y);

      glTexCoord2f(0, 1);
      glVertex2f(p2.x, p2.y);

      glTexCoord2f(1, 1);
      glVertex2f(p3.x, p3.y);

      glTexCoord2f(1, 0);
      glVertex2f(p4.x, p4.y);

      glEnd();

      glDisable(GL_TEXTURE_2D);
   }

   static float HEART_SIZE = 0.1f;
   static float CURSOR_SIZE = 0.01f;

   public static void renderHUD(float x, float y, float hp) {
      GameRenderer.bindTexture(6);
      for (int i = 0; i < hp; i++) {

         glEnable(GL_TEXTURE_2D);
         glBegin(GL_QUADS);

         glTexCoord2f(0, 0);
         glVertex2f(-1 + i * HEART_SIZE / Camera.getAspectRatio(), 1);

         glTexCoord2f(0, 1);
         glVertex2f(-1 + (i + 1) * HEART_SIZE / Camera.getAspectRatio(), 1);

         glTexCoord2f(1, 1);
         glVertex2f(-1 + (i + 1) * HEART_SIZE / Camera.getAspectRatio(), 1 - HEART_SIZE);

         glTexCoord2f(1, 0);
         glVertex2f(-1 + i * HEART_SIZE / Camera.getAspectRatio(), 1 - HEART_SIZE);
         glEnd();
         glPopMatrix();
         glDisable(GL_TEXTURE_2D);
      }

      GameRenderer.bindTexture(3);

      glEnable(GL_TEXTURE_2D);
      glBegin(GL_QUADS);
      glTexCoord2f(0, 0);
      glVertex2f(x - CURSOR_SIZE / Camera.getAspectRatio(), y + CURSOR_SIZE);

      glTexCoord2f(0, 1);
      glVertex2f(x + CURSOR_SIZE / Camera.getAspectRatio(), y + CURSOR_SIZE);

      glTexCoord2f(1, 1);
      glVertex2f(x + CURSOR_SIZE / Camera.getAspectRatio(), y - CURSOR_SIZE);

      glTexCoord2f(1, 0);
      glVertex2f(x - CURSOR_SIZE / Camera.getAspectRatio(), y - CURSOR_SIZE);

      glEnd();
      glDisable(GL_TEXTURE_2D);
   }

}
