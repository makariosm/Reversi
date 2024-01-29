package cs3500.reversi.provider.strategy.infallible;

import cs3500.reversi.provider.model.ROReversiModel;
import cs3500.reversi.provider.strategy.Move;
import cs3500.reversi.provider.strategy.fallible.CornerStrat;
import cs3500.reversi.provider.strategy.fallible.FallibleReversiStrategy;

/**
 * Reversi strategy that focuses on optimizing the corner strategy. For more information on this
 * strategy, see {@link CornerStrat}. If this strategy fails, the strategy will
 * focus on maximizing the score.
 *
 * @see CornerStrat
 * @see CaptureMostPieces
 */
public final class OptimizeCornerStratMaxScore implements InfallibleReversiStrategy {
  private final FallibleReversiStrategy optimizeCornerStrategy;
  private final InfallibleReversiStrategy maxScoreStrategy;

  /**
   * Constructs OptimizeCornerStratMaxScore object that initializes the two strategies to stack
   * onto each other.
   */
  public OptimizeCornerStratMaxScore() {
    this.optimizeCornerStrategy = new CornerStrat();
    this.maxScoreStrategy = new CaptureMostPieces();
  }

  @Override
  public Move chooseMove(ROReversiModel model)
          throws IllegalArgumentException, IllegalStateException {
    return this.optimizeCornerStrategy.chooseMove(model).orElseGet(() ->
            this.maxScoreStrategy.chooseMove(model));
  }
}
