package cs3500.reversi.view;

import java.util.Objects;

import cs3500.reversi.model.Hex;
import cs3500.reversi.model.ReversiModel;

/**
 * Represents the class that creates the view of the basic reversi model's grid.
 */
public class ReversiTextualView implements TextualView {
  private ReversiModel model;

  /**
   * Constructs the textual view for the model's grid.
   * @param model The reversi model of the reversi game being played.
   */
  public ReversiTextualView(ReversiModel model) {
    if (Objects.nonNull(model)) {
      this.model = model;
    }
    else {
      throw new IllegalArgumentException("Model cannot be null.");
    }
  }

  /**
   * Builds textual view of reversi model's grid.
   * @return String of representation of the grid.
   */
  public String toString() {
    StringBuilder view = new StringBuilder();
    for (int count = 0; count < model.getGridSize(); count++) {
      view.append(printRow(count)).append("\n");
    }
    return view.toString();
  }

  /**
   * Based on status of hex (player) at given coordinates,
   * determines string representation of the hex (X or O).
   * @param q The q coordinate of the chosen hex.
   * @param r The r coordinate of the chosen hex.
   * @param s The s coordinate of the chosen hex.
   * @return String (X or O) of player representation.
   */
  private String statusString(int q, int r, int s) {

    switch (model.getStatus(new Hex(q, r, s))) {

      case White:
        return "O ";

      case Black:
        return "X ";

      default:
        return "_ ";

    }
  }

  /**
   * Prints the necessary spaces for a given row based on grid size
   * to offset and align view of grid correctly.
   * @param row The given row being worked on.
   * @return string of spaces for the given row to offset and align view of grid correctly.
   */
  private String printSpaces(int row) {
    if (row <= Math.ceil(model.getGridSize() / 2)) {
      StringBuilder spaces = new StringBuilder();
      for (int i = 0; i < Math.floor(model.getGridSize() / 2) - row; i++) {
        spaces.append(" ");
      }
      return spaces.toString();
    }
    else {
      StringBuilder spaces = new StringBuilder();
      for (int i = 0; i < row - Math.floor(model.getGridSize() / 2); i++) {
        spaces.append(" ");
      }
      return spaces.toString();
    }
  }

  /**
   * Determines a given row's full view in the textual view.
   * @param row The index of the row being added to view.
   * @return string of row's representation in text.
   */
  private String printRow(int row) {
    int coord = (int) Math.floor(model.getGridSize() / 2);
    StringBuilder rowString = new StringBuilder(printSpaces(row));
    //    int coord = (int) Math.floor(gridSize / 2);

    int r = -coord + row;

    for (int q = -coord; q <= coord; q++) {
      for (int s = -coord; s <= coord; s++) {
        if (q + r + s == 0) {
          rowString.append(statusString(q, r, s));
        }
      }
    }
    return rowString.toString();
  }

}
