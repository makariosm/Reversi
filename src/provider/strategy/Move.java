package cs3500.reversi.provider.strategy;

import java.util.Objects;
import java.util.Optional;

import cs3500.reversi.provider.model.Hexagon;


/**
 * Represents a Reversi move to be made. Every move consists of a hexagon on which to make a move
 * on and a boolean representing whether to pass the current turn. Because this class should
 * logically represent only one single move, if a hexagon selected to make a move on, the current
 * turn should not be set to pass as well. Conversely, if the current turn is set to pass, there
 * should be no hexagon selected at all.
 */
public final class Move {
  private final Optional<Hexagon> hexagon;
  private final boolean pass;

  /**
   * Constructs a Move object that consists of a hexagon on which to make a move on.
   *
   * @param hexagon the hexagon on which to make a move on
   */
  public Move(Hexagon hexagon) {
    this.hexagon = Optional.of(hexagon);
    this.pass = false;
  }

  /**
   * Constructs a Move object that consists of a boolean representing whether to pass the
   * current turn.
   *
   * @param pass should the current turn be passed or not
   */
  public Move(boolean pass) {
    this.hexagon = Optional.empty();
    this.pass = pass;
  }

  /**
   * Gets the hexagon on which to make a move on.
   *
   * @return the hexagon on which to make a move on
   */
  public Optional<Hexagon> getHexagon() {
    return this.hexagon;
  }

  /**
   * Gets whether current turn is to be passed or not.
   *
   * @return <code>true</code> if the current turn is to be passed and <code>false</code> otherwise
   */
  public boolean getPass() {
    return this.pass;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Move)) {
      return false;
    }

    Move otherMove = (Move) o;
    return this.hexagon.equals(otherMove.hexagon) && this.pass == otherMove.pass;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.hexagon, this.pass);
  }
}
