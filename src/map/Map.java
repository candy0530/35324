package map;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.w3c.dom.css.Counter;

public class Map extends JPanel {

	private MapDetail[][] map;

	private int maxBurger = 1000;
	    
    private int mapSize;
    private int playerNum;
    
    private final int mapBorder = 6;
    private final int unitMapSize = 60; // setting unit of map size = 60 pixel
    
    private final int mapViewSize = 9;
    
    private Point userLocation;
    private ArrayList<Point> emptyGrid;     //record no burger location
    private Point[] PlayerInit;             //record player initial
    public Map(int playerNum) {        
        
        this.playerNum = playerNum;
        if(this.playerNum == 7 || this.playerNum ==6){
            this.mapSize = 47;
        }
        else if(this.playerNum == 5 || this.playerNum == 4){
            this.mapSize = 37;
        }
        else {
            this.mapSize = 37;
//            showOptionPane((int)(Math.random()*11));
        }
        
        userLocation = new Point((int) mapSize*unitMapSize/ 2, (int) mapSize*unitMapSize / 2);          //set the initial location
        
        //Avoid error
        new MapDetail().loadImg();
        map = new MapDetail[mapSize][mapSize];
        for(int i=0; i<mapSize; i++) {
            for(int j=0; j<mapSize; j++) {
                map[i][j] = new MapDetail();
                map[i][j].setWalkable(false);
            }
        }
        
        
//        // setting background
//        mapGenerator();
    }
    
    private void showOptionPane(int number){
        switch (number) {
        case 0:
            JOptionPane.showMessageDialog(null,"You only have \"" + playerNum + "\" person.\n");
            break;
        case 1:
            JOptionPane.showMessageDialog(null,"It has not enough person to play this game.\n");
            break;
        case 2:
            JOptionPane.showMessageDialog(null,"You are Great!\nYou are Great!\nYou are Great!\n\nIt's important, so I say \"THREE\" times\n");
            break;
        case 3:
            JOptionPane.showMessageDialog(null,"It's time to take a break!!\n");
            break;
        case 4:
            JOptionPane.showMessageDialog(null,"Do you have any Idea?\n");
            break;
        case 5:
            JOptionPane.showMessageDialog(null,"You only have \"" + playerNum + "\" person.\nMan proposes, God disposes.\n");
            break;
        case 6:
            JOptionPane.showMessageDialog(null,"You only have \"" + playerNum + "\" person.\nEven homer sometimes nods.\n");
            break;
        case 7:
            JOptionPane.showMessageDialog(null,"You only have \"" + playerNum + "\" person.\nFailure is the mother of success.\n");
            break;
        case 8:
            JOptionPane.showMessageDialog(null,"It's joking!\n");
            break;
        case 9:
            JOptionPane.showMessageDialog(null,"Don't hate me, please!\n");
            break;
        default:
            JOptionPane.showMessageDialog(null,"You only have \"" + playerNum + "\" person.\nBe careful your liver!\n");
            break;
        }
    }
    
    // movingCheck(move_x_pixel, move_y_pixel);
    private void movingCheck(int dx, int dy) {
//        int locationX = myselfLocation.x + dx;
//        int locationY = myselfLocation.y + dy;
        userLocation.translate(dx, dy);
//        if (map[locationX][locationY].getWalkable()) {
//            myselfLocation.translate(dx, dy);
//            if (map[locationX][locationY].getBurger()) {
//                map[locationX][locationY].setBurger(false);
//                noBurgerSite.add(new Point(locationX, locationY));
//            }
//        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        //transfer the location from center to top-left
        Point changeLocation = changeCoordinate(userLocation);
        
        //get the grid of this point
        int tempX = (int)changeLocation.getX()/unitMapSize;
        int tempY = (int)changeLocation.getY()/unitMapSize;
        
        int tempMapViewSizeX = mapViewSize;
        int tempMapViewSizeY = mapViewSize;
                
        if(changeLocation.getX()%unitMapSize != 0) {
            tempMapViewSizeX++;
        }
        if(changeLocation.getY()%unitMapSize != 0) {
            tempMapViewSizeY++;
        }
        
        //draw map and burger
        for(int i=0; i<tempMapViewSizeX; i++) {
        	for(int j=0; j<tempMapViewSizeY; j++) {
        		// draw background
        	    try {
        	        g.drawImage(
							map[i + tempX][j + tempY].getBgImg(),
                            ((i+tempX)*unitMapSize)-(int)changeLocation.getX(),
                            ((j+tempY)*unitMapSize)-(int)changeLocation.getY(),                         
                            unitMapSize, unitMapSize, null);

        	        g.drawImage(
							map[i + tempX][j + tempY].getBlockImg(),
                            ((i+tempX)*unitMapSize)-(int)changeLocation.getX(),
                            ((j+tempY)*unitMapSize)-(int)changeLocation.getY(),                         
                            unitMapSize, unitMapSize, null);
							
        	        // draw burger
                    g.drawImage(
                            map[i + tempX][j + tempY].getBurgerPicture(),
                            ((i+tempX)*unitMapSize)-(int)changeLocation.getX(),
                            ((j+tempY)*unitMapSize)-(int)changeLocation.getY() + (int)(4.2 *Math.cos( Math.toRadians( map[i + tempX][j + tempY].burgerDegree))),                        
                            unitMapSize, unitMapSize, null);
                    
                    map[i + tempX][j + tempY].burgerDegree += 4;
                    map[i + tempX][j + tempY].burgerDegree %= 360;
        	    }
        	    catch (Exception ex){
        	        System.out.println("Map Error!!");  
        	    }
        		
        	}
        }        
        
//        // setPlayer
//        g.setColor(Color.black);
//        g.fillRect(4 * unitMapSize, 4 * unitMapSize, 60, 60);

    }

    // for Server
    // generate the map
    public void mapGenerator() {
        
    	// Order to set Player's site
    	emptyGrid = new ArrayList<>();
        
        //set bonder of map
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                try {
                    if ((i < mapBorder) || (j < mapBorder) || (i >= mapSize - mapBorder) || (j >= mapSize - mapBorder)) {
                        map[i][j].setWalkable(false);
                    } else {
                        map[i][j].setWalkable(true);
                        emptyGrid.add(new Point(i, j));
                    }                    
                }
                catch (Exception e) {
                    System.out.println("(Map)mapGenerator() map["+i+"]["+j+"].setWalkable is wrong. (set bonder of map)");
                }
            }
        }
        
       
        // setting obstacle
        if(Math.random() > 0.5) {
            setObstacle();            
        }
        else {
            setObstacle3();
        }
        
        //set the max Burger number
        maxBurger = emptyGrid.size() - playerNum;
        
        //random the walkable
        Collections.shuffle(emptyGrid);
        // product the burger
        for (int i = emptyGrid.size()-1; i>=playerNum; i--) {
            Point temp = emptyGrid.get(i);;
            try {
                map[temp.x][temp.y].setBurger(true);
                emptyGrid.remove(i);                
            }
            catch (Exception e) {
                System.out.println("(Map)mapGenerator() map["+temp.x+"]["+temp.y+"].setBurger is wrong. (random the walkable)");
            }
        }
        
        PlayerInit = new Point[playerNum+1];
        PlayerInit[0] = getPixelLocation(new Point(-1, -1));
        
        // print the player site
        for(int i=0; i<emptyGrid.size(); i++) {
            PlayerInit[i+1] = getPixelLocation(emptyGrid.get(i));
//            System.out.println("Player initial");
//        	System.out.println("x= " + PlayerInit[i+1].getX() + "; y= " + PlayerInit[i+1].getY());
        }
        
//        System.out.println("(Map)getMaxBurger() is " + getMaxBurger());
    }
    
    //set Obstacle¡@1 space
    private void setObstacle() {
        for (int j = 1 + mapBorder; j < mapSize - mapBorder-1; j += (1+1)) {
            for (int i = 1 + mapBorder; i < mapSize - 1 - mapBorder; i++) {
                if (Math.random() > 0.4) {
                    try {
                        map[i][j].setWalkable(false);
                        emptyGrid.remove(new Point(i, j));
                        
                    }
                    catch (Exception e) {
                        System.out.println("(Map)mapGenerator() map["+i+"]["+j+"].setWalkable is wrong. (setting obstacle)");
                    }
                }
            }
        }
    }
    
  //set Obstacle¡@2 space
    private void setObstacle2() {
        for (int j = 1 + mapBorder; j < mapSize - mapBorder-1; j += (1+2)) {
            int counterWalkable = 0;
            for (int i = 1 + mapBorder; i < mapSize - 1 - mapBorder; i++) {
                if (Math.random() > 0.4 && counterWalkable != (2-1)) {
                    try {    
                        System.out.println("Counter Walkable: " + counterWalkable);
                        map[i][j].setWalkable(false);
                        emptyGrid.remove(new Point(i, j));
                        counterWalkable = 0;
                    }
                    catch (Exception e) {
                        System.out.println("(Map)mapGenerator() map["+i+"]["+j+"].setWalkable is wrong. (setting obstacle)");
                    }
                }
                else {
                    counterWalkable++;
                }
            }
        }
    }
    
    
    // if map have the space, the space size must bigger than one space 
    // we don't allow the only one space in the map  
    // first rule: ("1" is obstacle, "0" is walkable)
    // 1001 is allowed; 1011 is not allowed 
    // (column can not allow only one space)
    //
    // Second rule:
    // 1111                 11111
    // 0000                 00000
    // 0000   is allowed    11111   is not allowed
    // (row can not allow only one space)
    private void setObstacle3() {
        double[] rand = {0.4, 0.6, 0.8};
        for (int j = 1 + mapBorder; j < mapSize - mapBorder-1; j++) {
            int counterWalkable = 0;
            for (int i = 1 + mapBorder; i < mapSize - 1 - mapBorder; i++) {
                if(map[i][j-2].getWalkable() || j == 1+mapBorder) {
                    if (Math.random() > rand[j%3] && counterWalkable != (2-1)) {
                        try {    
                            System.out.print("0");
                             map[i][j].setWalkable(false);
                            emptyGrid.remove(new Point(i, j));
                            counterWalkable = 0;
                        }
                        catch (Exception e) {
                            System.out.println("(Map)mapGenerator() map["+i+"]["+j+"].setWalkable is wrong. (setting obstacle)");
                        }
                    }
                    else {
                        counterWalkable++;
                        System.out.print(" ");
                    }                        
                }
                else {
                    counterWalkable++;
                    System.out.print(" ");
                }
            }
            
            System.out.println("");
        }
    }
    
    private Point changeCoordinate(Point point) {
    	Point temp = new Point(point.x, point.y);
    	temp.translate((int)(-unitMapSize*4.5), (int)(-unitMapSize*4.5));
    	if(temp.x < 0  || temp.y < 0) {
    	    System.out.println("(Map)changeCoordinate have error.");
    	}    
    	return temp;
    }
	
    private void printPoint(Point point) {
    	System.out.println("(Map)Print the point: X= " + point.x + "; Y= " + point.y);
    }

//map information
    //for Server
    public boolean[] getMapInformation() {
    	boolean[] walkable = new boolean[mapSize*mapSize];
    	for(int y=0; y<mapSize; y++) {
    		for(int x=0; x<mapSize; x++) {
    			try {
    			    walkable[x*mapSize+y] = map[x][y].getWalkable();                   
                }
                catch (Exception e) {
                    System.out.println("(Map)getMapInformation() map["+x+"]["+y+"].getWalkable is wrong.");
                }
    		}
    	}
        return walkable;
    }
    
    public String getMapInformationString() {
        String mapInformation = "";
        for(int y=0; y<mapSize; y++) {
            for(int x=0; x<mapSize; x++) {
                try {
                    if(map[x][y].getWalkable()) {
                        mapInformation += "1";
                    }
                    else {
                        mapInformation += "0";
                    }
                }
                catch (Exception e) {
                    System.out.println("(Map)getMapInformationString() map["+x+"]["+y+"].getWalkable is wrong.");
                }
            }
        }
        if(mapInformation.length() != mapSize*mapSize) {
            System.out.println("(Map)getMapinformationString length is wrong.");
        }
        
        return mapInformation;
    }
        
    //for Client
    public void setMapInformation(boolean[] walkable) {
        if(walkable.length == mapSize*mapSize) {
            for(int y=0; y<mapSize; y++) {
                for(int x=0; x<mapSize; x++) {
                    try {                
                        map[x][y].setWalkable(walkable[x*mapSize+y]);
                    }
                    catch (Exception e) {
                        System.out.println("(Map)setMapInformation() map["+x+"]["+y+"].setWalkable is wrong.");
                    }
                }
            }            
        }
        else {
            System.out.println("(Map)setMapInformation walkable length is wrong.");
        }
        repaint();
    }
    
    public void setMapInformationString(String walkable) {
//        System.out.println("walkable: " + walkable);
//        System.out.println("mapSize: " + mapSize);
        
        char[] tempWalkable = walkable.toCharArray();
        
        if(tempWalkable.length == mapSize*mapSize) {
            for(int y=0; y<mapSize; y++) {
                for(int x=0; x<mapSize; x++) {
                    try {                
                        if(tempWalkable[y*mapSize+x] == '1') {
                            map[x][y].setWalkable(true);                    
                        }
                        else{
                            map[x][y].setWalkable(false);
                        }
                    }
                    catch (Exception e) {
                        System.out.println("(Map)setMapInformationString() map["+x+"]["+y+"].setWalkable is wrong.");
                    }
                }
            }            
        }else {
            System.out.println("(Map)setMapInformationString walkable length is wrong.");
        }
        
        repaint();
    }

//Player Initial
    public Point[] getPlayerInit() {
        if(PlayerInit.length < 4) {
            System.out.println("People is less than 4.");
        }
		return PlayerInit;
	}
    
    public boolean[] getViewBurger(Point playerLocation) {
    	boolean[] viewBurger = new boolean[(mapViewSize+1)*(mapViewSize+1)];
    
    	Point changeLocation = changeCoordinate(playerLocation);
    	
    	int tempX = (int)changeLocation.getX()/unitMapSize;
        int tempY = (int)changeLocation.getY()/unitMapSize;
        
    	for(int y=0; y<mapViewSize+1; y++) {
    		for(int x=0; x<mapViewSize+1; x++) {
    			viewBurger[y*(mapViewSize+1)+x] = map[x+tempX][y+tempY].getBurger();
    		}
    	}
    	return viewBurger;
    	
    }
   
    public String getViewBurgerString(Point playerLocation) {
        String tempViewBurger = "";
        //System.out.println("GetView");
        //printPoint(playerLocation);
        Point changeLocation = changeCoordinate(playerLocation);
        //printPoint(changeLocation);
        
        //test the changeLocation weather over Range
        if (changeLocation.x >= 0 && changeLocation.y >= 0) {
            int tempX = (int)changeLocation.getX()/unitMapSize;
            int tempY = (int)changeLocation.getY()/unitMapSize;
            
            for(int y=0; y<mapViewSize+1; y++) {
                for(int x=0; x<mapViewSize+1; x++) {
                    try {
                        if(tempX+x < mapSize && tempY+y < mapSize) {
                            if(map[tempX+x][tempY+y].getBurger()) {
                                tempViewBurger += "1";
                            }
                            else {
                                tempViewBurger += "0";
                            }                        
                        }else {
                            tempViewBurger += "0";
                        }                        
                    }
                    catch (Exception e) {
                        System.out.println("(Map)getViewBurgerString is over Range.");
                    }
                }
            }
        }
        else {
            //System.out.println("(Map)getViewBurgerString playerLocation is wrong.");
        }
//        System.out.println("ViewBurger:");
//        System.out.println(tempViewBurger);
        
        return tempViewBurger;
    }
    
    public void setViewBurger(boolean[] burger, Point playerLocation) {
    	Point changeLocation = changeCoordinate(playerLocation);       //transfer the point from center to top-left
    	int tempX = changeLocation.x / unitMapSize;
    	int tempY = changeLocation.y / unitMapSize;
    	
    	for(int y=0; y<mapViewSize+1; y++) {
    		for(int x=0; x<mapViewSize+1; x++) {
    			map[tempX + x][tempY + y].setBurger(burger[x*mapViewSize+y]);
    		}
    	}

        repaint();
    }
    
    public void setViewBurgerString(String burger, Point playerLocation) {
        char[] tempBurger = burger.toCharArray();
        if(tempBurger.length == (mapViewSize+1)*(mapViewSize+1)) {            
            setUserLocation(playerLocation);
            Point changeLocation = changeCoordinate(playerLocation);        //transfer the point from center to top-left
            
            int tempX = changeLocation.x / unitMapSize;
            int tempY = changeLocation.y / unitMapSize;
//            double time = System.currentTimeMillis();
            for(int y=0; y<mapViewSize+1; y++) {
                for(int x=0; x<mapViewSize+1; x++) {
                    try {
                        if(tempX+x < mapSize && tempY+y < mapSize) {
                            if(tempBurger[y*(mapViewSize+1)+x] == '1') {
                                map[tempX + x][tempY + y].setBurger(true);                    
                            }
                            else{
                                map[tempX + x][tempY + y].setBurger(false);
                            }                    
                        }                    
                    }
                    catch (Exception e) {
                        System.out.println("(Map)setViewBurgerString is over Range");
                    }
                }
            }
//            System.out.println("(Map)setViewBurger time = " + (System.currentTimeMillis() - time));
            repaint();
        }
        else {
            System.out.println("(Map)setViewBurgerString string butger is currect.");
        }
    }
    
    private void setUserLocation(Point userLocation) {
		this.userLocation = userLocation;
	}
    
    public Point getUserLocation() {
        return userLocation;
    }
    
	// to check collision
    public boolean isBurger(Point point){
        Point gridLocation = getGridLocation(point.x, point.y);
        try {
            return map[gridLocation.x][gridLocation.y].getBurger();    
        }
        catch (Exception e) {
            return false;
        }
    }
 
    public boolean isWalkable(Point point){
    	Point gridLocation = getGridLocation(point.x, point.y); 
    	try {
    	    return map[gridLocation.x][gridLocation.y].getWalkable();    	    
    	}
    	catch (Exception e) {
            return false;
        }
    }
    
    public void eatBurger(int x, int y) {
        Point gridLocation = getGridLocation(x, y); 
        map[gridLocation.x][gridLocation.y].setBurger(false);
        emptyGrid.add(gridLocation);
    }
    
    public void eatBurger(Point point) {
        Point gridLocation = getGridLocation(point.x, point.y); 
        map[gridLocation.x][gridLocation.y].setBurger(false);
        emptyGrid.add(gridLocation);
    }
    
    public void setSpreadBurger(int burgerNum){
        //random the walkable
        Collections.shuffle(emptyGrid);
        
        //spread burger
        for(int i=burgerNum-1; i>=0; i--) {
            Point temp = emptyGrid.get(i);
            map[temp.x][temp.y].setBurger(true);
            emptyGrid.remove(i);
        }
        repaint();
    }
    
    private Point getGridLocation(int pixelX, int pixelY) {
        return new Point(pixelX/unitMapSize, pixelY/unitMapSize);
    }
    
    private Point getGridLocation(Point pixel) {
        return new Point(pixel.x/unitMapSize, pixel.y/unitMapSize);
    }
    
    private Point getPixelLocation(Point grid) {
        return new Point(grid.x * unitMapSize + (unitMapSize/2), grid.y*unitMapSize + (unitMapSize/2));
    }
    
    public Point getPrison() {
        return getPixelLocation(new Point(4, 4));
    }
    public void setPrison(Point point) {
        this.userLocation = getGridLocation(point);
        repaint();
    }
    
    public int getMaxBurger(){
        return maxBurger;
    }
    
    public int getMapSize() {
        return mapSize;
    }
}

