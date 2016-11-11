package burgerking;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame{
	
	JPanel MainPanel;
	TCPClient client;
	TCPServer server;

	public MainFrame(){
		setTitle("ºÆ¨g°­§ì¤H");
       	setSize(805, 620);    	
    	setLayout(null); 
    	setResizable(false);
    	setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainPanel = new IDView(this);
        add(MainPanel); 
	}
	
	public static void main(String[] args){
		MainFrame frame = new MainFrame();
		frame.setVisible(true);		
    } 	
	
	public void changeView(JPanel NewPanel)
	{
        remove(MainPanel);
        MainPanel = NewPanel;
        add(MainPanel);		                
        repaint();		
	}
	
}


