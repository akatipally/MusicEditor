package cs3500.music.commons;

/**
 * Octave enum that enumerates the octave numbers that a note can take. This protects
 * the larger implementation from having notes with bogus octave values.
 */
public enum Octave {

  ZERO,

  ONE,

  TWO,

  THREE,

  FOUR,

  FIVE,

  SIX,

  SEVEN,

  EIGHT,

  NINE,

  TEN;



  @Override
  public String toString() {
    String result = "";
    switch (this) {
      case ZERO:
        result = "0";
        break;
      case ONE:
        result = "1";
        break;
      case TWO:
        result = "2";
        break;
      case THREE:
        result = "3";
        break;
      case FOUR:
        result = "4";
        break;
      case FIVE:
        result = "5";
        break;
      case SIX:
        result = "6";
        break;
      case SEVEN:
        result = "7";
        break;
      case EIGHT:
        result = "8";
        break;
      case NINE:
        result = "9";
        break;
      case TEN:
        result = "10";
        break;
      default:
        throw new IllegalArgumentException("Invalid octave.");
    }
    return result;

  }


}
