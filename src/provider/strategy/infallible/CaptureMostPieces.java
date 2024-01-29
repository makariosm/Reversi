package cs3500.reversi.provider.strategy.infallible;

import cs3500.reversi.provider.model.Disc;
import cs3500.reversi.provider.model.Hexagon;
import cs3500.reversi.provider.model.ROReversiModel;
import cs3500.reversi.provider.model.MutableReversiModel;
import cs3500.reversi.provider.strategy.Move;

/**
 * Reversi strategy that focuses on maximizing the score by capturing as many pieces/discs on this
 * turn as possible. If two hexagons return in a tie for the number of discs captured, then the
 * uppermost-leftmost hexagon is chosen with regard to the game board. Hence, the uppermost hexagon
 * is prioritized first. If some hexagons are equally "upmost", then the left-most hexagon is
 * chosen. If no playable hexagons can be found, the strategy will pass the move instead.
 */
public final class CaptureMostPieces implements InfallibleReversiStrategy {
  @Override
  public Move chooseMove(ROReversiModel model)
          throws IllegalArgumentException, IllegalStateException {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }

    final Disc turn = model.getTurn();
    final int score = model.getScore(turn);
    int piecesGained = 0;
    Hexagon chosenHexagon = null;

    for (Hexagon hexagon : model.getBoard().keySet()) {
      MutableReversiModel branchedModel = model.copyGame();
      try {
        branchedModel.placeDisc(hexagon);
      }
      catch (IllegalStateException ignore) {
        continue;
      }

      int newPiecesGained = branchedModel.getScore(turn) - score;

      if (chosenHexagon == null || newPiecesGained > piecesGained) {
        // no hexagon has been chosen yet or a better one has been found
        chosenHexagon = hexagon;
        piecesGained = newPiecesGained;
      } else if (newPiecesGained == piecesGained) {
        // if two hexagons tie for the number of pieces captured
        if (hexagon.getY() > chosenHexagon.getY()) {
          chosenHexagon = hexagon;
        } else if (hexagon.getY() == chosenHexagon.getY()) {
          if (hexagon.getX() < chosenHexagon.getX()) {
            chosenHexagon = hexagon;
          }
        }
      }
    }

    return chosenHexagon == null ? new Move(true) : new Move(chosenHexagon);
  }
}
