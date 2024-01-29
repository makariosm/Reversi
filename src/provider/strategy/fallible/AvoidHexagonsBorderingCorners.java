package cs3500.reversi.provider.strategy.fallible;

import java.util.List;
import java.util.Optional;

import cs3500.reversi.provider.model.Disc;
import cs3500.reversi.provider.model.Hexagon;
import cs3500.reversi.provider.model.ROReversiModel;
import cs3500.reversi.provider.model.MutableReversiModel;
import cs3500.reversi.provider.strategy.Move;

/**
 * Reversi strategy that maximizes the score by making moves only on hexagons that aren't next to
 * any corners hexagons because that would give the opponent the ability to get a corner on their
 * next turn.
 */
public final class AvoidHexagonsBorderingCorners implements FallibleReversiStrategy {
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
    final List<Hexagon> corners = List.of(new Hexagon(-(boardSideLen - 1), boardSideLen - 1),
            new Hexagon(0, boardSideLen - 1), new Hexagon(-(boardSideLen - 1), 0),
            new Hexagon(boardSideLen - 1, 0), new Hexagon(0, -(boardSideLen - 1)),
            new Hexagon(boardSideLen - 1, -(boardSideLen - 1)));

    for (Hexagon hexagon : model.getBoard().keySet()) {
      // skip hexagons that are next to any corners
      if (hexagonBordersCornerHexagons(hexagon, corners)) {
        continue;
      }

      MutableReversiModel branchedModel = model.copyGame();
      try {
        branchedModel.placeDisc(hexagon);
      } catch (IllegalStateException ignore) {
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

    return chosenHexagon == null ? Optional.empty() : Optional.of(new Move(chosenHexagon));
  }

  private boolean hexagonBordersCornerHexagons(Hexagon hexagon, List<Hexagon> corners) {
    int hexagonX = hexagon.getX();
    int hexagonY = hexagon.getY();

    for (Hexagon corner : corners) {
      if (corner.equals(new Hexagon(hexagonX + 1, hexagonY))
              || corner.equals(new Hexagon(hexagonX - 1, hexagonY))
              || corner.equals(new Hexagon(hexagonX, hexagonY + 1))
              || corner.equals(new Hexagon(hexagonX, hexagonY - 1))
              || corner.equals(new Hexagon(hexagonX - 1, hexagonY + 1))
              || corner.equals(new Hexagon(hexagonX + 1, hexagonY - 1))) {
        return true;
      }
    }

    return false;
  }
}
