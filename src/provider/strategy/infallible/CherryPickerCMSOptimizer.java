package cs3500.reversi.provider.strategy.infallible;

import cs3500.reversi.provider.model.ROReversiModel;
import cs3500.reversi.provider.strategy.Move;
import cs3500.reversi.provider.strategy.fallible.CherryPicker;
import cs3500.reversi.provider.strategy.fallible.FallibleReversiStrategy;

/**
 * Reversi strategy that focuses on cherry-picking easy wins. If this strategy fails, the strategy
 * will focus on the CMS (Corner Max Score) optimizer strategy.
 *
 * @see CherryPicker
 * @see OptimizeCornerStratMaxScore
 */
public final class CherryPickerCMSOptimizer implements InfallibleReversiStrategy {
  private final FallibleReversiStrategy cherryPicker;
  private final InfallibleReversiStrategy cmsOptimizer;

  /**
   * Constructs CherryPickerCMSOptimizer object that initializes the two strategies to stack onto
   * each other.
   */
  public CherryPickerCMSOptimizer() {
    this.cherryPicker = new CherryPicker();
    this.cmsOptimizer = new OptimizeCornerStratMaxScore();
  }

  @Override
  public Move chooseMove(ROReversiModel model)
          throws IllegalArgumentException, IllegalStateException {
    return this.cherryPicker.chooseMove(model).orElseGet(() ->
            this.cmsOptimizer.chooseMove(model));
  }
}
