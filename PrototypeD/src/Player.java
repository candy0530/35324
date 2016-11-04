import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;


public class Player extends JFrame implements KeyListener{
	
	//每個方向和靜止的角色動畫圖片
	private BufferedImage[] walkingDown = {Sprite.getSprite(0, 0), Sprite.getSprite(1, 0), Sprite.getSprite(2, 0), Sprite.getSprite(3, 0)}; // Gets the upper left images of my sprite sheet
	private BufferedImage[] walkingLeft = {Sprite.getSprite(0, 1), Sprite.getSprite(1, 1), Sprite.getSprite(2, 1), Sprite.getSprite(3, 1)};
	private BufferedImage[] walkingRight = {Sprite.getSprite(0, 2), Sprite.getSprite(1, 2), Sprite.getSprite(2, 2), Sprite.getSprite(3, 2)};
	private BufferedImage[] walkingUp = {Sprite.getSprite(0, 3), Sprite.getSprite(1, 3), Sprite.getSprite(2, 3), Sprite.getSprite(3, 3)};
	private BufferedImage[] standing1 = {Sprite.getSprite(0, 0)};

	//建立每個狀態的動畫
	private Animation walkLeft = new Animation(walkingLeft, 42);
	private Animation walkRight = new Animation(walkingRight, 42);
	private Animation walkUp = new Animation(walkingUp, 42);
	private Animation walkDown = new Animation(walkingDown, 42);
	private Animation standing = new Animation(standing1, 42);

	//目前動畫狀態為靜止
	private Animation animation = standing;
	
	public Player() {
		setLayout(null);
		setSize(500, 500);
		this.addKeyListener(this);
		setFocusable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//假設12fps = 1 / 12 = 每0.042秒一張frame 
		Thread thread = new Thread(new Runnable() {
            public void run() { 
                while(true) { 
                	try {
						Thread.sleep(42);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	animation.update();
                	if(animation != standing) {
                		repaint();
                	}
                } 
            }        
        }); 
        thread.start();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Player main = new Player();
		main.setVisible(true);
	}
	
	public void paint(Graphics g) {
		g.clearRect(0, 0, 500, 500);
	    g.drawImage(animation.getSprite(), 100, 100, null);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode()){
			case KeyEvent.VK_LEFT:
				System.out.println("left");
				animation = walkLeft;
			    animation.start();
				break;
			case KeyEvent.VK_UP:
				System.out.println("up");
				animation = walkUp;
			    animation.start();
				break;
			case KeyEvent.VK_RIGHT:
				System.out.println("right");
				animation = walkRight;
			    animation.start();
				break;
			case KeyEvent.VK_DOWN:
				System.out.println("down");
				animation = walkDown;
			    animation.start();
				break;
			case 32:
				System.out.println("stop");
				animation.stop();
			    animation.reset();
			    animation = standing;
			    repaint();
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

}
