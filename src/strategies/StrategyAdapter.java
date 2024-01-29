package cs3500.reversi.strategies;

import java.util.NoSuchElementException;

import cs3500.reversi.model.DiscStatus;
import cs3500.reversi.model.Hex;
import cs3500.reversi.model.RORModelAdapter;
import cs3500.reversi.model.ReadOnlyReversi;
import cs3500.reversi.provider.model.Hexagon;
import cs3500.reversi.provider.model.ROReversiModel;
import cs3500.reversi.provider.strategy.Move;
import cs3500.reversi.provider.strategy.infallible.InfallibleReversiStrategy;

/**
 * Adapter class for strategies, allowing for use of both our implementation of strategies and.
 * provider's
 */
public class StrategyAdapter implements HexStrategy {
  private InfallibleReversiStrategy theirStrat;

  public StrategyAdapter(InfallibleReversiStrategy strategy) {
    this.theirStrat = strategy;
  }

  /**
   * Get the hex using the given strategy.
   *
   * @param model      the immutable model, that the strategy is reading
   * @param playerType the disc color that the strategy is trying to choose a hex for
   * @return the hex {@link Hex} after using the strategy
   * @throws IllegalAccessException if there is no move to be made using the given strategy
   */
  @Override
  public Hex chooseHex(ReadOnlyReversi model, DiscStatus playerType) throws IllegalAccessException {
    ROReversiModel theirModel = new RORModelAdapter(model);
    Move move = theirStrat.chooseMove(theirModel);
    try {
      return this.hexagonToHex(move.getHexagon().orElseThrow());
    } catch (NoSuchElementException e) {
      throw new IllegalAccessException("Cannot make move with current strategy");
    }
  }

  private Hex hexagonToHex(Hexagon hexagon) {
    int r = - hexagon.getY();
    int s = - hexagon.getX();
    int q = - r - s;
    System.out.println(q + "," + r + "," + s);
    return new Hex(q, r, s);
  }

  /**
   * Is this a human strategy?.
   *
   * @return true if it's a human strategy, false otherwise
   */
  @Override
  public boolean isHuman() {
    return false;
  }
}
