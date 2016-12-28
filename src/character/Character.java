package character;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import data.Paintable;


public class Character implements Paintable{

	Point coordinate;
	Point displacement;
	double speed;
	int id;
	Item[] item;
	boolean itemStatus[]; 
	static private BufferedImage[] img;
	
	final static int itemNums = 2;
	public static final int imgHeight = 240;
	public static final int imgWidth = 240;
	
	public Character(){
//		coordinate.setLocation(1, 1);
		speed = 5;
		id = 0;
		coordinate = new Point();
		displacement = new Point();
		
    item = new Item[itemNums];
    itemStatus = new boolean[itemNums];		
		for(int i=0; i< item.length ; i++){
		  
		  //itemList[Max] is dummy item.
			item[i] = ItemCreator.getNewItem( this, (int)(Math.random()* (ItemCreator.itemList.size() -1)));
		}
		Arrays.fill(itemStatus, true);
	}
	
	public Character( int id){
	  
	  this();
	  this.setId( id);
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

	public void useItem( int num, Timer event){

	  assert( num < itemNums ):"WTH with the item use?";
	  assert( event == null ):"WTH with the event?";
	  
	  synchronized (this) {
	    
	    if ( itemStatus[num]){
	      
	      item[num].use(event);
	      itemStatus[num] = false;
	      
	      // turn all other items into unusable
	      // still feel something strange...
	      boolean[] oldItemStatus = this.itemStatus.clone();
	      
	      Arrays.fill(itemStatus, false);
	      TimerTask recoverItemUsable = new TimerTask() {
          
	        boolean[] oldStatus = oldItemStatus;
	        
          @Override
          public void run() {
            
            itemStatus = oldStatus;
          }
        };
        
        event.schedule(recoverItemUsable, item[num].getDuration());
	      item[num] = new DummyItem(this);	      
	    }
    }
	}
	
	public void removeItem( int num){
	  //Build for the ghost class.
	  //Some synchronized condition need to be solved 
	  //if this function needs to be used in the ordinary condition.
	  assert( num < itemNums ):"WTH with the item use?";
	  synchronized (this) {
	    
	    if ( itemStatus[num]){
	      
	      itemStatus[num] = false;
	      item[num] = new DummyItem(this);
	    }	    
    }
	}
	
	public BufferedImage getItemImg( int num){
	  
	  assert( num < itemNums ):"WTH with the item use?";
	  assert( item[num] != null):"Why a null item!!!";	  
	  
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
    
    String filePath[] = {
        "image/cop.png",
        "image/red.PNG",
        "image/brown.PNG",
        "image/yellow.PNG",
        "image/green.PNG",
        "image/blue.PNG",
        "image/dark-blue.PNG",
        "image/purple.PNG",
    };
    
    assert( filePath.length == 8):"Not the magic 8 characters.";
    
    img = new BufferedImage[filePath.length];
    
    for(int i=0; i < filePath.length; i++){

      img[i] = Paintable.loadOneImg(filePath[i], imgWidth, imgHeight);
    }
    
    assert( item[0] != null):"Item should have be loaded at this moment.";
    item[0].loadImg();
  }

  @Override
  public BufferedImage getImg() {
    assert( img != null): "LoadImg first!!";
    assert( this.id < img.length): " Img not loaded or wrong refernce.";
    return  Character.img[this.id];
  }
	
	
}

abstract class Item implements Paintable{

	Character owner;
	int imageId;
	private long duration;
	static BufferedImage[] img;
	static final int imgHeight = 75;
	static final int imgWidth = 75;
	
	public Item(Character owner) {
		this.owner = owner;
	}

	abstract void use( Timer event);
	
	public long getDuration(){
	  return duration;
	}
	
	void setDuration(long duration){
	   this.duration = duration; 
	}
	
  @Override
  public void loadImg() {
    //Should load the file path from some certain files, but, you know...
    
    String filePath[] = {
        null,
        "image/running.png",
        "image/run.png",
    };
    
    assert( filePath.length == ItemCreator.itemList.size()):"Wrong item image numbers.";
    
    img = new BufferedImage[filePath.length];
   
    //imageArray: one-based
    for(int i=1; i < filePath.length; i++){

      img[i] = Paintable.loadOneImg(filePath[i], imgWidth, imgHeight);
    }
    
    img[0] = Paintable.getBlackImg(imgWidth, imgHeight);
  }

  @Override
  public BufferedImage getImg() {
    //imageArray: one-based
    assert(img != null): "LoadImg first!!";
    assert( this.imageId < img.length):"Img not loaded or wrong refernce.";
    
    return img[this.imageId];
  }
	
}

class Booster extends Item{
	
    final long duration = 5000;
	public Booster(Character owner) {
		super(owner);
		imageId = 1;
		super.setDuration(this.duration);
	}

	@Override
	void use(Timer event) {
		double speedEnhance = 1.2;
		
		TimerTask endEffect = new TimerTask() {
      
      @Override
      public void run() {
        owner.setSpeed( owner.getSpeed() / speedEnhance);
        //effect
      }
    };
    
    event.schedule( endEffect, duration);
		
		this.owner.setSpeed( this.owner.getSpeed() * speedEnhance);
	}
}

class HyperBooster extends Item{
	
    final long duration = 3000;
	public HyperBooster(Character owner) {
		super(owner);
		imageId = 2;
		super.setDuration(this.duration);
	}

	@Override
	void use(Timer event) {
		int speedEnhance = 2;
   
    TimerTask endEffect = new TimerTask() {
      
      @Override
      public void run() {
        owner.setSpeed( owner.getSpeed() / speedEnhance);
        //effect
      }
    };
    
    event.schedule( endEffect, duration);
		
		this.owner.setSpeed( this.owner.getSpeed() * speedEnhance);
//		TODO stack a new speed recover event into timerTask
	}
}

class DummyItem extends Item{

  public DummyItem(Character owner) {
    super(owner);
    imageId = 0;
  }

  @Override
  void use(Timer event) {    
  }
  
}

class ItemCreator {
	
	static ArrayList<Class<? extends Item>> itemList = new ArrayList<>();
	
	static{
		itemList.add(Booster.class);
		itemList.add(HyperBooster.class);
    itemList.add(DummyItem.class);
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

