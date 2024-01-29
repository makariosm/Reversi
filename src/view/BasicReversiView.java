package cs3500.reversi.view;

import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import cs3500.reversi.controller.InputFeatures;
import cs3500.reversi.model.ReadOnlyReversi;
import cs3500.reversi.model.ReversiCell;

/**
 * Implementation of a view for Reversi that opens a Java Swing window that displays a GUI for th.
 * game.
 */
public class BasicReversiView extends JFrame implements ReversiView {

  private final JHexHintsReversiPanel panel;
  private ReversiCell currHexSelection;

  /**
   * Constructs a BasicReversiView.
   * @param model the model that the view is displaying.
   */
  public BasicReversiView(ReadOnlyReversi model) {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.panel = new JHexHintsReversiPanel(model, this);
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
  public void setCurrCellSelection(ReversiCell cell) {
    this.currHexSelection = cell;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPanelCurrCell(ReversiCell cell) {
    this.panel.setCurrHexSelection(cell);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ReversiCell getCurrCellSelection() {
    return this.currHexSelection;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean getAlreadySelected() {
    return this.panel.getAlreadySelected();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void repaint() {
    this.panel.repaint();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showErrorDialogue(String s) {
    JOptionPane.showMessageDialog(this, s, "Invalid Play",
            JOptionPane.ERROR_MESSAGE);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showTurnDialogue() {
    JOptionPane.showMessageDialog(this, "It's your turn!", "Hooray!",
            JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showGameOverDialogue(String s) {
    JOptionPane.showMessageDialog(this, s,
            "Game is Over!", JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setHotKey(KeyStroke key, String featureName) {
    this.panel.getInputMap().put(key, featureName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addFeatures(InputFeatures features) {
    this.panel.addFeatures(features);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addTitle(String s) {
    this.setTitle(s);
    this.setLocationRelativeTo(getComponentAt(new Point(0,0)));
  }

}
