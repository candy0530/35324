package burgerking;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class LobbyView extends JPanel implements Observer{
	
	JLabel ServerInf;
	JLabel ShowServerIP;
	JLabel ShowNumberOfPlayer;
	JLabel app_name;
	JLabel[] ShowPlayerList;
	MainFrame main_frame;
	private int playerNum =0;
	
	public LobbyView(MainFrame main_frame) {
		main_frame.client.registOberserver(this);
		setLocation(0, 0);
        setSize(800,600);
        setLayout(null);          
       
		
		JLabel name1 = new JLabel("開始",SwingConstants.CENTER);
		name1.setBounds(20,10,40,40);
		JLabel name2 = new JLabel("遊戲",SwingConstants.CENTER);
		name2.setBounds(20,35,40,40);
		
		JButton StartGame = new JButton();
    	StartGame.setLayout(null);
    	StartGame.add(name1);
    	StartGame.add(name2);
    	StartGame.setBounds(550,400,80,80);
    	StartGame.setVisible(true);    	
    	StartGame.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent me) {
                System.out.println("StartGame button 1 ok");
                main_frame.changeView(new GameView(main_frame) );
              } 
            }); 
    	add(StartGame);
    	
    	JButton ComeBack = new JButton("返回");
    	ComeBack.setBounds(660,400,80,80);
    	ComeBack.setVisible(true);
    	ComeBack.addMouseListener(new MouseAdapter() { 
            public void mouseClicked(MouseEvent me) { 
            	if(main_frame.client.isServer()){          		
            		main_frame.server.endConnection();
            	}
            	else
            		main_frame.client.endConnection();
            	System.out.println("button 2 ok"); 
            	main_frame.changeView(new IDView(main_frame) ); 
              }            
            }); 
    	add(ComeBack);   	
        
    	app_name = new JLabel("遊 戲 等 待 室");       
    	app_name.setLocation(170, 30);    	
    	app_name.setSize(460,100);
    	app_name.setFont(new Font("標楷體", Font.BOLD, 60));
    	add(app_name);
    	
    	ServerInf = new JLabel("伺服器資訊",SwingConstants.CENTER);
    	ServerInf.setBackground(new java.awt.Color(0,100,250));
    	ServerInf.setOpaque(true);
    	ServerInf.setLocation(550, 180);    	
    	ServerInf.setSize(210,40);    
    	ServerInf.setFont(new Font("標楷體", Font.BOLD, 20));
    	ServerInf.setForeground(Color.white);;
    	add(ServerInf);
    	
    	ShowServerIP = new JLabel("IP："+main_frame.client.getServerIP(),SwingConstants.CENTER);
    	ShowServerIP.setBackground(new java.awt.Color(0,100,250));
    	ShowServerIP.setOpaque(true);
    	ShowServerIP.setLocation(550, 220);    	
    	ShowServerIP.setSize(210,50);    
    	ShowServerIP.setFont(new Font("標楷體", Font.BOLD, 20));
    	ShowServerIP.setForeground(Color.white);;
    	add(ShowServerIP);
    	
    	ShowNumberOfPlayer = new JLabel("玩家人數：1",SwingConstants.CENTER);
    	ShowNumberOfPlayer.setBackground(new java.awt.Color(0,100,250));
    	ShowNumberOfPlayer.setOpaque(true);
    	ShowNumberOfPlayer.setLocation(550, 270);    	
    	ShowNumberOfPlayer.setSize(210,50);    
    	ShowNumberOfPlayer.setFont(new Font("標楷體", Font.BOLD, 20));
    	ShowNumberOfPlayer.setForeground(Color.white);;
    	add(ShowNumberOfPlayer);
    	
    	ShowPlayerList = new JLabel[8];
    	
    	int[][] PlayerListColorTable = {{255,255,255},
    									{255, 0, 0},
    									{255, 165, 0 },
    									{255, 255, 0},
    									{0, 255, 0},
    									{0, 127, 255},
    									{0, 0, 255},
    									{139, 0, 255 }};
    	for(int i = 0;i<8;i++)
    	{
    		ShowPlayerList[i] = new JLabel("",SwingConstants.CENTER);
    		ShowPlayerList[i].setBackground(new java.awt.Color(PlayerListColorTable[i][0],PlayerListColorTable[i][1],PlayerListColorTable[i][2]));
    		ShowPlayerList[i].setOpaque(true);
    		ShowPlayerList[i].setLocation(150, 180+40*i);    	
    		ShowPlayerList[i].setSize(300,40);    
    		ShowPlayerList[i].setFont(new Font("標楷體", Font.BOLD, 20));
    		ShowPlayerList[i].setForeground(Color.black);;
        	add(ShowPlayerList[i]);    		
    	}
    	ShowPlayerList[0].setText("玩家列表");
    	
    	JButton GameRule = new JButton("?");
        GameRule.setLocation(700, 500);        
        GameRule.setSize(50, 50);
        GameRule.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent me) {
				int x = (int)main_frame.getLocation().getX();
				int y = (int)main_frame.getLocation().getY();
				GameRule gameRule = new GameRule(x,y,main_frame);
			}
		});
        add(GameRule);
        
        JLabel background_label = new Background(); 
        add(background_label);
	}

	
	public void receiveNotify(String msg){	
		System.out.println(msg);
        String [] message = msg.split("#");
        if(message[0].equals("List")){
			//System.out.println("Get list!");
			playerNum= message.length -1;
			System.out.println(playerNum);
			for(int i =1;i<message.length;i++){
				ShowPlayerList[i].setText(message[i]);
			}
			for(int i =message.length;i<ShowPlayerList.length;i++){
				ShowPlayerList[i].setText("");
			}
			ShowNumberOfPlayer.setText("玩家人數：" + String.valueOf(playerNum));
		}    
    }	

}
