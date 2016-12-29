package TCP;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import TCP.TCPClient.Notify;
import background.MainServer;
import background.Observer;
import background.Subject;
import data.PlayerInfo;

public class TCPServer implements Subject,Runnable{		
	//存每個clinet的outstream
	private Hashtable clientOutsteamList =new Hashtable();
	private final int port =35324;
	private ServerSocket serverSock;
	private Socket clientSocket;
	InputStreamReader TCPReader;
	private String Remove = "ListRemove";
	MainServer mainServer;
	private boolean Connection = true;
	
	
	public TCPServer(MainServer mainServer){
	    this.mainServer = mainServer;
	}
	
	//觀察者
	private ArrayList<Observer> observerList =new ArrayList<>();
	private String observerMessage;
	//建立Server
	public void connect() {
		try {
			serverSock = new ServerSocket(port);
			//開啟執行緒
			new Thread(this).start();
		}		catch (Exception ex) {
			System.out.println("[TCPServer] Server建立失敗");
		}
		
	}	
	//執行執行緒
	public void run() {
		//System.out.println("OK");
		while (Connection) {
			try{
				// 等待連線的請求--串流
				clientSocket = serverSock.accept();				
						
				PrintStream writer = new PrintStream(clientSocket.getOutputStream());
				//存每個clinet的outstream
				clientOutsteamList.put(clientSocket,writer);
				// 建立與clientI/O執行緒
				Thread listenRequest = new Thread(new Process( clientSocket));
				// 啟動執行緒
				//if(Connection)
				    listenRequest.start();
				
			}catch (IOException ex) {
			    //ex.printStackTrace();
				System.out.println("Client要求連接失敗");
			}
		}
	}
	
	public void endConnection() {
		try {
			// 結束連線			
			respondAll("ServerBreak#");				
			//System.out.println("End Connect");
			for(Enumeration<PrintStream> one =clientOutsteamList.elements();one.hasMoreElements();)
			{	
				
				try{
					((PrintStream)one.nextElement()).close();					
									
					//System.out.println("[TCPServer] Server關閉clintStream");
				}catch (Exception ex) {
					System.out.println("[TCPServer] 關閉clint失敗: " );//+ ((Socket)one).getRemoteSocketAddress());
				}		
				clientOutsteamList.remove(one);
				//one.nextElement();							
			}
			Connection = false;
			if(TCPReader != null)
			    TCPReader.close();
			serverSock.close();
			
		} catch (IOException ex) {
			System.out.println("[TCPServer] 結束連線失敗");
		}
	}
	//與client傳輸信息的執行緒	
	public class Process implements Runnable {
		//InputStreamReader TCPReader;
	    Socket clientSocket;
	    
	    Process( Socket inputSocket){
                this.clientSocket = inputSocket;
	    }
		
		public void run() {
			
			String getLine;
			try {
				// 取得Socket的輸入資料流
				TCPReader = new InputStreamReader(clientSocket.getInputStream());
				// 暫存資料的Buffered
				BufferedReader reader = new BufferedReader(TCPReader);
				PrintStream writer = (PrintStream)clientOutsteamList.get(clientSocket);
				
				// 讀取資料
				while ((getLine = reader.readLine()) != null) {
				    
					//System.out.println("[TCPServer] "+getLine);
					//--處理input
					String[] clientText = getLine.split("#");
					//--client進入等候室 紀錄其ID且傳玩家列表給所有client
					if(clientText[0].equals("ID")){
						//傳送玩家列表
					    
					    String playerInfoMessage = mainServer.getlist(); 
					    String[] playerInfoSplitMessage = playerInfoMessage.split("#");
					    String IP = clientSocket.getInetAddress().getHostAddress();
					    boolean IPisIn =false;
					    for(int i =0;i<playerInfoSplitMessage.length;i++)
					    {
					        if(i%3==2 && IP.equals(playerInfoSplitMessage[i]))
					        {
					            IPisIn=true;
					            break;
					        }
					            
					    }
					    String message;
					    if(!IPisIn){
    						message = "ListAdd#" + clientText[1] +"#"+ clientSocket.getInetAddress().getHostAddress();    						
					    }
					    else{
					        message = "ListAdd111#";
					    }
                        setObserverMessage(message);
                        // 執行送報
                        notifyAllOberserver();
                        respondAll(mainServer.getlist());
			               //this.notify();
			            
//		                Thread notify = new Thread(new Notify());
//		                // 啟動執行緒
//		                notify.start();	
		                //System.out.println("[TCPServer]123222 "+getLine);
		                //respondAll(mainServer.getlist());
		              
					}
					else if(clientText[0].equals("Leave")){
						//處理output						
						clientOutsteamList.remove(clientSocket);
						String message = Remove+"#"+ clientSocket.getInetAddress().getHostAddress();						
						System.out.println("[TCPServer Leave] "+message);
						setObserverMessage(message);
						// 執行送報
						notifyAllOberserver();
//		                Thread notify = new Thread(new Notify());
//		                // 啟動執行緒
//		                notify.start();
		                respondAll(mainServer.getlist());
					}
					else if(clientText[0].equals("Displacement")){
					    mainServer.setTCPdatatoBuffer(getLine);
					    setObserverMessage(getLine);
					 // 執行送報
					    notifyAllOberserver();
//		                Thread notify = new Thread(new Notify());
//		                // 啟動執行緒
//		                notify.start();					    
					}
//					//--當市長開始遊戲，通知所有clinet開始遊戲
//					else if(clientText[0].equals("StartGame")){
//						//處理output
//						String message = "StartGame#";
//						//回傳信息給ALL
//						respondAll(message);
//					}				
					
					
				}
			} catch (Exception ex) {
				//ex.printStackTrace();
				System.out.println("連接離開: "+clientSocket.getRemoteSocketAddress() );
			}
			finally{				
								
				synchronized(clientOutsteamList){
					System.out.println("[TCPServer] 連接離開123: "+clientSocket.getRemoteSocketAddress() );
					if(clientOutsteamList.containsKey(clientSocket))
						clientOutsteamList.remove(clientSocket);
					String message = Remove+"#"+ clientSocket.getInetAddress().getHostAddress();                        
					//System.out.println("連接離開 123:"+ message );
					setObserverMessage(message);
//                    Thread notify = new Thread(new Notify());
//                    // 啟動執行緒
//                    notify.start();
                    notifyAllOberserver();
                    respondAll(mainServer.getlist());
					try{
						clientSocket.close();
						//System.out.println("[TCPServer] Server關閉clint成功");
					}catch (Exception ex) {
						System.out.println("[TCPServer] 關閉clint失敗: " + clientSocket.getRemoteSocketAddress());
					}					
				}
				
			}
		}
		
		
		
	}
	//觀察者註冊
	public void registOberserver(Observer observer){
        observerList.add(observer);
    }
	//觀察者移除
    public void removeOberserver(Observer observer){
        if(observerList.indexOf(observer)>-1)
        observerList.remove(observer);
    }
    //送報給所有人
    public void notifyAllOberserver() {
        for (int i=0;i<observerList.size();i++) {
            observerList.get(i).receiveNotify(this.observerMessage);
        }
    }
    private void setObserverMessage(String message){
        this.observerMessage = message;
    }
    //執行送報
    public class Notify implements Runnable {
		public void run() {
		    //synchronized (this) {
		        System.out.println("123");
               notifyAllOberserver();
               //this.notify();
               respondAll(mainServer.getlist());
            //}
			
		}
	}
	//server回傳資料給單一client
	private void respondClient(PrintStream writer , String outputMessage) {			
		try {
		    //System.out.println("lock here.");
			writer.println(outputMessage);
			// 刷新該串流的緩衝。
			writer.flush();
		} catch (Exception ex) {
			System.out.println("送出資料失敗: "+ clientSocket.getRemoteSocketAddress());
		}
	
	}
	//傳資料給所有client
	private void respondAll(String outputMessage) {	
		
		synchronized(clientOutsteamList){
			
			for(Enumeration one =clientOutsteamList.elements();one.hasMoreElements();)
			{
				PrintStream writer = (PrintStream)one.nextElement();
				respondClient(writer , outputMessage);
			}
		}
		
	}
	public void sendList(String message){
	    
	    //String message ="List#";
//	    message += Server.getList();
	    respondAll(message);
	}
	
	public void sendInitWalkable(String walkable){
        
        String message ="InitWalkable#";
        message += walkable;
        respondAll(message);
    }
	public void sendInitCoordinate(String coordinates){
        
        String message ="InitCoordinate#";
        message += coordinates;
        respondAll(message);
    }
	public void sendStart(){
	    Remove = "Remove";
	    Connection = false;
	    try {
	        serverSock.close();
        } catch (IOException e) {
            System.out.println("serverSock close IOException");
            // TODO: handle exception
        }
	    
        String message ="StartGame#";
        respondAll(message);
    }
	public void sendGameOver(){
        
        String message ="GameOver#";
        respondAll(message);
    }
}