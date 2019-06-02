package Game.Logic;

import Game.Graphics.Texture;

abstract public class TexturedEntity extends Entity {

    protected Texture texture;

    public TexturedEntity(float x, float y, float r, Texture texture) {
        super(x, y, r);
        this.texture = texture;
    }

    public void bindTexture() {
        texture.bind();
    }
}