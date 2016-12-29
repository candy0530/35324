package foreground;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.PublicKey;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import TCP.TCPClient;
import UDP.update.server.UDPServer;
import background.MainServer;
import character.Character;
import data.PlayerInfo;
import data.UserInfo;
import sound.EffectPlayer;
import character.Character;

public class MainFrame extends JFrame {

    private JPanel mainPanel;
    // TCP改
    private TCPClient client = null;
    
    private GameRule gameRule;
    private final int viewWidth = 800;
    private final int viewHeight = 600;
    private MainServer server = null;
    public UserInfo userInfo = null;
    public PlayerInfo playerInfo;
    private int userNum;


    public MainFrame() {
        setTitle("瘋狂鬼抓人");
        setSize(805, 620);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        EffectPlayer.loadSound();
        EffectPlayer effectSound = new EffectPlayer("sound/enter.mp3");
        effectSound.start();
        mainPanel = new IDView(this);
            
        add(mainPanel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (server != null) {
                    endConnection();
                }
                if (client != null) {
                    client.endConnection();
                }

            }
        });

    }

    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
    }

    public void changeView(JPanel newPanel) {
        remove(mainPanel);
        mainPanel = newPanel;
        add(mainPanel);
        repaint();
    }

    public void newServer() {
        server = null;
        server = new MainServer();
    }
    public void closeServer() {
        server.removeServer();
        server = null;
        //server = new MainServer();
    }

    public boolean isServerExist() {
        return (server != null);
    }

    public void endConnection() {
        server.endConnection();
    }

    public void newTCPClient(String IPAddress) {
        client = new TCPClient();
        client.connect(IPAddress);
    }



    public TCPClient getTCPClient() {
        return client;
    }

    public void setFocus() {
        mainPanel.requestFocus();

    }

    public void setTCPclientnull() {
        client = null;
    }

    public void showGameRule() {
        gameRule = new GameRule(this);
    }

    public void initView(JPanel panel) {
        panel.setLocation(0, 0);
        panel.setSize(viewWidth, viewHeight);
        panel.setLayout(null);
    }

    public void setTitle(JPanel panel, String Title) {
        JLabel applicationName = new JLabel(Title, SwingConstants.CENTER);
        applicationName.setLocation(0, 30);
        applicationName.setSize(viewWidth, 100);
        applicationName.setFont(new Font("標楷體", Font.BOLD, 60));
        panel.add(applicationName);
    }

    public void setGameRuleButton(JPanel panel) {
        JButton gameRule = new JButton();
        try {
            //Image img = ImageIO.read(getClass().getResource("image/button/newGame.png"));
            gameRule.setIcon(new ImageIcon("image/button/gameRule.png"));
            gameRule.setLayout(null);
            gameRule.setContentAreaFilled(false);
            gameRule.setBorderPainted(false);
          } catch (Exception ex) {
            System.out.println("gameRuleImage NOT found");
          }
        gameRule.setLocation(700, 500);
        gameRule.setSize(50, 50);
        gameRule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGameRule();
            }

        });
        panel.add(gameRule);
    }



    public void enterRoom() {
        // uID = 遊戲暱稱
        client.enterRoom(getUserName());
    }

    public void startGame() {
        server.getReady();
    }

    public void creatUser(String name) {
        userInfo = new UserInfo(name);
    }
    public String getUserName() {
        return userInfo.getName();
    }
    public void setUserIP() {
        userInfo.setIp(client.getMyIP());
    }
    public String getUserIP() {
        return userInfo.getIp();
    }
    public void setUserID(){
        for(int i = 0;i<playerInfo.size();i++)
        {
            //System.out.println("ID = " + playerInfo.get(i).getIp());
            //System.out.println("ID123 = " + userInfo.getIp());
            if(userInfo.getIp().equals(playerInfo.get(i).getIp()))
            {
                userInfo.setId(i+1);
                //System.out.println("MY ID = " + (i+1));
            }

        }
        
    }
    public int getUserID(){
        return userInfo.getId();
    }
    public void setUserCharacter(Character character){
        userInfo.setCharacter(character);
    }
    public Point getUserCoordinate(){
        return userInfo.getCharacter().getCoordinate();
    }
    public int getUserNum(){
        return userNum;
    }
    public void setUserNum(){
        this.userNum = playerInfo.size();
    }
    public void setPlayerBurger(int num,int id){
        playerInfo.get(id).setBurger(num);
    }
    public String getServerIP(){
        return playerInfo.get(0).getIp();
    }

    /*
    public void uploadCharacter(){
        String msg = getUDPMessage();  
        //處理字串
        String[] message = msg.split("#");
        for(int i =1 ;i<characters.length;i++)
        {
            //characters[i].setCoordinate(new Point(Integer.valueOf(message[i*3-1]), Integer.valueOf(message[i*3])));;
            playerInfo.get(i-1).setBurger(Integer.valueOf(message[i*3+1]));
   
        }
        //characters[0].setCoordinate(characters[Integer.valueOf(message[1])].getCoordinate());
        //characters[Integer.valueOf(message[1])].setCoordinate(new Point(-1, -1));
        
    }
    */
}
