import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cs3500.reversi.model.DiscStatus;
import cs3500.reversi.model.ReadOnlyReversi;
import cs3500.reversi.model.ReversiCell;
import cs3500.reversi.strategies.CaptureMostStrategy;

/**
 * Mock class for the capture most strategy (basic strategy) of a reversi game that mocks to see .
 * how the strategy chooses a cell.
 */
public class MockCaptureMostStrategy extends CaptureMostStrategy {

  StringBuilder log;

  public MockCaptureMostStrategy(StringBuilder log) {
    super();
    this.log = Objects.requireNonNull(log);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ReversiCell chooseHex(ReadOnlyReversi model, DiscStatus playerType)
          throws IllegalAccessException {
    Map<Integer, ReversiCell> movesAndScores = new HashMap<>();
    int maxScore = 0;

    // every hex
    // 6 (at most) adjacent opposite
    log.append("Finding all possible moves on grid.\n");
    for (ReversiCell h : model.getPossibleMoves()) {

      List<ReversiCell> loAdjacentOpposite = model.getAdjacentOpposite(h);

      int currHexScore = 0;
      // FOR ONE HEX
      for (int i = 0; i < loAdjacentOpposite.size() - 1; i++) {
        // for every existing adjacent opponent hex
        if (loAdjacentOpposite.get(i) != null) {
          ReversiCell hex = loAdjacentOpposite.get(i);
          if (i == 0) {
            currHexScore += model.numOppositeHexes(hex, 1, -1, 0) + 1;
          } else if (i == 1) {
            currHexScore += model.numOppositeHexes(hex, 1, 0, -1) + 1;
          } else if (i == 2) {
            currHexScore += model.numOppositeHexes(hex, 0, 1, -1) + 1;
          } else if (i == 3) {
            currHexScore += model.numOppositeHexes(hex, -1, 1, 0) + 1;
          } else if (i == 4) {
            currHexScore += model.numOppositeHexes(hex, -1, 0, 1) + 1;
          } else if (i == 5) {
            currHexScore += model.numOppositeHexes(hex, 0, -1, 1) + 1;
          }
        }
      }

      if (maxScore == currHexScore) {

        movesAndScores.put(currHexScore, h);

        int maxScoreQ = movesAndScores.get(maxScore).getQ();
        int maxScoreR = movesAndScores.get(maxScore).getR();

        if ((h.getQ() < maxScoreQ && h.getR() <= maxScoreR)
                || (h.getQ() == maxScoreQ && h.getR() < maxScoreR)) {
          movesAndScores.replace(maxScore, h);
        }
      } else {
        movesAndScores.put(currHexScore, h);
        maxScore = Math.max(maxScore, currHexScore);
      }

    }
    if (movesAndScores.isEmpty()) {
      throw new IllegalAccessException("No available cells with capture most strategy.");
    }
    ReversiCell maxHex = movesAndScores.get(maxScore);
    try {
      log.append(String.format("Choosing hex at q = %d, r = %d, s = %d ", maxHex.getQ(),
              maxHex.getR(), maxHex.getS())).append(
              "with return score of ").append(maxScore).append("\n");
    }
    catch (IllegalArgumentException e) {
      log.append(String.format("Choosing square at x = %d, y = %d ", maxHex.getX(),
              maxHex.getY())).append(
              "with return score of ").append(maxScore).append("\n");
    }

    return movesAndScores.get(maxScore);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isHuman() {
    return super.isHuman();
  }

}
