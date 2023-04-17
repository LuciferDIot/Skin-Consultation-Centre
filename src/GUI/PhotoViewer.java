package GUI;

import Classes.Consultation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PhotoViewer extends JFrame implements ActionListener {
    Consultation c;
    JLabel[] jLabels = new JLabel[3];
    JButton ok;
    int pathCount =0;

    PhotoViewer(Consultation c) throws InterruptedException {
        this.c = c;
        componentInitializer();

        JPanel panel = new JPanel();

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.insets = new Insets(20,10,10,10);
        gbc.gridy = 0;
        if (pathCount>=1){
            gbc.gridx = 0;
            panel.add(jLabels[0], gbc);
        }

        if (pathCount>=2){
            gbc.gridx = 1;
            panel.add(jLabels[1], gbc);
        }

        gbc.gridy = 2;
        if (pathCount==3){
            gbc.gridx = 0;
            panel.add(jLabels[2], gbc);
        }

        gbc.gridx = 1;
        panel.add(ok, gbc);

        ok.addActionListener(this);

        panel.setBackground(Color.BLACK);
        add(panel);
        pack();
        setLocation(600,75);
        setVisible(true);
    }

    private void componentInitializer(){

        ok = new JButton("OK");

        String[] paths = c.getPaths();
        for (int i = 0; i < paths.length ; i++) {
            if (paths[i] != null) {
                JLabel displayField = new JLabel();
                displayField.setSize(280, 190);
                Image image = new ImageIcon(paths[i], "Image 0" + (i + 1)).getImage();
                Image imageScale = image.getScaledInstance(displayField.getWidth(), displayField.getHeight(), Image.SCALE_SMOOTH);
                displayField.setIcon(new ImageIcon(imageScale));

                pathCount++;
                jLabels[i] = displayField;
            }
        }

        ok.setBackground(Color.RED);
        ok.setForeground(Color.BLACK);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==ok) this.dispose();
    }
}
