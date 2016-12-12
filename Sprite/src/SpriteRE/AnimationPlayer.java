package SpriteRE;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class AnimationPlayer {
	
	private Point displacement;
	private String pic, police = "cop";
	
	private BufferedImage[] thiefDown = new BufferedImage[4];
	private BufferedImage[] thiefLeft = new BufferedImage[4];
	private BufferedImage[] thiefRight = new BufferedImage[4];
	private BufferedImage[] thiefUp = new BufferedImage[4];
	
	private BufferedImage[] policeDown = {Sprite.getSprite(0, 0, police), Sprite.getSprite(1, 0, police), Sprite.getSprite(2, 0, police), Sprite.getSprite(3, 0, police)};
	private BufferedImage[] policeLeft = {Sprite.getSprite(0, 1, police), Sprite.getSprite(1, 1, police), Sprite.getSprite(2, 1, police), Sprite.getSprite(3, 1, police)};
	private BufferedImage[] policeRight = {Sprite.getSprite(0, 2, police), Sprite.getSprite(1, 2, police), Sprite.getSprite(2, 2, police), Sprite.getSprite(3, 2, police)};
	private BufferedImage[] policeUp = {Sprite.getSprite(0, 3, police), Sprite.getSprite(1, 3, police), Sprite.getSprite(2, 3, police), Sprite.getSprite(3, 3, police)};

	private Animation walkLeft;
	private Animation walkRight;
	private Animation walkUp;
	private Animation walkDown;
	private Animation animation;
	
	public AnimationPlayer(String pic) {
		this.pic = pic;
		for(int i = 0 ; i < 4 ; i++) {
			thiefDown[i] = Sprite.getSprite(i, 0, pic);
			thiefLeft[i] = Sprite.getSprite(i, 1, pic);
			thiefRight[i] = Sprite.getSprite(i, 2, pic);
			thiefUp[i] = Sprite.getSprite(i, 3, pic);
		}
		walkLeft = new Animation(thiefLeft);
		walkRight = new Animation(thiefRight);
		walkUp = new Animation(thiefUp);
		walkDown = new Animation(thiefDown);
		animation = walkDown;
	}
	
	public BufferedImage getSprite() {
		return animation.getSprite();
	}
	
	public void animationMove(Point displacement) {
	    if(displacement.x > 0) {
	        animationRight();
	    } else if(displacement.x < 0) {
	        animationLeft();
	    } else if(displacement.y > 0) {
	        animationDown();
	    } else {
	        animationUp();
	    }
	}
	
	public void animationUp() {
		animation = walkUp;
		animation.start();
	}
	
	public void animationLeft() {
		animation = walkLeft;
		animation.start();
	}
	
	public void animationRight() {
		animation = walkRight;
		animation.start();
	}
	
	public void animationDown() {
		animation = walkDown;
		animation.start();
	}
	
	public void updateAnimation() {
		animation.update();
	}
	
	public void stopAnimation() {
		animation.stop();
		animation.reset();
	}
	
	public void animationPolice() {
		walkUp.changeAnimation(policeUp);
		walkLeft.changeAnimation(policeLeft);
		walkDown.changeAnimation(policeDown);
		walkRight.changeAnimation(policeRight);
		animation.stop();
	    animation.reset();
		animation = walkDown;
	}
	
	public void animationPlayer() {
		walkUp.changeAnimation(thiefUp);
		walkLeft.changeAnimation(thiefLeft);
		walkDown.changeAnimation(thiefDown);
		walkRight.changeAnimation(thiefRight);
		animation.start();
	}
}
