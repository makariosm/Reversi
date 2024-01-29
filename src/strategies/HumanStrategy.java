package cs3500.reversi.strategies;

import cs3500.reversi.model.DiscStatus;
import cs3500.reversi.model.ReadOnlyReversi;
import cs3500.reversi.model.ReversiCell;
import cs3500.reversi.view.ReversiView;

/**
 * The implementation of a hex strategy that waits for a player to manually choose a hex to place.
 * their disc in.
 */
public class HumanStrategy implements HexStrategy {

  private final ReversiView ourView;

  public HumanStrategy(ReversiView ourView) {
    this.ourView = ourView;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ReversiCell chooseHex(ReadOnlyReversi model, DiscStatus playerType) {
    return ourView.getCurrCellSelection();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isHuman() {
    return true;
  }

}
