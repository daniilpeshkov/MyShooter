package Game.Logic;

abstract public class TexturedEntity extends Entity {

    protected int textureID;

    public TexturedEntity(float x, float y, float r, int textureID) {
        super(x, y, r);
        this.textureID = textureID;
    }

    public int getTextureID() {
        return textureID;
    }


}