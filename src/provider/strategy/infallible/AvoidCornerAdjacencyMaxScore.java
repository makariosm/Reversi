package cs3500.reversi.provider.strategy.infallible;

import cs3500.reversi.provider.model.ROReversiModel;
import cs3500.reversi.provider.strategy.Move;
import cs3500.reversi.provider.strategy.fallible.AvoidHexagonsBorderingCorners;
import cs3500.reversi.provider.strategy.fallible.FallibleReversiStrategy;

/**
 * Reversi strategy that focuses on playing at hexagons that aren't next to corners that maximize
 * the score. If this strategy fails, the strategy will simply select whichever hexagon maximizes
 * the score regardless of where it is.
 *
 * @see AvoidHexagonsBorderingCorners
 * @see CaptureMostPieces
 */
public final class AvoidCornerAdjacencyMaxScore implements InfallibleReversiStrategy {
  private final FallibleReversiStrategy avoidAdjToCorners;
  private final InfallibleReversiStrategy maxScoreStrategy;

  /**
   * Constructs AvoidCornerAdjacencyMaxScore object that initializes the two strategies to stack
   * onto each other.
   */
  public AvoidCornerAdjacencyMaxScore() {
    this.avoidAdjToCorners = new AvoidHexagonsBorderingCorners();
    this.maxScoreStrategy = new CaptureMostPieces();
  }

  @Override
  public Move chooseMove(ROReversiModel model)
          throws IllegalArgumentException, IllegalStateException {
    return this.avoidAdjToCorners.chooseMove(model).orElseGet(() ->
            this.maxScoreStrategy.chooseMove(model));
  }
}
