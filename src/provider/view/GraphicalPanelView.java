package cs3500.reversi.provider.view.graphical;

import cs3500.reversi.provider.controller.ViewFeatures;

/**
 * The interface for the panel component of a graphical-based view, to be used in the Reversi game.
 */
public interface GraphicalPanelView {
  /**
   * Adds a new feature to the panel representing a view listener.
   *
   * @param feature the feature to be added
   * @throws IllegalArgumentException if the feature to be added is <code>null</code> OR has
   *                                  already been added
   */
  void addFeature(ViewFeatures feature) throws IllegalArgumentException;
}
