package GuýDeneme;

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;

public class MyDrawPanelTest implements ActionListener{
JFrame frame;
	public static void main(String[] args) {
MyDrawPanelTest test=new MyDrawPanelTest();
test.go();
	}
public void go(){
	frame=new JFrame() ;
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

JButton button=new JButton("Click me");
button.addActionListener(this);

MyDrawPanel mypanel=new MyDrawPanel();

frame.getContentPane().add(BorderLayout.SOUTH,button);
frame.getContentPane().add(BorderLayout.CENTER,mypanel);
frame.setSize(300, 300);
frame.setVisible(true);

}
@Override
public void actionPerformed(ActionEvent e) {
	frame.repaint();
}
}
