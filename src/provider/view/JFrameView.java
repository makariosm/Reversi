package cs3500.reversi.provider.view.graphical;

import cs3500.reversi.provider.controller.ViewFeatures;
import cs3500.reversi.provider.model.ROReversiModel;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.JPanel;
import javax.swing.JLabel;


/**
 * A graphics-based rendering of the Reversi game. This class utilizes Java Swing to act as the
 * main JFrame view of the game.
 */
public final class JFrameView extends JFrame implements GraphicalFrameView {
  private final AbstractGameBoardPanel gameBoardPanel;
  private final JLabel gameInfo;

  /**
   * Constructs a JFrameView object that sets up the frame, panel, listeners, and other elements
   * of the GUI based on the given unmodifiable model of the Reversi game.
   *
   * @param model the read-only model of the Reversi game
   * @throws IllegalArgumentException if the given model is <code>null</code>
   */
  public JFrameView(ROReversiModel model) throws IllegalArgumentException {
    super();

    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }

    setSize(800, 800);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    this.gameBoardPanel = new GameBoardPanel(model);
    this.gameBoardPanel.setPreferredSize(new Dimension(800, 800));
    getContentPane().add(this.gameBoardPanel, BorderLayout.CENTER);

    this.gameInfo = new JLabel();
    this.gameInfo.setHorizontalAlignment(JLabel.CENTER);
    JPanel gameStatusPanel = new JPanel();
    gameStatusPanel.add(this.gameInfo);
    getContentPane().add(gameStatusPanel, BorderLayout.NORTH);

    setTitle("Reversi");
    setVisible(true);
  }


  @Override
  public void addFeature(ViewFeatures feature) throws IllegalArgumentException {
    this.gameBoardPanel.addFeature(feature);
  }

  @Override
  public void setHotKey(KeyStroke key, String featureName) throws IllegalArgumentException {
    if (key == null) {
      throw new IllegalArgumentException("Key stroke cannot be null.");
    } else if (featureName == null) {
      throw new IllegalArgumentException("Feature name cannot be null.");
    }

    this.gameBoardPanel.getInputMap().put(key, featureName);
  }

  @Override
  public void updateView() {
    this.gameBoardPanel.removeAll();
    this.gameBoardPanel.revalidate();
    this.gameBoardPanel.repaint();
  }

  @Override
  public void displayMessage(String message) throws IllegalArgumentException {
    if (message == null) {
      throw new IllegalArgumentException("The message to display cannot be null.");
    }

    this.gameInfo.setText(message);
  }

  @Override
  public void displayErrorMessage(String message) throws IllegalArgumentException {
    if (message == null) {
      throw new IllegalArgumentException("The error message to display cannot be null.");
    }

    JOptionPane.showMessageDialog(this, message, "Error",
            JOptionPane.ERROR_MESSAGE);
  }
}
