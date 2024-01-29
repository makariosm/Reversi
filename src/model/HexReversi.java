package cs3500.reversi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Basic representation of a hexagonal reversi game, extends abstract reversi model implementation.
 */
public class HexReversi extends AbstractReversi {

  private final Grid grid;
  private int gridSize;

  /**
   * Default basic model of reversi game with gridSize of 11.
   */
  public HexReversi() {
    super();
    this.gridSize = 11;
    this.grid = new HexGrid(this.gridSize);
  }

  /**
   * Constructs basic model of reversi game.
   * @param gridSize Sets the size of the grid, how many hexes are in the axes of the grid
   */
  public HexReversi(int gridSize) {
    super();
    this.gridSize = gridSize;
    this.grid = new HexGrid(gridSize);
  }

  /**
   * Constructs a reversi game with all its information/fields.
   * @param gridSize size of grid
   * @param grid the hex grid of the game
   * @param consecutivePass number of consecutive passes
   * @param isGameOver boolean that tells if game is over or not
   * @param blackTurn boolean that determines whose turn it is
   */
  public HexReversi(int gridSize, Grid grid, int consecutivePass, boolean isGameOver,
                      boolean blackTurn) {
    super(gridSize, consecutivePass, isGameOver, blackTurn);
    this.grid = grid;
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

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public void placeDisc(DiscStatus playerType, ReversiCell cell) {
    super.gameNotStartedWarning();
    super.wrongPlayerWarning(playerType);

    int q = cell.getQ();
    int r = cell.getR();
    int s = cell.getS();

    if (!grid.getGrid().containsKey(new Hex(q, r, s))) {
      throw new IllegalArgumentException("Hex with given coordinates does not exist on this grid.");
    }

    if (!grid.getGrid().get(new Hex(q, r, s)).equals(DiscStatus.Empty)) {
      throw new IllegalStateException("Cannot place a dic on a non-empty cell.");
    }
    // gets all the adjacent opponent hexes to clicked cell
    List<Boolean> loMovesMade = validatePlaceDiscMove(cell);

    // if all adjacent opposite hexes were null (clicked cell is
    // not adjacent to an opponent cell)
    if (loMovesMade.isEmpty()) {
      throw new IllegalStateException("No adjacent opposite hexes.");
    }

    // if for all adjacent opponent cells, no moves were allowable
    else if (!this.checkListContainsTrue(loMovesMade)) {
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
    gameNotStartedWarning();
    List<ReversiCell> loPossibleMoves = new ArrayList<>();
    int coord = (int) Math.floor(gridSize / 2);
    for (int q = -coord; q <= coord; q++) {
      for (int r = -coord; r <= coord; r++) {
        for (int s = -coord; s <= coord; s++) {
          if (q + r + s == 0) {
            if (this.isMoveLegalAt(new Hex(q, r, s))) {
              loPossibleMoves.add(new Hex(q, r, s));
            }
          }
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

    int q = cell.getQ();
    int r = cell.getR();
    int s = cell.getS();

    gameNotStartedWarning();
    if (!grid.getGrid().containsKey(new Hex(q, r, s))
            || !grid.getGrid().get(new Hex(q, r, s)).equals(DiscStatus.Empty)) {
      return false;
    }
    // gets all the adjacent opponent hexes to clicked cell
    List<Boolean> loMovesMade = super.validateTryPlaceDisc(cell);

    // have list of booleans be returned without making changes

    // if all adjacent opposite hexes were null (clicked cell is
    // not adjacent to an opponent cell) and no moves allowable
    return !loMovesMade.isEmpty() && super.checkListContainsTrue(loMovesMade);
  }

  @Override
  public Boolean tryPlaceDisc(ReversiCell adjHex, int i) {
    // switches between possible i (direction of path of opponent cells adjacent to target hex)
    switch (i) {
      // top right: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 0:
        return this.numOppositeHexes(adjHex, 1, -1, 0) > 0;
      // right: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 1:
        return this.numOppositeHexes(adjHex, 1, 0, -1) > 0;
      // bottom right: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 2:
        return this.numOppositeHexes(adjHex, 0, 1, -1) > 0;
      // bottom left: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 3:
        return this.numOppositeHexes(adjHex, -1, 1, 0) > 0;
      // left: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 4:
        return this.numOppositeHexes(adjHex, -1, 0, 1) > 0;
      // top left: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 5:
        return this.numOppositeHexes(adjHex, 0, -1, 1) > 0;
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
    int q = cell.getQ();
    int r = cell.getR();
    int s = cell.getS();

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
        loMovesMade.add(this.placeDiscHelper(new Hex(q, r, s), loAdjacentOpposite.get(i), i));
      }

    }
    return loMovesMade;
  }

  /**
   * Gets all adjacent opponent hexes to hex of given coordinates.
   *
   * @param cell The desired cell
   * @return list of hexes of adjacent cells.
   */
  public List<ReversiCell> getAdjacentOpposite(ReversiCell cell) {

    int q = cell.getQ();
    int r = cell.getR();
    int s = cell.getS();

    DiscStatus oppositeStatus;
    // determine opponent cell color based on turn
    if (blackTurn) {
      oppositeStatus = DiscStatus.White;
    } else {
      oppositeStatus = DiscStatus.Black;
    }
    // set result list to be all nulls initially
    List<ReversiCell> loAdjacent = new ArrayList<>();
    for (int i = 0; i < 6; i++) {
      loAdjacent.add(null);
    }
    // TOP RIGHT: if hex to the top right of clicked cell is on the grid, and the opposite color
    if (this.checkCellOnGridAndOpposite(q + 1, r - 1, s, oppositeStatus)) {
      // add hex at index 0
      loAdjacent.set(0, new Hex(q + 1, r - 1, s));
    }
    // RIGHT: if hex to the right of clicked cell is on the grid, and the opposite color
    if (this.checkCellOnGridAndOpposite(q + 1, r, s - 1, oppositeStatus)) {
      // add hex at index 1
      loAdjacent.set(1, new Hex(q + 1, r, s - 1));
    }
    // BOTTOM RIGHT: if hex to the bottom right of clicked cell is on the grid,
    // and the opposite color
    if (this.checkCellOnGridAndOpposite(q, r + 1, s - 1, oppositeStatus)) {
      // add hex at index 2
      loAdjacent.set(2, new Hex(q, r + 1, s - 1));
    }
    // BOTTOM LEFT: if hex to the bottom left of clicked cell is on the grid,
    // and the opposite color
    if (this.checkCellOnGridAndOpposite(q - 1, r + 1, s, oppositeStatus)) {
      // add hex at index 3
      loAdjacent.set(3, new Hex(q - 1, r + 1, s));
    }
    // LEFT: if hex to the left of clicked cell is on the grid, and the opposite color
    if (this.checkCellOnGridAndOpposite(q - 1, r, s + 1, oppositeStatus)) {
      // add hex at index 4
      loAdjacent.set(4, new Hex(q - 1, r, s + 1));
    }
    // TOP LEFT: if hex to the top left of clicked cell is on the grid, and the opposite color
    if (this.checkCellOnGridAndOpposite(q, r - 1, s + 1, oppositeStatus)) {
      // add hex at index 5
      loAdjacent.set(5, new Hex(q, r - 1, s + 1));
    }
    List<ReversiCell> loAdjacentCopy = new ArrayList<>(loAdjacent);
    //    // if none pass (list remains all null), throw exception
    //    if (this.checkListNull(loAdjacent)) {
    //      throw new IllegalStateException("No adjacent opposite color cells.");
    //    }
    return loAdjacentCopy;
  }

  /**
   * Is the cell passed in the opposite color of the current player and on the grid?.
   *
   * @param q              coordinate of given cell
   * @param r              coordinate of given cell
   * @param s              coordinate of given cell
   * @param oppositeStatus opposite status of current player
   * @return true if cell passed in is opposite the current player, false otherwise
   */
  private boolean checkCellOnGridAndOpposite(int q, int r, int s, DiscStatus oppositeStatus) {
    return grid.getGrid().containsKey(new Hex(q, r, s))
            && grid.getGrid().get(new Hex(q, r, s)).equals(oppositeStatus);
  }

  /**
   * Based on if the disc being placed at the target location is valid,
   * flips hexes in between
   * and outputs if a valid move and changes were made.
   *
   * @param targetHex The clicked hex by the player.
   * @param adjHex    The adjacent opponent cell from the target cell.
   * @param i         Represents the direction the adjHex is in respect to the targetHex.
   * @return True if valid move was made, false otherwise.
   */
  @Override
  public boolean placeDiscHelper(ReversiCell targetHex, ReversiCell adjHex, int i) {

    // switches between possible i (direction of path of opponent cells adjacent to target hex)
    switch (i) {
      // top right: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 0:
        if (this.numOppositeHexes(adjHex, 1, -1, 0) > 0) {
          this.makeChanges(targetHex,
                  this.numOppositeHexes(adjHex, 1, -1, 0),
                  1, -1, 0);
          return true;
        }
        return false;
      // right: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 1:
        if (this.numOppositeHexes(adjHex, 1, 0, -1) > 0) {
          this.makeChanges(targetHex,
                  this.numOppositeHexes(adjHex, 1, 0, -1),
                  1, 0, -1);
          return true;
        }
        return false;
      // bottom right: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 2:
        if (this.numOppositeHexes(adjHex, 0, 1, -1) > 0) {
          this.makeChanges(targetHex,
                  this.numOppositeHexes(adjHex, 0, 1, -1),
                  0, 1, -1);
          return true;
        }
        return false;
      // bottom left: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 3:
        if (this.numOppositeHexes(adjHex, -1, 1, 0) > 0) {
          this.makeChanges(targetHex,
                  this.numOppositeHexes(adjHex, -1, 1, 0),
                  -1, 1, 0);
          return true;
        }
        return false;
      // left: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 4:
        if (this.numOppositeHexes(adjHex, -1, 0, 1) > 0) {
          this.makeChanges(targetHex,
                  this.numOppositeHexes(adjHex, -1, 0, 1),
                  -1, 0, 1);
          return true;
        }
        return false;
      // top left: if opponent hex path adjacent to clicked cell has surrounding player hex,
      // make resulting changes to board
      case 5:
        if (this.numOppositeHexes(adjHex, 0, -1, 1) > 0) {
          this.makeChanges(targetHex,
                  this.numOppositeHexes(adjHex, 0, -1, 1),
                  0, -1, 1);
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
   * @param targetHex         The hex at the coordinates that player clicked.
   * @param numHexesInBetween The number of opponent hexes in between player hexes.
   * @param qDir              The number change in axis q when traveling along path of adjacent
   *                          cell.
   * @param rDir              The number change in axis r when traveling along path of adjacent
   *                          cell.
   * @param sDir              The number change in axis s when traveling along path of adjacent
   *                          cell.
   */
  @Override
  public void makeChanges(ReversiCell targetHex,
                          int numHexesInBetween, int qDir, int rDir, int sDir) {
    DiscStatus playerColor;

    // determine disc status corresponding to which player is playing
    if (blackTurn) {
      playerColor = DiscStatus.Black;
    } else {
      playerColor = DiscStatus.White;
    }

    // get initial coordinates of adjacent hex
    int q = targetHex.getQ();
    int r = targetHex.getR();
    int s = targetHex.getS();

    // starting at clicked hex, starts flipping colors of hexes
    for (int i = 0; i <= numHexesInBetween; i++) {
      Hex currentHex = new Hex(q, r, s);
      grid.getGrid().replace(currentHex, playerColor);

      // update coordinates, startGame to next hex in path
      q += qDir;
      r += rDir;
      s += sDir;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DiscStatus getStatus(ReversiCell cell) {
    int q = cell.getQ();
    int r = cell.getR();
    int s = cell.getS();

    return grid.getStatus(new Hex(q, r, s));
  }

  @Override
  public int numOppositeHexes(ReversiCell adjHex, int qDir, int rDir, int sDir) {
    int numHexesInBetween = 0;
    DiscStatus oppositeStatus;

    // get opposite color of player, same as adjacent hex
    if (grid.getGrid().get(adjHex).equals(DiscStatus.White)) {
      oppositeStatus = DiscStatus.White;
    } else {
      oppositeStatus = DiscStatus.Black;
    }

    // get initial coordinates of adjacent hex
    int q = adjHex.getQ();
    int r = adjHex.getR();
    int s = adjHex.getS();

    // traverse through same path of adjacent hex, from player-clicked hex
    // while in grid, follow path of adjacent hex and find how many opposite colored cells exist
    while (grid.getGrid().containsKey(new Hex(q, r, s))) {

      // if opposite color, move to next and add 1
      if (grid.getGrid().get(new Hex(q, r, s)).equals(oppositeStatus)) {
        numHexesInBetween++;
        q += qDir;
        r += rDir;
        s += sDir;
        continue;
      }
      // if not opponent color, break out
      break;
    }

    // if current hex is not on the grid, return 0, no surrounding cell
    if (!grid.getGrid().containsKey(new Hex(q, r, s))) {
      return 0;
    }
    // if surrounding cell is player color (not opponent's and not empty),
    // return number of cells traversed
    if (!grid.getGrid().get(new Hex(q, r, s)).equals(oppositeStatus)
            && !grid.getGrid().get(new Hex(q, r, s)).equals(DiscStatus.Empty)) {
      return numHexesInBetween;
    } else {
      return 0;
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
    for (ReversiCell hex : this.grid.getGrid().keySet()) {
      currGrid.put(new Hex(hex.getQ(), hex.getR(), hex.getS()), this.grid.getGrid().get(hex));
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
    int coord = (int) Math.floor(gridSize / 2);
    for (int q = -coord; q <= coord; q++) {
      for (int r = -coord; r <= coord; r++) {
        for (int s = -coord; s <= coord; s++) {
          if (q + r + s == 0) {
            if (grid.getGrid().get(new Hex(q, r, s)) == status) {
              score += 1;
            }
          }
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
