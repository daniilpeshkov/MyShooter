package Game.Graphics;

import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {


    public static float scale = 1.0f;

    public static float d_scale = 1.05f;

    public static float view_x = 0, view_y = 0;

    public static float MIN_VIEW = 10.0f;

    public static float aspectRatio = 0;

    public static float MIN_SCALE = 0.5f;

    public static float MAX_SCALE = 2f;

    public static void zoomOut() {
        if (scale * d_scale <= MAX_SCALE) {
            scale *= d_scale;
        }
    }

    public static void zoomIn() {
        if (scale / d_scale >= MIN_SCALE) {
            scale /= d_scale;
        }
    }

    public static void updateAspectRatio(float w, float h) {
        aspectRatio = w / h;
        view_y = MIN_VIEW;
        view_x = MIN_VIEW * aspectRatio;
    }

    public static float getAspectRatio() {
        return aspectRatio;
    }

    public static Vector3f transform(Vector3f cam_vec, Vector3f pos_vec) {

        return new Vector3f((pos_vec.x - cam_vec.x) / view_x / scale, (pos_vec.y - cam_vec.y) / view_y / scale, 0);
    }

    public static Vector3f transform(Vector3f center, Vector3f cam_vec, Vector3f pos_vec, Matrix3f rotation_matrix) {
        if (rotation_matrix != null) {
            pos_vec.x -= center.x;
            pos_vec.y -= center.y;
            rotation_matrix.transform(pos_vec, pos_vec);
            pos_vec.x += center.x;
            pos_vec.y += center.y;
        }
        return transform(cam_vec, pos_vec);
    }

}
