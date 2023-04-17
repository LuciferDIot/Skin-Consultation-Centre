package controllers;

import Classes.Doctor;
import Classes.Patient;
import Classes.encryption.TripleDes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Consultation {
    private final controllers.Doctor doctorController;

    public Consultation() {
        doctorController = new controllers.Doctor();
    }

    public Date getEndTime(Date date, int hours) throws ParseException {
        /*
         * returning end time belongs to given date after given hours.
         * */
        SimpleDateFormat parser = new SimpleDateFormat("yyyy:MM:dd:hh:mm aa");
        //Parsing date as given pattern cause of must-have that pattern to use in calendar class
        Date myDate = parser.parse(parser.format(date));
        Calendar cal =Calendar.getInstance();
        cal.setTime(myDate);

        //By this method hours will add to the calendar
        cal.add(Calendar.HOUR_OF_DAY,hours);
        return cal.getTime();
    }
    public Classes.Consultation checkDoctorAvailable(Doctor doctor, Date date , int hours) throws ParseException {
        /*
         * Checking doctor availability based on booked time periods of all consultations of that doctor
         * */
        Date endTime  = getEndTime(date,hours);

        for (Classes.Consultation c: Classes.Consultation.getCONSULTATIONS()){

            //checking given doctor equals to doctor allocated consultation
            if (doctor.equals(c.getDoctor())){

                //checking given date and time after to the consultation date and time of the doctor
                if (date.after(c.getDate())){
                    System.out.println(date+" is behind the "+c.getPATIENT().getName()+"'s time.");

                    //checking given date and time before to the consultation ending date and time of the doctor
                    if (date.before(c.getEndTime())) {
                        System.out.println(" end time :"+c.getEndTime()+" -"+c.getPATIENT().getName()+"'s time isn't end before this appointment start.");
                        System.out.println();
                        return c;
                    }
                }
                //checking given date and time before to the consultation date and time of the doctor
                else if (date.before(c.getDate())) {
                    System.out.println(endTime+" (End)"+c.getPATIENT().getName()+"'s time is behind the time.");

                    //checking ending date and time of the given date and time after to the consultation date and time of the doctor
                    if (endTime.after(c.getDate())) {
                        System.out.println(" starting time :"+c.getEndTime()+c.getPATIENT().getName()+"'s time is starting before this appointment end time.");
                        return c;
                    }
                }else return c;
            }
        }
        return null;
    }
    public boolean checkMobileNumber(JTextField field, Component class1, KeyEvent e){
        /*
         * Validating mobile number that input to the field by the user
         * */
        try {
            //Parsing entered mobile number to check is there
            Integer.parseInt(field.getText().trim());

            //checking backspace selected or not
            if (e.getKeyChar()!= KeyEvent.VK_BACK_SPACE) {

                //get and assigned value of entered input and checking the length of mobile number is 10
                String value = field.getText();
                if (value.length() <= 10) {

                    //again checking its only numbers and printing error
                    if (!(e.getKeyChar() >= '0' && e.getKeyChar() <= '9')) {
                        JOptionPane.showConfirmDialog(class1, "* Enter only numeric digits(0-9)", "Warning", JOptionPane.OK_CANCEL_OPTION);

                        //if error occurred setting text to empty
                        if (e.getSource() == field) field.setText("");
                    } else {
                        return value.length() == 9;
                    }
                } else {
                    JOptionPane.showConfirmDialog(class1, "Cannot exceed 10 digits", "Warning", JOptionPane.OK_CANCEL_OPTION);

                    // if entered numbers length exceeded 10 again setting string until 10 by removing other texts
                    StringBuilder s = new StringBuilder();
                    String[] split = value.split("");
                    for (int i = 0; i < 10; i++) {
                        s.append(split[i]);
                    }
                    field.setText(s.toString());
                }
            }
        }catch (NumberFormatException a){

            //if entered text is contain texts other than numbers, this error will pop up
            JOptionPane.showConfirmDialog(class1, "* Enter only numeric digits(0-9)", "Warning", JOptionPane.OK_CANCEL_OPTION);
            field.setText("");
        }
        return false;
    }

    public Classes.Consultation findConsultation(int patientID, int appointmentID, String mobileNumber, Component y){
        for (Classes.Consultation c: Classes.Consultation.getCONSULTATIONS()) {
            if (Objects.equals(c.getPATIENT().getMobileNumber(), mobileNumber)
                    && c.getAppointmentID()==appointmentID
                    && c.getPATIENT().getPatientID()==patientID
            ) return c;
        }
        JOptionPane.showConfirmDialog(y, "There is no such reservation", "Warning", JOptionPane.OK_CANCEL_OPTION);
        return null;
    }
    public boolean checkConsultationByAppointmentID(JTextField appointmentIdJField, JTextField patientIDField, Component class1){
        /*
         * checking consultation is exists by entered appointment ID
         * */
        try {
            //assigning entered appointment id
            int appointmentIDTemp = Integer.parseInt(appointmentIdJField.getText().trim());
            int patientIDTemp = Integer.parseInt(patientIDField.getText().trim());
            for (Classes.Consultation c: Classes.Consultation.getCONSULTATIONS()){

                //checking selected appointment id (by looping through consultation) equals to entered one by user
                if (c.getAppointmentID()==appointmentIDTemp &&
                        c.getPATIENT().getPatientID()==patientIDTemp) return true;
            }
            JOptionPane.showConfirmDialog(class1, "There isn't such a ID", "Warning", JOptionPane.OK_CANCEL_OPTION);
        }catch (NumberFormatException ignored){

            //if entered appointment id contains texts other than numerics this will occur
            JOptionPane.showConfirmDialog(class1, "* Enter only numeric digits(0-9)", "Warning", JOptionPane.OK_CANCEL_OPTION);
            appointmentIdJField.setText("");
        }
        //If any consultation not equal to entered appointment id will return false
        return false;
    }
    public void deleteConsultation(Classes.Consultation c){
        /*
         * Deleting consultation base on appointment id and mobile Number
         * */
        for (Classes.Consultation d :
                Classes.Consultation.getCONSULTATIONS()) {
            if (c.getAppointmentID()==d.getAppointmentID() &&
                    Objects.equals(c.getPATIENT().getMobileNumber(), d.getPATIENT().getMobileNumber())){
                Classes.Consultation.getCONSULTATIONS().remove(c);
                return;
            }
        }
    }


    public void fileWrite() throws Exception {
        /*
         * This method used to write created consultation into file
         * */

        //using this class encrypting the data
        TripleDes des = new TripleDes();

        //using this pattern, date will print in the file
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh-mm aa");

        //using java file serialization this will write in file
        FileOutputStream fileOutputStream = new FileOutputStream("src/TXTFiles/ConsultationsDetails.txt");
        ObjectOutputStream stream = new ObjectOutputStream(fileOutputStream);

        for (Classes.Consultation c: Classes.Consultation.getCONSULTATIONS()){
            //encrypting image urls
            c.setPaths(des.pathsEncryption(c.getPaths()));

            //encrypting notes if note isn't null
            if (c.getNotes()!=null) c.setNotes(des.encrypt(c.getNotes()));

            stream.writeObject(c);
            Patient p = c.getPATIENT();
            String write =
                    "Name             : " + p.getName() + " , " + p.getSurname() + "\n" +
                            "Mobile Number    : " + p.getMobileNumber() + "   || " + "DOB    : " + p.getDateOfBirth() + "\n" +
                            "Address          : " + p.getAddress() + "\n" +
                            "Note         : " + c.getNotes() + "\n" +
                            "Date         : " + formatter.format(c.getDate()) + "\n" +
                            "Hours        : " + c.getHours() + "\n" +
                            "Doctor Medical License  : " + c.getDoctor().getMedicalLicenceNumber() + "\n" +
                            "Specialisation Number   : " + c.getDoctor().getSpecialisation().getNumberOfSpecialisation() + "\n" +
                            "Appointment ID          : " + c.getAppointmentID() + "\n\n\n";

            System.out.println(write);
        }
    }

    public void readFile(Component class1) throws Exception {
        /*
         * reading file serializes data in the file
         * */

        // using this object decrypting the data that encrypted
        TripleDes des = new TripleDes();

        //reading serialized data in the file

        File file = new File("src/TXTFiles/ConsultationsDetails.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream stream1 = new ObjectInputStream(fileInputStream);

        // this boolean is will false when lines come to end of the file and thrown an error
        boolean const1 = true;
        while (const1){
            try {
                // reading serialized object
                Classes.Consultation c = (Classes.Consultation) stream1.readObject();

                if (c==null) throw new IOException();
                // decrypting note if it isn't null
                if (c.getNotes() != null) {
                    c.setNotes(des.decrypt(c.getNotes()));
                }
                // decrypting image urls
                c.setPaths(des.pathsDecryption(c.getPaths()));

                Doctor d = c.getDoctor();
                //checking doctor is in the center (because manager will have deleted)
                Doctor doctor = doctorController.checkDoctorIsAvailable(d.getMedicalLicenceNumber(),
                        d.getSpecialisation().getNumberOfSpecialisation(),c.getDate(),1);
                if (doctor!=null) {

                    // Checking date is before today or not. If date is pased consultation won't add into consultation
                    // list and patient will add into old patient array
                    doctor = doctorController.findDoctorByMedicalLicence(doctor.getMedicalLicenceNumber());
                    c.setDoctor(doctor);

                    Date date1 = new Date();
                    if (c.getDate().after(date1)) {
                        Classes.Consultation.getCONSULTATIONS().add(c);
                    }else {
                        String text = c.getPATIENT().getName()+" "+c.getPATIENT().getSurname()+"'s appointment id "+c.getAppointmentID()+
                                " is due date";
                        JOptionPane.showMessageDialog(null,text);
                    }
                    if (!Patient.getOldPatients().contains(c.getPATIENT())) {
                        if (Patient.getNumOfPatient()<c.getPATIENT().getPatientID()) {
                            Patient.setNumOfPatient(c.getPATIENT().getPatientID());
                        }
                        Patient.getOldPatients().add(c.getPATIENT());
                    }

                    if (Classes.Consultation.getMaxConsultationId() <c.getAppointmentID()) {
                        Classes.Consultation.setMaxConsultationId(c.getAppointmentID());
                    }

                }
                else {
                    // if doctor isn't in the consultation center patient will add into patient array
                    if (!Patient.getOldPatients().contains(c.getPATIENT())) Patient.getOldPatients().add(c.getPATIENT());
                    String message = "The doctor who owned medical license number = "+d.getMedicalLicenceNumber()+", is no longer working in this institution.\n\n" +
                            " Under the said doctor, the reservation of the said person is canceled.\n\n Please call, "+c.getPATIENT().getMobileNumber()+" and inform him.";
                    JOptionPane.showMessageDialog(class1, message, "Warning", JOptionPane.WARNING_MESSAGE);
                }
                System.out.println(c);
            }catch (EOFException e){
                // this will throw if serialized object written files comes to end of file
                const1=false;
            }catch (IOException | ClassNotFoundException | ParseException ignored){}
        }

        System.out.println("        Finished"       );
    }


    public Date getExactDateAndTime(Date date, String time) throws ParseException {
        /*
         * combined date and time
         * */
        SimpleDateFormat s = new SimpleDateFormat("yyyy : MM : dd : hh : mm aa");
        SimpleDateFormat s1 = new SimpleDateFormat("yyyy : MM : dd");
        String date1 = s1.format(date);

        return s.parse(date1+" : "+time);

    }



    public String[] fileCopied(String[] strings, int appointmentID, Patient patient) throws IOException {
        /*
         * Coping selected file from selected path into images package
         * */

        // this array will include
        String[] newPaths = new String[3];

        for (int i = 0; i < strings.length; i++) {
            if (strings[i]!=null){
                Path source = Paths.get(strings[i]);
                //getting extension of copied file
                String extension = strings[i].split("\\.")[1];

                //creating file path for new creating file
                String targetLocation = "src/TXTFiles/images/"+appointmentID+"-"+patient.getMobileNumber()+"-"+i+"."+extension;
                Path target = Paths.get(targetLocation);

                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                newPaths[i] = targetLocation;
            }
        }
        return newPaths;
    }

    public boolean fileDelete(String[] filePaths){
        /*
         * while going through file paths array, file will delete in that file path
         * if consultation is deleting copied images also will delete
         * */
        int i = 0, nullCounts=0;
        boolean isDeleted = false;
        if (filePaths!=null) {
            while (i < filePaths.length) {

                //checking in that position of file path is null
                if (filePaths[i] != null) {
                    File file = new File(filePaths[i]);

                    //if that file is exists it will delete
                    if (file.exists()) isDeleted = file.delete();
                }
                //if all paths are null isDeleted won't be true. So returned value would be wrong
                else nullCounts++;
                i++;
            }
        }
        if (nullCounts==3) isDeleted=true;

        return isDeleted;
    }

    public boolean isHourFromNow(Date date, Component y) throws ParseException {
        Date timeAfterHourFromNow = getEndTime(new Date(), 1);
        Date givenTimeEndingTime = getEndTime(date, 1);
        if (timeAfterHourFromNow.after(givenTimeEndingTime)) {
            JOptionPane.showConfirmDialog(y, "Reservation should be made at least an hour from now.", "Warning", JOptionPane.OK_CANCEL_OPTION);
            return false;
        }
        else return true;
    }
}
