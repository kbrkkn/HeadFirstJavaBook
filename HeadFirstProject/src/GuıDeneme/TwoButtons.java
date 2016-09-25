package GuýDeneme;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class TwoButtons {
JFrame frame;
JLabel label;
	public static void main(String[] args) {
TwoButtons test=new TwoButtons();
test.go();
	}
public void go(){
	frame=new JFrame();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	JButton colorButton=new JButton("Change Color");
	colorButton.addActionListener(new ColorListener());
	
	JButton labelButton=new JButton("Change label text");
	labelButton.addActionListener(new LabelListener());
	
	label=new JLabel("label");
	
	MyDrawPanel mypanel=new MyDrawPanel();
	
	frame.getContentPane().add(BorderLayout.WEST,label);
	frame.getContentPane().add(BorderLayout.EAST,labelButton);
	frame.getContentPane().add(BorderLayout.CENTER,mypanel);
	frame.getContentPane().add(BorderLayout.SOUTH,colorButton);

frame.setSize(500, 500);
frame.setVisible(true);



}
class ColorListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		frame.repaint();
	}}
class LabelListener implements ActionListener{
	public void actionPerformed(ActionEvent e){
		label.setText("hii ");
	}
}
}
