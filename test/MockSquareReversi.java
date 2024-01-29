import java.util.List;
import java.util.Objects;

import cs3500.reversi.model.ReversiCell;
import cs3500.reversi.model.SquareReversi;

/**
 * Represents the mock of a square reversi view game (model).
 */
public class MockSquareReversi extends SquareReversi {

  StringBuilder log;

  /** Constructs mock model.
   * @param log representing log of spreadsheet
   */
  public MockSquareReversi(StringBuilder log) {
    super();
    this.log = Objects.requireNonNull(log);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getGridSize() {
    return super.getGridSize();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isMoveLegalAt(ReversiCell c) {
    log.append(String.format("Checking if move is legal at x = %d, y = %d", c.getX(), c.getY()));
    return super.isMoveLegalAt(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ReversiCell> getPossibleMoves() {
    return super.getPossibleMoves();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int numOppositeHexes(ReversiCell adjHex, int qDir, int rDir, int sDir) {
    int score = super.numOppositeHexes(adjHex, qDir, rDir, sDir);
    return score;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ReversiCell> getAdjacentOpposite(ReversiCell c) {
    return super.getAdjacentOpposite(c);
  }

}
