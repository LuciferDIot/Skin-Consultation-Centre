package GUI;

import Classes.Consultation;
import GUI.panels.DetailEnterPanel;
import GUI.table.UserTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.ParseException;

public class MakeAppointment extends JFrame implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
    private JPanel userTablePanel;
    private final DetailEnterPanel detailPanel;
    private UserTableModel userTableModel;

    public MakeAppointment(boolean isUpdate) throws IOException, ParseException {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0,0,0,0);

        componentInitialize();

        detailPanel = new DetailEnterPanel(isUpdate, this, userTableModel);
        gbc.insets = new Insets(30,30,30,10);
        gbc.gridheight = 2;
        gbc.gridy =1;
        gbc.gridx =0;
        panel.add(detailPanel, gbc);

        gbc.gridheight = 1;
        gbc.insets = new Insets(40,10,0,10);
        gbc.gridy =1;
        gbc.gridx =1;
        panel.add(instructions(), gbc);

        gbc.insets = new Insets(0,10,10,10);
        gbc.gridy =2;
        panel.add(userTablePanel,gbc);


        addMouseListener(this);
        addMouseMotionListener(this);

        add(panel);
        panel.setBackground(Color.BLACK);
        setUndecorated(true);
        pack();
        setLocation(200,75);
        setVisible(true);
    }


    private void componentInitialize() {
        userTablePanel = new JPanel();

        userTableModel = new UserTableModel(Consultation.getCONSULTATIONS());
        JTable userTable = new JTable(userTableModel);
        userTable.setRowHeight(30);
        userTable.setBackground(Color.BLACK);
        userTable.setForeground(Color.WHITE);
        userTable.getTableHeader().setBackground(Color.BLACK);
        userTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setPreferredSize(new Dimension(600,400));

        userTable.setFillsViewportHeight(true);
        scrollPane.setBackground(Color.BLACK);
        userTablePanel.setBackground(Color.BLACK);
        TableColumnModel columnModel = userTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(10);
        columnModel.getColumn(1).setPreferredWidth(80);
        columnModel.getColumn(2).setPreferredWidth(30);
        columnModel.getColumn(3).setPreferredWidth(40);
        columnModel.getColumn(4).setPreferredWidth(15);
        columnModel.getColumn(5).setPreferredWidth(80);
        columnModel.getColumn(6).setPreferredWidth(10);
        columnModel.getColumn(7).setPreferredWidth(10);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        userTable.setGridColor(Color.darkGray);
        columnModel.getColumn(0).setCellRenderer( centerRenderer );
        columnModel.getColumn(3).setCellRenderer( centerRenderer );
        columnModel.getColumn(2).setCellRenderer( centerRenderer );
        columnModel.getColumn(5).setCellRenderer( centerRenderer );
        columnModel.getColumn(6).setCellRenderer( centerRenderer );
        userTable.setRowHeight((scrollPane.getPreferredSize().height-20)/10);

        userTablePanel.add(scrollPane);
    }

    private JPanel instructions(){
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0,30,0,10);

        Label instruction = new Label("Instructions".toUpperCase(), Label.CENTER);
        Label ins1 = new Label("-----------------------------------------------------------------------------------------------------------------------------------------------", Label.LEFT);
        Label ins2 = new Label("01. When you updating your reservation, you must upload your photos again. if you access update option ", Label.LEFT);
        Label ins3 = new Label("      don't click submit button without uploading photos again. Otherwise the photo will be erased ", Label.LEFT);
        Label ins4 = new Label("      without knowing you.", Label.LEFT);

        Label ins5 = new Label("02. Please make sure to remember the Appointment ID and the phone number given because if you must ", Label.LEFT);
        Label ins6 = new Label("      update details or delete the appointment it will require.", Label.LEFT);

        Label ins7 = new Label("03. Please be remind, if you are updating the appointment when you submitting type again your ", Label.LEFT);
        Label ins8 = new Label("      phone number. Otherwise it will show \"Please enter correct mobile number\" . ", Label.LEFT);


        instruction.setForeground(Color.white);
        ins1.setForeground(Color.LIGHT_GRAY);
        ins2.setForeground(Color.LIGHT_GRAY);
        ins3.setForeground(Color.LIGHT_GRAY);
        ins4.setForeground(Color.LIGHT_GRAY);
        ins5.setForeground(Color.LIGHT_GRAY);
        ins6.setForeground(Color.LIGHT_GRAY);
        ins7.setForeground(Color.LIGHT_GRAY);
        ins8.setForeground(Color.LIGHT_GRAY);
        ins1.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 12));
        ins2.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 12));
        instruction.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 16));
        ins3.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 12));
        ins4.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 12));
        ins5.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 12));
        ins6.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 12));
        ins7.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 12));
        ins8.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 12));

        gbc.gridy =0;
        panel.add(ins1,gbc);

        gbc.insets = new Insets(10,30,10,10);
        gbc.gridy =1;
        panel.add(instruction,gbc);

        gbc.insets = new Insets(0,30,0,10);
        gbc.gridy =2;
        panel.setBackground(Color.BLACK);
        panel.add(ins2,gbc);

        gbc.gridy =3;
        panel.setBackground(Color.BLACK);
        panel.add(ins3,gbc);

        gbc.gridy =4;
        panel.setBackground(Color.BLACK);
        panel.add(ins4,gbc);

        gbc.insets = new Insets(5,30,0,10);
        gbc.gridy =5;
        panel.setBackground(Color.BLACK);
        panel.add(ins5,gbc);

        gbc.insets = new Insets(0,30,0,10);
        gbc.gridy =6;
        panel.setBackground(Color.BLACK);
        panel.add(ins6,gbc);

        gbc.insets = new Insets(5,30,0,10);
        gbc.gridy =7;
        panel.setBackground(Color.BLACK);
        panel.add(ins7,gbc);

        gbc.insets = new Insets(0,30,0,10);
        gbc.gridy =8;
        panel.setBackground(Color.BLACK);
        panel.add(ins8,gbc);


        return panel;
    }

    int mouseX;
    int mouseY;
    public DetailEnterPanel getDetailPanel() {
        return detailPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent ke) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        setLocation(getX()+(e.getX()-mouseX),getY()+(e.getY()-mouseY));
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
