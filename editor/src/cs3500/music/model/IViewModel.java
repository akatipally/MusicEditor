package cs3500.music.model;

import java.util.ArrayList;
import java.util.TreeMap;

import cs3500.music.commons.Note;

/**
 * Interface for a view model to access data from the model without manipulating it.
 */
public interface IViewModel {

  /**
   * Gets all of the notes in the song.
   * @return a tree map of the notes
   */
  TreeMap<Integer, ArrayList<Note>> getNotes();

  /**
   * Gets the last beat of the song.
   * @return an int of last beat
   */
  int getEndBeat();

  /**
   * Gets the lowest note of the song.
   * @return the lowest note of the song
   */
  Note getLowestNote();

  /**
   * Gets the highest note of the song.
   * @return the highest note of the song
   */
  Note getHighestNote();

  /**
   * Gets the measure length for this piece.
   * @return The measure length in beats.
   */
  int getMeasureLength();

  /**
   * To get the note's duration.
   * @param note the note whose duration you want
   * @param startBeat the starting beat of that note
   * @return the duration of the note
   */
  int getNoteDuration(Note note, int startBeat);

  /**
   * To get the tempo of the song.
   * @return the tempo of the song
   */
  int getTempo();

  /**
   * To get the current beat playing.
   * @return the current beat
   */
  int getCurrBeat();

  /**
   * To increment the beat by 1.
   */
  void incrementBeat();


}
