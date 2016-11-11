package burgerking;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

public class TCPServer implements Runnable{	
	//存每個client的ID
	private Hashtable clientIDList =new Hashtable();
	//存每個clinet的outstream
	private Hashtable clientOutsteamList =new Hashtable();
	private final int port =35324;
	private ServerSocket serverSock;
	private Socket clientSocket;
	
	//建立Server
	public TCPServer() {
		try {
			serverSock = new ServerSocket(port);
			//開啟執行緒
			new Thread(this).start();
		}		catch (Exception ex) {
			System.out.println("Server建立失敗");
		}
		
	}	
	//執行執行緒
	public void run() {
		
		while (true) {
			try{
				// 等待連線的請求--串流
				clientSocket = serverSock.accept();
						
				PrintStream writer = new PrintStream(clientSocket.getOutputStream());
				//存每個clinet的outstream
				clientOutsteamList.put(clientSocket,writer);
				// 建立與clientI/O執行緒
				Thread listenRequest = new Thread(new Process());
				// 啟動執行緒
				listenRequest.start();
				
			}catch (Exception ex) {
				//System.out.println("Client要求連接失敗");
			}
		}
	}
	
	public void endConnection() {
		try {
			// 結束連線			
							
			//System.out.println("End Connect");
			for(Enumeration<PrintStream> one =clientOutsteamList.elements();one.hasMoreElements();)
			{	
				
				try{
					((PrintStream)one.nextElement()).close();					
									
					System.out.println("Server關閉clintStream");
				}catch (Exception ex) {
					System.out.println("關閉clint失敗: " );//+ ((Socket)one).getRemoteSocketAddress());
				}		
				clientOutsteamList.remove(one);
				//one.nextElement();							
			}
			serverSock.close();
		} catch (IOException ex) {
			System.out.println("結束連線失敗");
		}
	}
	//與client傳輸信息的執行緒	
	public class Process implements Runnable {
		InputStreamReader TCPReader;
		
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
					//System.out.println(getLine);
					//--處理input
					String[] clientText = getLine.split("#");
					//--client進入等候室 紀錄其ID且傳玩家列表給所有client
					if(clientText[0].equals("ID")){
						clientIDList.put(clientSocket, clientText[1]);
						//傳送玩家列表
						String message = "List#";						
						for(Enumeration one =clientIDList.elements();one.hasMoreElements();)
						{
							message += (String)one.nextElement()+"#";							
						}
						respondAll(message);
					}
					else if(clientText[0].equals("EndGame")){
						//處理output
						clientIDList.remove(clientSocket);
						clientOutsteamList.remove(clientSocket);
						String message = "List#";						
						for(Enumeration one =clientIDList.elements();one.hasMoreElements();)
						{
							message += (String)one.nextElement()+"#";							
						}
						//回傳信息給Client
						respondAll(message);
					}
					//--當市長開始遊戲，通知所有clinet開始遊戲
					else if(clientText[0].equals("StartGame")){
						//處理output
						String message = "StartGame#";
						//回傳信息給ALL
						respondAll(message);
					}				
					
					
				}
			} catch (Exception ex) {
				//System.out.println("連接離開: "+clientSocket.getRemoteSocketAddress() );
			}
			finally{
				
				//移除client的ID.IP
				synchronized(clientIDList){
					if(clientIDList.containsKey(clientSocket))
						clientIDList.remove(clientSocket);	
				}				
				synchronized(clientOutsteamList){
					System.out.println("連接離開: "+clientSocket.getRemoteSocketAddress() );
					if(clientOutsteamList.containsKey(clientSocket))
						clientOutsteamList.remove(clientSocket);
					try{
						clientSocket.close();
						System.out.println("Server關閉clint成功");
					}catch (Exception ex) {
						System.out.println("關閉clint失敗: " + clientSocket.getRemoteSocketAddress());
					}
				}
				
			}
		}
		//server回傳資料給本client
		private void respondClient(PrintStream writer , String outputMessage) {			
			try {				
				
				
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
		
	}
	
}
