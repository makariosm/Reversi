Overview: This codebase is responsible for all interfaces, classes, and methods responsible for making moves and representing all the components of the reversi game. In addition, with all this representation of the model of the reversi game, we are able to textually render the game, visually seeing the grid that the players are acting on, including which pieces belong to which players and changing in response to moves made from players. The current codebase deals with cells as hexes, and the entire grid shape as a hexagon itself, restricting the shapes of the grid accordingly (for example, grid size must be odd and the same length on all axes). The rules of the game are needed to be understood to play this game, and what moves are possible to be made. Some extensibility that is envisioned is regarding different shapes of grids. In the current form, the class for hex grid is made, but in the future can be part of an interface representing different types of grids. Also, it is assumed that there will be different types of reversi games, and that this implementation is the basic version (class BasicReversi). The player interface inlcuding the AI is out of scope, as we have not created this yet. As of now, the player is represented in part by the hex's DiscStatus, which changes to the player that is playing if it's hit by the player move.

Quick start: Play 
public void examplePlay() {
  model = new BasicReversi();
  view = new ReversiTextualView(model);
  model.startGame();
  model.placeDisc(2,-1,-1);
  Assert.assertEquals(view.toString(), 
		 "     _ _ _ _ _ _ \n" +
          "    _ _ _ _ _ _ _ \n" +
          "   _ _ _ _ _ _ _ _ \n" +
          "  _ _ _ _ _ _ _ _ _ \n" +
          " _ _ _ _ X X X _ _ _ \n" +
          "_ _ _ _ O _ X _ _ _ _ \n" +
          " _ _ _ _ X O _ _ _ _ \n" +
          "  _ _ _ _ _ _ _ _ _ \n" +
          "   _ _ _ _ _ _ _ _ \n" +
          "    _ _ _ _ _ _ _ \n" +
          "     _ _ _ _ _ _ \n");
}


Quick Start: Pass
public void examplePass() {
  model = new BasicReversi();
  view = new ReversiTextualView(model);
  model.startGame();
  model.pass();
  Assert.assertEquals(view.toString(), 
		 "     _ _ _ _ _ _ \n" +
          "    _ _ _ _ _ _ _ \n" +
          "   _ _ _ _ _ _ _ _ \n" +
          "  _ _ _ _ _ _ _ _ _ \n" +
          " _ _ _ _ X O _ _ _ _ \n" +
          "_ _ _ _ O _ X _ _ _ _ \n" +
          " _ _ _ _ X O _ _ _ _ \n" +
          "  _ _ _ _ _ _ _ _ _ \n" +
          "   _ _ _ _ _ _ _ _ \n" +
          "    _ _ _ _ _ _ _ \n" +
          "     _ _ _ _ _ _ \n");
}


Key components: The key components of my system are:
	- the model interface 
	- basic reversi class model (playing the game and all its components)
		- HexGrid: exists to represent the player’s hexes and plays
			- hashMap<Hex, DiscStatus>: map of hex to its discstatus 
				- Hex: representation of a cell in the hashmap grid, holds coordinates to its location (q, r, s) and responsible		       for representing players on grid
				- DiscStatus: enum representation of the status of a disc (color of the disc based on which player it belongs to: Black, White, Empty)
		- turn keeper (turnBlack): switches after every play or pass
		- gridSize (determines the max hex width/height of hexes to create the grid)
		- consecutiveCount (determines the amount of consecutive times a player has passed their turn)
	- the view interface
		- reversi textual view class(rendering the grid, and visualizing it)

The method startGame starts the game, populating the grid and setting the inner ring of the grid to be alternating player colors.
The method placeDisc drives the system, and in steps: finds the adjacent opponent hexes to the clicked cell (getAdjacentOpoosite), traverses through each path and finds how many opponent hexes are in between the clicked hex and the surrounding hex of the player’s color if there are any (numOppositeHexes which returns 0 if there are no surrounding hexes of the player's color), and places the hex with the player's color, and flips the color of all the hexes in the middle of the two hexes (makeChanges).
The pass method switches the player's turn, and adds to the consecutive pass count.
The method isGameOver determines if the game is over (if the consecutive passes are 2).
The methods getBlackScore and getWhiteScore determine how many hexes of the respective player are on the grid.
The method getStatus gets the disc status (black, white, empty) of the hex in the grid at the given coordinates.
The method getGridSize returns the number of hexes in the axes of the model's grid (has to be odd and greater than or equal to 3).
The method anyPossibleMoves determines if there are hexes on the grid that the current player can play on. If it returns false, a player needs to pass.


Source organization: All the implementation code is in the src folder. In the src folder contains the model and the view. The view only contains the view interface and the textual view class of this interface to render the grid. The model contains the interface model, the implementation class of it in the current game form, the disc status, hex, and the interface and class for the grid implementation. The interface grid exists for possible extension in the future for different-shaped games.
 src/
	Model/
		ReversiModel
		BasicReversi
		DiscStatus
		Hex
		HexGrid
		Grid
   	View/
		ReversiTextualView
		TextualView
 test/
        BasicReversiTests


Changes for part 2:

- Exising Model: Corrected old, inaccurate method to new method of anyPossibleMoves. Added getPossibleMoves 
to help work with strategy implementation, and created isLegalAt method in existing model to implement the one 
missing functionality 
mentioned in the assignment description: "Is it legal for the current player to play at a given coordinate?". 
- Added ReadOnlyReversiModel interface, that only is able to call methods to observe the contents of the grid and 
the information of the game, and not mutate it at all. This is necessary when trying to implement the view and making
 sure you are not 
mutating the model.
- Added basic class BasicPlayer that has a discstatus and strategy type, and implemented its interface, Player. 
Player will be responsible for getting the hex for a given player so that they can make a move with a given strategy.
- Added class ReversiKeyListener that takes in a model and view and listens for key events (pass "p" and enter,
 mutating the model through appropriate methods like place disc and pass on the clicked hex.
- MockReversiModel : Used to test the strategies with stub methods
- ReversiPanelMock: The class that represents the mock of the reversi panel class of the view for testing. Contains all 
stub methods of panel class
- ReversiViewMock: Represents mock of a reversi view, implementing all stub methods of the reversi view interface.

Strategies:
- HexStrategy : interface for all the different strategies of reversi game
- CaptureMostStrategy : class that implements strategy 1, finds the hex that captures the most hexes, and if a tie-breaker 
gets the top-most, left-most hex
- AvoidNextToCornersStrategy : class that implements strategy 2, finds the hex to play on that is not next to a corner,  and 
if a tie-breaker gets the top-most, left-most hex
- CaptureCornersStrategy : class that implements strategy 3, finds the corner hex that the player can play on if there are any,  
and if a tie-breaker, gets the top-most, left-most hex
- CombinedStrategy : combines strategies 1,2,and 3. Priority moves from strategy 3 to 2 to 1 					
This is a strategy an AI Player may be able to utilize.


View:
- ReversiView : interface of the all swing-implemented views of a reversi game
- BasicReversiView : Takes in panel implementation and functionality and displays it to screen, using swing UI
- JReversiPanel : Utilizes the model and its content and implements all the view functionality of the GUI to 
visualize the game, passing it onto BasicReversiView

- ReversiGame : Takes in the view and model and runs the entire reversi game, displaying the GUI hex grid of the 
game and will be able to start the moving of discs and the entire game functionality. 
In this class, the game will be started and start to be interactive.


Changes for part 3:

Fixes from previous submission: 
- Made models used in strategy classes read only to ensure no mutation
- Made combined strategy class utilize private version of all startegies
- Added transcript and removed key listener class
- key presses execute a move or pass (previously did nothing)
- click outside board now deselects cell
- added Features interface for the view to callback the controller

Design explanation:
The controller implements two interfaces (observers). These are InputFeatures (listens to view) and ModelListener
(listens to model). InputFeatures takes in information from user input and passes and places discs accordingly 
(responding to keyboard clicks). ModelListener listens to the model and switches control between the controllers 
(who have access to the players) accordingly after each place disc or pass. 

A human player presses on a hex and presses "enter" (place disc) or "p" (pass).
AI player automatically place a disc when its their turn or pass if they don't have any possible moves.

ReversiGame main:
- human is used in command line to run a human player
- "strategy 1", "strategy 2", "strategy 3", and "strategy 4" are used to pick a strategy for an AI player

Added list of observers to model (ModelListeners) and view (InputFeatures).

Interfaces added:
- InputFeatures : Implemented by the controller, these are the features the controller can use 
based on inputs in the view (from a human user), observes view and contains main game functions such as 
place disc and passing. For human players, specifically handles response to "p" (regardless of a cell click) 
and "enter" from a human player after it selects the cell it wants to move to.

- ModelListener : Implemented by the controller, this class contains methods used in the model that 
notify its controller (the features that the controller implement when listening to the model), 
including notifying controllers that it is their turn, and that the game is over (controller shouldn't 
make any more moves for given player)

- ModelFeatures : Implemented by the model. The features that a model should have to be able to interact 
with the view and controller, including the functionality for switching in between controllers. These 
include telling the model to start the black player's. turn in the beginning of the game, switching 
the controller's turn to another, and ending the turn of another controller when this one makes the final
move in the game (game is over). 


Classes Added:
- ReversiController : The implementation of a controller to play a game of Reversi. It listens 
to the model and the view as specified by the interfaces it implements, contains functionality 
of coordinating between model and view to make moves and pass, relaying the information/conducting 
the action in both.
- HumanStrategy: The implementation of a hex strategy that waits for a player to manually choose a hex to
place their disc in
- MockCaptureMostStrategy: mocks the most basic strategy for an AI player in order to observe how the 
strategy picks its resulting cell, sees which hexes have been looked at
- MockController : mocks the controller to test its functionality and observe it switching between 
players and between the view/model in order ot carry out game functionality.


Reversi Part 4:

Command line:
The argument is one of "human", or "strategy 1", "strategy 2", "strategy 3", or "strategy 4" for our 
implementation. The other argument works off of the providers' view/strategy implementations, so it 
is either "providerHuman" (does not implement a strategy but rather remains listening for human input),
 "providerStrategy1", "providerStrategy2", "providerStrategy3", or "providerStrategy4".

Working features: We were able to get all required features working with our/their view and strategy
implementations. 

Reversi Extra Credit:
Added all extra credit funtionality.
- Files affected/created :
	- Deleted BasicReversi, made to abstract
	- HexReversi extends AbstractReversi, so does SquareReversi
	- Created abstract class called ReversiCell (Hex and Square implement it)
	- Made a SquareGrid that implements interface Grid
	- Modified all strategies to function for squares too and return a reversi cell 
		instead of a hex
	- Made JSquareReversiPanel that controls view for square reversi
	- Has decorator HintPanel that adds the hint functionality to the view without
		 rewriting original view
	- Added SquareTextualReversi to easily visualize model for game and its functionality
	- ReversiGame now works for either model and view through command line
	- Added tests for all new functionality

New functionality (continued) and coordinate system:
- Hints is toggled by pressing "h" key
- Coordinate system is x, y coordinates
- Top left cell is 0, 0 and x and y increases as you move right and down, only positive coordinates
- Square grid size (number of squares on side) must be positive and even
- Still works off of "p" and "enter" keys to pass and place discs
- Starter 4 cells are made in the middle of the grid. 
- Same rules follow, can move in 8 directions instead of 6 in hex reversi
- Strategies are the same


Command line: 
- first argument : hex or square
- second : first player strategy
- third : second player strategy


