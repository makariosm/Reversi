package cs3500.reversi.view;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.JPanel;
import javax.swing.AbstractAction;
import javax.swing.event.MouseInputAdapter;
import cs3500.reversi.controller.InputFeatures;
import cs3500.reversi.model.ReadOnlyReversi;
import cs3500.reversi.model.ReversiCell;
import cs3500.reversi.model.Square;

/**
 * The class that represents a square reversi panel of a square reversi game.
 * Constructs the view panel for a square reversi game .
 */
public class JSquareReversiPanel extends JPanel {

  private final ReadOnlyReversi model;

  private ReversiCell currSquareSelection;

  protected boolean alreadySelected;

  private final ReversiView view;

  private final List<InputFeatures> featureListeners = new ArrayList<>();

  /**
   * Constructs The view panel for a square reversi game .
   * @param model The read only model for a square model
   * @param view The reversi view that the panel works on
   */
  public JSquareReversiPanel(ReadOnlyReversi model, ReversiView view) {
    this.view = view;
    this.model = Objects.requireNonNull(model);
    MouseEventsListener listener = new MouseEventsListener();
    this.addMouseListener(listener);
    this.addMouseMotionListener(listener);

    this.alreadySelected = false;
    this.currSquareSelection =
            new Square(model.getGridSize(), model.getGridSize());

    // Install action command -> Feature callback associations
    this.getActionMap().put("pass", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        for (InputFeatures f : featureListeners) {
          f.controllerPass();
        }
      }
    });
    this.getActionMap().put("place", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        for (InputFeatures f : featureListeners) {
          f.controllerPlaceDisc();
        }
      }
    });
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setColor(Color.darkGray);
    Rectangle bounds = this.getBounds();
    g2d.fill(bounds);
    g2d.transform(transformSquareToPixel());
    for (int x = 0; x < model.getGridSize(); x++) {
      for (int y = 0; y < model.getGridSize(); y++) {
        this.drawSquareCell(g2d, x, y, this.currSquareSelection.equals(new Square(x, y))
                && !this.alreadySelected);
      }
    }
  }

  protected void drawSquareCell(Graphics2D g2d, double x, double y, boolean selected) {
    this.drawSquare(g2d, x, y, selected);
    switch (model.getStatus(new Square((int) x, (int) y))) {
      case White:
        this.drawCircle(g2d, Color.WHITE, x, y);
        break;
      case Black:
        this.drawCircle(g2d, Color.BLACK, x, y);
        break;
      default:
        break;
    }
  }

  private void drawSquare(Graphics2D g2d, double x, double y, boolean b) {
    AffineTransform oldTransform = g2d.getTransform();
    x = x / 2;
    y = y / 2;
    g2d.translate(x, y);
    Shape squareInProgress = new Rectangle2D.Double(x, y, 2, 2);
    if (!b) {
      g2d.setColor(Color.GRAY);
    } else {
      g2d.setColor(Color.CYAN);
    }
    g2d.fill(squareInProgress);
    g2d.setColor(Color.BLACK);
    g2d.setStroke(new BasicStroke(0.025F));
    g2d.draw(squareInProgress);
    g2d.setTransform(oldTransform);
  }

  protected void drawCircle(Graphics2D g2d, Color color, double x, double y) {
    AffineTransform oldTransform = g2d.getTransform();
    x = x / 2;
    y = y / 2;
    g2d.translate(x, y);
    g2d.setColor(color);
    Shape circle = new Ellipse2D.Double(
            x + 0.125,     // left
            y + 0.125,     // top
            0.75,  // width
            0.75); // height
    g2d.fill(circle);
    g2d.setTransform(oldTransform);
  }

  public void setCurrSquareSelection(ReversiCell square) {
    this.currSquareSelection = square;
  }

  public boolean getAlreadySelected() {
    return this.alreadySelected;
  }

  public void addFeatures(InputFeatures features) {
    this.featureListeners.add(features);
  }

  private AffineTransform transformSquareToPixel() {
    AffineTransform ret = new AffineTransform();
    Dimension preferred = getPreferredLogicalSize();

    ret.scale(getWidth() / preferred.getWidth(), getHeight() / preferred.getHeight());
    return ret;
  }

  private AffineTransform transformPixelToSquare() {
    AffineTransform ret = new AffineTransform();
    Dimension preferred = getPreferredLogicalSize();
    ret.scale(preferred.getWidth() /  getWidth(), preferred.getHeight() / getHeight());
    return ret;
  }

  private Dimension getPreferredLogicalSize() {
    return new Dimension(model.getGridSize(), model.getGridSize());
  }


  @Override
  public Dimension getPreferredSize() {
    return new Dimension(750, 750);
  }

  private class MouseEventsListener extends MouseInputAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {

      // This point is measured in actual physical pixels
      Point physicalP = e.getPoint();
      // For us to figure out which hex it belongs to, we need to transform it
      // into logical coordinates
      Point2D logicalP = transformPixelToSquare().transform(physicalP, null);
      Point2D logicalPUpdated = new Point2D.Double(logicalP.getX(), logicalP.getY());


      Square selectedSquare = new Square((int) logicalPUpdated.getX(),
              (int) logicalPUpdated.getY());

      System.out.println("(" + selectedSquare.getX() + ", " + selectedSquare.getY() + ")");

      if (JSquareReversiPanel.this.currSquareSelection.equals(selectedSquare)) {
        JSquareReversiPanel.this.alreadySelected = !JSquareReversiPanel.this.alreadySelected;
      } else {
        JSquareReversiPanel.this.alreadySelected = false;
      }

      JSquareReversiPanel.this.currSquareSelection = selectedSquare;
      JSquareReversiPanel.this.view.setCurrCellSelection(selectedSquare);
      JSquareReversiPanel.this.repaint();
    }
  }
}
