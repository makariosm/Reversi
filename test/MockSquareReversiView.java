import javax.swing.JFrame;
import javax.swing.KeyStroke;
import cs3500.reversi.controller.InputFeatures;
import cs3500.reversi.model.ReadOnlyReversi;
import cs3500.reversi.model.ReversiCell;
import cs3500.reversi.view.ReversiView;

/**
 * Class that represents the view mock for a square reversi game.
 */
public class MockSquareReversiView extends JFrame implements ReversiView {

  public final ReversiPanelMock panel;

  /**
   * Constructs mock of reversi view in a reversi game. Used to mock view and reveal functionality.
   * @param model the read only model that the view uses to create the grid and its contents.
   */
  public MockSquareReversiView(ReadOnlyReversi model) {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.panel = new ReversiPanelMock(model);
    this.add(panel);
    this.pack();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void display(boolean show) {
    this.setVisible(show);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPanelCurrCell(ReversiCell hex) {
    // not needed
  }

  /**
   * Get the current hex that was selected aka the hex that got clicked on and pressed enter.
   *
   * @return the current hex that has been selected
   */
  @Override
  public ReversiCell getCurrCellSelection() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean getAlreadySelected() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showErrorDialogue(String s) {
    // not needed
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showTurnDialogue() {
    // not needed

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showGameOverDialogue(String s) {
    // not needed
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setHotKey(KeyStroke key, String featureName) {
    // not needed
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addFeatures(InputFeatures features) {
    // not needed
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addTitle(String s) {
    // not needed
  }

  /**
   * Sets the current hex that's been clicked on and pressed enter on in the view.
   *
   * @param selectedHex The selected hex
   */
  @Override
  public void setCurrCellSelection(ReversiCell selectedHex) {
    // not needed
  }



  /**
   * {@inheritDoc}
   */
  @Override
  public void repaint() {
    super.repaint();
  }


}
