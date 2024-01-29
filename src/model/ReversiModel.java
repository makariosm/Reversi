package cs3500.reversi.model;

/**
 * Represents interface of model for a reversi game,
 * with methods to play game and get results from game while playing.
 */
public interface ReversiModel extends ReadOnlyReversi, ModelFeatures {

  /**
   * Starts the reversi game, populating the grid to initial state of the game.
   * with first ring from center circle populated to alternating player colors.
   *
   * @throws IllegalStateException if game already started
   */
  void setup();

  /**
   * Method for a players placing a disc at a certain cell.
   *
   * @param cell The cell of the clicked cell.
   * @throws IllegalStateException    if game not started yet
   * @throws IllegalArgumentException if cell is an invalid hex coordinate (not on grid)
   */
  void placeDisc(DiscStatus playerType, ReversiCell cell);

  /**
   * Determines if there are any hexes on the board that a made can be made on.
   *
   * @return true if there is a valid move, false otherwise
   */
  boolean anyPossibleMoves();

  /**
   * Passes player's turn to opponent and adds to consecutive pass count.
   *
   * @throws IllegalStateException if game not started yet
   */
  void pass(DiscStatus playerType);
}
