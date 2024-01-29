package cs3500.reversi.provider.view.graphical;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import cs3500.reversi.provider.controller.ViewFeatures;
import cs3500.reversi.provider.model.ROReversiModel;


/**
 * Represents any graphical panel view that is also a custom Java Swing panel. Every graphical
 * panel view has access to a read-only model of the Reversi game and a list of all view
 * listeners to notify.
 *
 * @see GraphicalPanelView
 */
public abstract class AbstractGameBoardPanel extends JPanel implements GraphicalPanelView {
  protected final ROReversiModel game;
  protected final List<ViewFeatures> viewListeners;

  /**
   * Constructs an AbstractGameBoardPanel object using a given read-only model of the Reversi game.
   *
   * @param game the read-only model of the Reversi game
   * @throws IllegalArgumentException if the given model is <code>null</code>
   */
  public AbstractGameBoardPanel(ROReversiModel game) throws IllegalArgumentException {
    if (game == null) {
      throw new IllegalArgumentException("The game cannot be null.");
    }

    this.game = game;
    this.viewListeners = new ArrayList<>();
  }

  @Override
  public void addFeature(ViewFeatures feature) throws IllegalArgumentException {
    if (feature == null) {
      throw new IllegalArgumentException("The feature cannot be null.");
    } else if (this.viewListeners.contains(feature)) {
      throw new IllegalArgumentException("Cannot add feature twice.");
    }

    this.viewListeners.add(feature);
  }
}
