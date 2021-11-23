package cs3500.music.commons;

/**
 * Pitch enum that allows only certain values to represent pitch. Essentially protects
 * the overarching implementation from having notes with bogus pitch values.
 */

public enum Pitch {

  C,

  CSHARP,

  D,

  DSHARP,

  E,

  F,

  FSHARP,

  G,

  GSHARP,

  A,

  ASHARP,

  B;

  @Override
  public String toString() {
    String result = "";
    switch (this) {
      case C:
        result = "C";
        break;
      case CSHARP:
        result = "C#";
        break;
      case D:
        result = "D";
        break;
      case DSHARP:
        result = "D#";
        break;
      case E:
        result = "E";
        break;
      case F:
        result = "F";
        break;
      case FSHARP:
        result = "F#";
        break;
      case G:
        result = "G";
        break;
      case GSHARP:
        result = "G#";
        break;
      case A:
        result = "A";
        break;
      case ASHARP:
        result = "A#";
        break;
      case B:
        result = "B";
        break;
      default:
        throw new IllegalArgumentException("Invalid pitch.");
    }
    return result;
  }
}
