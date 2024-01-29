package cs3500.reversi.provider.view.graphical;

import java.awt.Graphics2D;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import javax.swing.AbstractAction;
import cs3500.reversi.provider.controller.ViewFeatures;
import cs3500.reversi.provider.model.Disc;
import cs3500.reversi.provider.model.Hexagon;
import cs3500.reversi.provider.model.ROReversiModel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Stroke;

/**
 * Represents a custom Java Swing panel that visually displays the current game board for Reversi.
 * The board to be displayed includes each individual hexagon that makes up the board along with
 * every disc that has been placed on the board.
 *
 * <p>For more information on hexagons and their coordinate system, see {@link Hexagon}.</p>
 * <p>For more information on discs, see {@link Disc}.</p>
 */
public final class GameBoardPanel extends AbstractGameBoardPanel {
  private Hexagon selectedHexagon;

  /**
   * Constructs a GameBoardPanel object that takes a model of the Reversi game.
   *
   * @param game the Reversi model
   * @throws IllegalArgumentException if the given Reversi model is <code>null</code>
   */
  public GameBoardPanel(ROReversiModel game) throws IllegalArgumentException {
    super(game);
    this.selectedHexagon = null;

    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        selectHexagon(mouseX, mouseY);

        removeAll();
        revalidate();
        repaint();
      }
    });

    this.getActionMap().put("setMove", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        for (ViewFeatures listener : viewListeners) {
          listener.makeMove(selectedHexagon);
          selectedHexagon = null;
        }
      }
    });

    this.getActionMap().put("setPass", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        for (ViewFeatures listener : viewListeners) {
          listener.pass();
        }
      }
    });
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    // set background color
    Color oldColor = g2d.getColor();
    g2d.setColor(new Color(64, 64, 64));
    g2d.fillRect(0, 0, getWidth(), getHeight());
    g2d.setColor(oldColor);

    for (Hexagon hexagon : this.game.getBoard().keySet()) {
      paintHexagon(g2d, hexagon);
      paintDisc(g2d, hexagon);
    }
  }

  /**
   * Selects a hexagon on the game board to be highlighted later. If the selected hexagon is
   * deselected if it is selected again, another hexagon is selected, or out of bounds selection
   * occurs. Note that only one hexagon can be selected at a time. In a Reversi game, a hexagon
   * would be selected/highlighted to show which hexagonal tile the player will make a move on.
   *
   * @param x the x position that has been selected
   * @param y the y position that has been selected
   */
  private void selectHexagon(int x, int y) {
    for (Hexagon hexagon : this.game.getBoard().keySet()) {
      if (buildHexagon(hexGridToPixelCoords(hexagon)).contains(x, y)) {
        if (this.game.getBoard().get(hexagon).hasDisc()) {
          this.selectedHexagon = null;
        } else if (this.selectedHexagon == null || !this.selectedHexagon.equals(hexagon)) {
          this.selectedHexagon = hexagon;
        } else {
          this.selectedHexagon = null;
        }

        System.out.printf("(%d, %d)\n", hexagon.getX(), hexagon.getY());
        return;
      }
    }

    this.selectedHexagon = null;
  }

  /**
   * Displays a visual view of the hexagon on the game board.
   *
   * @param g2d     the {@link Graphics2D} context to paint on
   * @param hexagon the hexagon to be displayed
   * @throws IllegalArgumentException if the given hexagon does not exist on the game board
   */
  private void paintHexagon(Graphics2D g2d, Hexagon hexagon) throws IllegalArgumentException {
    Disc disc = this.game.getDisc(hexagon);

    if (disc == null) {
      throw new IllegalArgumentException("Given hexagon does not exist in the game board.");
    }

    // convert hexagon coordinates to pixel coordinates
    Hexagon newHexagon = hexGridToPixelCoords(hexagon);

    // build the actual hexagon shape to display
    Path2D.Double hexLines = buildHexagon(newHexagon);

    // set stroke for displaying hexagon
    Color oldColor = g2d.getColor();
    Stroke oldStroke = g2d.getStroke();
    g2d.setColor(Color.BLACK);
    g2d.setStroke(new BasicStroke(3));
    g2d.draw(hexLines);
    g2d.setStroke(oldStroke);
    g2d.setColor(oldColor);

    // set color for displaying hexagon
    oldColor = g2d.getColor();
    // if the hexagon has been selected, highlight it in light blue
    g2d.setColor(hexagon.equals(this.selectedHexagon)
            ? new Color(117, 251, 253) : new Color(192, 192, 192));
    g2d.fill(hexLines);
    g2d.setColor(oldColor);
  }

  /**
   * Displays a visual view of the disc on the specified hexagon.
   *
   * @param g2d     the {@link Graphics2D} context to paint on
   * @param hexagon the hexagon representing the position where the disc is to be displayed
   * @throws IllegalArgumentException if the given hexagon is <code>null</code>
   */
  private void paintDisc(Graphics2D g2d, Hexagon hexagon) throws IllegalArgumentException {
    Disc disc = this.game.getDisc(hexagon);

    if (disc == null) {
      throw new IllegalArgumentException("Given hexagon does not exist in the game board.");
    } else if (!disc.hasDisc()) {
      return;
    }

    Hexagon discCoordinate = hexGridToPixelCoords(hexagon);
    double discDiameter = calcHexagonSideLength();

    AffineTransform oldTransform = g2d.getTransform();
    g2d.translate((getWidth() - discDiameter) / 2, (getHeight() - discDiameter) / 2);

    Color oldColor = g2d.getColor();
    g2d.setColor(disc.getDiscColor() == Disc.DiscColor.BLACK ? Color.BLACK : Color.WHITE);
    g2d.fillOval(discCoordinate.getX(), discCoordinate.getY(), (int) discDiameter,
            (int) discDiameter);
    g2d.setColor(oldColor);
    g2d.setTransform(oldTransform);

  }

  /**
   * Converts hexagonal coordinates from the hexagon grid to pixel coordinates that will specify
   * where any given hexagon would be located on display.
   *
   * <p>The origin - the hexagon at the center of the board - will be represented as (0, 0) in
   * pixel coordinates. Directional movement along the board in pixel coordinates will be similar
   * to that of hexagonal grid coordinates. More information on hexagonal grid coordinates can be
   * found at {@link Hexagon}.</p>
   *
   * @param hexagon the hexagon whose coordinates are to be converted
   * @return a new hexagon with properly converted coordinates
   * @throws IllegalArgumentException if the given hexagon is <code>null</code>
   */
  private Hexagon hexGridToPixelCoords(Hexagon hexagon) throws IllegalArgumentException {
    if (hexagon == null) {
      throw new IllegalArgumentException("Hexagon doesn't exist.");
    }

    double hexagonSideLength = calcHexagonSideLength();
    int newX = (int) ((2 * hexagon.getX() + hexagon.getY()) * (Math.cos(Math.PI / 6))
            * hexagonSideLength);

    // our y coordinate value increases bottom to top in our hexagonal grid coordinate system;
    // since the pixel coordinate system increases the y coordinate value top to bottom, we must
    // multiply newY here by -1 to account for this factor
    int newY = (int) -(3 * hexagon.getY() * (Math.sin(Math.PI / 6)) * hexagonSideLength);
    return new Hexagon(newX, newY);
  }

  /**
   * Calculates the side length of a hexagon relative to the dimensions of the panel.
   * This creates a hexagon with a side length such that it leaves an allowance on all sides of the
   * grid.
   *
   * @return hexagon side length
   */
  private double calcHexagonSideLength() {
    return 2.0 * Math.min(getWidth(), getHeight())
            / (3 * (2 * this.game.getGameBoardSideLength() + 1));
  }

  /**
   * Builds a hexagon shape based on the provided hexagon. The provided hexagon's coordinates
   * (assumed to be in pixel coordinates) are treated as the very center of the hexagon.
   *
   * @param hexagon the center pixel coordinates of the actual hexagon shape to be built
   * @return a hexagon shape represented by a {@link Path2D.Double}
   * @throws IllegalArgumentException if the given hexagon is <code>null</code>
   */
  private Path2D.Double buildHexagon(Hexagon hexagon) throws IllegalArgumentException {
    if (hexagon == null) {
      throw new IllegalArgumentException("Hexagon doesn't exist.");
    }

    Path2D.Double hexLines = new Path2D.Double();
    double hexagonSideLength = calcHexagonSideLength();

    // gets corner coordinates of hexagon to build
    for (int i = 0; i < 6; i++) {
      double theta = Math.PI / 3;
      double newX = (hexagon.getX() + hexagonSideLength * Math.cos(theta * i + Math.PI / 6))
              + (getWidth() * 0.5);
      double newY = (hexagon.getY() + hexagonSideLength * Math.sin(theta * i + Math.PI / 6))
              + (getHeight() * 0.5);

      // move to starting point on the first loop
      if (i == 0) {
        hexLines.moveTo(newX, newY);
      } else {
        hexLines.lineTo(newX, newY);
      }
    }

    hexLines.closePath();
    return hexLines;
  }
}
