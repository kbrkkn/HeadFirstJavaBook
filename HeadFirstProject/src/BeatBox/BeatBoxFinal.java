package BeatBox;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.sound.midi.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class BeatBoxFinal {
	JPanel mainPanel;
	ArrayList<JCheckBox> checkboxList;
	Sequencer sequencer;
	Sequence sequence;
	Track track;
	JFrame theFrame;

	JList incomingList;
	JTextField userMessage;
	int nextNum;
	Vector<String> listVector = new Vector<String>();
	String userName;
	ObjectOutputStream out;
	ObjectInputStream in;
	HashMap<String, boolean[]> otherSeqsMap = new HashMap<String, boolean[]>();
	Sequence mySequence = null;

	String[] instrumentNames = { "Bass-Drum", "Closed Hi-Hat", "Open Hi-Hat",
			"Acoustic snare", "crash cymbal", "Hand clamp", "Cowbell", "k",
			"l", "kkk", "kkkjh", "gg", "oo", "ýo", "yyy", "oooo" };
	int[] instruments = { 35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58,
			47, 67, 63 };

	public static void main(String[] args) {
		new BeatBoxFinal().startUp(args[0]);
	}

	public void startUp(String name) {
		userName = name;
		try {
			Socket sock = new Socket("127.0.0.1", 4242);
			out = new ObjectOutputStream(sock.getOutputStream());// yaz
			in = new ObjectInputStream(sock.getInputStream());// oku
			Thread remote = new Thread(new RemoteReader());
			remote.start();
		} catch (Exception e) {
			System.out.println("couldn't connect-you will have to play alone");
		}
		SetUpMidi();
		buildGUI();
	}

	public void buildGUI() {
		theFrame = new JFrame("Cyber BeatBox");
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BorderLayout layout = new BorderLayout();
		JPanel background = new JPanel(layout);
		background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		checkboxList = new ArrayList<JCheckBox>();
		Box buttonBox = new Box(BoxLayout.Y_AXIS);

		JButton start = new JButton("Start");
		start.addActionListener(new MyStartListener());
		buttonBox.add(start);

		JButton stop = new JButton("Stop");
		stop.addActionListener(new MyStopListener());
		buttonBox.add(stop);

		JButton upTempo = new JButton("Tempo Up");
		upTempo.addActionListener(new MyUpTempoListener());
		buttonBox.add(upTempo);

		JButton downTempo = new JButton("Tempo Down");
		downTempo.addActionListener(new MyDownTempoListener());
		buttonBox.add(downTempo);

		JButton sendIt = new JButton("Send It");
		sendIt.addActionListener(new MySendListener());
		buttonBox.add(sendIt);

		userMessage = new JTextField();
		buttonBox.add(userMessage);

		incomingList = new JList();
		incomingList.addListSelectionListener(new MyListSelectionListener());
		incomingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane theList = new JScrollPane(incomingList);
		buttonBox.add(theList);
		incomingList.setListData(listVector);

		Box nameBox = new Box(BoxLayout.Y_AXIS);
		for (int i = 0; i < 16; i++) {
			nameBox.add(new Label(instrumentNames[i]));
		}
		background.add(BorderLayout.EAST, buttonBox);
		background.add(BorderLayout.WEST, nameBox);

		theFrame.getContentPane().add(background);

		GridLayout grid = new GridLayout(16, 16);
		grid.setVgap(1);
		grid.setHgap(2);
		mainPanel = new JPanel(grid);
		background.add(BorderLayout.CENTER, mainPanel);

		for (int i = 0; i < 256; i++) {
			JCheckBox c = new JCheckBox();
			c.setSelected(false);
			checkboxList.add(c);
			mainPanel.add(c);
		}
		SetUpMidi();

		theFrame.setBounds(50, 50, 300, 300);
		theFrame.pack();
		theFrame.setVisible(true);
	}

	public void SetUpMidi() {
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequence = new Sequence(Sequence.PPQ, 4);
			track = sequence.createTrack();
			sequencer.setTempoInBPM(120);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void buildTrackandStart() {
		int[] trackList = null;
		sequence.deleteTrack(track);
		track = sequence.createTrack();
		for (int i = 0; i < 16; i++) {
			trackList = new int[16];
			int key = instruments[i];
			for (int j = 0; j < 16; j++) {
				JCheckBox jc = checkboxList.get(j + 16 * i);
				if (jc.isSelected()) {
					trackList[j] = key;
				} else
					trackList[j] = 0;
			}
			makeTracks(trackList);
			track.add(makeEvent(176, 1, 127, 0, 16));
		}
		track.add(makeEvent(192, 9, 1, 0, 15));
		try {
			sequencer.setSequence(sequence);
			sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			sequencer.start();
			sequencer.setTempoInBPM(120);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
		MidiEvent event = null;
		try {
			ShortMessage a = new ShortMessage();
			a.setMessage(comd, chan, one, two);
			event = new MidiEvent(a, tick);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return event;
	}

	public void makeTracks(int[] list) {
		for (int i = 0; i < 16; i++) {
			int key = list[i];
			if (key != 0) {
				track.add(makeEvent(144, 9, key, 100, i));
				track.add(makeEvent(128, 9, key, 100, i + 1));

			}
		}
	}

	public class MyStartListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			buildTrackandStart();
		}

	}

	public class MyStopListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			sequencer.stop();
		}

	}

	public class MyUpTempoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			float tempoFactor = sequencer.getTempoFactor();
			sequencer.setTempoFactor((float) (tempoFactor * 1.03));
		}

	}

	public class MyDownTempoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			float tempoFactor = sequencer.getTempoFactor();
			sequencer.setTempoFactor((float) (tempoFactor * .97));
		}

	}

	public class MySendListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean[] checkBoxState = new boolean[256];
			for (int i = 0; i < 256; i++) {
				JCheckBox check = checkboxList.get(i);
				if (check.isSelected()) {
					checkBoxState[i] = true;
				}

			}
			try {
				out.writeObject(userName + nextNum++ + ": "
						+ userMessage.getText());
				out.writeObject(checkBoxState);
			} catch (Exception e2) {
				System.out.println("sorry dude.Couldn't send it to the server");
			}
			userMessage.setText("");

		}

	}

	public class RemoteReader implements Runnable {
		boolean[] checkBoxState = null;
		String nameToShow = null;
		Object obj = null;

		@Override
		public void run() {
			try {
				while ((obj = in.readObject()) != null) {
					System.out.println("got an object from server");
					System.out.println(obj.getClass());
					nameToShow = (String) obj;
					checkBoxState = (boolean[]) in.readObject();
					otherSeqsMap.put(nameToShow, checkBoxState);
					listVector.add(nameToShow);
					incomingList.setListData(listVector);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class MyPlayMineListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (mySequence != null) {
				sequence = mySequence;
			}
		}
	}

	public class MyListSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent le) {
			if (!le.getValueIsAdjusting()) {
				String selected = (String) incomingList.getSelectedValue();
				if (selected != null) {
					boolean[] selectedState = (boolean[]) otherSeqsMap
							.get(selected);
					changeSequence(selectedState);
					sequencer.stop();
					buildTrackandStart();

				}
			}
		}
	}

	public void changeSequence(boolean[] checkBoxState) {
		for (int i = 0; i < 256; i++) {
			JCheckBox check = checkboxList.get(i);
			if (checkBoxState[i]) {
				check.setSelected(true);
			} else {
				check.setSelected(false);
			}
		}
	}
}
