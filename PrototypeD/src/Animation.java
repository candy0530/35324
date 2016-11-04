import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class Animation {
    private int currentFrame; //目前動畫的Frame數
    private int totalFrames;  //動畫的總Frame數
    private boolean stopped;  //決定動畫是否停止

    private List<Frame> frames = new ArrayList<Frame>();    //Arraylist of frames 
    //讀取動畫圖片
    public Animation(BufferedImage[] frames, int frameDelay) {
        this.stopped = true;

        for (int i = 0; i < frames.length; i++) {
            addFrame(frames[i], frameDelay);
        }
        this.currentFrame = 0;
        this.totalFrames = this.frames.size();
    }
    //開始動畫
    public void start() {
        if (!stopped) {
            return;
        }

        if (frames.size() == 0) {
            return;
        }

        stopped = false;
    }
    //停止動畫
    public void stop() {
        if (frames.size() == 0) {
            return;
        }

        stopped = true;
    }

    /*public void restart() {
        if (frames.size() == 0) {
            return;
        }

        stopped = false;
        currentFrame = 0;
    }*/
    
    //重置動畫，目前的Frame數回到0
    public void reset() {
        this.stopped = true;
        this.currentFrame = 0;
    }
    //加入新的動畫圖片到Arraylist
    private void addFrame(BufferedImage frame, int duration) {
        if (duration <= 0) {
            System.err.println("Invalid duration: " + duration);
            throw new RuntimeException("Invalid duration: " + duration);
        }

        frames.add(new Frame(frame, duration));
        currentFrame = 0;
    }
    //回傳目前動畫的frame圖片
    public BufferedImage getSprite() {
        return frames.get(currentFrame).getFrame();
    }

    public void update() {
    	//動畫沒停的話，目前動畫的Frame數+1
        if (!stopped) {
            currentFrame = (currentFrame + 1) % totalFrames;
        }
    }
}