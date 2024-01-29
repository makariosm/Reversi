package cs3500.reversi.model;

/**
 * Basic interface of a cell for a reversi game. For current implementations.
 * it is either a square or hex.
 */
public interface ReversiCell {

  /**
   * Determines if this cell is equal to a given object, based on their coordinates.
   * @param b The given other object.
   * @return true if coordinates are equal, false otherwise.
   */
  public boolean equals(Object b);

  /**
   * Determines q coordinate of this hex.
   * @return integer of hex's q coordinate.
   */
  public int getQ();

  /**
   * Determines r coordinate of this hex.
   * @return integer of hex's r coordinate.
   */
  public int getR();

  /**
   * Determines s coordinate of this hex.
   * @return integer of hex's s coordinate.
   */
  public int getS();

  /**
   * Determines x coordinate of this square.
   * @return integer of square's x coordinate.
   */
  public int getX();

  /**
   * Determines y coordinate of this square.
   * @return integer of hex's y coordinate.
   */
  public int getY();

}
