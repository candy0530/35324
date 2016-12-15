package character;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import datakit.Paintable;


public class Character implements Paintable{

	private Point coordinate;
	private Point displacement;
	private double speed;
	private int id;
	private Item[] item;
	static private BufferedImage[] img;
	final static int itemNums = 2;
	
	public Character(){
//		coordinate.setLocation(1, 1);
		speed = 2;
		item = new Item[itemNums];
		id = 0;
		
		for(int i=0; i< item.length ; i++){
			item[i] = ItemCreator.getNewItem( this, (int)(Math.random()* ItemCreator.itemList.size()));
		}
	}	
	
	public Point getDisplacement() {
    return displacement;
  }

  //server to client location updated 
  public void setMovement(Point point){
    displacement.setLocation(point);
    //TODO - judge for move-on direction to adjust the displacement 
    coordinate.translate(point.x, point.y);
  }

  public void setCoordinate(Point newP) {
	  
	  displacement.setLocation( newP.x - coordinate.x, newP.y - coordinate.y);
    this.coordinate = newP;
  }

  public Point getCoordinate(){
		return coordinate;
	}
	
	public double getSpeed(){
		return speed;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public void useItem(int number){
	  
	  assert( number < itemNums):"WTH with the item use?";
		if ( item[number] != null){
		  item[number].use();
		  item[number] = null;
		}
	}
	
	public BufferedImage getItemImg( int num){
	  return item[num].getImg();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	

  @Override
  public void loadImg() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public BufferedImage getImg() {
    // TODO Auto-generated method stub
    return  Character.img[this.id];
  }
	
	
}



abstract class Item implements Paintable{

	Character owner;
	int imageId;
	static BufferedImage[] img;
	
	public Item(Character owner) {
		this.owner = owner;
	}

	abstract void use();
	
  @Override
  public void loadImg() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public BufferedImage getImg() {
    // TODO Auto-generated method stub
    return null;
  }
	
}

class Booster extends Item{
		
	public Booster(Character owner) {
		super(owner);
		imageId = 0;
	}

	@Override
	void use() {
		double speedEnhance = 1.2;
		
		this.owner.setSpeed( this.owner.getSpeed() * speedEnhance);
		// TODO stack a new speed recover event into timerTask
	}
}

class HyperBooster extends Item{
	
	public HyperBooster(Character owner) {
		super(owner);
		imageId = 1;
	}

	@Override
	void use() {
		int speedEnhance = 2;
		
		this.owner.setSpeed( this.owner.getSpeed() * speedEnhance);
//		TODO stack a new speed recover event into timerTask
	}
}

class ItemCreator {
	
	static ArrayList<Class<? extends Item>> itemList = new ArrayList<>();
	
	static{
		itemList.add(Booster.class);
		itemList.add(HyperBooster.class);
	}
	
	static Item getNewItem( Character owner, int itemId)
	{
		try {
			return itemList.get(itemId).getDeclaredConstructor(Character.class).newInstance(owner);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}

