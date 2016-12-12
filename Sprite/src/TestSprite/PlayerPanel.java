package TestSprite;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import SpriteRE.*;

public class PlayerPanel extends JPanel implements KeyListener {

    ArrayList<AnimationPlayer> playerAnimation = new ArrayList<AnimationPlayer>();
    ArrayList<Point> location = new ArrayList<Point>();
    Thread thread;

    public PlayerPanel() {
        setLayout(null);
        this.addKeyListener(this);
        setBounds(0, 0, 540, 540);
        setFocusable(true);
        addAllPlayer();
        thread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(42);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    update();
                }
            }
        });
        thread.start();
    }

    public void addAllPlayer() {
        playerAnimation.add(new AnimationPlayer("red"));
        /*
         * playerAnimation.add(new Player("yellow")); playerAnimation.add(new
         * Player("green")); playerAnimation.add(new Player("blue"));
         * playerAnimation.add(new Player("cyan")); playerAnimation.add(new
         * Player("purple"));
         */

        location.add(new Point(270, 270));
        location.add(new Point(100, 100));
    }

    public void paint(Graphics g) {
        g.clearRect(0, 0, 540, 540);
        for (int i = 0; i < playerAnimation.size(); i++) {
            g.drawImage(playerAnimation.get(i).getSprite(), location.get(i).x, location.get(i).y, null);
        }
    }

    public void update() {
        for (int i = 0; i < playerAnimation.size(); i++) {
            playerAnimation.get(i).updateAnimation();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        // thread.resume();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                location.get(0).move(location.get(0).x - 10, location.get(0).y);
                playerAnimation.get(0).animationMove(new Point(-10, 0));
                break;
            case KeyEvent.VK_UP:
                location.get(0).move(location.get(0).x, location.get(0).y - 10);
                playerAnimation.get(0).animationMove(new Point(0, -10));
                break;
            case KeyEvent.VK_RIGHT:
                location.get(0).move(location.get(0).x + 10, location.get(0).y);
                playerAnimation.get(0).animationMove(new Point(10, 0));
                break;
            case KeyEvent.VK_DOWN:
                location.get(0).move(location.get(0).x, location.get(0).y + 10);
                playerAnimation.get(0).animationMove(new Point(0, 10));
                break;
            // thief was caught by police
            case KeyEvent.VK_SPACE:
                playerAnimation.get(0).animationPolice();
                break;
            // police catch thief
            case KeyEvent.VK_1:
                playerAnimation.get(0).animationPlayer();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        playerAnimation.get(0).stopAnimation();
        // thread.suspend();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }
}
