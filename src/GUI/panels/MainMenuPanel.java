package GUI.panels;

import Classes.Consultation;
import GUI.comparator.AppointmentIDComparator;
import GUI.Login;
import GUI.MakeAppointment;
import GUI.MobileNumberEnter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class MainMenuPanel extends JPanel implements ActionListener{
    private JButton viewDetail, deleteConsultation, addConsultation, updateConsultation, close, back;
    private JLabel viewDetailL, deleteConsultationL, addConsultationL, updateConsultationL;
    private final JFrame container;
    private final controllers.Consultation consultationController;

    public MainMenuPanel(JFrame container) {
        consultationController = new controllers.Consultation();
        this.container = container;
        componentInitialize();
        addDetailsForm();

        viewDetail.addActionListener(this);
        deleteConsultation.addActionListener(this);
        addConsultation.addActionListener(this);
        updateConsultation.addActionListener(this);
        close.addActionListener(this);
        back.addActionListener(this);
    }

    public static JPanel getTopicLabel(){
        JLabel title = new JLabel("Choose Your Selection", SwingConstants.CENTER),
                required = new JLabel("Glowing skin is always in.", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Serif", Font.BOLD, 20));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(title, BorderLayout.NORTH);
        panel.add(required, BorderLayout.CENTER);
        panel.setBackground(Color.GREEN.darker());
        return panel;
    }
    private void addDetailsForm(){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        setBackground(Color.black);


        gbc.insets = new Insets(10,0,20,0);
        setPreferredSize(new Dimension(300,500));

        gbc.gridheight =1;

        gbc.insets = new Insets(10,30,0,0);
        gbc.gridwidth =1;
        gbc.gridy =1;
        gbc.gridx =0;
        add(addConsultationL, gbc);

        gbc.insets = new Insets(5,0,30,30);
        gbc.gridy =2;
        gbc.gridx =1;
        add(addConsultation, gbc);

        gbc.insets = new Insets(10,30,0,0);
        gbc.gridy =3;
        gbc.gridx =0;
        add(updateConsultationL, gbc);

        gbc.insets = new Insets(5,0,30,30);
        gbc.gridy =4;
        gbc.gridx =1;
        add(updateConsultation, gbc);

        gbc.insets = new Insets(10,30,0,0);
        gbc.gridy =5;
        gbc.gridx =0;
        add(deleteConsultationL, gbc);

        gbc.insets = new Insets(5,0,30,30);
        gbc.gridy =6;
        gbc.gridx =1;
        add(deleteConsultation, gbc);

        gbc.insets = new Insets(10,30,0,0);
        gbc.gridy =8;
        gbc.gridx =0;
        add(viewDetailL, gbc);

        gbc.insets = new Insets(5,0,30,30);
        gbc.gridy =9;
        gbc.gridx =1;
        add(viewDetail, gbc);

        gbc.insets = new Insets(10,30,5,30);

        gbc.gridwidth =2;
        gbc.gridy = 10;
        gbc.gridx =0;
        add(back, gbc);

        gbc.insets = new Insets(10,30,30,30);

        gbc.gridy = 11;
        add(close, gbc);
        close.setPreferredSize(new Dimension(407/2,630/2));

    }

    private void componentInitialize() {

        addConsultationL = new JLabel("Find a doctor :", SwingConstants.LEFT);
        updateConsultationL = new JLabel("Update Appointment :", SwingConstants.LEFT);
        deleteConsultationL = new JLabel("Delete Appointment :", SwingConstants.LEFT);
        viewDetailL = new JLabel("View your \nappointment :", SwingConstants.LEFT);
        viewDetail = new JButton("View");
        deleteConsultation = new JButton("Delete");
        addConsultation = new JButton("Book Now");
        updateConsultation = new JButton("Update");
        close = new JButton("Close");
        back = new JButton("Back");

        close.setBackground(Color.red);
        back.setBackground(Color.magenta.darker());
        viewDetail.setBackground(Color.lightGray);
        deleteConsultation.setBackground(Color.lightGray);
        addConsultation.setBackground(Color.lightGray);
        updateConsultation.setBackground(Color.lightGray);
        close.setForeground(Color.WHITE);
        back.setForeground(Color.WHITE);

        addConsultationL.setForeground(Color.lightGray);
        updateConsultationL.setForeground(Color.lightGray);
        deleteConsultationL.setForeground(Color.lightGray);
        viewDetailL.setForeground(Color.lightGray);
        addConsultationL.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 12));
        DetailEnterPanel.sameInitializing(updateConsultationL, deleteConsultationL, viewDetailL);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==viewDetail){
            container.dispose();
            new MobileNumberEnter(false, true);
        }

        else if (e.getSource()==addConsultation) {
            try {
                container.dispose();
                new MakeAppointment(false);
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        }

        else if (e.getSource() == deleteConsultation) {
            container.dispose();
            new MobileNumberEnter(false,true);
        }

        else if (e.getSource() == updateConsultation) {
            container.dispose();
            new MobileNumberEnter(true,false);
        }

        else if (e.getSource() == back) {
            try {
                consultationController.fileWrite();
                Consultation.setCONSULTATIONS(new ArrayList<>());
                System.out.println("Consultation list reset.");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            container.dispose();
            try {
                new Login();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        else if (e.getSource() == close) {
            try {
                Consultation.getCONSULTATIONS().sort(new AppointmentIDComparator());
                consultationController.fileWrite();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            System.exit(0);
        }
    }

}
