package burgerking;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class GameRule extends JFrame{
	
	public GameRule(int x,int y) 
    { 
		//super("win2"); 
		setSize(600,400); 
		setLayout(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); 
		setVisible(true); 
		setResizable(false);
		setLocationRelativeTo(null);
		setLocation(x+100, y+100);
    } 
	
	
}
