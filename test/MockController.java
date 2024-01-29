import cs3500.reversi.model.Player;
import cs3500.reversi.controller.ReversiController;
import cs3500.reversi.model.DiscStatus;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.view.ReversiView;

/**
 * Mock for the basic reversi controller class. Used for testing the controller and its .
 * functionality
 */
public class MockController extends ReversiController {

  ReversiModel model;
  Player player;
  ReversiView view;
  boolean blackTurn;
  StringBuilder log;

  /**
   * Constructs a mock controller for Reversi.
   *
   * @param model  the model of the game of Reversi being played
   * @param player the player that uses the controller to play the game
   * @param view   the view that the controller is listening to, so it can properly control the game
   */
  public MockController(StringBuilder log, ReversiModel model, Player player, ReversiView view) {
    super(model, player, view);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void play(boolean blackTurn) {
    //    view.repaint();
    boolean hasControl = ((blackTurn && (player.getPlayerType() == DiscStatus.Black))
            || (!blackTurn && (player.getPlayerType() == DiscStatus.White)));
    this.blackTurn = blackTurn;

    if (blackTurn) {
      log.append(String.format("The current turn is black"));
    }
    else {
      log.append(String.format("The current turn is white"));
    }

    if (!model.isGameOver()) {
      if (hasControl) {
        if (!player.isHumanPlayer()) {
          this.controllerPlaceDisc();
        } else {
          view.showTurnDialogue();
        }
      }
    }
  }

  @Override
  public void end() {
    if (model.getWinners().size() == 2) {
      log.append("You Tied!");
    } else if (model.getWinners().contains(player.getPlayerType())) {
      log.append("You Won!");
    } else {
      log.append("You Lost!");
    }
  }




}
