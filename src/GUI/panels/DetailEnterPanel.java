package GUI.panels;

import Classes.*;
import GUI.comparator.DateComparatorUser;
import GUI.AppointmentDetail;
import GUI.comparator.AppointmentIDComparator;
import GUI.MainGUI;
import GUI.MobileNumberEnter;
import GUI.table.UserTableModel;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class DetailEnterPanel extends JPanel implements ActionListener, ItemListener, KeyListener {
    private JTextField name, surname, mobileNumber, address;
    private TextArea note;
    private JButton submit, reset, back, selectImagesB;
    private JLabel nameL, surnameL, mobileNumberL, addressL, dateL, numberOfHoursL, doctor, noteL, DOBL, selectImgL;
    private final JFrame container;
    private JComboBox<String> doctorDropDown, hoursDropDown;
    private final boolean isUpdate;
    private Consultation updateConsultation;
    private final UserTableModel userTableModel;
    private MobileNumberEnter mobileNumberEnter;
    private boolean mobileChecked, photoChanged = false;
    private final String[] imagesPaths = new String[3];
    private DatesPanel datesPanel;
    private final controllers.Patient patientController;
    private final controllers.Person personController;
    private final controllers.Consultation consultationController;
    private final controllers.Doctor doctorController;

    public DetailEnterPanel(boolean isUpdate, JFrame container, UserTableModel userTableModel) {

        //initializing controller
        patientController = new controllers.Patient();
        personController = new controllers.Person();
        consultationController = new controllers.Consultation();
        doctorController = new controllers.Doctor();

        //We initialized userTableModel for handle when event occur
        this.userTableModel = userTableModel;

        //By using this boolean we handle application methods and features to update data of booked consultations
        this.isUpdate = isUpdate;

        //If created detailEnterPanel is for using update a consultation, this method must execute first.
        if (isUpdate){
            //First of all we must get consultation class base on credentials,
            //For that we created MobileNumber class and get appointment id and mobileNumber. and we returned
            mobileNumberEnter =new MobileNumberEnter(true, false);

            //this container variable is initializing to eliminate the gui
            this.container = container;

            componentInitialize();
            addDetailsForm();
            container.dispose();

        }else {
            this.container = container;
            componentInitialize();
            addDetailsForm();
        }

        doctorDropDown.addItemListener(this);
        back.addActionListener(this);
        reset.addActionListener(this);
        submit.addActionListener(this);
        selectImagesB.addActionListener(this);
    }

    private void addDetailsForm(){
        /*
        * We used gridbaglayout for this panel and using this method, adding component into gui
        * */

        //this panel is the mother element used to add into frame
        JPanel panel = new JPanel(new BorderLayout());
        datesPanel = new DatesPanel();
        //setting title to display in the panel border
        setBorder(new TitledBorder("Make an appointment."));

        //this label will display in the above
        JLabel title = new JLabel("Enter Your Details Here", SwingConstants.CENTER),
                required = new JLabel("The spaces marked with * may be omitted.", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Serif", Font.BOLD, 20));

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panel.add(title, BorderLayout.NORTH);
        panel.add(required, BorderLayout.CENTER);
        panel.setBackground(Color.GREEN.darker());
        setBackground(Color.DARK_GRAY.darker());

        gbc.insets = new Insets(20,0,20,0);

        gbc.gridwidth = 4;
        gbc.gridheight =1;

        gbc.gridy =0;
        add(panel, gbc);

        gbc.insets = new Insets(10,30,10,30);
        gbc.gridwidth =1;
        gbc.gridx =0;
        gbc.gridy =1;
        add(doctor, gbc);

        gbc.gridx =1;
        add(doctorDropDown, gbc);

        gbc.gridx =0;
        gbc.gridy =2;
        add(nameL, gbc);

        gbc.gridx =1;
        add(name, gbc);

        gbc.gridx =0;
        gbc.gridy =3;
        add(surnameL, gbc);

        gbc.gridx =1;
        add(surname, gbc);

        gbc.gridx =0;
        gbc.gridy =4;
        add(addressL, gbc);

        gbc.gridx =1;
        add(address, gbc);

        gbc.gridx =0;
        gbc.gridy =5;
        add(mobileNumberL, gbc);

        gbc.gridx =1;
        add(mobileNumber, gbc);

        gbc.gridx =0;
        gbc.gridy =6;
        add(DOBL, gbc);

        gbc.gridx =1;
        gbc.gridheight = 2;
        add(datesPanel, gbc);

        gbc.gridheight = 1;
        gbc.gridx =0;
        gbc.gridy =7;
        add(dateL, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        add(numberOfHoursL, gbc);

        gbc.gridx = 1;
        add(hoursDropDown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        add(noteL, gbc);

        gbc.gridx = 1;
        add(note, gbc);

        gbc.gridx = 0;
        gbc.gridy = 11;
        add(selectImgL, gbc);

        gbc.gridx = 1;
        add(selectImagesB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 12;
        add(submit, gbc);

        gbc.gridx = 1;
        add(reset, gbc);

        gbc.insets = new Insets(10,30,30,30);
        gbc.gridwidth = 4;
        gbc.gridx = 0;
        gbc.gridy = 13;
        add(back, gbc);

        submit.addKeyListener(this);
        mobileNumber.addKeyListener(this);
        resetAllInputField();
    }


    private Consultation makeNewAppointment() throws ParseException {
        /*
        * Getting all input and validate it,
        * checking doctor available
        * checking patient exists
        * making a consultation
        * */
        boolean added=false;
        Consultation c = null;

        String doctorFullName = (String) doctorDropDown.getSelectedItem();
        //getting all names by capitalizing first letter of the word
        String patientFirstName = personController.nameCapitalising(name.getText());
        String patientSurName = personController.nameCapitalising(surname.getText());
        String addressString = address.getText();
        String mobileNumberString = mobileNumber.getText();
        String notes = note.getText();
        Date DOB_Date = datesPanel.getSelectedDOB();
        Date date2 = datesPanel.getSelectedDate();
        String time1 = Objects.requireNonNull(hoursDropDown.getSelectedItem()).toString().split(" - ")[0];

        //combining selected date and time into one date class
        Date date = consultationController.getExactDateAndTime(date2,time1);



        assert doctorFullName != null;
        try {
            if (

                //checking required fields are empty
                    personController.isEmptyAndOnlyLetters(patientFirstName,this) && personController.isEmptyAndOnlyLetters(patientSurName,this) &&
                            personController.isEmpty(addressString,this) &&
                            //is date of birth before month
                            personController.isDateOfBirthBeforeMonth(DOB_Date,this) &&
                    //is doctor selected and mobile checked
                            personController.isSelected(doctorFullName,this) && personController.checkMobile(mobileNumberString,this) &&
                    //is time selected and reserved time after an hour
                            personController.isSelected(time1,this) && mobileChecked && consultationController.isHourFromNow(date,this)

            ) {

                //finding doctor object by selected doctor name of dropdown by user
                Doctor doctorSelectByCustomer = doctorController.findDoctorObjectByFullName(doctorFullName);
                //finding doctor availability and if not available asking to add random doctor
                Doctor selectedDoctor = doctorController.randomlySelectADoctorIfUnavailable(doctorSelectByCustomer, date, 1, this);

                // if any other doctor is not available or user didn't agree to add another doctor will return null by randomlySelectADoctorIfUnavailable method
                if (selectedDoctor != null ) {

                    //using entered name and surname, checking is this previous customer or not
                    Patient patient = patientController.isCustomer(patientFirstName, patientSurName);

                    //if user is an old customer, isCustomer won't return null and will execute this. if not a customer before else will execute
                    if (patient != null) {
                        JOptionPane.showMessageDialog(this,"Patient Exists");

                        //if a previous customer asking for mobile number update is required or not, if say yes it will update
                        int yes = JOptionPane.showInternalConfirmDialog(null,"Need to update the previous mobile number to the new number just entered?");

                        if (yes==JOptionPane.YES_OPTION) patient.setMobileNumber(mobileNumberString);

                        //checking whether panel is using for update a consultation details or not
                        if (isUpdate) {
                            //assigning the object for use to update
                            c = updateConsultation;

                            //if previous images are selected, they must be deleted. Because new images will add by user
                            consultationController.fileDelete(c.getPaths());

                            //updating the object using this method
                            updateData(patientFirstName, patientSurName, addressString, mobileNumberString,
                                    DOB_Date, date, notes, selectedDoctor);

                            //it is an update or booking a consultation, setting the selected photo path is mandatory.
                            //checking photo updated and if it is these will attach to the object after coping images
                            // from file path to project paths
                            if (photoChanged){
                                consultationController.fileDelete(c.getPaths());
                            }
                        }

                        //when it's not an update, patient is also exists,
                        // making new consultation object base on 25 dollar cost
                        else {
                            c = new Consultation(date, 1, selectedDoctor, patient, notes);
                        }
                        System.out.println(c);

                        //it is an update or booking a consultation, setting the selected photo path is mandatory.
                        //checking photo updated and if it is these will attach to the object after coping images
                        // from file path to project paths
                        if (photoChanged) c.setPaths(consultationController.fileCopied(imagesPaths, c.getAppointmentID(), c.getPATIENT()));
                        added = true;
                    }
                    //if not a previous customer, this will execute
                    else {

                        //checking whether panel is using for update a consultation details or not
                        //if patient edited his name, he will add into consultation as a new user again
                        if (isUpdate) {
                            c = updateConsultation;

                            //if previous images are selected, they must be deleted. Because new images will add by user
                            consultationController.fileDelete(c.getPaths());

                            //updating the object using this method
                            updateData(patientFirstName, patientSurName, addressString, mobileNumberString, DOB_Date, date, notes, selectedDoctor);

                            //it is an update or booking a consultation, setting the selected photo path is mandatory.
                            //checking photo updated and if it is these will attach to the object after coping images
                            // from file path to project paths
                            if (photoChanged) c.setPaths(consultationController.fileCopied(imagesPaths, c.getAppointmentID(), c.getPATIENT()));
                        }
                        else {
                            //if user don't exist consultation will create by adding cost as 25 dollar
                            c = new Consultation(date, 1, selectedDoctor, patientFirstName, patientSurName, addressString, DOB_Date, notes);
                            try {
                                //adding mobile number. if mobile number wrong NumberFormatException will throw

                                c.getPATIENT().setMobileNumber(mobileNumberString);

                                //it is an update or booking a consultation, setting the selected photo path is mandatory.
                                //checking photo updated and if it is these will attach to the object after coping images
                                // from file path to project paths
                                if (photoChanged) c.setPaths(consultationController.fileCopied(imagesPaths, c.getAppointmentID(), c.getPATIENT()));
                                added = true;

                                //Auto generating id for patient
                                c.getPATIENT().setPatientID();
                            } catch (NumberFormatException e) {

                                //if mobile number wrong, created and stored all details must be fail.
                                // Then created details of consultation will delete
                                consultationController.fileDelete(c.getPaths());
                                consultationController.deleteConsultation(c);
                                JOptionPane.showMessageDialog(this, "The Mobile Number Is Wrong. Please enter correct one", "Warning", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                        System.out.println(c);
                    }
                }
                //if any doctor isn't available this will show up and won't submit the data
                else {
                    JOptionPane.showMessageDialog(this, "All doctors in that specialization category are reserved for that time range. Please select another time", "Warning", JOptionPane.INFORMATION_MESSAGE);
                }
                if (added) {
                    System.out.println(c.getPATIENT().toString() + "\n" + c.getDoctor().toString() + "\n" +
                            "Date :" + c.getDate() + "\n" +
                            "Appointment ID :" + c.getAppointmentID() + "\n" +
                            "Cost :" + c.getCost() + "\n" +
                            "Note :" + c.getNotes() + "\n\n");
                }
            }
            //checking mobile number is correct or not
            else if (!mobileChecked) {
                JOptionPane.showConfirmDialog(this, "Please enter again mobile your mobile number", "Warning", JOptionPane.OK_CANCEL_OPTION);
            }
        }
        //this will throw if mobile number is not a long
        catch (NumberFormatException e){
            JOptionPane.showConfirmDialog(this, "Please enter correct mobile number", "Warning", JOptionPane.OK_CANCEL_OPTION);
        } catch (IOException ignored) {
        }
        return c;
    }

    public void setAppointmentDetails(Consultation c) {
        /*
        * When this panel is using for update a consultation. this method will use to set valued into fields in GUI
        * */
        doctorDropDown.setSelectedItem(c.getDoctor().getName()+" "+c.getDoctor().getSurname());
        name.setText(c.getPATIENT().getName());
        surname.setText(c.getPATIENT().getSurname());
        if (c.getPATIENT().getAddress()==null) address.setText("");
        else address.setText(c.getPATIENT().getAddress());
        mobileNumber.setText(c.getPATIENT().getMobileNumber());
        datesPanel.setDOBValue(personController.getDateOfBirth(c.getPATIENT().getDateOfBirth()));
        datesPanel.setDateValue(c.getDate());
        hoursDropDown.setSelectedIndex(c.getHours());
        note.setText(c.getNotes());
    }

    public void setUpdateConsultation(Consultation updateConsultation) {
        this.updateConsultation = updateConsultation;
    }
    private void resetAllInputField(){
        /*
        * If it's an update or not mandatory. when reset button clicked this will happen
        * */
        doctorDropDown.setSelectedItem("None");
        name.setText("");
        surname.setText("");
        address.setText("");
        mobileNumber.setText("");
        datesPanel.setDOBValue(new Date());
        datesPanel.setDateValue(new Date());
        hoursDropDown.setSelectedIndex(0);
        note.setText("");
    }

    private void componentInitialize() {
        //By using this one, initializing required components for gui

        String[] doctorList = new String[Doctor.getAvailableNumOfDoctors()+1];

        Doctor[] d = WestminsterSkinConsultationManager.getDoctorList();
        doctorList[0] = "None";
        for (int i=0;i< d.length;i++) {
            if (d[i] !=null) doctorList[i+1] = d[i].getName()+" "+d[i].getSurname();
        }

        doctorDropDown = new JComboBox<>(doctorList);

        String[] hour = new String[]{
                "00 : 00 PM - 00 : 00 PM ",
                "01 : 30 PM - 02 : 30 PM", "02 : 30 PM - 03 : 30 PM",
                "03 : 30 PM - 04 : 30 PM", "04 : 30 PM - 05 : 30 PM",
                "05 : 30 PM - 06 : 30 PM", "06 : 30 PM - 07 : 30 PM",
                "07 : 30 PM - 08 : 30 PM", "08 : 30 PM - 09 : 30 PM",
                "09 : 30 PM - 10 : 30 PM", "10 : 30 PM - 11 : 30 PM",};
        hoursDropDown = new JComboBox<>(hour);

        name = new JTextField(10);
        surname = new JTextField(10);
        mobileNumber = new JTextField(10);
        address = new JTextField(10);
        note = new TextArea(3,2);
        nameL = new JLabel(         "First Name     :", SwingConstants.LEFT);
        surnameL = new JLabel(      "Surname        :", SwingConstants.LEFT);
        mobileNumberL = new JLabel( "Mobile Number  :", SwingConstants.LEFT);
        addressL = new JLabel(      "Address        :", SwingConstants.LEFT);
        dateL = new JLabel(         "Booking date   :", SwingConstants.LEFT);
        selectImgL = new JLabel(    "Select images* :", SwingConstants.LEFT);
        DOBL = new JLabel(          "Date Of Birth  :", SwingConstants.LEFT);
        submit = new JButton("Submit");
        reset = new JButton("Reset");
        back = new JButton("Back");
        selectImagesB = new JButton("Select Images");
        doctor = new JLabel(        "Doctor         :");
        noteL = new JLabel(         "Add a note*    :");
        numberOfHoursL = new JLabel("Reservation period :");

        submit.setBackground(Color.cyan.darker());
        reset.setBackground(Color.red);
        back.setBackground(Color.magenta.darker());
        submit.setForeground(Color.WHITE);
        reset.setForeground(Color.WHITE);
        back.setForeground(Color.WHITE);

        nameL.setForeground(Color.lightGray);
        surnameL.setForeground(Color.lightGray);
        mobileNumberL.setForeground(Color.lightGray);
        addressL.setForeground(Color.lightGray);
        dateL.setForeground(Color.lightGray);
        DOBL.setForeground(Color.lightGray);
        selectImgL.setForeground(Color.lightGray);
        numberOfHoursL.setForeground(Color.lightGray);
        doctor.setForeground(Color.lightGray);
        noteL.setForeground(Color.lightGray);

        nameL.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 12));
        DOBL.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 12));
        selectImgL.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 12));
        AppointmentDetail.fontInitializer(surnameL, mobileNumberL, noteL, addressL, 12);
        sameInitializing(dateL, numberOfHoursL, doctor);

    }

    static void sameInitializing(JLabel dateL, JLabel numberOfHoursL, JLabel doctor) {
        dateL.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 12));
        numberOfHoursL.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 12));
        doctor.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 12));

        UIManager.put("OptionPane.background", Color.lightGray);
        UIManager.put("Panel.background", Color.lightGray);
        UIManager.put("Panel.setForeground", Color.white);
    }

    private void updateData(String patientFirstName, String patientSurName, String addressString,
                            String mobileNumberString, Date DOB_Date, Date date,
                            String notes, Doctor selectedDoctor) {
        /*
        * parsing re entered value and updating the object that need to be updated
        * */
        Consultation uC = updateConsultation;
        Patient p =uC.getPATIENT();
        p.setName(patientFirstName);
        p.setSurname(patientSurName);
        p.setAddress(addressString);
        p.setMobileNumber(mobileNumberString);
        p.setDateOfBirth(DOB_Date);
        uC.setDoctor(selectedDoctor);
        uC.setHours(1);
        uC.setDate(date);
        uC.setNotes(notes);
    }

    private void jFileChooserPerform(){
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        jfc.removeChoosableFileFilter(jfc.getAcceptAllFileFilter());
        jfc.setDialogTitle("Multiple file and directory selection:");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Image files", ImageIO.getReaderFileSuffixes());
        jfc.addChoosableFileFilter(filter);
        jfc.setMultiSelectionEnabled(true);
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File[] files = jfc.getSelectedFiles();
            for (int i = 0; i < 3; i++) {
                if (files.length==i) break;
                imagesPaths[i] = files[i].getAbsolutePath();
            }
            if (files.length>3) JOptionPane.showMessageDialog(this,"Only first 3 files were added.");
        }
    }

    public MobileNumberEnter getMobileNumberEnter() {
        return mobileNumberEnter;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==submit) {
            if (isUpdate){
                try {
                    Consultation c =makeNewAppointment();
                    if (c!=null){
                        container.dispose();
                        new AppointmentDetail(c);
                    }
                } catch (ParseException ignored) {
                }

            } else {
                try {
                    Consultation c = makeNewAppointment();
                    if (c!=null) {
                        container.dispose();
                        new AppointmentDetail(c);
                    }
                    else {
                        JOptionPane.showMessageDialog(this, "No reservation was made", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (ParseException ignored) {
                }
            }
        }
        else if (e.getSource()== selectImagesB) {
            jFileChooserPerform();
            photoChanged=true;
        }
        else if (e.getSource()==back){
            container.dispose();
            try {
                new MainGUI();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource()==reset) {
            resetAllInputField();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource()==doctorDropDown){
            String item  = String.valueOf(doctorDropDown.getSelectedItem());
            if (item.trim().equals("None")) {
                ArrayList<Consultation> c = Consultation.getCONSULTATIONS();
                c.sort(new AppointmentIDComparator());

                userTableModel.setMyList(Consultation.getCONSULTATIONS());
            }else {

                ArrayList<Consultation> consultations = new ArrayList<>();

                for (Consultation selectedDoctorObject : Consultation.getCONSULTATIONS()) {
                    String text = selectedDoctorObject.getDoctor().getName() + " " +
                            selectedDoctorObject.getDoctor().getSurname();
                    if ((item).equals(text)) {
                        if (text.equals(item.trim())) {
                            consultations.add(selectedDoctorObject);
                        }
                    }
                }
                consultations.sort(new DateComparatorUser());

                userTableModel.setMyList(consultations);
            }

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar()!=KeyEvent.VK_BACK_SPACE && e.getKeyChar()!=KeyEvent.VK_ENTER) {
            if (!mobileNumber.getText().equals("")) {
                mobileChecked = consultationController.checkMobileNumber(mobileNumber, this, e);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
