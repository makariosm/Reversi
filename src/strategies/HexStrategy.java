package cs3500.reversi.strategies;

import cs3500.reversi.model.DiscStatus;
import cs3500.reversi.model.Hex;
import cs3500.reversi.model.ReadOnlyReversi;
import cs3500.reversi.model.ReversiCell;

/**
 * Represents a strategy that a player of Reversi would use to choose the hex they want to place.
 * their disc in.
 */
public interface HexStrategy {

  /**
   * Get the hex using the given strategy.
   * @param model the immutable model, that the strategy is reading
   * @param playerType the disc color that the strategy is trying to choose a hex for
   * @return the hex {@link Hex} after using the strategy
   * @throws IllegalAccessException if there is no move to be made using the given strategy
   */
  ReversiCell chooseHex(ReadOnlyReversi model, DiscStatus playerType) throws IllegalAccessException;

  /**
   * Is this a human strategy?.
   * @return true if it's a human strategy, false otherwise
   */
  boolean isHuman();
}
