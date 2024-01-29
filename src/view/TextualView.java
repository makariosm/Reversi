package cs3500.reversi.view;

/**
 * Represents interface of textual view, which visualizes the .
 * grid of a reversi model in string format
 */
public interface TextualView {

  /**
   * Builds textual view of reversi model's grid.
   * @return String of representation of the grid.
   */
  String toString();
}
