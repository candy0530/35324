package background;

import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import TCP.TCPServer;
import UDP.broadcast.client.UDPClient;
import map.Map;
import data.Player;
import data.PlayerInfo;
import character.Character;
import character.Ghost;

public class MainServer implements Observer{

    private TCPServer TCPserver = null;
    private UDPClient UDPclient;
    private Map map;
    private PlayerInfo playerInfo;
    private Timer gameTimer;
    //private TimerTask getDataTask;
    private TimerTask computeTask;
    private TimerTask sendDataTask;
    private TimerTask collisionTask;
    private TimerTask firstGhostTask;
    private Character[] characters;
    private Character[] record;
    private Vector<String> TCPdataBuffer;
    private int playerNum;
    private int characterNum;
    private int GhostID = 0;
    private int WinnerID;

    private final int gameTime = 300 * 1000;
    private final int firstGhostTime = 30 * 1000;
    private int gameRunTime = 32;  
    public MainServer() {
        newTCPServer();

        playerInfo = new PlayerInfo();        

        TCPdataBuffer = new Vector<String>();
        TCPserver.registOberserver(playerInfo);
    }

    private void creatTask() {
        /*
        getDataTask = new TimerTask() {
            public void run() {                              
                //System.out.println("~~~~~~~~~~~~~~~~~~~~~~");
                // add user info to buffer
            }
        };*/
        computeTask = new TimerTask() {
            public void run() {
                // 可否移動
              //System.out.println("server task 1 start " + System.currentTimeMillis());
                Vector<String> uploadBuffer = new Vector<String>();
                uploadBuffer.addAll(TCPdataBuffer);
                TCPdataBuffer.clear();
                for (int i = 0; i < uploadBuffer.size(); i++) {
                    String data = uploadBuffer.get(i);
                    String[] splitdata = data.split("#");
                    
                    Point movement = new Point(Integer.valueOf(splitdata[2]), Integer.valueOf(splitdata[3]));
                    int x = record[Integer.valueOf(splitdata[1])].getCoordinate().x + Integer.valueOf(splitdata[2]);
                    int y = record[Integer.valueOf(splitdata[1])].getCoordinate().y + Integer.valueOf(splitdata[3]);

                    Point point = new Point(x, y);
                    if (map.isWalkable(point))
                        record[Integer.valueOf(splitdata[1])].setMovement(movement);
                
                }
                //System.out.println("server task 1 end " + System.currentTimeMillis());
            }
        };
        sendDataTask = new TimerTask() {
            public void run() {
                //System.out.println("server task 2 start " + System.currentTimeMillis());
                String sendMsg = "";
                sendMsg = sendMsg + "1#" + GhostID;//第一名&警察
                for(int i = 1;i<record.length;i++)
                {
                    sendMsg = sendMsg + "#" +record[i].getCoordinate().x+"#" + record[i].getCoordinate().y;
                    sendMsg = sendMsg + "#" +playerInfo.get(i-1).getBurger();
                }
                
                
                for (int i = 1; i < characterNum; i++) {
                    //UDP封包格式
                    String sendMsgAll =sendMsg+"#"+ map.getViewBurgerString(record[i].getCoordinate());                    
                    sendMsgAll = sendMsgAll + "#" + gameRunTime;
                    UDPclient.setSendMsg(i-1, sendMsgAll);
                }
                //System.out.println("server task 2 end  " + System.currentTimeMillis());
            }
        };
        collisionTask = new TimerTask() {
            //int gameRunTime = 32;           
            
            public void run() {
                //System.out.println("server task 3 start " + System.currentTimeMillis());
                if(gameRunTime >= gameTime ){// or 鬼離開 or 有人漢堡超過max
                    gameover();                    
                    }                
                gameRunTime = gameRunTime + 42;
                for (int i = 1; i < characterNum; i++) 
                {
                    // 人與漢堡
                    if (map.isBurger(record[i].getCoordinate()) &(i != GhostID)) 
                    {
                       map.eatBurger(record[i].getCoordinate().x, record[i].getCoordinate().y);
                       int burger = playerInfo.get(i-1).getBurger()+1;
                       playerInfo.get(i-1).setBurger(burger);
                       if(burger > (map.getMaxBurger()*2/3))
                           gameover(); 
                           
                    }
                    // 人與鬼
//                    if (distance(record[i].getCoordinate(), characters[0].getCoordinate())<40 ){
                    if ( record[i].getCoordinate().distance( characters[0].getCoordinate()) < 40){
                        if(i== GhostID)
                            continue;
                        int Burger = playerInfo.get(i-1).getBurger();                       
                        setNormal(GhostID,Burger/3);                       
                        setGhost(i-1,Burger-(Burger/3));
                        /*
                        map.setSpreadBurger(Burger-(Burger/3));
                        playerInfo.get(i-1).setBurger(0);
                        playerInfo.get(GhostID-1).setBurger((Burger/3));                                              
                        characters[GhostID].setCoordinate(characters[0].getCoordinate());                        
                        record[i] = characters[0];                        
                        record[GhostID]= characters[GhostID];                        
                        GhostID = i;                       
                        characters[0].setCoordinate(map.getPrison());
                        
                        gameTimer.schedule(new TimerTask() {                           
                            @Override
                            public void run() {
                                characters[0].setCoordinate(characters[GhostID].getCoordinate());
                                characters[GhostID].setCoordinate(new Point(-1, -1));                                
                            }
                        }, 3000);*/
                       
                    }
                }
            }
        };        
        firstGhostTask = new TimerTask() {
            public void run() {
                int minburgreplayerID = 0;
                for (int i = 0; i < playerInfo.size(); i++) {
                    if((playerInfo.get(i).getBurger() < playerInfo.get(minburgreplayerID).getBurger())&&
                         (record[i+1].getCoordinate().x != (map.getPrison().x+60)&&
                         record[i+1].getCoordinate().y != (map.getPrison().y+60)))
                        minburgreplayerID = i;
                }
                
                int Burger = playerInfo.get(minburgreplayerID).getBurger();
                setGhost(minburgreplayerID,Burger);
                /*
                map.setSpreadBurger(Burger);
                playerInfo.get(minburgreplayerID).setBurger(0);
                minburgreplayerID = minburgreplayerID +1;                
                record[minburgreplayerID] = characters[0];
                GhostID = minburgreplayerID;                
                characters[0].setCoordinate(map.getPrison());
                gameTimer.schedule(new TimerTask() {                           
                    @Override
                    public void run() {
                        characters[0].setCoordinate(characters[GhostID].getCoordinate());
                        characters[GhostID].setCoordinate(new Point(-1, -1)); 
                    }
                }, 3000);*/
                firstGhostTask.cancel();
            }
        };
    }

    private void setGhost(int ID,int burger){
        map.setSpreadBurger(burger);
        playerInfo.get(ID).setBurger(0);
        ID++;           
        record[ID] = characters[0];
        GhostID = ID;                
        characters[0].setCoordinate(map.getPrison());
        gameTimer.schedule(new TimerTask() {                           
            @Override
            public void run() {
                characters[0].setCoordinate(characters[GhostID].getCoordinate());
                characters[GhostID].setCoordinate(new Point(-1, -1)); 
            }
        }, 5000);
        //firstGhostTask.cancel();
    }
    private void setNormal(int ID,int burger){
        playerInfo.get(ID-1).setBurger(burger); 
        characters[ID].setCoordinate(characters[0].getCoordinate());
        record[ID]= characters[ID];   
    }

    public void endConnection() {
        if(TCPserver != null)
            TCPserver.endConnection();
        TCPserver = null;
    }

    public boolean isTCPServerExist() {
        return (TCPserver != null);
    }

    private void newTCPServer() {
        TCPserver = new TCPServer(this);
        TCPserver.connect();
    }

    public void getReady() {
        setPlayerNum();
        characterNum = playerNum + 1;
        newUDPClient();

        map = new Map(playerNum);
        map.mapGenerator();

        for (int i = 0; i < playerNum; i++) {
            UDPclient.setSendMsg(i, "");
        }

        UDPclient.startUDPBroadCast();
        characters = new Character[characterNum];
        record = new Character[characterNum];
        Point[] coordinate = new Point[characterNum];           
        coordinate =  map.getPlayerInit();

        characters[0] = new Ghost();
        //characters[0] = new Character();// 改成警察
        record[0] = new Ghost();
        //record[0] = new Character();
        
        for (int i = 1; i < characterNum; i++) {
            characters[i] = new Character();
            record[i] = characters[i];
            record[i].setCoordinate(coordinate[i]);
        }
        // send map、user coordinate
        TCPserver.sendList(getlist());
        TCPserver.sendStart();       
        TCPserver.sendInitWalkable(map.getMapInformationString());// walkable = (string)010101      
        
        Point[] initcoordinate = map.getPlayerInit();
        //System.out.println("initcoordinate = " + initcoordinate[0] +" "+initcoordinate[1] );
        String initcoordinateMessage = "";
        
        for(int i = 0;i<initcoordinate.length;i++)
        {
            initcoordinateMessage = initcoordinateMessage + initcoordinate[i].x+"#" +  initcoordinate[i].y+"#" ;
        }
        
        gameTimer = new Timer();
        creatTask();
        int delaytime = 0;
        //gameTimer.schedule(getDataTask, 0+delaytime, 42);
        gameTimer.schedule(computeTask, 11+delaytime, 42);
        gameTimer.schedule(sendDataTask, 21+delaytime, 42);
        gameTimer.schedule(collisionTask, 32+delaytime, 42);
        gameTimer.schedule(firstGhostTask, delaytime+firstGhostTime);
        TCPserver.sendInitCoordinate(initcoordinateMessage);// coordinates = all player
                                                  // x#y#
        //把自己加入報社
        TCPserver.registOberserver(this);
    }

    private void gameover() {
        computeTask.cancel();
        sendDataTask.cancel();
        collisionTask.cancel();
        gameTimer.cancel();
        UDPclient.endUDPBroadCast();
        TCPserver.sendList(getlist());
        TCPserver.sendGameOver();
        TCPserver.removeOberserver(this);
    }

    private void newUDPClient() {
        String[] IP = new String[playerNum];
        for(int i = 0;i<playerNum;i++)            
        {
            IP[i] = playerInfo.get(i).getIp();
        }
        UDPclient = new UDPClient(IP);

    }

    public String getlist() {
        String message = "List";      
        try {
            setPlayerNum();
            for (int i = 0; i < playerNum; i++) {
                message = message + "#" + playerInfo.get(i).getName() + "#" + playerInfo.get(i).getIp() + "#"
                        + playerInfo.get(i).getBurger();
            }
            return message;
        } catch (Exception e) {
            System.out.println("List error");
        }       
        return "List error";
    }

    public void setTCPdatatoBuffer(String message) {
        TCPdataBuffer.add(message);

    }
    private void setPlayerNum(){
        this.playerNum = playerInfo.size();
    }
    private double distance(Point a,Point b){
        double dis;
        dis = Math.sqrt(Math.pow(b.x-a.x, 2) + Math.pow(b.y - a.y, 2));
        return dis;
    }
    
    public void receiveNotify(String msg) {
        String[] message = msg.split("#");
        if (message[0].equals("Remove")) {
            for(int i  =0;i<playerInfo.size();i++)
            {
                if(message[1].equals(playerInfo.get(i).getIp()))
                {
                    
                    characters[i+1].setCoordinate(new Point(map.getPrison().x+60,map.getPrison().y)); 
                    int burger = playerInfo.get(i).getBurger();
                    map.setSpreadBurger(burger);
                    playerInfo.get(i).setBurger(0);
                    
                    if((i+1) == GhostID)
                        gameover();
                }
 
            }
            
        }
    }
   public void removeServer(){
       System.out.println("removeServer OK");
       
        //playerInfo.removeAll(playerInfo);
    }
}
