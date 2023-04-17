package GUI;

import Classes.Doctor;
import Classes.WestminsterSkinConsultationManager;
import GUI.comparator.AlphabeticComparatorDoctor;
import GUI.comparator.ComparatorDoctorID;
import GUI.panels.MainMenuPanel;
import GUI.table.DoctorTableModel;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class MainGUI extends JFrame implements MouseListener, MouseMotionListener, ActionListener {


    private JButton alphabeticallyInitialize;
    private JLabel label;
    private DoctorTableModel tableModel;
    private boolean isAlphabetic;
    public MainGUI() throws IOException{
        this.isAlphabetic = false;
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;


        JPanel doctorTablePanel = doctorDetailsTable();
        JPanel mainButtonPanel = new MainMenuPanel(this);

        gbc.insets = new Insets(10,30,0,30);
        gbc.gridwidth =2;
        gbc.gridy =0;
        gbc.gridx =0;
        panel.add(MainMenuPanel.getTopicLabel(), gbc);

        gbc.gridwidth =1;
        gbc.insets = new Insets(0,30,10,0);
        gbc.gridy =1;
        gbc.gridx =0;
        panel.add(mainButtonPanel, gbc);

        gbc.insets = new Insets(0,0,10,30);
        gbc.gridx =1;
        panel.add(doctorTablePanel,gbc);

        alphabeticallyInitialize.addActionListener(this);

        panel.setBorder(new TitledBorder(new EtchedBorder(Color.WHITE,Color.GRAY), "Main GUI"));
        addMouseListener(this);
        addMouseMotionListener(this);
        add(panel);
        panel.setBackground(Color.BLACK);
        setUndecorated(true);
        pack();
        setLocation(350,75);
        setVisible(true);
    }


    private JPanel doctorDetailsTable() {
        label = new JLabel("Doctor names sort alphabetically :");
        label.setPreferredSize(new Dimension(350,15));
        alphabeticallyInitialize = new JButton("Enter");

        alphabeticallyInitialize.setBackground(Color.RED);
        alphabeticallyInitialize.setForeground(Color.WHITE);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Verdana", Font.PLAIN | Font.ITALIC, 14));

        JPanel mainPanel = new JPanel();


        tableModel = new DoctorTableModel();
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500,300));

        table.setBackground(Color.BLACK);
        table.setForeground(Color.WHITE);

        table.getTableHeader().setBackground(Color.BLACK);
        table.getTableHeader().setForeground(Color.WHITE);

        table.setFillsViewportHeight(true);
        scrollPane.setBackground(Color.BLACK);
        mainPanel.setBackground(Color.BLACK);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(10);
        columnModel.getColumn(4).setPreferredWidth(10);
        columnModel.getColumn(1).setMinWidth(60);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        columnModel.getColumn(0).setCellRenderer( centerRenderer );
        columnModel.getColumn(4).setCellRenderer( centerRenderer );
        columnModel.getColumn(2).setCellRenderer( centerRenderer );
        table.setGridColor(Color.darkGray);

        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width,scrollPane.getPreferredSize().height+45));
        table.setRowHeight((scrollPane.getPreferredSize().height-20)/10);

        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0,30,0,10);

        gbc.gridy =0;
        gbc.gridx =0;
        mainPanel.add(label, gbc);

        gbc.gridx =1;
        mainPanel.add(alphabeticallyInitialize, gbc);
        gbc.insets = new Insets(20,30,0,10);
        gbc.gridwidth = 2;
        gbc.gridy =1;
        gbc.gridx =0;
        mainPanel.add(scrollPane, gbc);

        return mainPanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    int mouseX, mouseY;

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()== alphabeticallyInitialize) {
            if (!isAlphabetic) {
                ArrayList<Doctor> d = tableModel.getMyList();
                d.sort(new AlphabeticComparatorDoctor());
                tableModel.fireTableDataChanged();
                label.setText("Sort by appointment number :");
                alphabeticallyInitialize.setBackground(Color.orange);
                isAlphabetic = true;
            }else {
                Doctor[] consultations = WestminsterSkinConsultationManager.getDoctorList();
                ArrayList<Doctor> doctors = new ArrayList<>();
                for (Doctor consultation : consultations) {
                    if (consultation != null) doctors.add(consultation);
                }

                doctors.sort(new ComparatorDoctorID());
                tableModel.setMyList(doctors);
                tableModel.fireTableDataChanged();

                label.setText("Doctor names sort alphabetically :");
                alphabeticallyInitialize.setBackground(Color.RED);
                isAlphabetic = false;
            }
        }
    }
}
