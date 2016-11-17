package burgerking;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameView extends JPanel {

    MainFrame main_frame;
    TimerTask task;
    Timer gameTimer;

    public GameView(MainFrame main_frame) {

        setLocation(0, 0);
        setSize(800, 600);
        setLayout(null);
        setFocusable(true);

        JPanel MapPanel = new JPanel();
        MapPanel.setLayout(null);
        MapPanel.setLocation(10, 30);
        MapPanel.setSize(540, 540);
        add(MapPanel);

        JPanel PlayerList = new JPanel();
        PlayerList.setLayout(null);
        PlayerList.setLocation(560, 30);
        PlayerList.setSize(230, 240);
        add(PlayerList);

        JPanel TimerPanel = new JPanel();
        TimerPanel.setLayout(null);
        TimerPanel.setLocation(560, 300);
        TimerPanel.setSize(230, 70);
        add(TimerPanel);

        JPanel UserInfo = new JPanel();
        UserInfo.setLayout(null);
        UserInfo.setLocation(560, 400);
        UserInfo.setSize(230, 180);
        add(UserInfo);

        task = new TimerTask() {
            int time = 300;
            JLabel label = new JLabel();

            public void run() {
                time--;
                main_frame.MainPanel.requestFocus();
                label.setText("遊戲倒數" + Integer.toString(time) + "秒");
                label.setBounds(10, 0, 230, 70);
                label.setFont(new Font("標楷體", Font.BOLD, 30));
                label.setForeground(Color.RED);
                ;
                TimerPanel.add(label);
                repaint();

                if (time == 270) {

                } else if (time == 0) {
                    task.cancel();
                    gameTimer.cancel();
                    main_frame.changeView(new RankView(main_frame));
                }

            }

        };
        gameTimer = new Timer();
        gameTimer.schedule(task, 0, 1000);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == 37)
                    System.out.println("key left");
                else if (e.getKeyCode() == 38)
                    System.out.println("key up");
                else if (e.getKeyCode() == 39)
                    System.out.println("key right");
                else if (e.getKeyCode() == 40)
                    System.out.println("key down");
                else if (e.getKeyCode() == 90)
                    System.out.println("key z");
                else if (e.getKeyCode() == 88)
                    System.out.println("key x");
                else
                    System.out.println(e.getKeyCode());
            }
        });

        JLabel background_label = new Background();
        add(background_label);

    }

}
