package cs3500.reversi.strategies;

import java.util.ArrayList;
import java.util.List;
import cs3500.reversi.model.DiscStatus;
import cs3500.reversi.model.Hex;
import cs3500.reversi.model.ReadOnlyReversi;
import cs3500.reversi.model.ReversiCell;
import cs3500.reversi.model.Square;

/**
 * The implementation of a hex strategy that tries to choose a hex that stays away from making
 * moves next to corners which makes it harder for the other player.
 */
public class AvoidNextToCornerStrategy implements HexStrategy {

  /**
   * {@inheritDoc}
   */
  @Override
  public ReversiCell chooseHex(ReadOnlyReversi model, DiscStatus playerType)
          throws IllegalAccessException {

    List<ReversiCell> cellOptions = this.optionsAdd(model);
    if (cellOptions.isEmpty()) {
      throw new IllegalAccessException("No available cells with avoid corners strategy.");
    }
    return this.getUpperLeftMostHexInList(cellOptions);
  }

  private List<ReversiCell> optionsAdd(ReadOnlyReversi model) {
    List<ReversiCell> cellOptions = new ArrayList<>();
    try {
      int coord = (int) Math.floor(model.getGridSize() / 2);
      for (int q = -coord; q <= coord; q++) {
        for (int r = -coord; r <= coord; r++) {
          for (int s = -coord; s <= coord; s++) {
            if (q + r + s == 0 && !this.hexIsNextToCorner(model, q, r, s)) {
              if (model.isMoveLegalAt(new Hex(q, r, s))) {
                cellOptions.add(new Hex(q, r, s));
              }
            }
          }
        }
      }
    }
    catch (IllegalArgumentException e) {
      for (int x = 0; x < model.getGridSize(); x++) {
        for (int y = 0; y < model.getGridSize(); y++) {
          if (!this.squareIsNextToCorner(model, x, y)) {
            if (model.isMoveLegalAt(new Square(x, y))) {
              cellOptions.add(new Square(x, y));
            }
          }
        }
      }
    }
    return cellOptions;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isHuman() {
    return false;
  }

  private ReversiCell getUpperLeftMostHexInList(List<ReversiCell> cellOptions) {
    if (cellOptions.isEmpty()) {
      return null;
    }
    if (cellOptions.size() == 1) {
      return cellOptions.get(0);
    } else {
      ReversiCell resultHex = cellOptions.get(0);
      for (int i = 0; i < cellOptions.size() - 2; i++) {
        ReversiCell first = cellOptions.get(i);
        ReversiCell second = cellOptions.get(i + 1);

        try {
          if ((first.getQ() < second.getQ() && first.getR() <= second.getR())
                  || (first.getQ() == second.getQ() && first.getR() < second.getR())) {
            resultHex = first;
          } else {
            resultHex = second;
          }
        } catch (IllegalArgumentException e) {
          if ((first.getX() < second.getX() && first.getY() <= second.getY())
                  || (first.getX() == second.getX() && first.getY() < second.getY())) {
            resultHex = first;
          } else {
            resultHex = second;
          }
        }
      }
      return resultHex;
    }
  }

  private boolean hexIsNextToCorner(ReadOnlyReversi model, int q, int r, int s) {
    return !hexIsCorner(model, q, r, s)
            && ((Math.abs(s) <= 1 && Math.abs(q) >= Math.floor(model.getGridSize() / 2) - 1
            && Math.abs(r) >= Math.floor(model.getGridSize() / 2) - 1)
            || (Math.abs(q) <= 1 && Math.abs(s) >= Math.floor(model.getGridSize() / 2) - 1
            && Math.abs(r) >= Math.floor(model.getGridSize() / 2) - 1));
  }

  private boolean hexIsCorner(ReadOnlyReversi model, int q, int r, int s) {
    return (s == 0 && Math.abs(q) == Math.floor(model.getGridSize() / 2)
            && Math.abs(r) == Math.floor(model.getGridSize() / 2))
            || (q == 0 && Math.abs(s) == Math.floor(model.getGridSize() / 2)
            && Math.abs(r) == Math.floor(model.getGridSize() / 2));
  }

  private boolean squareIsNextToCorner(ReadOnlyReversi model, int x, int y) {
    return !squareIsCorner(model, x, y)
            && ((x <= 1 && y <= 1) || (x >= model.getGridSize() - 2 && y >= model.getGridSize() - 2)
            || (x <= 1 && y >= model.getGridSize() - 2)
            || (x >= model.getGridSize() - 2 && y <= 1));
  }

  private boolean squareIsCorner(ReadOnlyReversi model, int x, int y) {
    return (x == 0 && y == 0) || (x == model.getGridSize() - 1 && y == model.getGridSize() - 1)
            || (x == 0 && y == model.getGridSize() - 1) || (x == model.getGridSize() - 1 && y == 0);
  }





}
