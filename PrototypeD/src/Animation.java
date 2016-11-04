import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class Animation {
    private int currentFrame; //�ثe�ʵe��Frame��
    private int totalFrames;  //�ʵe���`Frame��
    private boolean stopped;  //�M�w�ʵe�O�_����

    private List<Frame> frames = new ArrayList<Frame>();    //Arraylist of frames 
    //Ū���ʵe�Ϥ�
    public Animation(BufferedImage[] frames, int frameDelay) {
        this.stopped = true;

        for (int i = 0; i < frames.length; i++) {
            addFrame(frames[i], frameDelay);
        }
        this.currentFrame = 0;
        this.totalFrames = this.frames.size();
    }
    //�}�l�ʵe
    public void start() {
        if (!stopped) {
            return;
        }

        if (frames.size() == 0) {
            return;
        }

        stopped = false;
    }
    //����ʵe
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
    
    //���m�ʵe�A�ثe��Frame�Ʀ^��0
    public void reset() {
        this.stopped = true;
        this.currentFrame = 0;
    }
    //�[�J�s���ʵe�Ϥ���Arraylist
    private void addFrame(BufferedImage frame, int duration) {
        if (duration <= 0) {
            System.err.println("Invalid duration: " + duration);
            throw new RuntimeException("Invalid duration: " + duration);
        }

        frames.add(new Frame(frame, duration));
        currentFrame = 0;
    }
    //�^�ǥثe�ʵe��frame�Ϥ�
    public BufferedImage getSprite() {
        return frames.get(currentFrame).getFrame();
    }

    public void update() {
    	//�ʵe�S�����ܡA�ثe�ʵe��Frame��+1
        if (!stopped) {
            currentFrame = (currentFrame + 1) % totalFrames;
        }
    }
}