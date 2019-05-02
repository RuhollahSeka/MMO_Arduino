package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by msi1 on 4/20/2018.
 */
public class MyPanel extends JPanel
{
    private UIHandler UIHandler;
    private MyPanel prevPanel;

    public MyPanel(int width, int height, MyPanel prevPanel, UIHandler UIHandler)
    {
        this.prevPanel = prevPanel;
        this.UIHandler = UIHandler;
        this.setSize(width, height);
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(null);
        this.setFocusable(true);
        this.requestFocus();
        this.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {

            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                System.out.println(e.getX() + ", " + e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {

            }

            @Override
            public void mouseEntered(MouseEvent e)
            {

            }

            @Override
            public void mouseExited(MouseEvent e)
            {

            }
        });
        this.setBackground(Color.WHITE);
    }

    public UIHandler getUIHandler()
    {
        return UIHandler;
    }

    public MyPanel getPrevPanel()
    {
        return prevPanel;
    }
}
