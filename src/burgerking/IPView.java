package burgerking;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class IPView extends JPanel{

	MainFrame main_frame;
	
	public IPView(MainFrame main_frame){
		
		setLocation(0, 0);
        setSize(800,600);
        setLayout(null);     	
        
    	JLabel app_name = new JLabel("漢 堡 神 偷 王");       
    	app_name.setLocation(170, 30);    	
    	app_name.setSize(460,100);
    	app_name.setFont(new Font("標楷體", Font.BOLD, 60));
    	add(app_name);
    	
    	JLabel lobby_Ip_label = new JLabel("遊戲ＩＰ");       
    	lobby_Ip_label.setLocation(300, 150);
    	lobby_Ip_label.setSize(70,100);    	
		add(lobby_Ip_label); 
		
		JTextField lobby_Ip_text = new JTextField();
		lobby_Ip_text.setLocation(370, 175);
		lobby_Ip_text.setSize(130,50);   
		lobby_Ip_text.addKeyListener(new ourKeyListenerIP());
		add(lobby_Ip_text);	
		
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
        
        JButton confirm = new JButton("確認");
		confirm.setBounds(300,250,200,100);
		confirm.setVisible(true);
		confirm.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent me) {
                System.out.println("IP view button 1 ok"); 
                try {
					if(lobby_Ip_text.getText().equals("")){
						throw new Exception();
					}else{
						main_frame.client =new TCPClient(lobby_Ip_text.getText());
						main_frame.changeView(new LobbyView(main_frame) );
						main_frame.client.enterRoom("USER2");
					}            		
				} catch (Exception e) {
					 System.out.println("Exception ok");
					 JOptionPane.showMessageDialog(null, "要輸入遊戲位置喔", "溫馨提醒@@?", JOptionPane.INFORMATION_MESSAGE);
				}                
              } 
            }); 
    	add(confirm);
    	
    	JButton cancel = new JButton("取消");
    	cancel.setBounds(300,400,200,100);
    	cancel.setVisible(true);
    	cancel.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent me) {  
				main_frame.changeView(new IDView(main_frame) );
              } 
            }); 
    	add(cancel);
        
        JLabel background_label = new Background(); 
        add(background_label);
	}

}
