import java.util.List;
import java.util.Objects;

import cs3500.reversi.model.HexReversi;
import cs3500.reversi.model.ReversiCell;

/**
 * Mock class for a reversi game model, allowing mock of methods to observe how model makes moves.
 */
public class MockHexReversi extends HexReversi {

  StringBuilder log;

  /** Constructs mock model.
   * @param log representing log of spreadsheet
   */
  public MockHexReversi(StringBuilder log) {
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
