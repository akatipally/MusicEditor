package cs3500.music.model;

import java.util.ArrayList;
import java.util.TreeMap;

import cs3500.music.commons.Note;

/**
 * The implementation of the ViewModel interface. The purpose of this class is to be a barrier
 * between the implementations of IMusicEditor and the IMusicView. To use this, a ViewModel
 * needs to be passed the Editor, the index of the song to play, the measureLength to display
 * and the tempo of the song.
 */
public class ViewModel implements IViewModel {
  private final TreeMap<Integer, ArrayList<Note>> notes;
  private final int measureLength;
  private int tempo;
  private int sheetPadding;
  private int currBeat;

  /**
   * Constructor for a view model.
   * @param editor the editor with which you want to get the values
   * @param index the sheet number to get them from
   * @param measureLength the length of the measure
   * @param tempo the tempo of the song
   */
  public ViewModel(IMusicEditor editor, int index, int measureLength, int tempo) {
    notes = editor.getBeats(index);
    this.measureLength = measureLength;
    this.tempo = editor.getTempo();
    this.currBeat = 0;
  }

  /**
   * To return all the notes in a song.
   * @return the notes in the song
   */
  public TreeMap<Integer, ArrayList<Note>> getNotes() {
    return notes;
  }

  @Override
  public int getEndBeat() {
    try {
      return this.notes.lastKey();
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  public Note getLowestNote() {
    Note currLowestNote = null;
    for (Object value : notes.values()) {
      ArrayList<Note> currNotes = (ArrayList<Note>) value;
      for (int n = 0; n < currNotes.size(); n++) {
        if (currLowestNote == null) {
          currLowestNote = currNotes.get(n);
        } else {
          if (currNotes.get(n).compareTo(currLowestNote) < 0) {
            currLowestNote = currNotes.get(n);
          }
        }
      }
    }
    return currLowestNote;
  }

  @Override
  public int getTempo() {
    return this.tempo;
  }

  @Override
  public int getCurrBeat() {
    return this.currBeat;
  }

  @Override
  public void incrementBeat() {
    this.currBeat += 1;
  }

  @Override
  public Note getHighestNote() {
    Note currHighestNote = null;
    for (Object value : notes.values()) {
      ArrayList<Note> currNotes = (ArrayList<Note>) value;
      for (int n = 0; n < currNotes.size(); n++) {
        if (currHighestNote == null) {
          currHighestNote = currNotes.get(n);
        } else {
          if (currNotes.get(n).compareTo(currHighestNote) > 0) {
            currHighestNote = currNotes.get(n);
          }
        }
      }
    }
    return currHighestNote;
  }

  @Override
  public int getNoteDuration(Note note, int startBeat) {
    int length = 0;
    int n = 1;
    boolean noteNotOver = true;
    if (note.isBeginningOfNote()) {
      if (this.notes.get(startBeat).contains(note)) {
        int index = notes.get(startBeat).indexOf(note);
        if (!(this.notes.get(startBeat).get(index).isBeginningOfNote())) {
          throw new IllegalArgumentException("Must be beginning of note.");
        }
        do {
          if (this.notes.containsKey(startBeat + n)) {
            if (this.notes.get(startBeat + n).contains(note)) {
              index = notes.get(startBeat + n).indexOf(note);
              if (this.notes.get(startBeat + n).get(index).isBeginningOfNote()) {
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
   * To get the measure length of a song.
   * @return the measure length of the song
   */
  public int getMeasureLength() {
    return this.measureLength;
  }

}
