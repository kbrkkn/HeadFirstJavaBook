package GuýDeneme;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SimpleAnimation {//OUTER CLASS
int x=70,y=70;
	public static void main(String[] args) {
SimpleAnimation an=new SimpleAnimation();
an.go();
	}
public void go()
{
	JFrame frame=new JFrame();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
MyDrawPanel2 mypanel=new MyDrawPanel2();

frame.getContentPane().add(mypanel);
frame.setSize(300, 300);
frame.setVisible(true);

for (int i = 0; i <130; i++) {
	x++;
	y++;
	frame.repaint();
	try {
		Thread.sleep(50);
	} catch (Exception e) {
		// TODO: handle exception
	}
}}
class MyDrawPanel2 extends JPanel{//INNER CLASS
	public void paintComponent(Graphics g){
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setColor(Color.green);
		g.fillOval(x, y, 40, 40);
	}
}
}
