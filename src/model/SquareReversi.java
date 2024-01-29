package cs3500.reversi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model implementation for a square reversi game. Has x, y coordinates and .
 * extends abstract reversi functionality. Default grid is 8x8
 */
public class SquareReversi extends AbstractReversi implements ReversiModel {

  // grid that players interact with
  private final Grid grid;
  private final int gridSize;

  /**
   * Constructs default square reversi model of 8 squares by 8 squares.
   */
  public SquareReversi() {
    super();
    this.gridSize = 8;
    this.grid = new SquareGrid(this.gridSize);
  }

  /**
   * Constructs square reversi model based on given grid size.
   * @param gridSize The number of squares on the side of the grid
   */
  public SquareReversi(int gridSize) {
    super();
    this.gridSize = gridSize;
    this.grid = new SquareGrid(gridSize);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setup() {
    if (!this.isGameOver) {
      throw new IllegalStateException("Game already started.");
    }
    // populate
    grid.makeGrid();
    // make first ring around center alternating
    grid.starterGrid();
    // sets game over to false, game is now in progress
    this.isGameOver = false;
  }

  @Override
  public void placeDisc(DiscStatus playerType, ReversiCell cell) {
    super.gameNotStartedWarning();
    super.wrongPlayerWarning(playerType);

    int x = cell.getX();
    int y = cell.getY();

    if (!grid.getGrid().containsKey(new Square(x, y))) {
      throw new IllegalArgumentException("Hex with given coordinates does not exist on this grid.");
    }

    if (!grid.getGrid().get(new Square(x, y)).equals(DiscStatus.Empty)) {
      throw new IllegalStateException("Cannot place a dic on a non-empty cell.");
    }

    List<Boolean> loMovesMade = this.validatePlaceDiscMove(cell);

    // if all adjacent opposite hexes were null (clicked cell is
    // not adjacent to an opponent cell)
    if (loMovesMade.isEmpty()) {
      throw new IllegalStateException("No adjacent opposite hexes.");
    }

    // if for all adjacent opponent cells, no moves were allowable
    else if (!super.checkListContainsTrue(loMovesMade)) {
      throw new IllegalStateException("Adjacent opposite hexes present, but no moves allowable.");
    }

    // if move was allowable, switch turns and clear consecutive pass counter
    else {
      blackTurn = !blackTurn;
      this.consecutivePass = 0;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ReversiCell> getPossibleMoves() {
    super.gameNotStartedWarning();
    List<ReversiCell> loPossibleMoves = new ArrayList<>();

    for (int x = 0; x < this.gridSize; x++) {
      for (int y = 0; y < this.gridSize; y++) {
        if (this.isMoveLegalAt(new Square(x, y))) {
          loPossibleMoves.add(new Square(x, y));
        }
      }
    }
    return loPossibleMoves;
  }

  /**
   * Determines if a move is legal to the given cell is legal. Uses cube coordinates system
   *
   * @param cell the desired cell
   * @return true if the move is legal at the given coordinates, false otherwise
   */
  @Override
  public boolean isMoveLegalAt(ReversiCell cell) {

    int x = cell.getX();
    int y = cell.getY();

    super.gameNotStartedWarning();
    if (!grid.getGrid().containsKey(new Square(x, y))
            || !grid.getGrid().get(new Square(x, y)).equals(DiscStatus.Empty)) {
      return false;
    }
    // gets all the adjacent opponent hexes to clicked cell
    List<Boolean> loMovesMade = super.validateTryPlaceDisc(cell);

    // have list of booleans be returned without making changes

    // if all adjacent opposite hexes were null (clicked cell is
    // not adjacent to an opponent cell) and no moves allowable
    return !loMovesMade.isEmpty() && this.checkListContainsTrue(loMovesMade);
  }

  @Override
  public Boolean tryPlaceDisc(ReversiCell adjSquare, int i) {
    // switches between possible i (direction of path of opponent cells adjacent to target hex)
    switch (i) {
      // top right: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 0:
        return this.numOppositeHexes(adjSquare, 1, -1, 0) > 0;

      // right: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 1:
        return this.numOppositeHexes(adjSquare, 1, 0, 0) > 0;

      // bottom right: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 2:
        return this.numOppositeHexes(adjSquare, 1, 1, 0) > 0;

      // bottom : if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 3:
        return this.numOppositeHexes(adjSquare, 0, 1, 0) > 0;

      // bottom left: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 4:
        return this.numOppositeHexes(adjSquare, -1, 1, 0) > 0;

      // left: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 5:
        return this.numOppositeHexes(adjSquare, -1, 0, 0) > 0;

      // top left: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 6:
        return this.numOppositeHexes(adjSquare, -1, -1, 0) > 0;

      // top : if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 7:
        return this.numOppositeHexes(adjSquare, 0, -1, 0) > 0;
      default:
        throw new IllegalArgumentException("Direction cannot be handled.");
    }
  }

  /**
   * Returns a list of all booleans checking if the given hex has a move in all 6 directions.
   * Side Effect: if a move can be made in a certain direction, make the move, then check other
   * directions.
   *
   * @param cell The desire cell.
   * @return list of booleans each boolean resembles if a move can be made in a certain direction.
   */
  @Override
  public List<Boolean> validatePlaceDiscMove(ReversiCell cell) {
    int x = cell.getX();
    int y = cell.getY();

    List<ReversiCell> loAdjacentOpposite = this.getAdjacentOpposite(cell);
    if (super.checkListNull(loAdjacentOpposite)) {
      throw new IllegalStateException("No adjacent opposite color cells.");
    }
    List<Boolean> loMovesMade = new ArrayList<>();

    for (int i = 0; i < loAdjacentOpposite.size(); i++) {
      // for every existing adjacent opponent hex
      if (loAdjacentOpposite.get(i) != null) {
        // calls method to make move and make applicable changes if allowable
        // adds a boolean to loMoves (true if a successful move, false if not)
        loMovesMade.add(this.placeDiscHelper(new Square(x, y), loAdjacentOpposite.get(i), i));
      }
    }
    return loMovesMade;
  }


  /**
   * Gets the disc status (black, white, empty) of a hex of coordinates q, r, s from the grid.
   *
   * @param cell The desired cell.
   * @return the disc status of the clicked cell (the player who holds this cell)
   * @throws IllegalStateException    if game not started yet
   * @throws IllegalArgumentException if q,r,s is an invalid hex coordinate (not on grid)
   */
  @Override
  public DiscStatus getStatus(ReversiCell cell) {
    int x = cell.getX();
    int y = cell.getY();

    return grid.getStatus(new Square(x, y));
  }

  /**
   * Determines number of opponent hexes in between clicked hex and surrounding player hex
   * (0 if no surrounding player cell exists and move is not valid).
   *
   * @param adjSquare Opponent hex adjacent to clicked hex.
   * @param xDir      The number change in axis q when traveling along path of adjacent cell.
   * @param yDir      The number change in axis r when traveling along path of adjacent cell.
   * @param na        The number change in axis s when traveling along path of adjacent cell.
   * @return The number of opponent hexes plus clicked cell needed to flip
   */
  @Override
  public int numOppositeHexes(ReversiCell adjSquare, int xDir, int yDir, int na) {

    int numHexesInBetween = 0;
    DiscStatus oppositeStatus;

    // get opposite color of player, same as adjacent hex
    if (grid.getGrid().get(adjSquare).equals(DiscStatus.White)) {
      oppositeStatus = DiscStatus.White;
    } else {
      oppositeStatus = DiscStatus.Black;
    }

    // get initial coordinates of adjacent hex
    int x = adjSquare.getX();
    int y = adjSquare.getY();

    // traverse through same path of adjacent hex, from player-clicked hex
    // while in grid, follow path of adjacent hex and find how many opposite colored cells exist
    while (grid.getGrid().containsKey(new Square(x, y))) {

      // if opposite color, move to next and add 1
      if (grid.getGrid().get(new Square(x, y)).equals(oppositeStatus)) {
        numHexesInBetween++;
        x += xDir;
        y += yDir;
        continue;
      }
      // if not opponent color, break out
      break;
    }

    // if current hex is not on the grid, return 0, no surrounding cell
    if (!grid.getGrid().containsKey(new Square(x, y))) {
      return 0;
    }
    // if surrounding cell is player color (not opponent's and not empty),
    // return number of cells traversed
    if (!grid.getGrid().get(new Square(x, y)).equals(oppositeStatus)
            && !grid.getGrid().get(new Square(x, y)).equals(DiscStatus.Empty)) {
      return numHexesInBetween;
    } else {
      return 0;
    }
  }

  /**
   * Gets all adjacent opponent hexes to hex of given coordinates.
   *
   * @param cell The desired cell
   * @return list of hexes of adjacent cells.
   */
  @Override
  public List<ReversiCell> getAdjacentOpposite(ReversiCell cell) {
    int x = cell.getX();
    int y = cell.getY();

    DiscStatus oppositeStatus;
    // determine opponent cell color based on turn
    if (blackTurn) {
      oppositeStatus = DiscStatus.White;
    } else {
      oppositeStatus = DiscStatus.Black;
    }
    // set result list to be all nulls initially
    List<ReversiCell> loAdjacent = new ArrayList<>();
    for (int i = 0; i < 8; i++) {
      loAdjacent.add(null);
    }

    // TOP RIGHT: if hex to the top right of clicked cell is on the grid, and the opposite color
    if (this.checkCellOnGridAndOpposite(x + 1, y - 1, oppositeStatus)) {
      // add hex at index 0
      loAdjacent.set(0, new Square(x + 1, y - 1));
    }
    // RIGHT: if hex to the right of clicked cell is on the grid, and the opposite color
    if (this.checkCellOnGridAndOpposite(x + 1, y, oppositeStatus)) {
      // add hex at index 0
      loAdjacent.set(1, new Square(x + 1, y));
    }
    // BOTTOM RIGHT: if hex to the bottom right of clicked cell is on the grid,
    // and the opposite color
    if (this.checkCellOnGridAndOpposite(x + 1, y + 1, oppositeStatus)) {
      // add hex at index 0
      loAdjacent.set(2, new Square(x + 1, y + 1));
    }
    // BOTTOM : if hex to the bottom of clicked cell is on the grid,
    // and the opposite color
    if (this.checkCellOnGridAndOpposite(x, y + 1, oppositeStatus)) {
      // add hex at index 0
      loAdjacent.set(3, new Square(x, y + 1));
    }
    // BOTTOM LEFT: if hex to the bottom left of clicked cell is on the grid,
    // and the opposite color
    if (this.checkCellOnGridAndOpposite(x - 1, y + 1, oppositeStatus)) {
      // add hex at index 0
      loAdjacent.set(4, new Square(x - 1, y + 1));
    }
    // LEFT: if hex to the left of clicked cell is on the grid, and the opposite color
    if (this.checkCellOnGridAndOpposite(x - 1, y, oppositeStatus)) {
      // add hex at index 0
      loAdjacent.set(5, new Square(x - 1, y));
    }
    // TOP LEFT: if hex to the top left of clicked cell is on the grid, and the opposite color
    if (this.checkCellOnGridAndOpposite(x - 1, y - 1, oppositeStatus)) {
      // add hex at index 0
      loAdjacent.set(6, new Square(x - 1, y - 1));
    }
    // TOP : if hex to the top of clicked cell is on the grid, and the opposite color
    if (this.checkCellOnGridAndOpposite(x, y - 1, oppositeStatus)) {
      // add hex at index 0
      loAdjacent.set(7, new Square(x, y - 1));
    }
    List<ReversiCell> loAdjacentCopy = new ArrayList<>(loAdjacent);
    return loAdjacentCopy;
  }

  /**
   * Is the cell passed in the opposite color of the current player and on the grid?.
   *
   * @param x              coordinate of given cell
   * @param y              coordinate of given cell
   * @param oppositeStatus opposite status of current player
   * @return true if cell passed in is opposite the current player, false otherwise
   */
  private boolean checkCellOnGridAndOpposite(int x, int y, DiscStatus oppositeStatus) {
    return grid.getGrid().containsKey(new Square(x, y))
            && grid.getGrid().get(new Square(x, y)).equals(oppositeStatus);
  }

  /**
   * Based on if the disc being placed at the target location is valid,
   * flips hexes in between
   * and outputs if a valid move and changes were made.
   *
   * @param targetSquare The clicked hex by the player.
   * @param adjSquare    The adjacent opponent cell from the target cell.
   * @param i            Represents the direction the adjHex is in respect to the targetHex.
   * @return True if valid move was made, false otherwise.
   */
  @Override
  public boolean placeDiscHelper(ReversiCell targetSquare, ReversiCell adjSquare, int i) {

    // switches between possible i (direction of path of opponent cells adjacent to target hex)
    switch (i) {
      // top right: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 0:
        if (this.numOppositeHexes(adjSquare, 1, -1, 0) > 0) {
          this.makeChanges(targetSquare,
                  this.numOppositeHexes(adjSquare, 1, -1, 0),
                  1, -1, 0);
          return true;
        }
        return false;
      // right: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 1:
        if (this.numOppositeHexes(adjSquare, 1, 0, 0) > 0) {
          this.makeChanges(targetSquare,
                  this.numOppositeHexes(adjSquare, 1, 0, 0),
                  1, 0, 0);
          return true;
        }
        return false;
      // bottom right: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 2:
        if (this.numOppositeHexes(adjSquare, 1, 1, 0) > 0) {
          this.makeChanges(targetSquare,
                  this.numOppositeHexes(adjSquare, 1, 1, 0),
                  1, 1, 0);
          return true;
        }
        return false;
      // bottom : if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 3:
        if (this.numOppositeHexes(adjSquare, 0, 1, 0) > 0) {
          this.makeChanges(targetSquare,
                  this.numOppositeHexes(adjSquare, 0, 1, 0),
                  0, 1, 0);
          return true;
        }
        return false;
      // bottom left: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 4:
        if (this.numOppositeHexes(adjSquare, -1, 1, 0) > 0) {
          this.makeChanges(targetSquare,
                  this.numOppositeHexes(adjSquare, -1, 1, 0),
                  -1, 1, 0);
          return true;
        }
        return false;
      // left: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 5:
        if (this.numOppositeHexes(adjSquare, -1, 0, 0) > 0) {
          this.makeChanges(targetSquare,
                  this.numOppositeHexes(adjSquare, -1, 0, 0),
                  -1, 0, 0);
          return true;
        }
        return false;
      // top left: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 6:
        if (this.numOppositeHexes(adjSquare, -1, -1, 0) > 0) {
          this.makeChanges(targetSquare,
                  this.numOppositeHexes(adjSquare, -1, -1, 0),
                  -1, -1, 0);
          return true;
        }
        return false;
      // top : if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 7:
        if (this.numOppositeHexes(adjSquare, 0, -1, 0) > 0) {
          this.makeChanges(targetSquare,
                  this.numOppositeHexes(adjSquare, 0, -1, 0),
                  0, -1, 0);
          return true;
        }
        return false;
      default:
        throw new IllegalArgumentException("Direction cannot be handled.");
    }
  }


  /**
   * Makes disc status of clicked cell the player's color, and for every opponent hex in between.
   * flip to player color
   *
   * @param targetSquare        The hex at the coordinates that player clicked.
   * @param numSquaresInBetween The number of opponent hexes in between player hexes.
   * @param xDir                The number change in axis q when traveling along path of adjacent
   *                            cell.
   * @param yDir                The number change in axis r when traveling along path of adjacent
   *                            cell.
   * @param na                  placeholder coordinate.
   */
  @Override
  public void makeChanges(ReversiCell targetSquare, int numSquaresInBetween, int xDir,
                          int yDir, int na) {
    DiscStatus playerColor;

    // determine disc status corresponding to which player is playing
    if (blackTurn) {
      playerColor = DiscStatus.Black;
    } else {
      playerColor = DiscStatus.White;
    }

    // get initial coordinates of adjacent hex
    int x = targetSquare.getX();
    int y = targetSquare.getY();

    // starting at clicked hex, starts flipping colors of hexes
    for (int i = 0; i <= numSquaresInBetween; i++) {
      Square currSquare = new Square(x, y);
      grid.getGrid().replace(currSquare, playerColor);

      // update coordinates, startGame to next hex in path
      x += xDir;
      y += yDir;
    }
  }

  /**
   * Returns copy of current reversi model grid.
   *
   * @return copy of grid
   */
  @Override
  public Map<ReversiCell, DiscStatus> getCopyOfGrid() {
    Map<ReversiCell, DiscStatus> currGrid = new HashMap<>();
    for (ReversiCell square : this.grid.getGrid().keySet()) {
      currGrid.put(new Square(square.getX(), square.getY()), this.grid.getGrid().get(square));
    }
    return currGrid;
  }

  /**
   * Calculates how many hexes on a grid are of the given disc status.
   *
   * @param status The disc status we are calculating for.
   * @return The number of hexes on a grid are of the given disc status.
   */
  @Override
  public int getScoreHelper(DiscStatus status) {
    int score = 0;
    for (int x = 0; x < this.gridSize; x++) {
      for (int y = 0; y < this.gridSize; y++) {
        if (grid.getGrid().get(new Square(x, y)) == status) {
          score += 1;
        }
      }
    }
    return score;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getGridSize() {
    return this.gridSize;
  }

}