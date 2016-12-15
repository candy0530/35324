package udp.update.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer implements Runnable {
    private final int port = 35201;
    private DatagramSocket socket;
    private String receiveMsg = "";

    public UDPServer() {

    }

    public void initUDPServer() {
        new Thread(this).start();
    }

    public void run() {
        try {
            final int SIZE = 8192; // 設定最大的訊息大小為 8192
            byte buffer[] = new byte[SIZE]; // 設定訊息暫存區
            System.out.println("server is on，waiting for client to send data......");
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket = new DatagramSocket(port); // 設定接收的 UDP Socket
            for (int count = 0;; count++) {
                socket.receive(packet); // 接收封包
                if (packet.getLength() == 0)
                    continue;
                receiveMsg = new String(buffer, 0, packet.getLength()); // 將接收訊息轉換為字串
                System.out.println(count + " : receive = " + receiveMsg); // 印出接收到的訊息
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void endConnection() {
        socket.close();
    }

    public String getReceiveMsg() {
        return receiveMsg;
    }

}