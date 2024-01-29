package cs3500.reversi.model;

import java.util.List;
import java.util.Map;

/**
 * Represents the model for a game of Reversi with only observation methods, the type of model that.
 * cannot be mutated.
 */
public interface ReadOnlyReversi {

  /**
   * Gets the model's grid size.
   *
   * @return the number of hexes on each of the axes.
   * @throws IllegalStateException if game not started yet
   */
  int getGridSize();

  /**
   * Gets the disc status (black, white, empty) of a hex of coordinates q, r, s from the grid.
   *
   * @param cell The desired cell.
   * @return the disc status of the clicked cell (the player who holds this cell)
   * @throws IllegalStateException    if game not started yet
   * @throws IllegalArgumentException if q,r,s is an invalid hex coordinate (not on grid)
   */
  DiscStatus getStatus(ReversiCell cell);

  /**
   * Determines all the possible hexes on the grid where a move can be made with the given player.
   *
   * @return list of hexes that given player can make a move on
   */
  List<ReversiCell> getPossibleMoves();

  /**
   * Determines number of opponent hexes in between clicked hex and surrounding player hex
   * (0 if no surrounding player cell exists and move is not valid).
   *
   * @param adjHex Opponent hex adjacent to clicked hex.
   * @param qDir   The number change in axis q when traveling along path of adjacent cell.
   * @param rDir   The number change in axis r when traveling along path of adjacent cell.
   * @param sDir   The number change in axis s when traveling along path of adjacent cell.
   * @return The number of opponent hexes plus clicked cell needed to flip
   */
  int numOppositeHexes(ReversiCell adjHex, int qDir, int rDir, int sDir);

  /**
   * Gets all adjacent opponent hexes to hex of given coordinates.
   *
   * @param cell The desired cell
   * @return list of hexes of adjacent cells.
   */
  List<ReversiCell> getAdjacentOpposite(ReversiCell cell);


  boolean isMoveLegalAt(ReversiCell cell);

  /**
   * Determines the black player's score.
   *
   * @return Number of cells on grid belonging to black player.
   * @throws IllegalStateException if game not started yet
   */
  int getBlackScore();

  /**
   * Determines the white player's score.
   *
   * @return Number of cells on grid belonging to white player.
   * @throws IllegalStateException if game not started yet
   */
  int getWhiteScore();

  /**
   * Scans the entire grid and checks to see if there are any possible moves for a player.
   *
   * @return true if there is at least one move for a player to make, false otherwise.
   * @throws IllegalStateException if game not started yet
   */
  boolean anyPossibleMoves();

  /**
   * Determines if game is over based on if two consecutive passes were made.
   *
   * @return true if game is over.
   * @throws IllegalStateException if game not started yet
   */
  boolean isGameOver();

  /**
   * Determines if it is black player's turn.
   * @return true if black's turn, else if not
   */
  boolean isBlackTurn();

  /**
   * Get the winner of the game, if it is a tie return both players.
   * @return the list of winners
   */
  List<DiscStatus> getWinners();

  int getConsecutivePasses();

  /**
   * Returns copy of current reversi model grid.
   * @return copy of grid
   */
  Map<ReversiCell, DiscStatus> getCopyOfGrid();
}
