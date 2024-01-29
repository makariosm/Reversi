package cs3500.reversi.model;

import java.util.Map;

/**
 * Interface for all types of grid in a reversi game.
 */
public interface Grid {

  /**
   * Gets grid of type map from grid class.
   * @return Map of grid
   */
  Map<ReversiCell, DiscStatus> getGrid();

  /**
   * Makes and populates initial game grid.
   */
  public void makeGrid();

  /**
   * Populates first ring around center cell, alternating player colors.
   */
  public void starterGrid();

  /**
   * Gets disc status of hex at given coordinates.
   * @param cell The reversi cell with its coordinates.
   * @return disc status of chosen cell.
   * @throws IllegalArgumentException if an invalid hex coordinate (not on grid)
   */
  public DiscStatus getStatus(ReversiCell cell);

}
