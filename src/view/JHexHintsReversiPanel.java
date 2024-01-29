package cs3500.reversi.view;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.BasicStroke;
import java.awt.event.ActionEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cs3500.reversi.controller.InputFeatures;
import cs3500.reversi.model.DiscStatus;
import cs3500.reversi.model.Hex;
import javax.swing.AbstractAction;
import cs3500.reversi.model.ReadOnlyReversi;
import cs3500.reversi.model.ReversiCell;

/**
 * The decorator class for the hints feature of the hex reversi view.
 * Enables the hints features
 */
public class JHexHintsReversiPanel extends JReversiPanel {

  private boolean showHints;

  /**
   * Constructs a JReversiPanel.
   * @param model the model of the game of Reversi that is displayed in the panel
   * @param view  the parent view of the JPanel.
   */
  public JHexHintsReversiPanel(ReadOnlyReversi model, ReversiView view) {
    super(model, view);
    this.showHints = false;
    this.getActionMap().put("hint", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        for (InputFeatures f : featureListeners) {
          toggleHints();
          repaint();
        }
      }
    });
  }

  private void toggleHints() {
    this.showHints = !showHints;
  }

  private String hintString() {
    Map<ReversiCell, Integer> movesAndScores = new HashMap<>();

    // for every cell in the grid
    for (ReversiCell h : model.getCopyOfGrid().keySet()) {

      // get adjacent opposite cells
      List<ReversiCell> loAdjacentOpposite = model.getAdjacentOpposite(h);

      int currHexScore = 0;
      // FOR ONE HEX
      // for every adjacent opposite cell to the hex (every path) most likely 1 or 2 only
      // add up score
      for (int i = 0; i < loAdjacentOpposite.size() - 1; i++) {
        // for every existing adjacent opponent hex
        if (loAdjacentOpposite.get(i) != null) {
          ReversiCell hex = loAdjacentOpposite.get(i);
          if (i == 0) {
            currHexScore += model.numOppositeHexes(hex, 1, -1, 0);
          } else if (i == 1) {
            currHexScore += model.numOppositeHexes(hex, 1, 0, -1);
          } else if (i == 2) {
            currHexScore += model.numOppositeHexes(hex, 0, 1, -1);
          } else if (i == 3) {
            currHexScore += model.numOppositeHexes(hex, -1, 1, 0);
          } else if (i == 4) {
            currHexScore += model.numOppositeHexes(hex, -1, 0, 1);
          } else if (i == 5) {
            currHexScore += model.numOppositeHexes(hex, 0, -1, 1);
          }
        }
      }
      movesAndScores.put(h, currHexScore);
    }

    int newQ = this.getCurrHexSelection().getQ();
    int newR = this.getCurrHexSelection().getR();
    int newS = this.getCurrHexSelection().getS();

    int result = movesAndScores.get(new Hex(newQ, newR, newS));

    return String.valueOf(result);
  }

  protected void drawHint(Graphics2D g2d, double q, double r) {

    if (this.showHints && model.getStatus(this.currHexSelection) == DiscStatus.Empty) {
      AffineTransform oldTransform = g2d.getTransform();
      q = q / 2;
      r = r / 2;
      g2d.translate(q, r);
      q = q + r;
      if (r > 0) {
        r = (r - 2 * (Math.abs(r) * 0.1374));
      } else if (r < 0) {
        r = (r + 2 * (Math.abs(r) * 0.1374));
      }

      FontRenderContext frc = g2d.getFontRenderContext();
      Font font1 = new Font("Courier", Font.PLAIN, 1);
      Font smallerFont = font1.deriveFont(0.5F);

      String str1 = hintString(); // PUT NUM OPPOSITE HEXES HERE INSTEAD OF 0

      TextLayout tl = new TextLayout(str1, smallerFont, frc);
      g2d.setColor(Color.BLACK);
      if (Math.abs(this.currHexSelection.getR()) >= 0 && this.currHexSelection.getR() > 0) {
        q = Math.min(q - (0.1374 * Math.abs(this.currHexSelection.getR())), q - 0.1374);
        if (Math.abs(this.currHexSelection.getR()) >= 1) {
          q = q - ((0.5 * 0.1374) * Math.abs(this.currHexSelection.getR()));
          r = r + ((0.5 * 0.1374) * Math.abs(this.currHexSelection.getR()));
        }
      } else if (Math.abs(this.currHexSelection.getR()) >= 2 && this.currHexSelection.getR() < 0) {
        q = q + (0.1374 * Math.abs(this.currHexSelection.getR()));
      }
      tl.draw(g2d, (float) q, (float) r);
      g2d.setTransform(oldTransform);
    }
  }

  @Override
  protected void drawHexagon(Graphics2D g2d, double x, double y, boolean selected) {
    AffineTransform oldTransform = g2d.getTransform();
    x = x / 2;
    y = y / 2;
    g2d.translate(x, y);
    x = x + y;
    if (y > 0) {
      y = (y - 2 * (Math.abs(y) * 0.1374));
    } else if (y < 0) {
      y = (y + 2 * (Math.abs(y) * 0.1374));
    }
    Path2D hexagonInProgress = new Path2D.Double();
    hexagonInProgress.moveTo(x, y - 0.577);
    hexagonInProgress.lineTo(x + 0.5, y - 0.2887);
    hexagonInProgress.lineTo(x + 0.5, y + 0.2887);
    hexagonInProgress.lineTo(x, y + 0.577);
    hexagonInProgress.lineTo(x - 0.5, y + 0.2887);
    hexagonInProgress.lineTo(x - 0.5, y - 0.2887);
    hexagonInProgress.lineTo(x, y - 0.577);
    if (!selected) {
      g2d.setColor(Color.GRAY);
      g2d.fill(hexagonInProgress);
    } else {
      g2d.setColor(Color.CYAN);
      g2d.fill(hexagonInProgress);
      g2d.setColor(Color.BLACK);
      this.drawHint(g2d, x, y);

    }
    g2d.setColor(Color.BLACK);
    g2d.setStroke(new BasicStroke(0.025F));
    g2d.draw(hexagonInProgress);
    g2d.setTransform(oldTransform);
  }

}
