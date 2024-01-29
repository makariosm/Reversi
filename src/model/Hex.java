package cs3500.reversi.model;

import java.util.Objects;

/**
 * Represents a Hex, a cell of hexagonal shape that has a coordinate with 3 axes .
 * (to be put in the reversi grid) and methods to determine equality and get its properties.
 */
public class Hex implements ReversiCell {
  // value on q axis
  private final int q;
  // value on r axis
  private final int r;
  // value on s axis
  private final int s;

  /**
   * Constructs a Hex, a cell of hexagonal shape with its coordinates in the reversi grid.
   * @param q The q coordinate of the hex cell
   * @param r The r coordinate of the hex cell
   * @param s The s coordinate of the hex cell
   */
  public Hex(int q, int r, int s) {
    this.q = q;
    this.r = r;
    this.s = s;
    // INVARIANT: ensures that q, r, s add to 0
    //The sum of a hex coordinate has to be equal to 0, for it to be a valid hex on a hex grid,
    //otherwise we could potentially make hexes that should not be on the grid, that may affect the
    //game state
    //q,r,s should also not be able to change because once a hex is created its defining
    // characteristics are its coordinates, so we enforced that they cannot be changed by making
    // them final.
    if (q + r + s != 0) {
      throw new IllegalArgumentException("q + r + s must be 0");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object b) {
    if (b instanceof Hex) {
      return q == ((Hex) b).q && r == ((Hex) b).r && s == ((Hex) b).s;
    }
    else {
      return false;
    }
  }

  /**
   * Determines the hash code of this hex.
   * @return the integer of the hash code of this hex.
   */
  @Override
  public int hashCode() {
    return Objects.hash();
  }

  /**
   * Determines q coordinate of this hex.
   * @return integer of hex's q coordinate.
   */
  @Override
  public int getQ() {
    return this.q;
  }

  /**
   * Determines r coordinate of this hex.
   * @return integer of hex's r coordinate.
   */
  @Override
  public int getR() {
    return this.r;
  }

  /**
   * Determines s coordinate of this hex.
   * @return integer of hex's s coordinate.
   */
  @Override
  public int getS() {
    return this.s;
  }

  @Override
  public int getX() {
    throw new IllegalArgumentException("Not a square");
  }

  @Override
  public int getY() {
    throw new IllegalArgumentException("Not a square");
  }
}