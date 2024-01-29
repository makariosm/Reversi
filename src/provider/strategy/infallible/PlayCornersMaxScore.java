package cs3500.reversi.provider.strategy.infallible;

import cs3500.reversi.provider.model.ROReversiModel;
import cs3500.reversi.provider.strategy.Move;
import cs3500.reversi.provider.strategy.fallible.FallibleReversiStrategy;
import cs3500.reversi.provider.strategy.fallible.PlayAtCorners;

/**
 * Reversi strategy that focuses on playing at the corners of the game board. If this strategy
 * fails, the strategy will focus on maximizing the score.
 *
 * @see PlayAtCorners
 * @see CaptureMostPieces
 */
public final class PlayCornersMaxScore implements InfallibleReversiStrategy {
  private final FallibleReversiStrategy cornerStrategy;
  private final InfallibleReversiStrategy maxScoreStrategy;

  /**
   * Constructs PlayCornersMaxScore object that initializes the two strategies to stack onto
   * each other.
   */
  public PlayCornersMaxScore() {
    this.cornerStrategy = new PlayAtCorners();
    this.maxScoreStrategy = new CaptureMostPieces();
  }

  @Override
  public Move chooseMove(ROReversiModel model)
          throws IllegalArgumentException, IllegalStateException {
    return this.cornerStrategy.chooseMove(model).orElseGet(() ->
            this.maxScoreStrategy.chooseMove(model));
  }
}
