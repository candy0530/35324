package foreground;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import background.OurKeyListenerIP;
import data.PlayerInfo;
import data.UserInfo;
import sound.SoundPlayer;

public class IPView extends JPanel {

    private MainFrame mainFrame;
    private JTextField lobbyIpText;
    SoundPlayer bgm;
    
    public IPView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        mainFrame.initView(this);
        mainFrame.setTitle(this,"漢 堡 神 偷 王");
        mainFrame.setGameRuleButton(this);

        JLabel lobbyIpLabel = new JLabel("遊戲ＩＰ");
        lobbyIpLabel.setLocation(300, 150);
        lobbyIpLabel.setSize(70, 100);
        add(lobbyIpLabel);

        lobbyIpText = new JTextField();
        lobbyIpText.setLocation(370, 175);
        lobbyIpText.setSize(130, 50);
        lobbyIpText.addKeyListener(new OurKeyListenerIP());
        add(lobbyIpText);

        JButton confirmButton = new JButton();
        try {
            //Image img = ImageIO.read(getClass().getResource("image/button/newGame.png"));
            confirmButton.setIcon(new ImageIcon("image/button/confirmButton.png"));
            confirmButton.setLayout(null);
            confirmButton.setContentAreaFilled(false);
            //newGame.setBorderPainted(false);
          } catch (Exception ex) {
            System.out.println("confirmButtonImage NOT found");
          }
        confirmButton.setBounds(300, 250, 200, 100);
        confirmButton.setVisible(true);
        confirmButton.addActionListener(confirmActionListener);
        add(confirmButton);

        JButton cancelButton = new JButton();
        try {
            //Image img = ImageIO.read(getClass().getResource("image/button/newGame.png"));
            cancelButton.setIcon(new ImageIcon("image/button/cancelButton.png"));
            cancelButton.setLayout(null);
            cancelButton.setContentAreaFilled(false);
            //newGame.setBorderPainted(false);
          } catch (Exception ex) {
            System.out.println("cancelButtonImage NOT found");
          }
        cancelButton.setBounds(300, 400, 200, 100);
        cancelButton.setVisible(true);
        cancelButton.addActionListener(cancelActionListener);
        add(cancelButton);

        JLabel backgroundLabel = new Background();
        add(backgroundLabel);
    }
    
    public IPView(MainFrame mainFrame, SoundPlayer bgm) {
      this( mainFrame);
      this.bgm = bgm;
    }
    
    ActionListener confirmActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

          //mainFrame.playerInfo = new PlayerInfo();                  
          //mainFrame.newTCPClient(lobbyIpText.getText()); 
          //mainFrame.setUserIP();//get from TCP?
          //mainFrame.enterRoom();  
          //mainFrame.getTCPClient().registOberserver(mainFrame.playerInfo); 
          //send user name to server                    
          //mainFrame.changeView(new LobbyView(mainFrame,35324));
            try {                
                if (lobbyIpText.getText().equals("")) {
                    throw new Exception();
                } else {
                    bgm.close();
                    mainFrame.playerInfo = new PlayerInfo();
                    mainFrame.newTCPClient(lobbyIpText.getText());                    
                    mainFrame.setUserIP();
                    mainFrame.getTCPClient().registOberserver(mainFrame.playerInfo);                    
                    mainFrame.changeView(new LobbyView(mainFrame,35324));
                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(null, "要輸入遊戲位置喔", "溫馨提醒@@?", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    };
    ActionListener cancelActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          bgm.close();
          mainFrame.changeView(new IDView(mainFrame));
        }
    };
}
