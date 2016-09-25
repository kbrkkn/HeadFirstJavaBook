import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.JFrame;


public class MiniMusicPlayer3 {
	static JFrame f=new JFrame("My First Music Video");
static MyDrawPanel m1;

	public static void main(String[] args) {
MiniMusicPlayer3 mini=new MiniMusicPlayer3();
mini.go();
	}
	public void setUpGui(){
		m1=new MyDrawPanel();
		f.setContentPane(m1);
		f.setBounds(30, 30, 300, 300);
		f.setVisible(true);
	}
public void go(){
	setUpGui();
	try {
		Sequencer sequencer=MidiSystem.getSequencer();
		sequencer.open();
		sequencer.addControllerEventListener(m1, new int[]{127});
		Sequence seq=new Sequence(Sequence.PPQ,4);
		Track track=seq.createTrack();
		
		int r=0;//note
		for (int i = 0; i <120; i+=2) {
			r=(int)((Math.random()*127)+1);
			track.add(makeEvent(144,1,r,100,i));
			track.add(makeEvent(176,1,127,0,i));
			track.add(makeEvent(128,1,r,100,i+2));
		}
		sequencer.setSequence(seq);
		sequencer.start();
		sequencer.setTempoInBPM(120);
	} catch (Exception e) {
e.printStackTrace();	}
	
}
public MidiEvent makeEvent(int comd,int chan,int one,int two,int tick){
		MidiEvent event=null;
		try {
			ShortMessage a=new ShortMessage();
			a.setMessage(comd, chan, one, two);
			event=new  MidiEvent(a, tick);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return event;
	}

}
