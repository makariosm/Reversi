package cs3500.reversi.model;

import java.util.Objects;

/**
 * The implementation for a square that is a reversi cell, ahs x and y coordinates.
 */
public class Square implements ReversiCell {

  private final int x;
  private final int y;

  /**
   * Constructs a Square, a cell of hexagonal shape with its coordinates in the reversi grid.
   * @param x The x coordinate of the hex cell
   * @param y The y coordinate of the hex cell
   */
  public Square(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Determines if this hex is equal to a given object, based on their coordinates.
   * @param b The given other object.
   * @return true if coordinates are equal, false otherwise.
   */
  @Override
  public boolean equals(Object b) {
    if (b instanceof Square) {
      return x == ((Square) b).getX() && y == ((Square) b).getY();
    }
    else {
      return false;
    }
  }

  /**
   * Determines the hash code of this square.
   * @return the integer of the hash code of this square.
   */
  @Override
  public int hashCode() {
    return Objects.hash();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getX() {
    return this.x;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getY() {
    return this.y;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getQ() {
    throw new IllegalArgumentException("Not a hex");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getR() {
    throw new IllegalArgumentException("Not a hex");
  }

  @Override
  public int getS() {
    throw new IllegalArgumentException("Not a hex");
  }

}
