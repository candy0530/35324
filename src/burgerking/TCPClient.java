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
	// �إ�Socket�ܼ�
	private Socket mySocket;
	InputStreamReader streamReader;
	PrintStream writer;
	
	//�[���
	private ArrayList<Observer> observerList;
    
    private String message;

	// �إ߳s�u
	public TCPClient(String serverIpAddress) {
		ServerIp = serverIpAddress;
		observerList = new ArrayList<Observer>();
		try {
			// �ШD�إ߳s�u
			mySocket = new Socket(ServerIp, ServerPort);
			// �إ�I/O��Ƭy�A���oSocket����J��Ƭy
			streamReader = new InputStreamReader(mySocket.getInputStream());
			// ���oSocket����X��Ƭy
			writer = new PrintStream(mySocket.getOutputStream());
			//�}�Ұ����
			new Thread(this).start();		

		} catch (IOException ex) {
			System.out.println("Client�إ߳s�u����");
		}
	}
	//�[��̵��U
	public void registOberserver(Observer observer){
        observerList.add(observer);
    }
	//�[��̲���
    public void removeOberserver(Observer observer){
        if(observerList.indexOf(observer)>-1)
        observerList.remove(observer);
    }
    //�e�����Ҧ��H
    public void notifyAllOberserver(){
        for(Observer observer:observerList){
            observer.receiveNotify(this.message);
        }
    }
    //����e��
    public class Notify implements Runnable {
		InputStreamReader TCPReader;
		
		public void run() {
			notifyAllOberserver();
		}
	}
	//�B�z��ƶǶi�ǥX		
	public void run() {
		String getLine;	
		BufferedReader reader = new BufferedReader(streamReader);
		// Ū�����
		try {	
			
			while ((getLine = reader.readLine()) != null) {
				
				System.out.println("TCP: "+getLine);
				this.message = getLine;
				
				//����e��
				Thread notify = new Thread(new Notify());
				// �Ұʰ����
				notify.start();			
		        
//				String[] serverText = getLine.split("#");
//				if(serverText[0].equals("StartGame")){
//					System.out.println("GO!");
//					//testLabel.setText("GO!");
//				}//				
				/*
				 * 
				 * �B�zserver�ǤJ��input
				 * 
				 */	
			}
		} catch (Exception ex) {
			/*try{
				mySocket.close();
				System.out.println("����clint���\");
			}catch (Exception eex) {
				System.out.println("����clint����");
			}*/
			//ex.printStackTrace();
			System.out.println("����qserverŪ��");
		}
	}
	//�ǰT��Server
	private void writeToServer(String outputMessage) {
		try {
			// �e�X���
			writer.println(outputMessage);
			// ��s�Ӧ�y���w�ġC
			writer.flush();					
							
		} catch (Exception ex) {
			System.out.println("����");
		}
	}
	
	
	//�i�J�ж�
	public void enterRoom(String uID){
		setID(uID);
		/*
		 * �B�z�n�ǥX��Server���H��
		 */
		String message = "ID#"+ClientID;			
		writeToServer(message);
		
	}	
	//�����}�l�C��
	public void setStartGame(){
		// �B�z�n�ǥX��Server���H��		 
		String message = "StartGame#";			
		writeToServer(message);
	}
	
	
	public String getServerIP(){
		String serverIp = mySocket.getInetAddress().toString().substring(1);
		if(isServer()){
			try
			{
			serverIp =  InetAddress.getLocalHost().getHostAddress();//mySocket.getLocalAddress().toString().substring(1);
			}catch (Exception ex) {
				System.out.println("���oIP����");
			}			
		}
		return serverIp;
		
	}
	
	public void endConnection() {
		String message = "EndGame#";			
		writeToServer(message);
		try {
			// �����s�u			
			mySocket.close();		
			streamReader.close();
			writer.close();			
			//System.out.println("End Connect");
		} catch (IOException ex) {
			System.out.println("�����s�u����");
		}
	}
	
	private void setID(String ID){
		ClientID = ID;
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