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
	//�s�C��client��ID
	private Hashtable clientIDList =new Hashtable();
	//�s�C��clinet��outstream
	private Hashtable clientOutsteamList =new Hashtable();
	private final int port =35324;
	private ServerSocket serverSock;
	private Socket clientSocket;
	
	//�إ�Server
	public TCPServer() {
		try {
			serverSock = new ServerSocket(port);
			//�}�Ұ����
			new Thread(this).start();
		}		catch (Exception ex) {
			System.out.println("Server�إߥ���");
		}
		
	}	
	//��������
	public void run() {
		
		while (true) {
			try{
				// ���ݳs�u���ШD--��y
				clientSocket = serverSock.accept();
						
				PrintStream writer = new PrintStream(clientSocket.getOutputStream());
				//�s�C��clinet��outstream
				clientOutsteamList.put(clientSocket,writer);
				// �إ߻PclientI/O�����
				Thread listenRequest = new Thread(new Process());
				// �Ұʰ����
				listenRequest.start();
				
			}catch (Exception ex) {
				//System.out.println("Client�n�D�s������");
			}
		}
	}
	
	public void endConnection() {
		try {
			// �����s�u			
							
			//System.out.println("End Connect");
			for(Enumeration<PrintStream> one =clientOutsteamList.elements();one.hasMoreElements();)
			{	
				
				try{
					((PrintStream)one.nextElement()).close();					
									
					System.out.println("Server����clintStream");
				}catch (Exception ex) {
					System.out.println("����clint����: " );//+ ((Socket)one).getRemoteSocketAddress());
				}		
				clientOutsteamList.remove(one);
				//one.nextElement();							
			}
			serverSock.close();
		} catch (IOException ex) {
			System.out.println("�����s�u����");
		}
	}
	//�Pclient�ǿ�H���������	
	public class Process implements Runnable {
		InputStreamReader TCPReader;
		
		public void run() {
			
			String getLine;
			try {
				// ���oSocket����J��Ƭy
				TCPReader = new InputStreamReader(clientSocket.getInputStream());
				// �Ȧs��ƪ�Buffered
				BufferedReader reader = new BufferedReader(TCPReader);
				PrintStream writer = (PrintStream)clientOutsteamList.get(clientSocket);
				
				// Ū�����
				while ((getLine = reader.readLine()) != null) {
					//System.out.println(getLine);
					//--�B�zinput
					String[] clientText = getLine.split("#");
					//--client�i�J���ԫ� ������ID�B�Ǫ��a�C���Ҧ�client
					if(clientText[0].equals("ID")){
						clientIDList.put(clientSocket, clientText[1]);
						//�ǰe���a�C��
						String message = "List#";						
						for(Enumeration one =clientIDList.elements();one.hasMoreElements();)
						{
							message += (String)one.nextElement()+"#";							
						}
						respondAll(message);
					}
					else if(clientText[0].equals("EndGame")){
						//�B�zoutput
						clientIDList.remove(clientSocket);
						clientOutsteamList.remove(clientSocket);
						String message = "List#";						
						for(Enumeration one =clientIDList.elements();one.hasMoreElements();)
						{
							message += (String)one.nextElement()+"#";							
						}
						//�^�ǫH����Client
						respondAll(message);
					}
					//--�����}�l�C���A�q���Ҧ�clinet�}�l�C��
					else if(clientText[0].equals("StartGame")){
						//�B�zoutput
						String message = "StartGame#";
						//�^�ǫH����ALL
						respondAll(message);
					}				
					
					
				}
			} catch (Exception ex) {
				//System.out.println("�s�����}: "+clientSocket.getRemoteSocketAddress() );
			}
			finally{
				
				//����client��ID.IP
				synchronized(clientIDList){
					if(clientIDList.containsKey(clientSocket))
						clientIDList.remove(clientSocket);	
				}				
				synchronized(clientOutsteamList){
					System.out.println("�s�����}: "+clientSocket.getRemoteSocketAddress() );
					if(clientOutsteamList.containsKey(clientSocket))
						clientOutsteamList.remove(clientSocket);
					try{
						clientSocket.close();
						System.out.println("Server����clint���\");
					}catch (Exception ex) {
						System.out.println("����clint����: " + clientSocket.getRemoteSocketAddress());
					}
				}
				
			}
		}
		//server�^�Ǹ�Ƶ���client
		private void respondClient(PrintStream writer , String outputMessage) {			
			try {				
				
				
				writer.println(outputMessage);
				// ��s�Ӧ�y���w�ġC
				writer.flush();
			} catch (Exception ex) {
				System.out.println("�e�X��ƥ���: "+ clientSocket.getRemoteSocketAddress());
			}
		
		}
		//�Ǹ�Ƶ��Ҧ�client
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
