package cs3500.reversi.view;

import javax.swing.KeyStroke;
import cs3500.reversi.controller.InputFeatures;
import cs3500.reversi.model.ReversiCell;

/**
 * Represents a GUI view of the Reversi game for a player to be able to play and see the game.
 */
public interface ReversiView {

  /**
   * Makes the Java Swing window visible.
   * @param show if true make it visible, false don't
   */
  void display(boolean show);

  /**
   * Change the current hex panel, equivalent of clicking on a hex and pressing enter.
   * @param cell given hex, that the current hex panel changes to
   */
  void setPanelCurrCell(ReversiCell cell);


  /**
   * Get the current hex that was selected aka the hex that got clicked on and pressed enter.
   * @return the current hex that has been selected
   */
  ReversiCell getCurrCellSelection();

  /**
   * Has this hex been selected already?.
   * @return true if the hex has already been selected, false otherwise.
   */
  boolean getAlreadySelected();

  /**
   * Refresh the view to the current version of the model.
   */
  void repaint();

  /**
   * Show an error message based on the exception thrown by the model.
   * @param s the string you want to publish in the error pop-up
   */
  void showErrorDialogue(String s);

  /**
   * Give a player a notification that it's their turn.
   */
  void showTurnDialogue();

  /**
   * Give a player a notification that the game is over.
   * @param s the given string they want to pass in to the game over message.
   */
  void showGameOverDialogue(String s);

  /**
   * Set certain keyboard keys to certain actions you would like to map them to.
   * @param key the keyboard key you want to map
   * @param featureName the value or action you want to map the keyboard key to.
   */
  void setHotKey(KeyStroke key, String featureName);

  /**
   * Add an InputFeature as a listener to the view.
   * @param features the feature you would like to add
   */
  void addFeatures(InputFeatures features);

  /**
   * Add a title to the given window, whether it be white or black to show which plauer you are for.
   * a given window.
   * @param s the string you want to pass in to show whether you are white or black.
   */
  void addTitle(String s);

  /**
   * Sets the current hex that's been clicked on and pressed enter on in the view.
   * @param selectedHex The selected hex
   */
  void setCurrCellSelection(ReversiCell selectedHex);
}
