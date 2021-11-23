package cs3500.music.tests;


import java.io.IOException;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class MockMidiReceiver implements Receiver {

  private Appendable ap = null;

  public MockMidiReceiver(Appendable ap) {
    this.ap = ap;
  }

  @Override
  public void send(MidiMessage message, long timeStamp) {
    int info = ((ShortMessage) message).getData1();
    StringBuilder result = new StringBuilder("");
    result.append(info);
    result.append(" ");
    result.append(timeStamp);
    result.append(" ");
    int instrument = ((ShortMessage) message).getChannel();
    result.append(instrument);
    result.append("\n");
    try {
      this.ap.append(result.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void close() {
    this.close();
  }
}
