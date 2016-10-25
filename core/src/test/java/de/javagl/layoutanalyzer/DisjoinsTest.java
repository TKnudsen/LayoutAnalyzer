package de.javagl.layoutanalyzer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.javagl.layoutanalyzer.utils.Disjoins;

public class DisjoinsTest
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(
            () -> createAndShowGUI());
    }

    private static void createAndShowGUI()
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setLayout(new BorderLayout());
        DisjoinsTestPanel disjoinsTestPanel = new DisjoinsTestPanel();
        f.getContentPane().add(disjoinsTestPanel, BorderLayout.CENTER);
        f.setSize(1000,800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}

class DisjoinsTestPanel extends JPanel implements MouseMotionListener
{
    private Rectangle2D rectangle0;
    private Rectangle2D rectangle1;
    
    DisjoinsTestPanel()
    {
        addMouseMotionListener(this);
        
        rectangle0 = new Rectangle2D.Double(300, 300, 200, 100);
        rectangle1 = new Rectangle2D.Double(200, 200, 100, 200);
    }
    
    @Override
    protected void paintComponent(Graphics gr)
    {
        super.paintComponent(gr);
        Graphics2D g = (Graphics2D)gr;
        
        g.setColor(Color.BLUE);
        g.draw(rectangle0);

        g.setColor(Color.BLACK);
        g.draw(rectangle1);
        
        double disjoinX = Disjoins.computeMinDisjoinMovement(
            rectangle0.getMinX(), rectangle0.getMaxX(), 
            rectangle1.getMinX(), rectangle1.getMaxX());
        double disjoinY = Disjoins.computeMinDisjoinMovement(
            rectangle0.getMinY(), rectangle0.getMaxY(), 
            rectangle1.getMinY(), rectangle1.getMaxY());
        Point2D disjoin = Disjoins.computeMinDisjoinMovement(rectangle0, rectangle1, null);
        
        System.out.println(disjoinX+", "+disjoinY+": Result "+disjoin);
        
        Rectangle2D r = new Rectangle2D.Double(
            rectangle0.getX() + disjoin.getX(), rectangle0.getY()+disjoin.getY(),
            rectangle0.getWidth(), rectangle0.getHeight());
        
        g.setColor(Color.GREEN);
        g.draw(r);
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        rectangle0.setRect(e.getX(), e.getY(), rectangle0.getWidth(), rectangle0.getHeight());
        repaint();
    }
    
    
    
}
