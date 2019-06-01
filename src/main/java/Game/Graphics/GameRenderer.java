package Game.Graphics;

import Game.Logic.Entity;
import Game.Logic.TexturedEntity;
import org.joml.Matrix3f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class GameRenderer {

    public static void renderEntity(TexturedEntity entity, Vector3f cameraPos) {


         Matrix3f rotation_matrix = new Matrix3f();
         rotation_matrix.rotate(entity.getFi(), 0,0,1,rotation_matrix);

         Vector3f pos = entity.getPos();

         Vector3f p1 = Camera.transform(entity.getPos(), cameraPos, new Vector3f(pos.x - entity.getR()/2.0f, pos.y + entity.getR()/2.0f, 0),rotation_matrix);
         Vector3f p2 = Camera.transform(entity.getPos(), cameraPos, new Vector3f(pos.x + entity.getR()/2.0f, pos.y + entity.getR()/2.0f, 0),rotation_matrix);
         Vector3f p3 = Camera.transform(entity.getPos(), cameraPos, new Vector3f(pos.x + entity.getR()/2.0f, pos.y - entity.getR()/2.0f, 0),rotation_matrix);
         Vector3f p4 = Camera.transform(entity.getPos(), cameraPos, new Vector3f(pos.x - entity.getR()/2.0f, pos.y - entity.getR()/2.0f, 0),rotation_matrix);

         glShadeModel(GL_SMOOTH);

         glColor3f(1f, 1.0f, 1f);

         glEnable(GL_TEXTURE_2D);
         entity.bindTexture();
         glBegin(GL_QUADS);

         glTexCoord2f(0,0);
         glVertex2f(p1.x, p1.y);

         glTexCoord2f(0,1);
         glVertex2f(p2.x, p2.y);

         glTexCoord2f(1,1);
         glVertex2f(p3.x, p3.y);

         glTexCoord2f(1,0);
         glVertex2f(p4.x, p4.y);

         glEnd();

         glDisable(GL_TEXTURE_2D);
    }

}
