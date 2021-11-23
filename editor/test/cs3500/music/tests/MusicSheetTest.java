package cs3500.music.tests;

import org.junit.Test;

import java.util.ArrayList;
import java.util.TreeMap;

import cs3500.music.model.MusicSheet;
import cs3500.music.commons.Note;
import cs3500.music.commons.Octave;
import cs3500.music.commons.Pitch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the MusicSheet cass.
 */
public class MusicSheetTest {

  MusicSheet sheet = new MusicSheet();

  Note note1 = new Note(Pitch.A, Octave.FIVE, true, 0, 1);
  Note note2 = new Note(Pitch.B, Octave.FIVE, true, 0, 1);
  Note note3 = new Note(Pitch.C, Octave.FOUR, true, 0, 1);

  @Test
  public void constructorTest() {
    assertEquals(true, sheet.getBeats().isEmpty());
  }

  @Test
  public void addNoteTest() {
    sheet.addNote(note1, 5, 0, false);
    sheet.addNote(note2, 4, 0, false);
    sheet.addNote(note3, 6, 3, false);
    TreeMap<Integer, ArrayList<Note>> info = sheet.getBeats();
    for (int n = 0; n == 5; n++) {
      assertEquals(true, info.get(n).contains(note1));
    }
    for (int n = 0; n == 4; n++) {
      assertEquals(true, info.get(n).contains(note2));
    }
    for (int n = 3; n == 6; n++) {
      assertEquals(true, info.get(n).contains(note3));
    }
  }

  @Test
  public void addNoteTestNoteCollidesIntoSameNoteAtLaterBeatAndTerminates() {

    sheet.addNote(note3, 6, 3, false);
    sheet.addNote(note3, 0, 6, false);
    TreeMap<Integer, ArrayList<Note>> info = sheet.getBeats();

    for (int n = 0; n == 6; n++) {
      assertEquals(true, info.get(n).contains(note3));
    }
    int indexOfStart = info.get(3).indexOf(note3);
    assertEquals(true, info.get(3).get(indexOfStart).isBeginningOfNote());
  }

  @Test
  public void addNoteTestMeantToBeSustain() {

    sheet.addNote(note3, 1, 3, true);
    TreeMap<Integer, ArrayList<Note>> info = sheet.getBeats();
    int indexOfStart = info.get(3).indexOf(note3);
    assertEquals(false, info.get(3).get(indexOfStart).isBeginningOfNote());

  }

  @Test
  public void addNoteTestWhereThereWasAlreadyASustainAndSustainOnSustain() {
    sheet.addNote(note3, 6, 2, false);
    sheet.addNote(note3, 7, 4, false);
    TreeMap<Integer, ArrayList<Note>> info = sheet.getBeats();

    for (int n = 0; n == 7; n++) {
      assertEquals(true, info.get(n).contains(note3));
    }
    int indexOfStart = info.get(4).indexOf(note3);
    int indexOfPrev = info.get(3).indexOf(note3);
    assertEquals(true, info.get(4).get(indexOfStart).isBeginningOfNote());
    assertEquals(false, info.get(3).get(indexOfPrev).isBeginningOfNote());
    //There is only note. It will be indexed the same place.
    assertEquals(false, info.get(6).get(indexOfStart).isBeginningOfNote());
  }

  @Test
  public void addNoteInvalidParametersTest() {
    try {
      sheet.addNote(note1, -1, 0, false);
      fail();
    } catch (Exception e) {
      try {
        sheet.addNote(note1, 4, -2, false);
        fail();
      } catch (Exception i) {
        //If it got pas everything else, the exceptions were thrown which is right.
      }
    }
  }

  @Test
  public void removeNoteTestWholeNoteAndPartOfNote() {

    sheet.addNote(note1, 5, 0, false);
    sheet.addNote(note2, 4, 0, false);
    sheet.addNote(note3, 6, 3, false);
    TreeMap<Integer, ArrayList<Note>> info = sheet.getBeats();

    for (int n = 0; n == 5; n++) {
      assertEquals(true, info.get(n).contains(note1));
    }
    for (int n = 0; n == 4; n++) {
      assertEquals(true, info.get(n).contains(note2));
    }
    for (int n = 3; n == 6; n++) {
      assertEquals(true, info.get(n).contains(note3));
    }

    sheet.removeNote(note1, 2);
    sheet.removeNote(note2, 0);

    for (int n = 0; n == 1; n++) {
      assertEquals(true, info.get(n).contains(note1));
    }

    for (int n = 2; n == 5; n++) {
      assertEquals(false, info.get(n).contains(note1));
    }
    for (int n = 0; n == 4; n++) {
      assertEquals(false, info.get(n).contains(note2));
    }
  }

  @Test
  public void removeNoteCollisionWithABeginningOfTheSameNoteTerminates() {
    sheet.addNote(note1, 5, 4, false);
    sheet.addNote(note1, 6, 0, false);
    sheet.removeNote(note1, 0);
    TreeMap<Integer, ArrayList<Note>> info = sheet.getBeats();

    for (int n = 0; n == 3; n++) {
      assertEquals(false, info.get(n).contains(note1));
    }
    for (int n = 4; n == 9; n++) {
      assertEquals(true, info.get(n).contains(note1));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeNoteIllegalBeatValue() {
    sheet.removeNote(note1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeNoteNoteNotAtThisBeat() {
    sheet.addNote(note1, 2, 0, false);
    sheet.removeNote(note2, 0);
  }

  @Test
  public void getHighestNoteTest() {
    sheet.addNote(note1, 1, 5, false);
    sheet.addNote(note2, 1, 3, false);
    sheet.addNote(note3, 1, 7, false);
    Note note = sheet.getHighestNote();
    assertEquals(true, note.equals(note2));
  }

  @Test
  public void getHighestNoteTestEmptySheet() {
    assertEquals(null, sheet.getHighestNote());
  }

  @Test
  public void getLowestNoteTest() {
    sheet.addNote(note1, 1, 5, false);
    sheet.addNote(note2, 1, 3, false);
    sheet.addNote(note3, 1, 7, false);
    Note note = sheet.getLowestNote();
    assertEquals(true, note.equals(note3));
  }

  @Test
  public void getLowestNoteTestEmptySheet() {
    assertEquals(null, sheet.getLowestNote());
  }

  @Test
  public void getFurthestBeatTest() {
    sheet.addNote(note1, 2, 9, false);
    sheet.addNote(note2, 1234, 3, false);
    int furthestBeat = sheet.getFurthestBeat();
    assertEquals(true, furthestBeat == 1237);
  }

  @Test
  public void getFurthestBeatTestEmptySheet() {
    assertEquals(true, -1 == sheet.getFurthestBeat());
  }

  @Test
  public void sortNotesTest() {
    sheet.addNote(note1, 1, 0, false);
    sheet.addNote(note2, 1, 0, false);
    sheet.addNote(note3, 1, 0, false);

    ArrayList<Note> sortedNotes = new ArrayList<Note>();
    sortedNotes.add(0, note3);
    sortedNotes.add(1, note1);
    sortedNotes.add(2, note2);
    sheet.sortListOfNotes(sheet.getBeats().get(0));
    assertEquals(sortedNotes, sheet.getBeats().get(0));
  }

  @Test
  public void toStringTestNoNotes() {
    assertEquals("No notes to present.", sheet.getSheetState());
  }

  @Test
  public void toStringTest() {
    sheet.addNote(note1, 1, 0, false);
    assertEquals(sheet.getSheetState(), "\n   A5   \n0  X    \n");
  }

  @Test
  public void getBeatDuration() {
    sheet.addNote(note1, 5, 0, false);
    sheet.addNote(note1, 5, 2, false);
    assertEquals(2, sheet.getBeatDuration(note1, 0));
    assertEquals(5, sheet.getBeatDuration(note1, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getBeatDurationDoesntWorkIfASustainNoteIsChosen() {
    sheet.addNote(note1, 5, 0, false);
    sheet.getBeatDuration(note1, 1);
  }

  @Test
  public void getAmountofBeatsTest() {
    sheet.addNote(note1, 5, 0, false);
    sheet.addNote(note2, 5, 2, false);
    assertEquals(7, sheet.amountOfBeats());
  }

}
