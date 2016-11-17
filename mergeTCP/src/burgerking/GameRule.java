package burgerking;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

public class GameRule implements ActionListener{
	
	JButton button;
	JDialog RuleMessage;
	public GameRule(int x,int y,MainFrame main_frame) 
    { 

		RuleMessage = new JDialog(main_frame);
		RuleMessage.setModal(true);
		RuleMessage.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
		RuleMessage.setLayout(null);	
		RuleMessage.setResizable(false);
		RuleMessage.setLocationRelativeTo(null);
		RuleMessage.setLocation(x+100, y+100);
		RuleMessage.setSize(600,400); 
		

        
        JScrollBar vbar=new JScrollBar(JScrollBar.VERTICAL, 0, 20, 0, 100);
        vbar.setBounds(500, 50, 25, 300);


        RuleMessage.add(vbar, BorderLayout.EAST);
        
        
        JPanel rulePanel = new JPanel();
        rulePanel.setBounds(50, 45, 450, 305);
        
        
        JLabel background_label = new JLabel();
		ImageIcon background = new ImageIcon("image/bg.png");
		background_label.setIcon(background);
		background_label.setLocation(0, 0);
		background_label.setSize(800,600);
        rulePanel.add(background_label);
        
        vbar.addAdjustmentListener(new AdjustmentListener() {			
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				System.out.println(e.getValue());
				
				
			}
		});
        
        RuleMessage.add(rulePanel);
        
        RuleMessage.setVisible(true);

		
    } 
	  public void actionPerformed(ActionEvent e) {
		  RuleMessage.dispose();
	  }

}
