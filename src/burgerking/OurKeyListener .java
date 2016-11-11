package burgerking;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class OurKeyListener implements KeyListener{
			private int counter = 0;
			private int maxString = 8;
			@Override
			public void keyTyped(KeyEvent e) {
				
				// TODO Auto-generated method stub
				if(e.getKeyChar() >= KeyEvent.VK_0 && e.getKeyChar() <= KeyEvent.VK_9 && counter < maxString){
					counter++;
				}
				else if(e.getKeyChar() >= KeyEvent.VK_A && e.getKeyChar() <= KeyEvent.VK_Z && counter < maxString) {
					counter++;
				}else if(e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z' && counter < maxString){
					counter++;
				}else if(e.getKeyChar() == KeyEvent.VK_BACK_SPACE || e.getKeyChar() == KeyEvent.VK_DELETE){
					if(counter > 0)
						counter--;						
				}else{
					e.consume();
				}
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
class ourKeyListenerIP implements KeyListener{
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
