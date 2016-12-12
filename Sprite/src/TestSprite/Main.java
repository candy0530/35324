package TestSprite;


import javax.swing.JFrame;

public class Main extends JFrame {
	
	private PlayerPanel playerPanel = new PlayerPanel();
	
	public Main() {
		setLayout(null);
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(playerPanel);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Main main = new Main();
		main.setVisible(true);
	}
}
