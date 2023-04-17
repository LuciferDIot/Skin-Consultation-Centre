package GUI;

import Classes.Consultation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileNumberEnter extends JFrame implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
    private final boolean isView;
    private String mobileNumber;
    private JButton confirm, close;
    private JTextField mobile,appointmentIDField, patientIDField;
    private boolean mobileChecked, appointmentIDChecked, patientIDChecked;
    private final boolean isUpdate;
    private final controllers.Consultation consultationController;
    private final controllers.Person personController;
    private final controllers.Patient patientController;

    public MobileNumberEnter(boolean isUpdate, boolean isView){
        this.consultationController = new controllers.Consultation();
        this.personController = new controllers.Person();
        this.patientController = new controllers.Patient();
        this.isView = isView;
        this.isUpdate = isUpdate;
        mobileChecked = false;
        appointmentIDChecked = false;
        patientIDChecked = false;

        JPanel panel = makePanel();
        add(panel);

        mobile.addKeyListener(this);
        appointmentIDField.addKeyListener(this);
        patientIDField.addKeyListener(this);
        close.addActionListener(this);
        confirm.addActionListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        panel.setBackground(Color.BLACK);
        setUndecorated(true);
        pack();
        setLocation(600,75);
        setVisible(true);
    }

    private JPanel makePanel(){
        JLabel label = new JLabel("Enter Your Mobile Number to proceed", JLabel.CENTER);
        JLabel mobileL = new JLabel("Mobile Number  :");
        JLabel appointment = new JLabel("Appointment ID :");
        JLabel patient = new JLabel("Patient ID :");
        confirm = new JButton("Confirm");
        close = new JButton("Back");
        mobile = new JTextField(10);
        appointmentIDField = new JTextField(10);
        patientIDField = new JTextField(10);


        mobileL.setForeground(Color.lightGray);
        appointment.setForeground(Color.lightGray);
        label.setForeground(Color.lightGray);
        patient.setForeground(Color.lightGray);
        label.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 14));
        confirm.setBackground(Color.cyan.darker());
        close.setBackground(Color.red);
        confirm.setForeground(Color.WHITE);
        close.setForeground(Color.WHITE);


        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20,30,20,30);

        gbc.gridwidth =2;
        gbc.gridy=0;
        gbc.gridx =0;
        panel.add(label,gbc);

        gbc.insets = new Insets(10,30,10,30);
        gbc.gridwidth =1;
        gbc.gridy=1;
        gbc.gridx =0;
        panel.add(appointment,gbc);

        gbc.gridx =1;
        panel.add(appointmentIDField,gbc);

        gbc.gridy=2;
        gbc.gridx =0;
        panel.add(patient,gbc);

        gbc.gridx =1;
        panel.add(patientIDField,gbc);

        gbc.gridy=3;
        gbc.gridx =0;
        panel.add(mobileL,gbc);

        gbc.gridx =1;
        panel.add(mobile,gbc);

        gbc.insets = new Insets(10,30,30,30);
        gbc.gridwidth =1;
        gbc.gridy=4;
        gbc.gridx =0;
        panel.add(confirm,gbc);

        gbc.gridx =1;
        panel.add(close,gbc);

        return panel;
    }

    private void clickedConfirmed(){
        //assigning mobile number
        mobileNumber = mobile.getText().trim();


        //
        appointmentIDChecked =
                consultationController.checkConsultationByAppointmentID(appointmentIDField, patientIDField, this);
        mobileChecked = personController.checkMobile(mobileNumber);
        patientIDChecked = patientController.checkPatientID(patientIDField.getText().trim(), this);


        if (mobileChecked && appointmentIDChecked && patientIDChecked){
            if (personController.checkMobile(mobileNumber)){
                if (isUpdate) {
                    try {
                        Consultation c = findConsultation();
                        if (c!=null){
                            MakeAppointment m = new MakeAppointment(true);
                            m.getDetailPanel().setUpdateConsultation(c);
                            m.getDetailPanel().setAppointmentDetails(c);
                            m.getDetailPanel().getMobileNumberEnter().dispose();
                            this.dispose();
                        }

                    } catch (IOException | ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (isView) {
                    Consultation c = findConsultation();
                    try {
                        if (c!=null) {
                            new AppointmentDetail(c);
                            this.dispose();
                        }
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        } else if (!mobileChecked) {
            JOptionPane.showConfirmDialog(this, "The mobile phone number should contain 10 digits.", "Warning", JOptionPane.OK_CANCEL_OPTION);
        } else if (!appointmentIDChecked) {
            JOptionPane.showConfirmDialog(this, "Appointment id field not meet requirements", "Warning", JOptionPane.OK_CANCEL_OPTION);
        }else {
            JOptionPane.showConfirmDialog(this, "Patient id field not meet requirements", "Warning", JOptionPane.OK_CANCEL_OPTION);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==close) {
            this.dispose();
            try {
                new MainGUI();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if (e.getSource() == confirm) {
            if (mobile.getText().equals("")||
            appointmentIDField.getText().equals("")||
            patientIDField.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Required field must be empty");
            }else clickedConfirmed();
        }

    }
    public Consultation findConsultation(){
        this.mobileNumber = mobile.getText().trim();
        int appointmentIDText = Integer.parseInt(appointmentIDField.getText().trim());
        int patientIDText = Integer.parseInt(patientIDField.getText().trim());
        System.out.println(mobileNumber+" "+appointmentIDText+" "+patientIDText);
        return consultationController.findConsultation(patientIDText, appointmentIDText, mobileNumber, this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        JTextField field = (JTextField) e.getSource();
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(String.valueOf(e.getKeyChar()));
        Matcher m1 = p.matcher(field.getText());
        if (e.getKeyChar()!=KeyEvent.VK_BACK_SPACE){
            if (!m.matches() && !m1.matches()) {
                JOptionPane.showConfirmDialog(this, "* Enter only numeric digits(0-9)", "Warning", JOptionPane.OK_CANCEL_OPTION);
                if (e.getSource() == appointmentIDField) {
                    appointmentIDField.setText("");
                } else if (e.getSource() == patientIDField) {
                    patientIDField.setText("");
                } else if (e.getSource() == mobile) {
                    mobile.setText("");
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    int mouseX, mouseY;

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
