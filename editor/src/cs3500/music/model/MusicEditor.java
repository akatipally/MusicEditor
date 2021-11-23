package cs3500.music.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import cs3500.music.commons.Note;


/**
 * MusicEditor class that is an implementation of IMusicEditor. It holds
 * an ArrayList of sheets which can be added to. These sheets can have notes added to them,
 * have notes deleted, edit a notes length, overlay one sheet's notes on top of the other,
 * and set a sheet's notes to be played after another sheet.
 */
public class MusicEditor implements IMusicEditor<MusicSheet> {

  ArrayList<MusicSheet> sheets;
  int tempo;

  /**
   * Constructor that initializes the list of MusicSheet.
   */
  public MusicEditor() {
    this.sheets = new ArrayList<MusicSheet>();
  }

  @Override
  public void createNewSheet() {
    this.sheets.add(new MusicSheet());
  }

  @Override
  public void addSingleNote(int sheetNumber, Note note, int duration, int beat)
          throws IllegalArgumentException {
    //Following if statements check for invalid input.
    if (sheetNumber > this.sheets.size() - 1) {
      throw new IllegalArgumentException("No sheet at that index.");
    }
    if (sheetNumber < 0) {
      throw new IllegalArgumentException("Invalid index");
    }
    if (duration < 0) {
      throw new IllegalArgumentException("Invalid duration. Negative number.");
    }
    if (beat < 0) {
      throw new IllegalArgumentException("Invalid beat. Negative number.");
    } else {
      //The logic of addNote comes from the MusicSheet class.
      this.sheets.get(sheetNumber).addNote(note, duration, beat, !(note.isBeginningOfNote()));
    }
  }

  @Override
  public void overlayTwoSheets(int baseSheetIndex, int overlaySheetIndex)
          throws IllegalArgumentException {
    if (baseSheetIndex > this.sheets.size() - 1 || overlaySheetIndex > this.sheets.size() - 1) {
      throw new IllegalArgumentException("These sheet indices are invalid.");
    }
    if (baseSheetIndex < 0 || overlaySheetIndex < 0) {
      throw new IllegalArgumentException("These sheet indices are invalid.");
    } else {
      MusicSheet overlaySheet = this.sheets.get(overlaySheetIndex);
      TreeMap<Integer, ArrayList<Note>> overlayInfo = overlaySheet.getBeats();
      Iterator i = overlayInfo.keySet().iterator();
      //The way this part works is iterating through the beats of the the second sheet,
      //retrieving the notes at each beat and adding them to the first sheet.
      while (i.hasNext()) {
        Integer beat = (Integer) i.next();
        ArrayList<Note> currNotes = overlayInfo.get(beat);
        for (int n = 0; n < currNotes.size(); n++) {
          if (currNotes.get(n).isBeginningOfNote()) {
            //If the note at this current beat is a beginning note, it uses the normal
            //implementation of adding notes which can be read in the interface.
            int duration = overlaySheet.getBeatDuration(currNotes.get(n), beat);
            this.sheets.get(baseSheetIndex).addNote(currNotes.get(n), duration, beat, false);
          } else {
            //If the note at this beat of the second sheet is a sustain, and there is no note
            //of this type in the first sheet, this part will add this sustain.
            if (this.sheets.get(baseSheetIndex).getBeats().containsKey(beat)) {
              if (!(this.sheets.get(baseSheetIndex).getBeats().get(beat)
                      .contains(currNotes.get(n)))) {
                //The reason why the meantToBeSustain boolean is true here, is because the
                //implementation of add overwrites the Note's own beginningOfBeat boolean.
                //This is the only special case where my implementation does not want this, which
                //is why this boolean is necessary.
                this.sheets.get(baseSheetIndex).addNote(currNotes.get(n), 1, beat, true);
              } else {
                continue;
              }
            } else {
              //If the beat of the first sheet has this note, it will use the logic of addNote
              //to determine what to do.
              this.sheets.get(baseSheetIndex).addNote(currNotes.get(n), 1, beat, true);
            }

          }
        }
      }
    }
  }

  @Override
  public void playSheetsConsecutively(int baseSheetIndex, int nextSheetIndex)
          throws IllegalArgumentException {
    if (baseSheetIndex > this.sheets.size() - 1 || nextSheetIndex > this.sheets.size() - 1) {
      throw new IllegalArgumentException("These sheet indices are invalid.");
    }
    if (baseSheetIndex < 0 || nextSheetIndex < 0) {
      throw new IllegalArgumentException("These sheet indices are invalid.");
    } else {
      //This method works the same way as the overlay method, but instead simply takes the notes
      //of the second sheet and places them on the first sheet with their start beats
      //shifted by an amount equal to the farthest beat of the first sheet.
      int endofBase = this.sheets.get(baseSheetIndex).amountOfBeats();
      MusicSheet nextSheet = this.sheets.get(nextSheetIndex);
      TreeMap<Integer, ArrayList<Note>> nextInfo = nextSheet.getBeats();
      Iterator i = this.sheets.get(nextSheetIndex).getBeats().keySet().iterator();
      while (i.hasNext()) {
        Integer beat = (Integer) i.next();
        ArrayList<Note> currNotes = nextInfo.get(beat);
        for (int n = 0; n < currNotes.size(); n++) {
          if (currNotes.get(n).isBeginningOfNote()) {
            int duration = nextSheet.getBeatDuration(currNotes.get(n), beat);
            int newBeat = beat + endofBase;
            this.sheets.get(baseSheetIndex).addNote(currNotes.get(n), duration, newBeat, false);
          } else {
            continue;
          }
        }
      }
    }
  }

  @Override
  public void deleteNote(int sheetIndex, Note note, int beat)
          throws IllegalArgumentException {
    if (sheetIndex > this.sheets.size() - 1 || sheetIndex < 0) {
      throw new IllegalArgumentException("Sheet index is invalid.");
    } else {
      if (!(this.sheets.get(sheetIndex).getBeats().containsKey(beat))) {
        throw new IllegalArgumentException("This sheet has no notes at this beat.");
      } else {
        if (!(this.sheets.get(sheetIndex).getBeats().get(beat).contains(note))) {
          throw new IllegalArgumentException("This note isnt played at this beat.");
        } else {
          this.sheets.get(sheetIndex).deleteEntireNote(note, beat);
        }
      }
    }
  }

  @Override
  public void editNote(int sheetIndex, Note note, int startBeat, int newEndBeat)
          throws IllegalArgumentException {
    if (sheetIndex < 0 || sheetIndex > this.sheets.size() - 1) {
      throw new IllegalArgumentException("Invalid sheet index.");
    }

    if (!(this.sheets.get(sheetIndex).getBeats().containsKey(startBeat))) {
      throw new IllegalArgumentException("This beat has no notes.");
    }

    if (!(this.sheets.get(sheetIndex).getBeats().get(startBeat).contains(note))) {
      throw new IllegalArgumentException("This note isnt played at this beat.");
    }
    //The way this if statement works is the note can only be edited if it was a beginning note
    // and not a sustain. If the note was made shorter, it will add the note and then delete
    //the remainder after the new endBeat. If the note was made longer, it will add the note and
    //that's it.
    startBeat = this.sheets.get(sheetIndex).getBeginningOfNote(note, startBeat);
    int i = this.sheets.get(sheetIndex).getBeatDuration(note, startBeat);
    this.deleteNote(sheetIndex, note, startBeat);
    this.addSingleNote(sheetIndex, note, newEndBeat - startBeat + 1, startBeat);

  }

  /**
   * Gets a music sheet at an index.
   * @param sheetIndex the index to get the sheet from
   * @return the MusicSheet
   * @throws IllegalArgumentException if it is an invalid index
   */
  public MusicSheet getSheet(int sheetIndex) throws IllegalArgumentException {
    if (this.sheets.size() > sheetIndex) {
      return this.sheets.get(sheetIndex);
    }
    else {
      return (this.sheets.get(sheetIndex));
    }
  }

  @Override
  public TreeMap<Integer, ArrayList<Note>> getBeats(int index) {
    if (this.sheets.size() < 0 || this.sheets.size() > index) {
      return this.sheets.get(index).getBeats();
    }
    else {
      throw new IllegalArgumentException("Invalid index.");
    }
  }

  @Override
  public void addRest(int index, int measureLength) {
    this.getSheet(index).addRest(measureLength);
  }

  /**
   * To set the tempo of the song.
   * @param tempo the tempo to be set to
   */
  public void setTempo(int tempo) {
    this.tempo = tempo;
  }

  /**
   * To get the tempo of the song.
   * @return the tempo of the song
   */
  public int getTempo() {
    return this.tempo;
  }

  /**
   * To get the state of the song's sheet.
   * @param index the sheet at which to get the state
   * @return the state of the sheet
   */
  public String getSheetState(int index) {
    if (index < 0 || index > this.sheets.size() ) {
      throw new IllegalArgumentException("Invalid index.");
    }
    else {
      this.getSheet(0);
      return this.getSheet(index).getSheetState();
    }
  }

  @Override
  public List<Note> allNotes() {
    List<Note> notes = new ArrayList<Note>();
    for (int i = 0; i < this.sheets.get(0).getFurthestBeat(); i++) {
      if (this.getBeats(0).containsKey(i)) {
        notes.addAll(this.getBeats(0).get(i));
      }
    }
    return notes;
  }
}

