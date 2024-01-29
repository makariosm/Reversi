package cs3500.reversi.strategies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs3500.reversi.model.DiscStatus;
import cs3500.reversi.model.ReadOnlyReversi;
import cs3500.reversi.model.ReversiCell;

/**
 * The implementation of a hex strategy that tries to choose a hex that captures the most hexes.
 * possible in that move.
 */
public class CaptureMostStrategy implements HexStrategy {

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

        int maxScoreQ;
        int maxScoreR;
        try {
          maxScoreQ = movesAndScores.get(maxScore).getQ();
          maxScoreR = movesAndScores.get(maxScore).getR();
          if ((h.getQ() < maxScoreQ && h.getR() <= maxScoreR)
                  || (h.getQ() == maxScoreQ && h.getR() < maxScoreR)) {
            movesAndScores.replace(maxScore, h);
          }

        } catch (IllegalArgumentException e) {
          maxScoreQ = movesAndScores.get(maxScore).getX();
          maxScoreR = movesAndScores.get(maxScore).getY();

          if ((h.getX() < maxScoreQ && h.getY() <= maxScoreR)
                  || (h.getX() == maxScoreQ && h.getY() < maxScoreR)) {
            movesAndScores.replace(maxScore, h);
          }
        }
      } else {
        movesAndScores.put(currHexScore, h);
        maxScore = Math.max(maxScore, currHexScore);
      }
    }
    if (movesAndScores.isEmpty()) {
      throw new IllegalAccessException("No available cells with capture most strategy.");
    }
    return movesAndScores.get(maxScore);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isHuman() {
    return false;
  }

}
