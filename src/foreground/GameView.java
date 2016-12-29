package foreground;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Console;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Sprite.AnimationPlayer;
import UDP.update.server.UDPServer;
import background.Observer;
import map.Map;
import sound.EffectPlayer;
import sound.SoundPlayer;
import character.Character;
import character.Ghost;
import data.PlayerInfo;
import data.UserInfo;

public class GameView extends JPanel implements Observer{

    private MainFrame mainFrame;
    private TimerTask getDataTask;
    private TimerTask computeTask;
    private TimerTask sendDataTask;
    //private TimerTask collisionTask;
    //private TimerTask itemLifetime;
    private Timer gameTimer;
    private final int gameTime = 300*1000;
    private int moveKeyBuffer,itemKeyBuffer; 
    private JPanel timerPanel; 
    private int gameRunTime = 11;
    private AnimationPanel animationPanel;
    private Map mapPanel;
    private int myID;
    private Character[] character;
    private PlayerRanking playerList;
    private UDPServer UDPserver;
    SoundPlayer bgm;

    public GameView(MainFrame mainFrame){
        
        this.mainFrame = mainFrame;
        //userNum = mainFrame.getUserNum();
        mainFrame.getTCPClient().registOberserver(this);   
        
        mainFrame.initView(this);
        initUDPServer();        
        creatTask();
        new Character().loadImg();
        character = new Character[mainFrame.getUserNum()+1];
        character[0] = new Ghost();
        for(int i =1;i<mainFrame.getUserNum()+1;i++){
            character[i] = new Character(i); 
        }
        
        bgm = new SoundPlayer("sound/GameBGM.mp3", true);
        bgm.start();
        
        mainFrame.setUserID();
        myID= mainFrame.getUserID();
        mainFrame.setUserCharacter(character[myID]);
        
        
        animationPanel = new AnimationPanel(character,mainFrame.getUserID());
        animationPanel.setLayout(null);
        animationPanel.setLocation(10, 30);
        animationPanel.setSize(540, 540);
        animationPanel.setOpaque(false);
        add(animationPanel); 
        
        
        
        mapPanel = new Map(mainFrame.getUserNum());
        mapPanel.setLayout(null);
        mapPanel.setLocation(10, 30);
        mapPanel.setSize(540, 540);
        mapPanel.setOpaque(false);
        add(mapPanel); 
               

        playerList = new PlayerRanking(mainFrame.playerInfo);
        playerList.setLayout(null);
        playerList.setLocation(560, 30);
        playerList.setSize(230, 280);
        add(playerList);

        timerPanel = new JPanel();
        timerPanel.setLayout(null);
        timerPanel.setLocation(560, 320);
        timerPanel.setSize(230, 70);
        add(timerPanel);
        
        //JOptionPane.showMessageDialog(null,"You only have \"" + myID + "\" person.\n");
        
        UserItem userInfo = new UserItem(mainFrame);
        userInfo.setLayout(null);
        userInfo.setLocation(560, 400);
        userInfo.setSize(230, 180);//item:75*75
        add(userInfo);
        
        gameTimer = new Timer();
        int delaytime = 0;
        gameTimer.schedule(getDataTask, 0+delaytime, 42);            
        gameTimer.schedule(computeTask, 11+delaytime, 42);          
        gameTimer.schedule(sendDataTask, 21+delaytime, 42);           

        
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()>=  KeyEvent.VK_LEFT &&e.getKeyCode()<=  KeyEvent.VK_DOWN)
                    moveKeyBuffer = e.getKeyCode();
                else if(e.getKeyCode() == KeyEvent.VK_Z|| e.getKeyCode() == KeyEvent.VK_X)
                    itemKeyBuffer = e.getKeyCode();
            }
            
            public void keyReleased(KeyEvent e) {
                
                if(e.getKeyCode() == moveKeyBuffer)
                    moveKeyBuffer = 0;
                else if(e.getKeyCode() == itemKeyBuffer)
                    itemKeyBuffer = 0;
            }
        });


        JLabel backgroundLabel = new Background();
        add(backgroundLabel);

        EffectPlayer effetctSound = new EffectPlayer("sound/coGame.mp3");
        effetctSound.start();
    }
    private void creatTask(){
        getDataTask = new TimerTask() {
            public void run() {
                //send 位移 to server
                
                int speedup = 1;
                String movementMessage = Integer.toString(mainFrame.getUserID())+"#";
                int speed =(int)mainFrame.userInfo.getCharacter().getSpeed()*speedup;
                if (moveKeyBuffer == KeyEvent.VK_LEFT) {
                    movementMessage +=Integer.toString(-1*speed) + "#0#";
                } else if (moveKeyBuffer == KeyEvent.VK_UP) {
                    movementMessage +="0#"+Integer.toString(-1*speed) + "#";
                } else if (moveKeyBuffer == KeyEvent.VK_RIGHT) {
                    movementMessage +=Integer.toString(1*speed) + "#0#";
                } else if (moveKeyBuffer == KeyEvent.VK_DOWN) {
                    movementMessage +="0#"+Integer.toString(1*speed) +"#";
                } 
                else {
                    movementMessage +="0#0#";
                }
                
                if(itemKeyBuffer == KeyEvent.VK_Z){
                    System.out.println("gameTime: " + gameTimer);
                    mainFrame.userInfo.getCharacter().useItem(0,gameTimer);                  
                }else if(itemKeyBuffer == KeyEvent.VK_X){
                    mainFrame.userInfo.getCharacter().useItem(1,gameTimer);
                }
                //ID#X#Y

                mainFrame.getTCPClient().setDisplacement(movementMessage);

                //System.out.println("client task 1 end " + System.currentTimeMillis());
            }
        };
        computeTask = new TimerTask() {
            JLabel showTimeLabel = new JLabel();
            
            public void run() {
                //System.out.println("client task 2 start " + System.currentTimeMillis());
                requestFocus();
                showTimeLabel.setText("遊戲倒數" + Integer.toString((gameTime - gameRunTime)/1000) + "秒");
                showTimeLabel.setBounds(10, 0, 230, 70);
                showTimeLabel.setFont(new Font("標楷體", Font.BOLD, 30));
                showTimeLabel.setForeground(Color.RED);
                ;
                timerPanel.add(showTimeLabel);
                repaint();
                //gameRunTime = gameRunTime + 42;
             // idle
                //System.out.println("client task 2 end "+ System.currentTimeMillis());
            }
        };
        sendDataTask = new TimerTask() {
            public void run() {
                //System.out.println("client task 3 start " + System.currentTimeMillis());
                // get data from server
                //System.out.println("START + " + System.currentTimeMillis());
                String msg = UDPserver.getReceiveMsg();
                //System.out.println(msg);
                //處理字串
                String[] message = msg.split("#");
                
                
                //顯示地圖
                //System.out.println(message.length);
//                if(message.length == 8)
//                    System.out.println(msg);  
                if(message.length > 0)
                {   
                    int ghostID = Integer.valueOf(message[1]);
                    playerList.setGhostID(Integer.valueOf(message[1])-1);
                    if(ghostID == myID){
                        mainFrame.setUserCharacter(character[0]);
                        //System.out.println("change OK!!");
                    }
                    else{
                        mainFrame.setUserCharacter(character[myID]); 
                    }   
                    String burgerString = message[mainFrame.getUserNum()*3+2];
                    Point playerLocation = new Point(Integer.valueOf(message[myID*3-1]), Integer.valueOf(message[myID*3]));
                    
                    mapPanel.setViewBurgerString(burgerString, playerLocation);  
                    gameRunTime = Integer.valueOf(message[message.length-1]);
                    //System.out.println("end " + System.currentTimeMillis());
                    for(int i = 1;i<mainFrame.getUserNum()+1;i++){
                        int x = Integer.valueOf(message[i*3-1]);
                        int y = Integer.valueOf(message[i*3]);
                        //System.out.println(i+"~~~~~~~~~~~~~~~~~~~");
                        character[i].setCoordinate(new Point(x,y));
                        //character[i].setCoordinate(new Point(character[i].getCoordinate().x+10,character[i].getCoordinate().y));
                        //character[i].setMovement(new Point(10,0));
                        mainFrame.setPlayerBurger(Integer.valueOf(message[i*3+1]),i-1);
                        
                    }
                    character[0].setCoordinate(character[ghostID].getCoordinate());
                    character[ghostID].setCoordinate(new Point(-60, -60));
                }
                
                if( mainFrame.userInfo.getCharacter() != character[0]){
                  
                  Point p = mainFrame.userInfo.getCharacter().getCoordinate();
                  
                  if ( mapPanel.isBurger(p)){
                    EffectPlayer effectSound = new EffectPlayer("sound/eat.mp3");
                    effectSound.start();
                  }
                  
                  if ( p.distance(character[0].getCoordinate()) <= 40 ){
                    EffectPlayer effectSound = new EffectPlayer("sound/catch.mp3");
                    effectSound.start();
                  }
                }
                //Point myCoordinate = mainFrame.getUserCoordinate();
                animationPanel.update(character,mainFrame.userInfo.getCharacter());
                playerList.setPlayerInfo(mainFrame.playerInfo);
                //System.out.println("client task 3 end " + System.currentTimeMillis());
                //System.out.println("end " + System.currentTimeMillis());
                //更新玩家資訊
                
                /*
                character[0].setCoordinate(new Point(Integer.valueOf(message[ghostID*3-1]), Integer.valueOf(message[ghostID*3])));
                character[ghostID].setCoordinate(new Point(-1, -1));
                */
                
            }
        };
        /*
        collisionTask = new TimerTask() {
            public void run() {
                //畫圖

            }
        };*/
        /*
        itemLifetime = new TimerTask(){
            public void run(){
                
            }
        };*/
    }
    private void initUDPServer() {
        UDPserver = new UDPServer();
        UDPserver.initUDPServer();
    }
    public void receiveNotify(String msg) {
        String[] message = msg.split("#");
        if (message[0].equals("InitWalkable")) {
            mapPanel.setMapInformationString(message[1]);
        }else if(message[0].equals("InitCoordinate")){            
            for(int i = 0;i<character.length;i++)
            {
                int x = Integer.valueOf(message[i*2+1]);
                int y = Integer.valueOf(message[i*2+2]);
                character[i].setCoordinate(new Point(x, y));
            }
            //設定character位置
        }else if(message[0].equals("GameOver")){
            getDataTask.cancel();
            computeTask.cancel();
            sendDataTask.cancel();
            //collisionTask.cancel();
            gameTimer.cancel();
            mainFrame.getTCPClient().removeOberserver(this);
            //UDPClose();
            UDPserver.endConnection();
            bgm.close();
            mainFrame.changeView(new RankView(mainFrame,myID == 1));
        }else if (message[0].equals("ServerBreak")) {
            getDataTask.cancel();
            computeTask.cancel();
            sendDataTask.cancel();
            gameTimer.cancel();
            

            UDPserver.endConnection();
            
            mainFrame.getTCPClient().endConnection();           

            mainFrame.getTCPClient().registOberserver(mainFrame.playerInfo); 
            mainFrame.getTCPClient().removeOberserver(this);
            mainFrame.setTCPclientnull();
            
            bgm.close();
            mainFrame.changeView(new IDView(mainFrame));

        }
        
        //send to playerInfo
    }

}
