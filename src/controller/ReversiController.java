package cs3500.reversi.controller;

import java.awt.event.KeyEvent;
import java.util.Objects;
import javax.swing.KeyStroke;
import cs3500.reversi.model.DiscStatus;
import cs3500.reversi.model.Hex;
import cs3500.reversi.model.Player;
import cs3500.reversi.model.ReversiCell;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.provider.controller.ViewFeatures;
import cs3500.reversi.provider.model.Hexagon;
import cs3500.reversi.provider.view.graphical.GraphicalFrameView;
import cs3500.reversi.view.ReversiView;

/**
 * The implementation of a controller to play a game of Reversi.
 * It listens to the model and the view as specified by the interfaces it implements.
 */
public class ReversiController implements ModelListener, InputFeatures, ViewFeatures {

  private final ReversiModel model;
  private final ReversiView view;
  private final Player player;
  private boolean blackTurn;
  private GraphicalFrameView theirView;

  /**
   * Constructs a controller for Reversi.
   *
   * @param model  the model of the game of Reversi being played
   * @param player the player that uses the controller to play the game
   * @param view   the view that the controller is listening to, so it can properly control the game
   */
  public ReversiController(ReversiModel model, Player player, ReversiView view) {
    if (Objects.isNull(model) || Objects.isNull(player) || Objects.isNull(view)) {
      throw new IllegalArgumentException("Model, player, and view cannot be null.");
    } else {
      this.model = model;
      this.view = view;
      this.player = player;
    }
    if (player.isHumanPlayer()) {
      view.addFeatures(this);
      view.setHotKey(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "pass");
      view.setHotKey(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "place");
      view.setHotKey(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0), "hint");
    }


    model.addFeatures(this);
    if (player.getPlayerType() == DiscStatus.Black) {
      view.addTitle("BLACK");
    } else {
      view.addTitle("WHITE");
    }
    view.display(true);
  }

  /**
   * Constructs a controller for Reversi with provider implementation.
   *
   * @param model     the model of the game of Reversi being played
   * @param player    the player that uses the controller to play the game
   * @param theirView provider's view that the controller is listening to,
   *                  so it can properly control the game
   */
  public ReversiController(ReversiModel model, Player player, GraphicalFrameView theirView) {
    if (Objects.isNull(model) || Objects.isNull(player) || Objects.isNull(theirView)) {
      throw new IllegalArgumentException("Model, player, and view cannot be null.");
    } else {
      this.model = model;
      this.theirView = theirView;
      this.player = player;
      this.view = null;
    }
    if (player.isHumanPlayer()) {
      theirView.addFeature(this);
      theirView.setHotKey(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "setPass");
      theirView.setHotKey(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
              "setMove");
    }

    model.addFeatures(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void controllerPlaceDisc() {

    try {
      ReversiCell currHex = player.play();
      view.setPanelCurrCell(currHex);

      if (!Objects.isNull(currHex) && !view.getAlreadySelected()) {
        this.model.placeDisc(player.getPlayerType(), currHex);
        view.repaint();
      }
      if (model.isGameOver()) {
        this.end();
        model.endOther(this.player.getPlayerType());
      }
      model.switchController(this.blackTurn);
    } catch (IllegalAccessException exception) {
      this.controllerPass();
    } catch (IllegalStateException | IllegalArgumentException exception) {
      view.showErrorDialogue(exception.getMessage());
    }
    System.out.println("Enter pressed");

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void controllerPass() {
    try {
      this.model.pass(player.getPlayerType());
      if (!model.isGameOver()) {
        model.switchController(this.blackTurn);
      } else {
        this.end();
        model.endOther(this.player.getPlayerType());
      }
    } catch (IllegalArgumentException e) {
      if (view != null) {
        view.showErrorDialogue(e.getMessage());
      } else {
        theirView.displayErrorMessage(e.getMessage());
      }
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void play(boolean blackTurn) {

    boolean hasControl = ((blackTurn && (player.getPlayerType() == DiscStatus.Black))
            || (!blackTurn && (player.getPlayerType() == DiscStatus.White)));
    this.blackTurn = blackTurn;

    if (view != null) {
      view.repaint();
      if (!model.isGameOver()) {
        if (hasControl) {
          if (!player.isHumanPlayer()) {
            this.controllerPlaceDisc();
          } else {
            view.showTurnDialogue();
          }
        }
      }
    } else {
      theirView.updateView();

      if (!model.isGameOver()) {
        if (hasControl) {
          try {
            if (!player.isHumanPlayer()) {
              ReversiCell hex = player.theirPlay();
              this.makeMove(new Hexagon(-hex.getS(), -hex.getR()));
            } else {
              theirView.displayMessage("Your turn :)");
            }
          } catch (IllegalAccessException e) {
            this.controllerPass();
          }
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void repaint() {
    if (view != null) {
      view.repaint();
    } else {
      theirView.updateView();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void end() {
    if (view != null) {
      if (model.getWinners().size() == 2) {
        view.showGameOverDialogue("You Tied!");
      } else if (model.getWinners().contains(player.getPlayerType())) {
        view.showGameOverDialogue("You Win! :)");
      } else {
        view.showGameOverDialogue("You Lose :(");
      }
    } else {
      if (model.getWinners().size() == 2) {
        theirView.displayMessage("You Tied!");
      } else if (model.getWinners().contains(player.getPlayerType())) {
        theirView.displayMessage("You Win! :)");
      } else {
        theirView.displayMessage("You Lose :(");
      }
    }
  }

  /**
   * Makes a move on the Reversi board at the specified hexagon. This method is to be called when
   * a move is made through the view. If any exception is thrown, this method should handle them
   * accordingly such that the game can continue normally.
   *
   * @param hexagon the hexagon where the move is to be made
   */
  @Override
  public void makeMove(Hexagon hexagon) {

    try {
      if (!Objects.isNull(hexagon)) {
        int r = -hexagon.getY();
        int s = -hexagon.getX();
        int q = -r - s;

        this.model.placeDisc(player.getPlayerType(), new Hex(q, r, s));
        theirView.displayMessage("Their turn :(");
        theirView.updateView();
      }

      // last play
      if (model.isGameOver()) {
        this.end();
        model.endOther(this.player.getPlayerType());
      }
      model.switchController(this.blackTurn);
    } catch (IllegalArgumentException | IllegalStateException exception) {
      theirView.displayErrorMessage(exception.getMessage());
    }
    System.out.println("Enter pressed");
  }

  /**
   * Passes the turn in the Reversi game. This method is called when a move is passed through the
   * view. If any exception is thrown, this method should handle them accordingly such that the
   * game can continue normally.
   */
  @Override
  public void pass() {
    this.controllerPass();
    theirView.displayMessage("Their turn :(");
  }
}
