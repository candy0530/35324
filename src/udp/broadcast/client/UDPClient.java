package udp.broadcast.client;

import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class UDPClient implements Runnable {

    InputStreamReader streamReader;
    PrintStream writer;
    private int ServerPort = 35201; // port : 連接埠

    private InetSocketAddress[] allServer = {};
    private String[] msg = new String[7];
    private boolean broadCastStatus = true;

    // 建立連線
    public UDPClient(String[] allIp) {
        allServer = new InetSocketAddress[allIp.length];
        for (int i = 0; i < allIp.length; i++) {
            try {
                allServer[i] = new InetSocketAddress(InetAddress.getByName(allIp[i]), ServerPort);
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void setSendMsg(int id, String sendMsg) {
        msg[id] = sendMsg;
    }

    public void startUDPBroadCast() {
        new Thread(this).start();
    }

    public void run() {
        while (broadCastStatus) {
            try {
                DatagramSocket mySocket = new DatagramSocket(); // 建立傳送的 UDP
                                                                // Socket
                for (int index = 0; index < allServer.length; index++) {
                    byte buffer[] = msg[index].getBytes(); // 將訊息字串 msg 轉換為位元串

                    DatagramPacket packet1 = new DatagramPacket(buffer, buffer.length, allServer[index]);
                    mySocket.send(packet1);
                    Thread.sleep(500);
                }
                mySocket.close(); // 關閉 UDP socket
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public void endUDPBroadCast() {
        broadCastStatus = false;
    }
    // The function is only for Testing
    public void setServer(int index, InetSocketAddress server) {
        allServer[index] = server;
    }

}