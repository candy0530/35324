package burgerking;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Background extends JLabel{
	
	public Background(){
		ImageIcon background = new ImageIcon("image/bg.png");
		setIcon(background);
		setLocation(0, 0);
		setSize(800,600);		
	}

}
