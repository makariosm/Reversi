//import java.util.List;
//import java.util.Objects;
//
//import cs3500.reversi.model.BasicReversi;
//import cs3500.reversi.model.Hex;
//
///**
// * Mock class for a reversi game model, allowing mock of methods to observe how model makes moves.
// */
//public class MockReversiModel extends BasicReversi {
//
//  StringBuilder log;
//
//  /** Constructs mock model.
//   * @param log representing log of spreadsheet
//   */
//  public MockReversiModel(StringBuilder log) {
//    super();
//    this.log = Objects.requireNonNull(log);
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public int getGridSize() {
//    return super.getGridSize();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public boolean isMoveLegalAt(int q, int r, int s) {
//    return super.isMoveLegalAt(q, r, s);
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public List<Hex> getPossibleMoves() {
//    return super.getPossibleMoves();
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public int numOppositeHexes(Hex adjHex, int qDir, int rDir, int sDir) {
//    int score = super.numOppositeHexes(adjHex, qDir, rDir, sDir);
//    return score;
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public List<Hex> getAdjacentOpposite(int q, int r, int s) {
//    return super.getAdjacentOpposite(q, r, s);
//  }
//
//}
