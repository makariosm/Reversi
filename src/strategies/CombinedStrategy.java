package cs3500.reversi.strategies;

import cs3500.reversi.model.DiscStatus;
import cs3500.reversi.model.ReadOnlyReversi;
import cs3500.reversi.model.ReversiCell;

/**
 * The implementation of a hex strategy that tries to choose a hex that captures a corner if.
 * possible because corner cannot be recaptured.
 */
public class CombinedStrategy implements HexStrategy {

  private final HexStrategy cornersStrategy = new CaptureCornersStrategy();

  private final HexStrategy avoidNextToCornerStrategy = new AvoidNextToCornerStrategy();

  private final HexStrategy captureMostStrategy = new CaptureMostStrategy();

  /**
   * {@inheritDoc}
   */
  @Override
  public ReversiCell chooseHex(ReadOnlyReversi model, DiscStatus playerType)
          throws IllegalAccessException {

    try {
      return cornersStrategy.chooseHex(model, playerType);
    } catch (IllegalAccessException e) {
      try {
        return avoidNextToCornerStrategy.chooseHex(model, playerType);
      } catch (IllegalAccessException r) {
        return captureMostStrategy.chooseHex(model, playerType);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isHuman() {
    return false;
  }
}
