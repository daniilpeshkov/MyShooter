package Game.Graphics;

public class Animation {

    Texture[] textures;

    int millisPerFrame;
    int currentFrameID = 0;
    int passedTime = 0;

    boolean isPlaying = false;

    public Animation(Texture[] textures, int millis_per_cycle) {
        this.textures = textures;
        millisPerFrame = millis_per_cycle / textures.length;
    }

    public void update(int dTime) {
        passedTime += dTime;
        if (passedTime > millisPerFrame) {
            currentFrameID += 1;
            if (currentFrameID == textures.length) {
                currentFrameID = 0;
            }
            passedTime -= millisPerFrame;
        }

    }

    public void bind() {
        textures[currentFrameID].bind();
    }

    public void reset() {
        currentFrameID = 0;
        passedTime = 0;
    }

}