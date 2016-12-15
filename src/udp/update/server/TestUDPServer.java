package udp.update.server;

import static org.junit.Assert.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestUDPServer {

    UDPServer server;

    @Before
    public void setUp() {
        server = new UDPServer();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInitUDPServer() {
        // test
        server.initUDPServer();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // teardown
        server.endConnection();
    }

    @Test
    public void testEndConnection() {
        // setup
        server.initUDPServer();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // test
        server.endConnection();
    }

    @Test
    public void testReceiveMsg() {
        // setup
        server.initUDPServer();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // test
        String sendMsg = "Test receiveMsg.";
        sendMsgToServer(sendMsg);
        assertEquals("Didn't receive Msg or didn't update.", sendMsg, server.getReceiveMsg());

        // teardown
        server.endConnection();
    }

    @Test
    public void testReceiveEmptyMsg() {
        // setup
        server.initUDPServer();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // test
        String expectMsg = "I'm testing.";
        sendMsgToServer(expectMsg);
        sendMsgToServer("");
        assertEquals("Didn't receive Msg or didn't update.", expectMsg, server.getReceiveMsg());

        // teardown
        server.endConnection();
    }

    @Test
    public void testGetReceiveMsg() {
        // test
        assertEquals("Cannot get receive Msg.", "", server.getReceiveMsg());
        server.initUDPServer();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String expectMsg = "Test getReceiveMsg.";
        sendMsgToServer(expectMsg);
        assertEquals("Didn't receive Msg or didn't update.", expectMsg, server.getReceiveMsg());

        // teardown
        server.endConnection();
    }

    // Fake UDPClient
    private void sendMsgToServer(String msg) {
        byte buffer[] = msg.getBytes(); // 將訊息字串 msg 轉換為位元串
        // 封裝該位元串成為封包 agramPacket，同時指定傳送對象
        try {
            DatagramPacket packet1 = new DatagramPacket(buffer, buffer.length,
                    InetAddress.getLocalHost(), 35201);
            DatagramSocket socket1 = new DatagramSocket(); // 建立傳送的 UDP
            // Socket
            socket1.send(packet1);
            socket1.close();
            Thread.sleep(1000); // Waiting for sending to server time
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
