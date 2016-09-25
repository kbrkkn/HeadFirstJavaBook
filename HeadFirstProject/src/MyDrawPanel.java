import java.awt.Color;
import java.awt.Graphics;

import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.ShortMessage;
import javax.swing.JPanel;


public class MyDrawPanel extends JPanel implements ControllerEventListener {
boolean msg=false;
	@Override
	public void controlChange(ShortMessage event) {
msg=true;
repaint();
	}
public void paintComponent(Graphics g){
	if(msg){
		int r=(int)(Math.random()*256);
		int gr=(int)(Math.random()*256);
		int b=(int)(Math.random()*256);
		g.setColor(new Color(r,gr,b));
		
		int ht=(int)((Math.random()*120)+10);
		int wt=(int)((Math.random()*120)+10);
		int x=(int)((Math.random()*40)+10);
		int y=(int)((Math.random()*40)+10);
		g.fillRect(x, y, wt, ht);
		msg=false;



	}
}
}
