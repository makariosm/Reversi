package cs3500.reversi.view;

import java.util.Objects;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.Square;

/**
 * Textual View for of a square reversi game, displays grid in text.
 */
public class SquareReversiTextualView implements TextualView {

  ReversiModel model;

  /**
   * Constructs a textual view for of a square reversi game, displays grid in text.
   * @param model The reversi model the view uses
   */
  public SquareReversiTextualView(ReversiModel model) {
    if (Objects.nonNull(model)) {
      this.model = model;
    }
    else {
      throw new IllegalArgumentException("Model cannot be null.");
    }
  }

  @Override
  public String toString() {
    StringBuilder string = new StringBuilder();
    for (int column = 0; column < model.getGridSize(); column++) {
      string.append(printColumn(column)).append("\n");
    }
    return string.toString();
  }

  private String printColumn(int column) {
    StringBuilder string = new StringBuilder();
    for (int row = 0; row < model.getGridSize(); row++) {
      string.append(this.statusString(row, column));
    }
    return string.toString();
  }

  private String statusString(int x, int y) {

    switch (model.getStatus(new Square(x, y))) {

      case White:
        return "O ";

      case Black:
        return "X ";

      default:
        return "_ ";

    }
  }
}
