package Sprite;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class Animation {
    private int currentFrame; 
    private int totalFrames;  
    private boolean stopped;  

    private ArrayList<BufferedImage> frames = new ArrayList<BufferedImage>();    
    
    public Animation(BufferedImage[] frames) {
        this.stopped = true;

        for (int i = 0; i < frames.length; i++) {
            addFrame(frames[i]);
        }
        this.currentFrame = 0;
        this.totalFrames = this.frames.size();
    }
    
    public void changeAnimation(BufferedImage[] frames) {
        this.stopped = true;
        this.frames.clear();
        for (int i = 0; i < frames.length; i++) {
            addFrame(frames[i]);
        }
        this.currentFrame = 0;
        this.totalFrames = this.frames.size();
    }
    
    public void start() {
        if (!stopped) {
            return;
        }

        if (frames.size() == 0) {
            return;
        }

        stopped = false;
    }
    
    public void stop() {
        if (frames.size() == 0) {
            return;
        }

        stopped = true;
    }
    
    public void reset() {
        this.stopped = true;
        this.currentFrame = 0;
    }
    
    private void addFrame(BufferedImage frame) {
        frames.add(frame);
        currentFrame = 0;
    }
   
    public BufferedImage getSprite() {
        return frames.get(currentFrame);
    }

    public void update() {
        if (!stopped) {
            currentFrame = (currentFrame + 1) % totalFrames;
            //System.out.println("currentFrame " + currentFrame);
        }
    }
}