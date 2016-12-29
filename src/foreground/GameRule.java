package foreground;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

public class GameRule implements ActionListener {

    private JDialog ruleMessageFrame;
    private BufferedImage description;
    private final int viewWidth = 700;
    private final int viewHeight = 500;
    private final int descriptionWidth = 500;
    private final int descriptionHeight = 400;
    

    public GameRule(MainFrame mainFrame) {
        int locationX = mainFrame.getLocation().x;
        int locationY = mainFrame.getLocation().y;
        ImageIcon background = new ImageIcon("image/des_background.jpg");
        Image image = background.getImage();        
        Image newimg = image.getScaledInstance(viewWidth, viewHeight, java.awt.Image.SCALE_SMOOTH); 
        background = new ImageIcon(newimg);

        ruleMessageFrame = new JDialog(mainFrame);
        ruleMessageFrame.setModal(true);
        ruleMessageFrame.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
        ruleMessageFrame.setLayout(null);
        ruleMessageFrame.setResizable(false);
        ruleMessageFrame.setLocationRelativeTo(null);
        ruleMessageFrame.setLocation(locationX + 50, locationY + 50);
        ruleMessageFrame.setSize(viewWidth, viewHeight);

        JScrollBar descriptionScrollBar = new JScrollBar(JScrollBar.VERTICAL, 0, 20, 0, 800);
        descriptionScrollBar.setBounds(viewWidth-100, 50, 25, descriptionHeight);

        ruleMessageFrame.add(descriptionScrollBar, BorderLayout.EAST);

        try {
            description = ImageIO.read(new File("image/description.png"));

        } catch (Exception e) {
            // TODO: handle exception
        }
        JLabel descriptionBackgroundLabel = new JLabel(background);
        descriptionBackgroundLabel.setLocation(-100, -50);
        descriptionBackgroundLabel.setSize(viewWidth, viewHeight);

        JPanel descriptionBackgroundPanel = new JPanel();
        descriptionBackgroundPanel.setLayout(null);
        descriptionBackgroundPanel.setBounds(100, 50, descriptionWidth, descriptionHeight);

        
        JPanel descriptionPanel = new mypanel(description);
        descriptionPanel.setBounds(0, 0, 921, 2127);
        descriptionPanel.setOpaque(false);
        descriptionBackgroundPanel.add(descriptionPanel);
        descriptionBackgroundPanel.add(descriptionBackgroundLabel);

        ruleMessageFrame.add(descriptionBackgroundPanel);

        descriptionScrollBar.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                //System.out.println(e.getValue());

                descriptionPanel.setBounds(0, -e.getValue(), 921, 2127);
            }
        });

        JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setLocation(0, 0);
        backgroundLabel.setSize(viewWidth, viewHeight);
        ruleMessageFrame.add(backgroundLabel);
        ruleMessageFrame.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        ruleMessageFrame.dispose();
    }

    class mypanel extends JPanel {
        BufferedImage description;

        public mypanel(BufferedImage description) {
            this.description = description;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(description, 0, 0, descriptionWidth, 1000, null);
        }

    }

}
