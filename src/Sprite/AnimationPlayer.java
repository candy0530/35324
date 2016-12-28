package Sprite;
import java.awt.Point;
import java.awt.image.BufferedImage;
import character.Character;

public class AnimationPlayer {
	
	private BufferedImage[] imageDown = new BufferedImage[4];
	private BufferedImage[] imageLeft = new BufferedImage[4];
	private BufferedImage[] imageRight = new BufferedImage[4];
	private BufferedImage[] imageUp = new BufferedImage[4];

	private Animation walkLeft, walkRight, walkUp, walkDown, animation;
	
	private Character character; 
	
	public AnimationPlayer(Character chara,int index) {
		this.character = chara; 
		//System.out.println("AnimationPlayer " + character.getId());
		for(int i = 0 ; i < 4 ; i++) {

		  imageDown[i] = chara.getImg().getSubimage( i *Character.imgWidth/4 , 3 *Character.imgHeight/4, Character.imgWidth/4, Character.imgHeight/4);
		  imageLeft[i] = chara.getImg().getSubimage( i *Character.imgWidth/4 , 2 *Character.imgHeight/4, Character.imgWidth/4, Character.imgHeight/4);
		  imageRight[i] = chara.getImg().getSubimage( i *Character.imgWidth/4 , 1 *Character.imgHeight/4, Character.imgWidth/4, Character.imgHeight/4);
		  imageUp[i] = chara.getImg().getSubimage( i *Character.imgWidth/4 , 0 *Character.imgHeight/4, Character.imgWidth/4, Character.imgHeight/4);
		  
		}
		walkLeft = new Animation(imageLeft);
		walkRight = new Animation(imageRight);
		walkUp = new Animation(imageUp);
		walkDown = new Animation(imageDown);
		animation = walkDown;
	}
	
	//call by other class to get player's image now
	public BufferedImage getSprite() {
		return animation.getSprite();
	}
	
	//call by other class to check player's change and change sprite
	public void animationMove(Point displacement) {
	    
	    if(displacement.x < 0) {
	        //System.out.println("Move OK1");
	        animationRight();
	    } else if(displacement.x > 0) {
	        //System.out.println("Move OK2");
	        animationLeft();
	    } else if(displacement.y < 0) {
	        //System.out.println("Move OK3");
	        animationDown();
	    } else if (displacement.y > 0) {
	        //System.out.println("Move OK4");
	        animationUp();
	    } else {
	        //System.out.println("Move OK5");
	        stopAnimation();
	    }
	    updateAnimation();
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
}
