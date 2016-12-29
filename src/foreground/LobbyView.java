package foreground;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import background.Observer;
import sound.SoundPlayer;

public class LobbyView extends JPanel implements Observer {

    private JLabel showServerIP;
    private JLabel showNumberOfPlayer;
    private JLabel[] showPlayerList;
    private MainFrame mainFrame;
    private JButton startGameButton;
    SoundPlayer bgm;


    public LobbyView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        
        mainFrame.initView(this);
        mainFrame.setTitle(this,"遊 戲 等 待 室");
        mainFrame.setGameRuleButton(this);
        
        bgm = new SoundPlayer("sound/lobbyBGM.mp3", true);
        bgm.start();

        JLabel name1 = new JLabel("開始", SwingConstants.CENTER);
        name1.setBounds(20, 10, 40, 40);
        JLabel name2 = new JLabel("遊戲", SwingConstants.CENTER);
        name2.setBounds(20, 35, 40, 40);

        startGameButton = new JButton();
        startGameButton.setLayout(null);
        startGameButton.add(name1);
        startGameButton.add(name2);
        startGameButton.setBounds(550, 400, 80, 80);
        startGameButton.setVisible(true);
        startGameButton.addActionListener(startGameActionListener);
        add(startGameButton);

        JButton comeBackButton = new JButton("返回");
        comeBackButton.setBounds(660, 400, 80, 80);
        comeBackButton.setVisible(true);
        comeBackButton.addActionListener(comeBackActionListener);
        add(comeBackButton);

        JLabel serverInfo = new JLabel("伺服器資訊", SwingConstants.CENTER);
        serverInfo.setBackground(new java.awt.Color(0, 100, 250));
        serverInfo.setOpaque(true);
        serverInfo.setLocation(550, 180);
        serverInfo.setSize(210, 40);
        serverInfo.setFont(new Font("標楷體", Font.BOLD, 20));
        serverInfo.setForeground(Color.white);
        add(serverInfo);

        showServerIP = new JLabel("IP：" + mainFrame.getTCPClient().getServerIP(), SwingConstants.CENTER);
        showServerIP.setBackground(new java.awt.Color(0, 100, 250));
        showServerIP.setOpaque(true);
        showServerIP.setLocation(550, 220);
        showServerIP.setSize(210, 50);
        showServerIP.setFont(new Font("標楷體", Font.BOLD, 20));
        showServerIP.setForeground(Color.white);
        add(showServerIP);

        showNumberOfPlayer = new JLabel("玩家人數：", SwingConstants.CENTER);
        showNumberOfPlayer.setBackground(new java.awt.Color(0, 100, 250));
        showNumberOfPlayer.setOpaque(true);
        showNumberOfPlayer.setLocation(550, 270);
        showNumberOfPlayer.setSize(210, 50);
        showNumberOfPlayer.setFont(new Font("標楷體", Font.BOLD, 20));
        showNumberOfPlayer.setForeground(Color.white);
        add(showNumberOfPlayer); 
        
        
        int[][] PlayerListColorTable = { { 255, 255, 255 }, { 255, 0, 0 }, { 255, 165, 0 }, { 255, 255, 0 },
                { 0, 255, 0 }, { 0, 127, 255 }, { 0, 0, 255 }, { 139, 0, 255 } };
        showPlayerList = new JLabel[8];
        for (int i = 0; i < 8; i++) {
            showPlayerList[i] = new JLabel("", SwingConstants.CENTER);
            showPlayerList[i].setBackground(new java.awt.Color(PlayerListColorTable[i][0], PlayerListColorTable[i][1],
                    PlayerListColorTable[i][2]));
            showPlayerList[i].setOpaque(true);
            showPlayerList[i].setLocation(150, 180 + 40 * i);
            showPlayerList[i].setSize(300, 40);
            showPlayerList[i].setFont(new Font("標楷體", Font.BOLD, 20));
            showPlayerList[i].setForeground(Color.black);
            add(showPlayerList[i]);
        }
        showPlayerList[0].setText("玩家列表");

        JLabel backgroundLabel = new Background();               
        mainFrame.enterRoom();       
        
        mainFrame.getTCPClient().registOberserver(this); 
        add(backgroundLabel);
    }
    
    public LobbyView(MainFrame mainFrame, int dummy) {
        this(mainFrame);
        startGameButton.setVisible(false);
    }
    public LobbyView(MainFrame mainFrame, Boolean isServer) {
        this(mainFrame);
        startGameButton.setVisible(isServer);
    }
    

    // TCP加
    private LobbyView getLobby() {
        return this;
    }

    // TCP改
    public void receiveNotify(String msg) {
        String[] message = msg.split("#");
        if (message[0].equals("List")) {
            //List#NAME#IP#BURGER
            mainFrame.setUserNum();
            int playerNum = mainFrame.getUserNum();    
            System.out.println("client playerNum = " + playerNum);
            for (int i = 1; i <= playerNum; i++) {
                showPlayerList[i].setText(message[i*3-2]);
            }
            for (int i = playerNum+1; i < showPlayerList.length; i++) {
                
                showPlayerList[i].setText("");
            }
            showNumberOfPlayer.setText("玩家人數：" + String.valueOf(mainFrame.getUserNum()));
            
        } else if (message[0].equals("StartGame")) {
            mainFrame.setUserNum(); 
            mainFrame.getTCPClient().removeOberserver(this);
            
            bgm.close();
            mainFrame.changeView(new GameView(mainFrame));
        } else if (message[0].equals("ServerBreak")) {
            
            mainFrame.getTCPClient().removeOberserver(this);
            mainFrame.setTCPclientnull();
            
            bgm.close();
            mainFrame.changeView(new IDView(mainFrame));

        }
        //send to playerInfo
    }
    
    ActionListener startGameActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          if ( mainFrame.playerInfo.size() >3){
            mainFrame.startGame();
          }            
          else{
            JOptionPane.showMessageDialog(null,"You only have \"" + mainFrame.playerInfo.size() + "\" person.\n");
          }
           
        }
    };
    ActionListener comeBackActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (mainFrame.isServerExist()) {
                mainFrame.getTCPClient().removeOberserver(getLobby());
                mainFrame.endConnection();
            } else {
                mainFrame.getTCPClient().removeOberserver(getLobby());
                mainFrame.getTCPClient().endConnection();
            }
            // TCP end
            mainFrame.setTCPclientnull();
            bgm.close();
            mainFrame.changeView(new IDView(mainFrame));
        }
    };
}
