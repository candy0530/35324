package UDP.broadcast.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class FakeUDPServer implements Runnable {
    private int port = 35201;
    private DatagramSocket socket;
    public String receiveMsg = "";

    public FakeUDPServer(int port) {
        this.port = port;
        new Thread(this).start();
    }

    public void run() {

        try {
            final int SIZE = 8192; // 設定最大的訊息大小為 8192
            byte buffer[] = new byte[SIZE]; // 設定訊息暫存區
            System.out.println("server is on，waiting for client to send data......");
            socket = new DatagramSocket(port); // 設定接收的 UDP Socket
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            for (int count = 0;; count++) {
                socket.receive(packet); // 接收封包
                if (buffer.length == 0)
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

}