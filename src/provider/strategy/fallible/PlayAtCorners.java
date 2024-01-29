package cs3500.reversi.provider.strategy.fallible;

import java.util.List;
import java.util.Optional;

import cs3500.reversi.provider.model.Disc;
import cs3500.reversi.provider.model.Hexagon;
import cs3500.reversi.provider.model.ROReversiModel;
import cs3500.reversi.provider.model.MutableReversiModel;
import cs3500.reversi.provider.strategy.Move;

/**
 * Reversi strategy that focuses on playing at the corners of the game board since discs in corners
 * cannot be captured because they don’t have hexagons on their other side.
 */
public final class PlayAtCorners implements FallibleReversiStrategy {

  @Override
  public Optional<Move> chooseMove(ROReversiModel model) throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }

    final Disc turn = model.getTurn();
    final int score = model.getScore(turn);
    int piecesGained = 0;
    Hexagon chosenHexagon = null;
    int boardSideLen = model.getGameBoardSideLength();

    // the order of corners is as follows:
    // top-left corner, top right corner,
    // leftmost corner, rightmost corner,
    // bottom-left corner, bottom-right corner.
    final List<Hexagon> corners = List.of(new Hexagon(-(boardSideLen - 1), boardSideLen - 1),
            new Hexagon(0, boardSideLen - 1), new Hexagon(-(boardSideLen - 1), 0),
            new Hexagon(boardSideLen - 1, 0), new Hexagon(0, -(boardSideLen - 1)),
            new Hexagon(boardSideLen - 1, -(boardSideLen - 1)));

    for (Hexagon hexagon : corners) {
      MutableReversiModel branchedModel = model.copyGame();
      try {
        branchedModel.placeDisc(hexagon);
      } catch (IllegalStateException ignore) {
        continue;
      }

      int newPiecesGained = branchedModel.getScore(turn) - score;
      if (chosenHexagon == null || newPiecesGained > piecesGained) {
        chosenHexagon = hexagon;
        piecesGained = newPiecesGained;
      }
    }

    return chosenHexagon == null ? Optional.empty() : Optional.of(new Move(chosenHexagon));
  }
}
