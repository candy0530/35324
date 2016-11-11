package burgerking;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class IDView extends JPanel{
	MainFrame main_frame;
	
	public IDView(MainFrame main_frame){

        setLocation(0, 0);
        setSize(800,600);
        setLayout(null); 
        
    	JLabel app_name = new JLabel("�~ �� �� �� ��");       
    	app_name.setLocation(170, 30);    	
    	app_name.setSize(460,100);
    	app_name.setFont(new Font("�з���", Font.BOLD, 60));
    	add(app_name);
    	
    	JLabel user_ID_label = new JLabel("�C���ʺ�");       
    	user_ID_label.setLocation(300, 150);
    	user_ID_label.setSize(70,100);
		add(user_ID_label); 
		
		JTextField user_ID_text = new JTextField();
		user_ID_text.setLocation(370, 175);
		user_ID_text.setSize(130,50);  
		user_ID_text.addKeyListener(new OurKeyListener());
		add(user_ID_text);	
		
        JButton GameRule = new JButton("?");
        GameRule.setLocation(700, 500);        
        GameRule.setSize(50, 50);
        GameRule.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				int x = (int)main_frame.getLocation().getX();
				int y = (int)main_frame.getLocation().getY();
				GameRule GR = new GameRule(x,y);
			}
		});
        add(GameRule);
        
        JButton NewGame = new JButton("�}�s�C��");
    	NewGame.setBounds(300,250,200,100);
    	NewGame.setVisible(true);
    	NewGame.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent me) {
            	try {
					if(user_ID_text.getText().equals("")){
						throw new Exception();
					}else{
						main_frame.server = new TCPServer();
						main_frame.client = new TCPClient("127.0.0.1");
		                System.out.println("start game button 1 ok");
		                main_frame.changeView(new LobbyView(main_frame));
		                main_frame.client.enterRoom(user_ID_text.getText());
					}            		
				} catch (Exception e) {
					 System.out.println("Exception ok");
					 JOptionPane.showMessageDialog(null, "�n��J�C���ʺٳ�", "���ɴ���@@?", JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
		});    	
    	add(NewGame);
    	
    	JButton JoinGame = new JButton("�[�J�C��");
    	JoinGame.setBounds(300,400,200,100);
    	JoinGame.setVisible(true);
    	JoinGame.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent me) {
				try {
					if(user_ID_text.getText().equals("")){
						throw new Exception();
					}else{
						main_frame.changeView(new IPView(main_frame));
					}            		
				} catch (Exception e) {
					 System.out.println("Exception ok");
					 JOptionPane.showMessageDialog(null, "�n��J�C���ʺٳ�", "���ɴ���@@?", JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
		});
    	add(JoinGame); 
        
        JLabel background_label = new Background(); 
        add(background_label);
	}	
	
}
