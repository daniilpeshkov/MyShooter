package Game.Logic;

public class TexturedEntity extends Entity {

    protected int textureID;

    public TexturedEntity(float x, float y, float r, int textureID) {
        super(x, y, r);
        this.textureID = textureID;
        this.getCore()[0] = (byte) textureID;
    }

    public int getTextureID() {
        return textureID;
    }

    @Override
    public void update(int dTime) {

    }

    @Override
    public void collidesWith(Entity entity) {

    }
}