package cs3500.music.model;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import cs3500.music.commons.Note;


/**
 * This is the interface for a MusicEditor model. It's parameterized over
 * the parameter MusicSheet.
 *
 * @param <MusicSheet> A sheet of music that holds information on beats and notes.
 */

public interface IMusicEditor<MusicSheet> {

  /**
   * This method creates a new music sheet, which is added to the list of music sheets
   * in the editor. This new sheet has no beats or notes, and can be indexed by giving the
   * order in which it was created.
   */
  void createNewSheet();

  /**
   * This method takes in an index for a sheet that is to be edited, the note that is to be added,
   * and adds a note of that pitch and octave to the sheet. The sheet index must be a valid one, so
   * within the range of 0 to the size of the music editor minus one. The beat and duration must be
   * greater than 0. If the beat being added overlaps with note of the same tone and octave, then
   * the adding process will stop, with the last sustain note being placed in the beat before the
   * start of the next note. If the note being added has a sustain of the same note at the beat in
   * question, the note will overwrite the sustain with a new begin note, and will continue to add
   * based on the duration, adding sustain notes wherever there is no note of that octave and pitch
   * until the duration is satisfied.
   *
   * @param sheetNumber The index of the sheet to add a note to.
   * @param note        The Note being added.
   * @param duration    The amount of beats that the note lasts.
   * @param beat        The beat that the note begins from.
   * @throws IllegalArgumentException If the sheetNumber, duration, or beat is invalid.
   */
  void addSingleNote(int sheetNumber, Note note, int duration, int beat)
          throws IllegalArgumentException;

  /**
   * This method takes in two indices that exist within the music editor's collection of sheets
   * and overlays the notes of the second index's sheet over the notes of the first index's sheet.
   * If a beginning note in the overlay's sheet overlaps with a beginning note in the base
   * sheet, the note will not change. If the beginning note of the overlay's sheet overlaps with
   * a sustain, the sustain will become a beginning note and the duration will extend for
   * as long as the note lasts. If a sustain overlaps with a beginning note in the original, the
   * beginning note will remain. If a sustain overlaps with a sustain, nothing happens.
   * The length of the original sheet will extend if there are notes in the overlay sheet's
   * measures that extends beyond the length of the original sheet.
   *
   * @param baseSheetIndex    The index of the base sheet.
   * @param overlaySheetIndex The index of the overlay's sheet.
   * @throws IllegalArgumentException If the indices of the sheets are invalid.
   */
  void overlayTwoSheets(int baseSheetIndex, int overlaySheetIndex);

  /**
   * This method will take two indices that exist within the collection of sheets
   * of the MusicEditor, and play the beats of the second sheet after the last beat of
   * the first sheet. The notes of the second sheet will commence on the beat
   * right after the last active note of the first sheet.
   *
   * @param baseSheetIndex The index of the sheet that will play first.
   * @param nextSheetIndex The index of the sheet that will play second.
   * @throws IllegalArgumentException If the sheet indices are out of bounds.
   */
  void playSheetsConsecutively(int baseSheetIndex, int nextSheetIndex);

  /**
   * This method will take a sheet index within the bounds of the editor, a note, and a beat
   * to start from and will delete every appearance of the note until the next rest of that note
   * or the next beginning note of that note. Essentially, if a beginning note or a sustain note
   * of the note given is present at the beat, it will continuously delete until the next beginning
   * or rest.
   *
   * @param sheetIndex The index of the sheet to have a note removed.
   * @param note       The note to be deleted.
   * @param beat       The beat to start from.
   * @throws IllegalArgumentException if the sheetIndex is out of bounds, the note doesnt exist at
   *                                  this beat, or the beat is out of bounds of the sheet.
   */
  void deleteNote(int sheetIndex, Note note, int beat) throws IllegalArgumentException;

  /**
   * This method allows the user to edit the length of a beat. The sheet index must be in bounds,
   * the note must exist at the start beat, and the newEndBeat must not be negative. If the note
   * runs into the beginning of the same note at a later beat, the method will stop.
   * The newEndBeat can be whatever the user wishes, either lower or higher than the original
   * end beat. The beat given must have the note as a BEGINNING of the note. It cannot be a sustain.
   * If the note is a sustain at this given beat, an IllegalArgumentException will be thrown
   * and the user will have to try again.
   *
   * @param sheetIndex The sheet index to edit.
   * @param note       The note that is to be edited.
   * @param startBeat  The beat to start editing from.
   * @param newEndBeat The new ending beat of the note.
   * @throws IllegalArgumentException If the sheetIndex is out of bounds, if the note is out of
   *                                  bounds, if the startBeat is out of bounds, if the note is not
   *                                  in the startBeat, if the newEndBeat is out of bounbds, and if
   *                                  the note being edited was a sustain note.
   */
  void editNote(int sheetIndex, Note note, int startBeat, int newEndBeat)
          throws IllegalArgumentException;

  /**
   * Gets all the beats in a song.
   * @param index the sheet at which to get the beats
   * @return the treemap of all the beats
   */
  TreeMap<Integer, ArrayList<Note>> getBeats(int index);

  /**
   * Method that allows a user to add a rest at a particular beat. If this method is not called
   * on a beat, it will simply have a null value at the key of that beat.
   * @param index The beat to add a rest at.
   * @param measureLength The amount of rest to add.
   */
  void addRest(int index, int measureLength);

  /**
   * To set the tempo of the song.
   * @param tempo the tempo to be set to
   */
  void setTempo(int tempo);

  /**
   * To get the tempo of the song.
   * @return the tempo of the song
   */
  int getTempo();

  /**
   * A method to get all of the notes of a song, in the form of a unordered list.
   * @return THe list of notes.
   */
  List<Note> allNotes();
}


