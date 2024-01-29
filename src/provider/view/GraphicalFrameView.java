package cs3500.reversi.provider.view.graphical;

import javax.swing.KeyStroke;

import cs3500.reversi.provider.controller.ViewFeatures;

/**
 * The interface for the frame component of a graphical-based view, to be used in the Reversi game.
 */
public interface GraphicalFrameView {

  /**
   * Adds a feature to the view representing a view listener.
   *
   * @param feature the feature to be added
   * @throws IllegalArgumentException if the feature to be added is <code>null</code> OR has
   *                                  already been added
   */
  void addFeature(ViewFeatures feature) throws IllegalArgumentException;

  /**
   * Sets a hotkey for a specific feature.
   *
   * @param key the hotkey being set
   * @param featureName the name of the feature associated with the hotkey
   * @throws IllegalArgumentException if the given keystroke or feature name is <code>null</code>
   */
  void setHotKey(KeyStroke key, String featureName) throws IllegalArgumentException;

  /**
   * Updates the view.
   */
  void updateView();

  /**
   * Displays a specific message somewhere on the view.
   *
   * @param message the message to display
   * @throws IllegalArgumentException if the given message to display is <code>null</code>
   */
  void displayMessage(String message) throws IllegalArgumentException;

  /**
   * Displays a specific error message somewhere on the view.
   *
   * @param message the error message to display
   * @throws IllegalArgumentException if the given error message to display is <code>null</code>
   */
  void displayErrorMessage(String message) throws IllegalArgumentException;
}