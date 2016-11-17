package burgerking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

public class YourDialog extends JDialog implements ActionListener {

	  JButton button;

	  public YourDialog() {
	     button = new JButton("Close");
	     button.addActionListener(this);
	     add(button);
	     pack();
	     setVisible(true);
	  }

	  public void actionPerformed(ActionEvent e) {
	      dispose();
	  }
	}
