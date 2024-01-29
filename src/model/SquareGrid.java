package cs3500.reversi.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The grid for a square reversi game, has x and y coordinates instead of q, r, s.
 * and 0,0 is the top left corner.
 */
public class SquareGrid implements Grid {

  public Map<ReversiCell, DiscStatus> grid;
  private final int gridSize;

  /**
   * Constructs the Square Grid representation of a reversi model.
   * @param gridSize The size of the hex grid (number of hexes on the axes of the grid)
   */
  public SquareGrid(int gridSize) {
    // invariant: grid size must be even and positive
    if (gridSize % 2 == 0 && gridSize > 2) {
      this.gridSize = gridSize;
      grid = new HashMap<>();
    }
    else {
      throw new IllegalArgumentException("Grid has to be positive and even.");
    }
  }

  /**
   * Makes a hex grid with the given map of hexes to colors and size.
   * @param grid the map of hexes and their disc status
   * @param gridSize the size of the grid
   */
  public SquareGrid(Map<ReversiCell, DiscStatus> grid, int gridSize) {
    // invariant: grid size must be odd and greater than or equal to 3 hexes in order to be a valid
    //grid. If the grid is even it cannot have a center hex, which is needed in the game. If it is
    //less than 3 then the initial ring of alternating hexes meant to be able to start the game
    //cannot be created because then the grid you are asking for is too small and you would not be
    //able to create a ring around the center.
    //also gridsize cannot change which is enforced by making it final, because grid should not
    //change sizes mid-game
    if (gridSize % 2 == 1 && gridSize >= 3) {
      this.gridSize = gridSize;
      this.grid = grid;
    }
    else {
      throw new IllegalArgumentException("Grid has to be at least 3 hexes wide and odd.");
    }
  }

  /**
   * Gets grid of type map from grid class.
   *
   * @return Map of grid
   */
  @Override
  public Map<ReversiCell, DiscStatus> getGrid() {
    return this.grid;
  }

  /**
   * Makes and populates initial game grid.
   */
  @Override
  public void makeGrid() {
    for (int x = 0; x < gridSize; x++) {
      for (int y = 0; y < gridSize; y++) {
        grid.put(new Square(x, y), DiscStatus.Empty);
      }
    }
  }

  /**
   * Populates first ring around center cell, alternating player colors.
   */
  @Override
  public void starterGrid() {
    int middle = (gridSize / 2) - 1;

    grid.replace(new Square(middle, middle), DiscStatus.Empty, DiscStatus.Black);
    grid.replace(new Square(middle + 1, middle + 1), DiscStatus.Empty, DiscStatus.Black);
    grid.replace(new Square(middle + 1, middle), DiscStatus.Empty, DiscStatus.White);
    grid.replace(new Square(middle, middle + 1), DiscStatus.Empty, DiscStatus.White);
  }

  /**
   * Gets disc status of cell with given coordinates.
   *
   * @return disc status of chosen cell.
   * @throws IllegalArgumentException if q,r,s is an invalid hex coordinate (not on grid)
   */
  @Override
  public DiscStatus getStatus(ReversiCell cell) {
    int x = cell.getX();
    int y = cell.getY();

    if (!grid.containsKey(new Square(x, y))) {
      throw new IllegalArgumentException("Square with given coordinates" +
              " does not exist on this grid.");
    } else {
      return grid.get(new Square(x, y));
    }

  }

}
