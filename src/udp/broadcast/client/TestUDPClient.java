package udp.broadcast.client;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestUDPClient {

    UDPClient client;
    FakeUDPServer[] allServer = {};
    int[] allPort = {1234, 1235, 1236, 1237, 1238, 1239, 1240};
    String ip;

    @Before
    public void setUp() {
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @After
    public void tearDown() {
        for (int i = 0; i < allServer.length; i++){
            allServer[i].endConnection();
            allServer[i].receiveMsg = "";
        }
        client.endUDPBroadCast();
    }

    @Test
    public void testStartUDPBroadCastForOneServer() {
        // setup
        String sendMsg = "Test startUDPBroadCast for one server.";
        String[] allIp = {ip};
        client = new UDPClient(allIp);
        allServer = new FakeUDPServer[1];
        int port = allPort[0];
        allServer[0] = new FakeUDPServer(port);
        try {
            client.setServer(0, new InetSocketAddress(InetAddress.getByName(ip), port));
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        client.setSendMsg(0, sendMsg);

        // test
        client.startUDPBroadCast();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals("Didn't send to Server.", sendMsg, allServer[0].receiveMsg);
    }

    @Test
    public void testStartUDPBroadCastForServeralServer() {
        // setup
        String sendMsg = "Test startUDPBroadCast for serveral server.";
        String[] allIp = {ip, ip, ip, ip, ip, ip, ip};
        client = new UDPClient(allIp);
        allServer = new FakeUDPServer[7];
        for (int i = 0; i < allServer.length; i++) {
            int port = allPort[i];
            allServer[i] = new FakeUDPServer(port);
            try {
                client.setServer(i, new InetSocketAddress(InetAddress.getByName(ip), port));
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            client.setSendMsg(i, sendMsg);
        }

        // test
        client.startUDPBroadCast();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (int i = 0; i < allServer.length; i++) {
            assertEquals("Didn't send to Server.", sendMsg, allServer[i].receiveMsg);
        }
    }

    @Test
    public void testStartUDPBroadCastForNoServer() {
        // setup
        String[] allIp = {};
        client = new UDPClient(allIp);

        // test
        client.startUDPBroadCast();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testStartUDPBroadCastForServerNotOpen() {
        // setup
        String sendMsg = "Test startUDPBroadCast for server not open.";
        String[] allIp = {ip};
        client = new UDPClient(allIp);
        int port = allPort[0];
        try {
            client.setServer(0, new InetSocketAddress(InetAddress.getByName(ip), port));
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        client.setSendMsg(0, sendMsg);

        // test
        client.startUDPBroadCast();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testSetSendMsg() {
        // setup
        String sendMsg = "Test setSendMsg.";
        String sendOtherMsg = "Test setSendMsg 5.";
        String[] allIp = {ip, ip, ip, ip, ip, ip, ip};
        client = new UDPClient(allIp);
        allServer = new FakeUDPServer[7];
        for (int i = 0; i < allServer.length; i++) {
            int port = allPort[i];
            allServer[i] = new FakeUDPServer(port);
            try {
                client.setServer(i, new InetSocketAddress(InetAddress.getByName(ip), port));
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            client.setSendMsg(i, sendMsg);
        }

        // test
        client.setSendMsg(5, sendOtherMsg);
        client.startUDPBroadCast();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (int i = 0; i < allServer.length; i++) {
            if (i == 5) continue;
            assertEquals("Didn't set Msg correctly.", sendMsg, allServer[i].receiveMsg);
        }
        assertEquals("Didn't set Msg correctly.", sendOtherMsg, allServer[5].receiveMsg);
    }
    
    @Test
    public void testSendMsgTime() {
        // setup
        String sendMsg1 = "Test SendMsgTime 1.";
        String sendMsg2 = "Test SendMsgTime 2.";
        String[] allIp = {ip};
        client = new UDPClient(allIp);
        allServer = new FakeUDPServer[1];
        int port = allPort[0];
        allServer[0] = new FakeUDPServer(port);
        try {
            client.setServer(0, new InetSocketAddress(InetAddress.getByName(ip), port));
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // test
        client.setSendMsg(0, sendMsg1);
        client.startUDPBroadCast();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        client.setSendMsg(0, sendMsg2);
        assertEquals("First msg didn't send to Server.", sendMsg1, allServer[0].receiveMsg);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals("Second msg didn't send to Server.", sendMsg2, allServer[0].receiveMsg);
    }

    @Test
    public void testSendMsgBeforeSetMsg() {
        // setup
        String expectMsg = "";
        String[] allIp = {ip};
        client = new UDPClient(allIp);
        allServer = new FakeUDPServer[1];
        int port = allPort[0];
        allServer[0] = new FakeUDPServer(port);
        try {
            client.setServer(0, new InetSocketAddress(InetAddress.getByName(ip), port));
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // test
        client.startUDPBroadCast();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals("Msg has changed before setMsg.", expectMsg, allServer[0].receiveMsg);
    }

    @Test
    public void testAfterLongPeriodMsgWontChanged() {
        // setup
        String expectMsg = "Test AfterLongPeriodMsgWontChanged.";
        String[] allIp = {ip};
        client = new UDPClient(allIp);
        allServer = new FakeUDPServer[1];
        int port = allPort[0];
        allServer[0] = new FakeUDPServer(port);
        try {
            client.setServer(0, new InetSocketAddress(InetAddress.getByName(ip), port));
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        client.setSendMsg(0, expectMsg);
        client.startUDPBroadCast();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals("Msg shouldn't change.", expectMsg, allServer[0].receiveMsg);
        
        // test
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals("Msg shouldn't change.", expectMsg, allServer[0].receiveMsg);
    }
}