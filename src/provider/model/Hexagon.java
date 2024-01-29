package cs3500.reversi.provider.model;

import java.util.Objects;

/**
 * Represents a single hexagon tile on a Reversi game board. Each hexagon is positioned using x-y
 * coordinates with the origin at the center of the board (0, 0). The x coordinate determines the
 * row position (increasing from top to bottom), and the y coordinate determines the column
 * position (increasing from bottom-left to top-right) on the game board.
 */
public final class Hexagon {
  private final int x;
  private final int y;

  /**
   * Constructs a Hexagon object at (x, y).
   *
   * @param x the x coordinate representing the row position of the hexagon increasing from the
   *          top to the bottom of the Reversi game board (0-based index)
   * @param y the y coordinate representing the column position of the hexagon increasing from the
   *          bottom-left to the top-right of the Reversi game board (0-based index)
   */
  public Hexagon(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Gets the x coordinate of <code>this</code> hexagon.
   *
   * @return the x coordinate
   */
  public int getX() {
    return this.x;
  }

  /**
   * Gets the y coordinate of <code>this</code> hexagon.
   *
   * @return the y coordinate.
   */
  public int getY() {
    return this.y;
  }

  @Override
  public String toString() {
    return String.format("(%d, %d)", this.x, this.y);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Hexagon)) {
      return false;
    }
    Hexagon otherHexagon = (Hexagon) o;
    return this.x == otherHexagon.x && this.y == otherHexagon.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.x, this.y);
  }
}
