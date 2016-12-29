package foreground;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UserItem extends JPanel{
    private MainFrame mainFrame;
    private int itemSize = 75;
    private int burgerSize;
    private final int itemLocationY = 90;
    private final Point burgerLocation = new Point(30 , 10);
    private final Point burgerNumLocation;
    private final Font fontStyle = new Font("Serif", Font.BOLD, 35);
    
    private JLabel burgerNumLabel;
    
    
    public UserItem(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        burgerSize = itemSize;
        burgerNumLocation = new Point(30+burgerSize, 10);
        
        //set layout
        JLabel burgerPicture = new JLabel();
        burgerPicture.setIcon(new ImageIcon(new ImageIcon("image/Map/burger3.png").getImage().getScaledInstance(burgerSize, burgerSize, Image.SCALE_DEFAULT)));
        burgerPicture.setLocation(burgerLocation);
        burgerPicture.setSize(burgerSize, burgerSize);
        add(burgerPicture);
        
        burgerNumLabel = new JLabel("X 0");
        burgerNumLabel.setLocation(burgerNumLocation);
        burgerNumLabel.setSize(100, burgerSize);
        burgerNumLabel.setFont(fontStyle);
        add(burgerNumLabel);
    }
    
    
    @Override
    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        
        //reset the burger number
        burgerNumLabel.setText("X " + mainFrame.playerInfo.get(mainFrame.getUserID()-1).getBurger());

        //draw the left item
        graphics.drawImage(this.mainFrame.userInfo.getCharacter().getItemImg(0), 25, itemLocationY, itemSize, itemSize, null);
        graphics.drawRect(25, itemLocationY, itemSize, itemSize);
        
        //draw the right item 
        graphics.drawImage(this.mainFrame.userInfo.getCharacter().getItemImg(1), 25+itemSize+30, itemLocationY, itemSize, itemSize, null);
        graphics.drawRect(25+itemSize+30, itemLocationY, itemSize, itemSize);
        
    }
}
