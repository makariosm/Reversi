package cs3500.reversi.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the hexagonal grid for a reversi game and its methods of building one.
 */
public class HexGrid implements Grid {

  public Map<ReversiCell, DiscStatus> grid;
  private final int gridSize;

  /**
   * Constructs the HexGrid representation of a reversi model.
   * @param gridSize The size of the hex grid (number of hexes on the axes of the grid)
   */
  public HexGrid(int gridSize) {
    // invariant: grid size must be odd and greater than or equal to 3 hexes in order to be a valid
    //grid. If the grid is even it cannot have a center hex, which is needed in the game. If it is
    //less than 3 then the initial ring of alternating hexes meant to be able to start the game
    //cannot be created because then the grid you are asking for is too small and you would not be
    //able to create a ring around the center.
    //also gridsize cannot change which is enforced by making it final, because grid should not
    //change sizes mid-game
    if (gridSize % 2 == 1 && gridSize >= 3) {
      this.gridSize = gridSize;
      grid = new HashMap<>();
    }
    else {
      throw new IllegalArgumentException("Grid has to be at least 3 hexes wide and odd.");
    }
  }

  /**
   * Makes a hex grid with the given map of hexes to colors and size.
   * @param grid the map of hexes and their disc status
   * @param gridSize the size of the grid
   */
  public HexGrid(Map<ReversiCell, DiscStatus> grid, int gridSize) {
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
   * Returns the hashmap grid representation of the Hex Grid of hexes as keys .
   * and disc statuses as values
   * @return hex grid's map representation of the game.
   */
  public Map<ReversiCell, DiscStatus> getGrid() {
    return this.grid;
  }

  /**
   * Makes and populates map grid with all coordinates with empty disc statuses.
   */
  public void makeGrid() {
    int coord = (int) Math.floor(gridSize / 2);
    for (int q = -coord; q <= coord; q++) {
      for (int r = -coord; r <= coord; r++) {
        for (int s = -coord; s <= coord; s++) {
          if (q + r + s == 0) {
            grid.put(new Hex(q,r,s), DiscStatus.Empty);
          }
        }
      }
    }
  }

  /**
   * Populates map grid, populating first ring around center cell with alternating player colors.
   */
  public void starterGrid() {
    grid.replace(new Hex(0, -1, 1), DiscStatus.Empty, DiscStatus.Black);
    grid.replace(new Hex(1, -1, 0), DiscStatus.Empty, DiscStatus.White);
    grid.replace(new Hex(1, 0, -1), DiscStatus.Empty, DiscStatus.Black);
    grid.replace(new Hex(0, 1, -1), DiscStatus.Empty, DiscStatus.White);
    grid.replace(new Hex(-1, 1, 0), DiscStatus.Empty, DiscStatus.Black);
    grid.replace(new Hex(-1, 0, 1), DiscStatus.Empty, DiscStatus.White);

  }

  /**
   * Gets disc status of hex at given coordinates.
   *
   * @param cell The reversi cell with its coordinates.
   * @return disc status of chosen cell.
   * @throws IllegalArgumentException if an invalid hex coordinate (not on grid)
   */
  @Override
  public DiscStatus getStatus(ReversiCell cell) {
    int q = cell.getQ();
    int r = cell.getR();
    int s = cell.getS();

    if (!grid.containsKey(new Hex(q, r, s))) {
      throw new IllegalArgumentException("Hex with given coordinates does not exist on this grid.");
    } else {
      return grid.get(new Hex(q, r, s));
    }
  }

}
