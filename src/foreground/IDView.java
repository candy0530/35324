package foreground;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.InetAddress;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import background.OurKeyListener;
import data.UserInfo;
import sound.SoundPlayer;
import data.PlayerInfo;;


public class IDView extends JPanel {

    private MainFrame mainFrame;
    private JTextField userIDText;
    SoundPlayer bgm;

    Background2 backgroundLabel2;
    Background backgroundLabel;

    public IDView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        mainFrame.initView(this);
        mainFrame.setTitle(this, "漢 堡 神 偷 王");
        mainFrame.setGameRuleButton(this);
        
        bgm = new SoundPlayer("sound/enterBGM.mp3", true);
        bgm.start();

        JLabel userIDLabel = new JLabel("遊戲暱稱");
        userIDLabel.setLocation(300, 150);
        userIDLabel.setSize(70, 100);
        add(userIDLabel);
        

        userIDText = new JTextField();
        userIDText.setLocation(370, 175);
        userIDText.setSize(130, 50);
        userIDText.addKeyListener(new OurKeyListener());
        add(userIDText);
        
        if(mainFrame.userInfo != null)
            userIDText.setText(mainFrame.userInfo.getName()); 
        JButton newGame = new JButton();
        try {
            //Image img = ImageIO.read(getClass().getResource("image/button/newGame.png"));
            newGame.setIcon(new ImageIcon("image/button/newGame.png"));
            newGame.setLayout(null);
            newGame.setContentAreaFilled(false);
            newGame.setBorderPainted(false);
          } catch (Exception ex) {
            System.out.println("newGameImage NOT found");
          }
        newGame.setBounds(300, 250, 200, 100);
        newGame.setVisible(true);
        newGame.addActionListener(newGameActionListener);
        add(newGame);

        JButton joinGame = new JButton();
        try {
            //Image img = ImageIO.read(getClass().getResource("image/button/newGame.png"));
            joinGame.setIcon(new ImageIcon("image/button/joinGame.png"));
            joinGame.setLayout(null);
            joinGame.setContentAreaFilled(false);
            joinGame.setBorderPainted(false);
          } catch (Exception ex) {
            System.out.println("joinGameImage NOT found");
          }
        joinGame.setBounds(300, 400, 200, 100);
        joinGame.setVisible(true);
        joinGame.addActionListener(joinGameActionListener);
        add(joinGame);

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
    }

    ActionListener newGameActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          //mainFrame.creatUser(userIDText.getText().toString());                            
          //mainFrame.playerInfo = new PlayerInfo();                    
          //mainFrame.newServer();
          //String IP = "";  
          //mainFrame.newTCPClient(IP);     
          //mainFrame.setUserIP();                   
          //mainFrame.getTCPClient().registOberserver(mainFrame.playerInfo);                      
          //mainFrame.changeView(new LobbyView(mainFrame));
            try {
                if (userIDText.getText().equals("")) {                    
                    throw new Exception();
               } else {
                    mainFrame.creatUser(userIDText.getText().toString());                                       
                    mainFrame.playerInfo = new PlayerInfo();                    
                    mainFrame.newServer();
                    String IP = InetAddress.getLocalHost().getHostAddress().toString();
                    mainFrame.newTCPClient(IP);   
                    mainFrame.setUserIP();
                    mainFrame.getTCPClient().registOberserver(mainFrame.playerInfo);                      
                    mainFrame.changeView(new LobbyView(mainFrame));
                    bgm.close();
                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(null, "要輸入遊戲暱稱喔", "溫馨提醒@@?", JOptionPane.INFORMATION_MESSAGE);               
            }
        }
    };
    ActionListener joinGameActionListener = new ActionListener() {       
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (userIDText.getText().equals("")) {
                    throw new Exception();
                } else {
                    mainFrame.creatUser(userIDText.getText().toString());                    
                    mainFrame.userInfo = new UserInfo(userIDText.getText().toString());
                    mainFrame.changeView(new IPView(mainFrame, bgm));
                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(null, "要輸入遊戲暱稱喔", "溫馨提醒@@?", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    };

}
