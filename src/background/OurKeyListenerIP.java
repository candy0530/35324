package background;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class OurKeyListenerIP implements KeyListener{
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyChar() >= KeyEvent.VK_0 && e.getKeyChar() <= KeyEvent.VK_9){
			
		}else if(e.getKeyChar() == '.'){
			
		}else
			e.consume();
	}			
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}
}