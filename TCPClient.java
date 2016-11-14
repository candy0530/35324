package burgerking;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;


public class TCPClient implements Subject , Runnable{
	
	//JLabel testLabel;
	private String ServerIp = "127.0.0.1";
	private final int ServerPort =35324;
	private String ClientID = "USER1";
	// 建立Socket變數
	private Socket mySocket;
	InputStreamReader streamReader;
	PrintStream writer;
	
	//觀察者
	private ArrayList<Observer> observerList;
    private String message;

	// 建立連線
	public TCPClient(String serverIpAddress) {
		ServerIp = serverIpAddress;
		observerList = new ArrayList<Observer>();
		try {
			// 請求建立連線
			mySocket = new Socket(ServerIp, ServerPort);
//			Thread noticeServer = new Thread(new NoticeServerOut());
//			// 啟動執行緒
//			noticeServer.start();
			// 建立I/O資料流，取得Socket的輸入資料流			
			streamReader = new InputStreamReader(mySocket.getInputStream());
			// 取得Socket的輸出資料流
			writer = new PrintStream(mySocket.getOutputStream());
			
			//開啟執行緒
			new Thread(this).start();
			

		} catch (IOException ex) {
			System.out.println("[TCPClient] Client建立連線失敗");
		}
	}
	
  
	//處理資料傳進傳出		
	public void run() {
		String getLine;	
		BufferedReader reader = new BufferedReader(streamReader);
		// 讀取資料
		
		try {
			
			while ((getLine = reader.readLine()) != null) {
				
				System.out.println("[TCPClient] "+getLine);
				this.message = getLine;
				
				//執行送報
				Thread notify = new Thread(new Notify());
				// 啟動執行緒
				notify.start();	
				
				
//				String[] serverText = getLine.split("#");
//				if(serverText[0].equals("StartGame")){
//					System.out.println("GO!");
//					//testLabel.setText("GO!");
//				}//				
				/*
				 * 
				 * 處理server傳入的input
				 * 
				 */	
			}
		} catch (Exception ex) {
			/*try{
				mySocket.close();
				System.out.println("關閉clint成功");
			}catch (Exception eex) {
				System.out.println("關閉clint失敗");
			}*/
			//ex.printStackTrace();
			System.out.println("[TCPClient] 停止從server讀取");
		}
	}
	//傳訊給Server
	private void writeToServer(String outputMessage) {
		
		try {
			// 送出資料
			writer.println(outputMessage);
			// 刷新該串流的緩衝。
			System.out.println("[TCPClient] 成功");
			writer.flush();					
							
		} catch (Exception ex) {
			System.out.println("[TCPClient] 失敗");
		}
	}
	private void setID(String ID){
		ClientID = ID;
	}
	private void setMessage(String msg){
		
		this.message =msg;;
		
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
    public void notifyAllOberserver(){
        for(Observer observer:observerList){
            observer.receiveNotify(this.message);
        }
    }
    //執行送報
    public class Notify implements Runnable {
		InputStreamReader TCPReader;
		
		public void run() {
			notifyAllOberserver();
		}
	}
    public class NoticeServerOut implements Runnable {
		//InputStreamReader TCPReader;
		
		public void run() {
			while(!mySocket.isClosed()){ //怪怪的
				
				try{
					  Thread.sleep(4000);
				      mySocket.sendUrgentData(1);
				      System.out.println("[TCPClient] Server");
				}catch(Exception ex){
					ex.printStackTrace();
					System.out.println("[TCPClient] Server已離開");
					setMessage("ServerBreak#");
					notifyAllOberserver();
					try {
						// 結束連線			
						mySocket.close();		
						streamReader.close();
						writer.close();			
						//System.out.println("End Connect");
					} catch (IOException exx) {
						System.out.println("[TCPClient] 結束連線失敗");
					}
				}
			}
			
		}
		
	}
	//進入房間
	public void enterRoom(String uID){
		setID(uID);
		/*
		 * 處理要傳出給Server的信息
		 */
		String message = "ID#"+ClientID;			
		writeToServer(message);
		
	}	
		
	public String getServerIP(){
		String serverIp = mySocket.getInetAddress().toString().substring(1);
		if(isServer()){
			try{
			serverIp = InetAddress.getLocalHost().getHostAddress().toString();
			}
			catch(IOException ex){
				System.out.println("[TCPClient] 取得IP位置失敗");
			}
		}
		return serverIp;
		
	}
	
	public void endConnection() {
		String message = "EndGame#";			
		writeToServer(message);
		try {
			// 結束連線			
			mySocket.close();		
			streamReader.close();
			writer.close();			
			//System.out.println("End Connect");
		} catch (IOException ex) {
			System.out.println("[TCPClient] 結束連線失敗");
		}
	}
	
	//當市長開始遊戲，通知所有clinet開始遊戲
		public void setStartGame(){
			// 處理要傳出給Server的信息		 
			String message = "StartGame#";			
			writeToServer(message);
		}
	
	//need?
	public boolean isServer() {
		//System.out.println(mySocket.getLocalAddress()+", "+mySocket.getInetAddress());
		if(mySocket.getLocalAddress().equals(mySocket.getInetAddress()))
			return true;
		else
			return false;
	}	
	
	
}