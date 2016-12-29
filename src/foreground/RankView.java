package foreground;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.InetAddress;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import data.Player;
import data.PlayerInfo;
import sound.EffectPlayer;
import sound.SoundPlayer;

public class RankView extends JPanel {

    private JPanel[] showPlayerRank;
    private JButton newGame;
    private JButton exitGame;
    private MainFrame mainFrame;
    private int NumberOfPlayer = 7;
    private boolean isServer;
    private String IP;
    
    //setting frame information
    private final int height = 50;
    private final int winnerWidth = 80;
    private final int playerIDWidth = 220;
    private final int burgerWidth = 125;
    private final Point winnerLocation = new Point(0, 0);
    private final Point playerIDLocation = new Point(80, 0);
    private final Point burgerLocation = new Point(300, 0);
    
    private final int[][] PlayerListColorTable = { { 255, 255, 255 }, { 255, 0, 0 }, { 255, 165, 0 }, { 255, 255, 0 },
            { 0, 255, 0 }, { 0, 127, 255 }, { 0, 0, 255 }, { 139, 0, 255 } };
    private final Font fontStyle = new Font("Serif", Font.BOLD, 20);
    private final Border borderStyle = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    
    Background2 backgroundLabel2;
    Background backgroundLabel;
    SoundPlayer bgm;
    
    public RankView(MainFrame mainFrame) {        
        this.mainFrame = mainFrame;
        mainFrame.initView(this);
        mainFrame.setTitle(this,"排 行 榜");
        
        bgm = new SoundPlayer("sound/rankBGM.mp3", true);
        bgm.start();
        
        NumberOfPlayer = mainFrame.playerInfo.size();
        
        showPlayerRank = new JPanel[NumberOfPlayer+1];  
        
        //setting Background
        for(int i=0; i<showPlayerRank.length; i++) {
            showPlayerRank[i] = new JPanel();
            showPlayerRank[i].setLayout(null);
            showPlayerRank[i].setBackground(new java.awt.Color(PlayerListColorTable[i][0],
                  PlayerListColorTable[i][1], PlayerListColorTable[i][2]));
            showPlayerRank[i].setLocation(170, 140 + height * i);
            showPlayerRank[i].setSize(425, height);
            add(showPlayerRank[i]);
        }
        
        //Test information
        /*
        Player player = new Player("sgfd", "123");
        player.setBurger(50);        
        mainFrame.playerInfo.add(new Player("rwere", "12.23"));
        mainFrame.playerInfo.add(new Player("1314342", "12.23"));
        mainFrame.playerInfo.add(new Player("rweqwqwerre", "12.23"));
        mainFrame.playerInfo.add(new Player("rweafasdfasre", "12.23"));
        mainFrame.playerInfo.add(new Player("rweadfewfre", "12.23"));
        mainFrame.playerInfo.add(player);
        mainFrame.playerInfo.add(player);
        */
        
        //get the winner information
        int winner = 0;
        for(int i=1; i<this.mainFrame.playerInfo.size(); i++) {
            if(this.mainFrame.playerInfo.get(winner).getBurger() < this.mainFrame.playerInfo.get(i).getBurger()) {
                winner = i;
            }
        }
        
        //show label
        JLabel showWinnerLabel = new JLabel("Winner");
        showWinnerLabel.setLocation(winnerLocation);
        showWinnerLabel.setSize(winnerWidth, height);
        showWinnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        showWinnerLabel.setFont(fontStyle);
        showWinnerLabel.setBorder(borderStyle);
        showPlayerRank[0].add(showWinnerLabel);
        
        JLabel showPlayerLabel = new JLabel("Player's Name");
        showPlayerLabel.setLocation(playerIDLocation);
        showPlayerLabel.setSize(playerIDWidth, height);
        showPlayerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        showPlayerLabel.setFont(fontStyle);
        showPlayerLabel.setBorder(borderStyle);
        showPlayerRank[0].add(showPlayerLabel);
        
        JLabel showBurgerLabel = new JLabel("Burger");
        showBurgerLabel.setLocation(burgerLocation);
        showBurgerLabel.setSize(burgerWidth, height);
        showBurgerLabel.setHorizontalAlignment(SwingConstants.CENTER);        
        showBurgerLabel.setFont(fontStyle);
        showBurgerLabel.setBorder(borderStyle);
        showPlayerRank[0].add(showBurgerLabel);
        
        //show player information
        for(int i=0; i<this.mainFrame.playerInfo.size(); i++) {
            JLabel showWinner = new JLabel();
            if(i == winner ) {
                ImageIcon imageIcon = new ImageIcon(new ImageIcon("image/Ranking/winner.png").getImage().getScaledInstance(height, height, Image.SCALE_DEFAULT));
                showWinner.setIcon(imageIcon);
            }
            showWinner.setForeground(new Color(255-PlayerListColorTable[i+1][0], 255-PlayerListColorTable[i+1][1], 255-PlayerListColorTable[i+1][2]));
            showWinner.setLocation(winnerLocation);
            showWinner.setSize(winnerWidth, height);
            showWinner.setHorizontalAlignment(SwingConstants.CENTER);        
            showWinner.setFont(fontStyle);
            showWinner.setBorder(borderStyle);
            showPlayerRank[i+1].add(showWinner);
            
            JLabel showPlayerID = new JLabel(this.mainFrame.playerInfo.get(i).getName());
            showPlayerID.setForeground(new Color(255-PlayerListColorTable[i+1][0], 255-PlayerListColorTable[i+1][1], 255-PlayerListColorTable[i+1][2]));
            showPlayerID.setLocation(playerIDLocation);
            showPlayerID.setSize(playerIDWidth, height);
            showPlayerID.setHorizontalAlignment(SwingConstants.CENTER);        
            showPlayerID.setFont(fontStyle);
            showPlayerID.setBorder(borderStyle);
            showPlayerRank[i+1].add(showPlayerID);
            
            JLabel showBurgerNumber = new JLabel(Integer.toString(this.mainFrame.playerInfo.get(i).getBurger()));
            showBurgerNumber.setForeground(new Color(255-PlayerListColorTable[i+1][0], 255-PlayerListColorTable[i+1][1], 255-PlayerListColorTable[i+1][2]));
            showBurgerNumber.setLocation(burgerLocation);
            showBurgerNumber.setSize(burgerWidth, height);
            showBurgerNumber.setHorizontalAlignment(SwingConstants.CENTER);        
            showBurgerNumber.setFont(fontStyle);
            showBurgerNumber.setBorder(borderStyle);
            showPlayerRank[i+1].add(showBurgerNumber);
        }
            
        
        newGame = new JButton("繼續遊戲");
        newGame.setBounds(650, 200, 100, 100);
        newGame.setVisible(true);
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent me) {
                if(isServer){
                    try {
                        String name = mainFrame.playerInfo.get(0).getName();
                        mainFrame.endConnection();
                        mainFrame.setTCPclientnull();
                        
                        mainFrame.creatUser(name);                                       
                        mainFrame.playerInfo = new PlayerInfo();                    
                        mainFrame.newServer();                      
                        String IP = InetAddress.getLocalHost().getHostAddress().toString();
                        mainFrame.newTCPClient(IP);   
                        mainFrame.setUserIP();
                        mainFrame.getTCPClient().registOberserver(mainFrame.playerInfo);
                        
                        bgm.close();
                        mainFrame.changeView(new LobbyView(mainFrame)); 
                        
                    }catch (Exception exception) {
                            JOptionPane.showMessageDialog(null, "ERROR!!!", "溫馨提醒@@?", JOptionPane.INFORMATION_MESSAGE);               
                    }
                }else{
                    try {                  
                            mainFrame.getTCPClient().endConnection();
                            mainFrame.setTCPclientnull();
                            
                            mainFrame.playerInfo = new PlayerInfo();
                            mainFrame.newTCPClient(IP);                    
                            mainFrame.setUserIP();
                            mainFrame.getTCPClient().registOberserver(mainFrame.playerInfo);
                            
                            bgm.close();
                            mainFrame.changeView(new LobbyView(mainFrame,35324));
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(null, "要輸入遊戲位置喔", "溫馨提醒@@?", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                //mainFrame.closeServer();
                //mainFrame.playerInfo.removeAll(mainFrame.playerInfo);
                
                //mainFrame.changeView(new LobbyView(mainFrame));
                
            }
        });
        add(newGame);
        
        exitGame = new JButton("離開遊戲");
        exitGame.setBounds(650, 350, 100, 100);
        exitGame.setVisible(true);
        exitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent me) {
                if (mainFrame.isServerExist()) {                   
                    mainFrame.endConnection();
                } else { 
                    mainFrame.getTCPClient().endConnection();
                }
                // TCP end
                mainFrame.setTCPclientnull();
                
                bgm.close();
                mainFrame.changeView(new IDView(mainFrame));
            }
        });
        add(exitGame);
        
        backgroundLabel2 = new Background2();
        backgroundLabel = new Background();
        
        JLabel button= new JLabel();
        button.setSize(50, 50);
        button.setLocation(0, 550);
        button.addMouseListener(
            new MouseListener() {
              
              @Override
              public void mouseReleased(MouseEvent e) {}             
              @Override
              public void mousePressed(MouseEvent e) {}              
              @Override
              public void mouseExited(MouseEvent e) {}        
              @Override
              public void mouseEntered(MouseEvent e) {}
              
              @Override
              public void mouseClicked(MouseEvent e) {
                mainFrame.background = !mainFrame.background;
                if(mainFrame.background){
                  backgroundLabel2.setIcon(null);
                  backgroundLabel.setIcon(new ImageIcon("image/background.png"));
                  repaint();                  
                }
                else {
                  backgroundLabel2.setIcon(new ImageIcon(new ImageIcon("image/title2.png").getImage().getScaledInstance(195, 130, Image.SCALE_DEFAULT)));
                  backgroundLabel.setIcon(new ImageIcon("image/background5.png"));
                  repaint();
                }
              }
            });
        
        add(button);

        if(mainFrame.background){
          backgroundLabel2.setIcon(null);
          backgroundLabel.setIcon(new ImageIcon("image/background.png"));
          repaint();                  
        }
        else {
          backgroundLabel2.setIcon(new ImageIcon(new ImageIcon("image/title2.png").getImage().getScaledInstance(195, 130, Image.SCALE_DEFAULT)));
          backgroundLabel.setIcon(new ImageIcon("image/background5.png"));
          repaint();
        }
        
        add(backgroundLabel2); 
        add(backgroundLabel);
        
        EffectPlayer effectSound = new EffectPlayer("sound/rank.mp3");
        effectSound.start();
    }
    
    public RankView(MainFrame mainFrame, Boolean isServer) {
        
        this(mainFrame);
        newGame.setVisible(isServer);
        this.isServer = isServer;
        this.IP = mainFrame.getServerIP();
    }
}
