package Modules.MIDI;

// Java program showing how to change the instrument type
import javax.sound.midi.*;
import java.util.*;

public class MIDItest {
	
	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException {
		
		MIDItest player = new MIDItest();
		
		int note = 10;
		int instrument = 10;
		
//		Scanner in = new Scanner(System.in);
//		System.out.println("Enter the instrument to be played");
//		int instrument = in.nextInt();
//		System.out.println("Enter the note to be played");
//		int note = in.nextInt();
		
		
		MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
		MidiDevice device = null;
		for(MidiDevice.Info i: info) {
			System.out.printf("%s\n| %s\n| Vendor: %s\n| Version: %s\n", i.getName(), i.getDescription(), i.getVendor(), i.getVersion());
			if(i.getName().equals("MIDI function")) {
				device = MidiSystem.getMidiDevice(i);
				break;
			}
		}
		
		if(device != null) {
			device.open();
			Receiver rec = device.getReceiver();
			ShortMessage msg = new ShortMessage();
			msg.setMessage(ShortMessage.NOTE_ON, 1, 1, 1);
			rec.send(msg, -1);
		}
		
		
		
		
		
		
		
		try {
			
			Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.open();
			Sequence sequence = new Sequence(Sequence.PPQ, 4);
			Track track = sequence.createTrack();
			
			// Set the instrument type
			track.add(makeEvent(192, 1, instrument, 0, 1));
			
			// Add a note on event with specified note
			track.add(makeEvent(144, 1, note, 100, 1));
			
			// Add a note off event with specified note
			track.add(makeEvent(128, 1, note, 100, 4));
			
			track.add(makeEvent(144, 1, note+10, 100, 5));
			track.add(makeEvent(128, 1, note+10, 100, 8));
			track.add(makeEvent(144, 1, note+20, 100, 9));
			track.add(makeEvent(128, 1, note+20, 100, 12));
			track.add(makeEvent(144, 1, note+30, 100, 13));
			track.add(makeEvent(128, 1, note+30, 100, 16));
			
			sequencer.setSequence(sequence);
			sequencer.start();
			
			
			
			while (true) {
				
				if (!sequencer.isRunning()) {
					sequencer.close();
					System.exit(1);
				}
			}
		}
		catch (Exception ex) {
			
			ex.printStackTrace();
		}
	}
	
	public static MidiEvent makeEvent(int command, int channel, int note, int velocity, int tick)
	{
		
		MidiEvent event = null;
		
		try {
			
			ShortMessage a = new ShortMessage();
			a.setMessage(command, channel, note, velocity);
			
			event = new MidiEvent(a, tick);
		}
		catch (Exception ex) {
			
			ex.printStackTrace();
		}
		return event;
	}
}
