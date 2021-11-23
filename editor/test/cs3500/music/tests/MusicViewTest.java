package cs3500.music.tests;

import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import javax.sound.midi.InvalidMidiDataException;
import cs3500.music.model.IMusicEditor;
import cs3500.music.model.IViewModel;
import cs3500.music.model.ViewModel;
import cs3500.music.util.CompositionBuilder;
import cs3500.music.util.MusicBuilder;
import cs3500.music.util.MusicReader;
import cs3500.music.view.AudibleView;

import static org.junit.Assert.assertEquals;

/**
 * To test the MIDI music view.
 */
public class MusicViewTest {
  MusicReader reader = new MusicReader();
  CompositionBuilder builder = new MusicBuilder();

  @Test
  public void testOutput() {
    StringBuffer buffer = new StringBuffer();
    MockMidiReceiver receiver = new MockMidiReceiver(buffer);
    FileReader read;
    File file = new File("mary-little-lamb.txt");
    try {
      read = new FileReader(file);
    } catch (Exception e) {
      throw new IllegalArgumentException("File incorrect!");
    }
    reader.parseFile(read, builder);
    IMusicEditor editor = (cs3500.music.model.MusicEditor) builder.build();
    IViewModel model = new ViewModel(editor, 0, 4, editor.getTempo());
    AudibleView view = new AudibleView(model);
    view.setReceiver(receiver);
    try {
      view.renderSong(model, 200000);
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
    int i = 0;
    for (int n = 0; n < buffer.toString().length(); n++) {
      if (buffer.toString().charAt(n) == '\n') {
        i++;
      }
    }
    Scanner scan = new Scanner(buffer.toString());
    int firstNote = scan.nextInt();
    scan.next();
    scan.next();
    scan.next();
    int durationOfFirstNote = scan.nextInt();
    int instrument = scan.nextInt();
    int numberOfNotesInMaryHadALittleLamb = 34;
    int totalNumberOfExpectedMidiMessages = numberOfNotesInMaryHadALittleLamb * 2;
    int firstNoteOfMaryHadALittleLambFrequencyThatIsPlayed = 55;
    int tempo = 200000;
    int durationOfFirstNoteInt = 7 * tempo;
    int instrumentOfFirstNote = 0;
    //These 4 tests check all the essentials: The total number of midi messages,
    //frequency of a note that is played first, the duration of that note, and the instrument
    //of that note (or channel).
    assertEquals(totalNumberOfExpectedMidiMessages, i);
    assertEquals(firstNote, firstNoteOfMaryHadALittleLambFrequencyThatIsPlayed);
    assertEquals(durationOfFirstNote, durationOfFirstNoteInt);
    assertEquals(instrument, instrumentOfFirstNote);
  }

}

