package ui;

import javax.swing.*;
import java.awt.*;

public abstract class MyFrame extends JFrame
{
    public MyFrame(int width, int height)
    {
        setSize(new Dimension(width, height));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setPanel(JPanel panel)
    {
        JPanel contentPane = (JPanel) this.getContentPane();

        contentPane.removeAll();
        contentPane.add(panel);
        panel.setFocusable(true);
        panel.requestFocus();
        contentPane.revalidate();
        contentPane.repaint();
        this.pack();
    }
}
