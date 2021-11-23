package cs3500.music.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import cs3500.music.commons.Note;
import cs3500.music.commons.Octave;
import cs3500.music.commons.Pitch;


public class MusicSheet {

  /**
   * A music sheet is represented by a HashMap, where the keys are Integers that represent
   * beat number, and each key is mapped to an ArrayList of Notes that represent all the notes
   * played at this beat.
   */
  private TreeMap<Integer, ArrayList<Note>> beats;

  /**
   * Constructor that initializes the fields of a music sheet.
   */
  public MusicSheet() {
    this.beats = new TreeMap<>();
  }

  /**
   * This method takes in a Note and a start beat number and a duration for how long
   * the note will last. The meantToBeSustain is only used for the special case of adding sustains
   * during an overlay. The reason this special case was necessary is because of the implementation
   * details. The adding process requires a for loop and an index variable, making the
   * note a sustain if the index is not 0. However, during an overlay, each sustain is added
   * individually which will make the index 0 every single add operation. Thus, the special case
   * boolean prevents this from happening.
   * Adding notes works as such: The method, if given proper indices that are not out of bounds,
   * will add a beginnning note to the starting beat of the TreeMap, and will add a sustain to
   * every part of the TreeMap indexed until the value beat + duration is reached. If the
   * note runs into a beginning note of the same pitch and octave before beat + duration is reached,
   * the method will cease. If the beginning note occurs during a sustain of the same note at that
   * beat, the sustain will be overwritten by a beginning note and the normal add process until
   * beat + duration is reached. If the beginning note occurs during a beginning note of the same
   * note at that beat, it will run the normal add process, resulting in either the same note
   * as before if the duration was shorter than what was there before, or extending the sustain
   * until beat + duration is reached.
   *
   * @param note             The note to be added.
   * @param duration         The duration of the note in beats.
   * @param beat             The starting beat of the note.
   * @param meantToBeSustain The special case boolean used during overlays.
   * @throws IllegalArgumentException If the duration or beat numbers were invalid (negative).
   */
  public void addNote(Note note, int duration, int beat, boolean meantToBeSustain)
          throws IllegalArgumentException {
    if (beat < 0 || duration < 0) {
      throw new IllegalArgumentException("Invalid duration or beat number.");
    }
    for (int n = 0; n < duration; n++) {
      if (n == 0 && !meantToBeSustain) { //Special case boolean comes into play here.
        Pitch pitch = note.getPitch();
        Octave octave = note.getOctave();
        int instrument = note.getInstrument();
        int volume = note.getVolume();
        note = new Note(pitch, octave, true, instrument, volume);
      } else {
        Pitch pitch = note.getPitch();
        Octave octave = note.getOctave();
        int instrument = note.getInstrument();
        int volume = note.getVolume();
        note = new Note(pitch, octave, false, instrument, volume);
      }
      if (beats.containsKey(beat + n)) {
        if (beats.get(beat + n).contains(note)) {
          int oldIndex = beats.get(beat + n).indexOf(note);
          if (note.isBeginningOfNote() && beats.get(beat + n).get(oldIndex).isBeginningOfNote()) {
            continue; //This is the case of this being a begin note and a begin note existing here.
          }
          if (note.isBeginningOfNote()
                  && !(beats.get(beat + n).get(oldIndex).isBeginningOfNote())) {
            beats.get(beat + n).set(oldIndex, note);
            continue; //This is the case of a begin note starting where there was a sustain.
          }
          if (!note.isBeginningOfNote()
                  && beats.get(beat + n).get(oldIndex).isBeginningOfNote()) {
            break; //This is the case of a sustain reaching a begin note and terminating the add.
          }
          if (!note.isBeginningOfNote()
                  && !(beats.get(beat + n).get(oldIndex).isBeginningOfNote())) {
            continue; //This is the case of a sustain being put on a sustain.
          }

        }
        ArrayList newBeats = beats.get(beat + n);
        newBeats.add(note);
        sortListOfNotes(newBeats);
        beats.put(beat + n, newBeats);
      } else {
        ArrayList newBeats = new ArrayList<Note>();
        newBeats.add(note);
        this.beats.put(beat + n, newBeats);
      }
    }
  }

  /**
   * This method takes in a Note and a beginning beat, and removes all instances of the note
   * in following beats until it reaches a rest or a begin note of the same pitch and octave.
   *
   * @param note          The note to be removed.
   * @param beginningBeat The beat to start removing from.
   * @throws IllegalArgumentException If the note does not exist at this beat, or the beginningBeat
   *                                  is not a valid beat in the sheet.
   */
  public void deleteEntireNote(Note note, int beginningBeat) throws IllegalArgumentException {
    if (!(this.beats.containsKey(beginningBeat))) {
      throw new IllegalArgumentException("This beat has no notes.");
    }
    if (!(this.beats.get(beginningBeat).contains(note))) {
      throw new IllegalArgumentException("This beat does not contain this note.");
    }
    else {
      int index = this.beats.get(beginningBeat).indexOf(note);
      Note thisNote = this.beats.get(beginningBeat).get(index);
      if (!thisNote.isBeginningOfNote()) {
        deleteEntireNote(note, beginningBeat - 1);
        return;
      }
      else {
        removeNote(note, beginningBeat);
      }
    }
  }

  /**
   * To remove a note from a song.
   * @param note the note to be removed
   * @param beginningBeat the starting beat to remove the note from
   * @throws IllegalArgumentException if you cannot remove the note given or no note exists
   */
  public void removeNote(Note note, int beginningBeat)
          throws IllegalArgumentException {
    if (!(this.beats.containsKey(beginningBeat))) {
      throw new IllegalArgumentException("This beat has no notes.");
    }
    if (!(this.beats.get(beginningBeat).contains(note))) {
      throw new IllegalArgumentException("This beat does not contain this note.");
    } else {
      boolean noteNotOver = true;
      do {
        ArrayList<Note> newBeats = this.beats.get(beginningBeat);
        int index = newBeats.indexOf(note);
        newBeats.remove(index);
        // The method keeps removing notes and iterating beginningNote
        // until a begin note of this pitch and octave or rest occurs.
        if (this.beats.containsKey(beginningBeat + 1)) {
          if (this.beats.get(beginningBeat + 1).contains(note)) {
            int nextIndex = this.beats.get(beginningBeat + 1).indexOf(note);
            if (!(this.beats.get(beginningBeat + 1).get(nextIndex).isBeginningOfNote())) {
              beginningBeat++;
            } else {
              noteNotOver = false;
            }
          } else {
            noteNotOver = false;
          }
        } else {
          noteNotOver = false;
        }
      } while (noteNotOver);
    }
  }

  /**
   * Returns the highest pitched note present in the sheet.
   *
   * @return The highest Note.
   */

  public Note getHighestNote() {
    Note highestNote = null;
    if (this.beats.isEmpty()) {
      return null;
    } else {
      for (Object value : this.beats.values()) {
        ArrayList<Note> notes = (ArrayList<Note>) value;
        for (int n = 0; n < notes.size(); n++) {
          if (highestNote == null) {
            highestNote = notes.get(n);
          } else {
            if (notes.get(n).compareTo(highestNote) > 0) {
              highestNote = notes.get(n);
            }
          }
        }
      }
    }
    return highestNote;
  }

  /**
   * Gets the lowest pitched note in the sheet.
   * @return Loweste note.
   */
  public Note getLowestNote() {
    Note lowestNote = null;
    if (this.beats.isEmpty()) {
      return null;
    } else {
      for (Object value : this.beats.values()) {
        ArrayList<Note> notes = (ArrayList<Note>) value;
        for (int n = 0; n < notes.size(); n++) {
          if (lowestNote == null) {
            lowestNote = notes.get(n);
          } else {
            if (notes.get(n).compareTo(lowestNote) < 0) {
              lowestNote = notes.get(n);
            }
          }
        }
      }
    }
    return lowestNote;
  }

  /**
   * Returns the farthest beat reached.
   *
   * @return Furthest beat.
   */
  public Integer getFurthestBeat() {
    Integer furthestBeat = -1;
    for (Integer key : this.beats.keySet()) {
      if (key > furthestBeat) {
        furthestBeat = key;
      }
    }
    if (furthestBeat == -1) {
      return -1;
    }
    return furthestBeat + 1;
  }

  /**
   * Sorts a list of notes based on their pitch and octave, lowest to highest.
   */
  public void sortListOfNotes(ArrayList<Note> notes) {
    Collections.sort(notes);
  }

  /**
   * Returns the collection of data representing the beats and their notes.
   */
  public TreeMap<Integer, ArrayList<Note>> getBeats() {
    return this.beats;
  }

  /**
   * Method that adds a rest.
   * @param measureLength The length of rest to add.
   */
  public void addRest(int measureLength) {
    for (int n = 1; n <= measureLength; n++) {
      this.beats.put(this.getFurthestBeat() + n, new ArrayList<>());
    }
  }

  /**
   * To get the beginning beat of a note.
   * @param note the note to work with
   * @param beat the beat at which it is
   * @return the beginning beat of the note
   * @throws IllegalArgumentException if the beat given is invalid
   */
  public int getBeginningOfNote(Note note, int beat) throws  IllegalArgumentException {
    if (!(this.beats.containsKey(beat))) {
      throw new IllegalArgumentException("This beat has no notes.");
    }
    if (!(this.beats.get(beat).contains(note))) {
      throw new IllegalArgumentException("This beat does not contain this note.");
    }
    else {
      int index = this.beats.get(beat).indexOf(note);
      Note thisNote = this.beats.get(beat).get(index);
      if (!thisNote.isBeginningOfNote()) {
        getBeginningOfNote(note, beat - 1);
      }
      else {
        return beat;
      }
    }
    return 0;
  }

  /**
   * This method takes in a note and a beat number, and if the note is present at this beat
   * and is the beginning of the note, will measure the amount of time that it plays until
   * the next rest or another beginning of this note.
   *
   * @param note The Note to find the duration of.
   * @param beat The beat number this Note starts from.
   * @return The int representing how many beats the note lasts.
   * @throws IllegalArgumentException If the note is not present at this beat or if it is a sustain
   *                                  at this beat.
   */
  public int getBeatDuration(Note note, int beat) throws IllegalArgumentException {
    int length = 0;
    int n = 1;
    boolean noteNotOver = true;
    if (note.isBeginningOfNote()) {
      if (this.beats.get(beat).contains(note)) {
        int index = beats.get(beat).indexOf(note);
        if (!(this.beats.get(beat).get(index).isBeginningOfNote())) {
          throw new IllegalArgumentException("Must be beginning of note.");
        }
        do {
          if (this.beats.containsKey(beat + n)) {
            if (this.beats.get(beat + n).contains(note)) {
              index = beats.get(beat + n).indexOf(note);
              if (this.beats.get(beat + n).get(index).isBeginningOfNote()) {
                return n;
              } else {
                n++;
                length++;
              }
            } else {
              return n;
            }
          } else {
            return n;
          }
        } while (noteNotOver);

      } else {
        return length;
      }
    }
    throw new IllegalArgumentException("Must be a beginning of a note.");
  }

  /**
   * Returns the length of the piece.
   *
   * @return An integer repesenting the length of the piece.
   */
  public int amountOfBeats() {
    return this.beats.size();
  }

  /**
   * To get the sheet's state.
   * @return the sheet's state
   */
  public String getSheetState() {
    ArrayList<Note> newNotes = new ArrayList<>();
    for (Octave oct : Octave.values()) {
      for (Pitch pit : Pitch.values()) {
        if (oct.equals(Octave.TEN) && pit.equals(Pitch.G)) {
          break;
        }
        newNotes.add(new Note(pit, oct, false, 0, 0));
      }
    }
    StringBuilder result = new StringBuilder("");
    if (this.getFurthestBeat() == -1 || this.getHighestNote() == null
            || this.getLowestNote() == null) {
      return "No notes to present.";
    }

    int beginIndex = newNotes.indexOf(this.getLowestNote());
    int endIndex = newNotes.indexOf(this.getHighestNote());
    List<Note> printNotes = newNotes.subList(beginIndex, endIndex + 1);
    result.append("\n");
    ArrayList<Integer> beatNumbers = new ArrayList<>();
    int beatNumberColumnLength = String.valueOf(this.getFurthestBeat()).toString().length();
    for (int n = 0; n < beatNumberColumnLength; n++) {
      result.append(" ");
    }
    result.append("  ");
    for (int n = 0; n < printNotes.size(); n++) {
      result.append(printNotes.get(n).toString());
      for (int i = printNotes.get(n).toString().length(); i < 5; i++) {
        result.append(" ");
      }
    }
    result.append("\n");
    for (int n = 0; n < this.getFurthestBeat(); n++) {
      beatNumbers.add(n);
    }
    for (int n = 0; n < beatNumbers.size(); n++) {
      Integer p = beatNumbers.get(n);
      for (int i = beatNumberColumnLength; i > p.toString().length(); i--) {
        result.append(" ");
      }
      result.append((beatNumbers.get(n)));
      result.append("  ");
      if (!(this.getBeats().containsKey(beatNumbers.get(n)))) {
        for (int j = 0; j < printNotes.size(); j++) {
          result.append("     ");
        }
      } else {
        for (int j = 0; j < printNotes.size(); j++) {
          if (this.getBeats().get(beatNumbers.get(n)).contains(printNotes.get(j))) {
            int indexNote = this.getBeats().get(beatNumbers.get(n)).indexOf(printNotes.get(j));
            if (this.getBeats().get(beatNumbers.get(n)).get(indexNote).isBeginningOfNote()) {
              result.append("X");
              result.append("    ");
            } else {
              result.append("|") ;
              result.append("    ");
            }
          } else {
            result.append("     ");
          }
        }
      }
      result.append("\n");
    }
    return result.toString();
  }
}