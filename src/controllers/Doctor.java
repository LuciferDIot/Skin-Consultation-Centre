package controllers;

import Classes.Specialisation;
import Classes.WestminsterSkinConsultationManager;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Classes.ManagerOptions.*;

public class Doctor {

    controllers.Consultation consultation;

    public boolean printingTheDoctorDetail(Classes.Doctor doctor){
        print(  "|   ID                    : "+doctor.getID()+"                                  \n"+
                "|   Medical licence number: "+doctor.getMedicalLicenceNumber()+"       \n"+
                "|   Name                  : "+doctor.getName()+" , "+doctor.getSurname()+"       \n"+
                "|   Mobile Number    : "+doctor.getMobileNumber()+"   || "+"DOB    : "+doctor.getDateOfBirth()+"       \n"+
                "|   Address          : "+doctor.getAddress()+"       \n"+
                "|   Specialisation Number : "+doctor.getSpecialisation().toString()+"       \n\n");
        return true;
    }

    public int setDoctorSpecializationNumber(BufferedReader reader){
        /*
         * Getting inputs for specialization's and setting int value according to specialization type
         *  */

        print("|          Enter doctors specialization belongs to the list    |");
        for (Specialisation s : Specialisation.values()){
            System.out.println("|                 "+s.getNumberOfSpecialisation()+" - "+ s +"                  ");
        }

        //assigning specialization Number into this variable
        int specializationNumber;
        while (true){
            try {
                specializationNumber = Integer.parseInt(enterYourDetails("|                  Enter doctor's specialization               |:",reader));
                if (specializationNumber<1 || 19<specializationNumber) throw new InputMismatchException();
                return specializationNumber;
            } catch (InputMismatchException | IOException Ignore){
                //if input int is out of range this will print cause of throwing InputMismatchException
                inputMismatch();
                break;
            } catch (NumberFormatException e){
                //if input type wrong this error will print
                print("|                    Integer format expected                   |");
            }
        }
        return 0;
    }

    public boolean checkDoctorSpecializationNumber(int input){
        try {
            //checking int is in range of specialization types
            if (input <1 || 19< input) throw new InputMismatchException();
            return true;
        } catch (InputMismatchException |NumberFormatException Ignore){
            //if input int is out of range this will print cause of throwing InputMismatchException
            return false;
        }
    }

    public Classes.Doctor findDoctorObjectByFullName(String fullName){
        /*
         * checking doctors by using firstname and surname*/

        for (Classes.Doctor doctor: WestminsterSkinConsultationManager.getDoctorList() ) {
            if (doctor != null) {
                // combining doctors name to check equality
                String value = doctor.getName() + " " + doctor.getSurname();
                if (fullName.trim().equals(value)) return doctor;
            }
        }
        // if doctor name is not equals to any doctors name will return null
        return null;
    }

    public Classes.Doctor randomlySelectADoctorIfUnavailable(Classes.Doctor doctor, Date date, int hours, Component component) throws ParseException {
        /*
         * if doctor is not available at that time randomly a doctor will return according to the specialization of that busy doctor
         * */

        consultation = new Consultation();
        //checking availability of doctor. if doctor is available, the doctor will return
        if ( consultation.checkDoctorAvailable(doctor,date,hours)==null) return doctor;
        else {
            //if doctor not available this will execute
            for (Classes.Doctor d : WestminsterSkinConsultationManager.getDoctorList()) {
                if (d==null) continue;

                //checking doctor specialization equality
                if (d != doctor && d.getSpecialisation() == doctor.getSpecialisation()) {

                    //if doctors specialization is equal check the availability of the doctor ,
                    // and will return the consultation if a consultation is exists for that doctor
                    Classes.Consultation c = consultation.checkDoctorAvailable(d, date, hours);
                    if (c == null) {
                        // ask user to add random doctor for creating consultation cause of entered doctors availability
                        JOptionPane.showMessageDialog(component, "The doctor is not available at this time.", "Info", JOptionPane.ERROR_MESSAGE);
                        int yes =JOptionPane.showConfirmDialog(component, "Added randomly another doctor that specializes category. " +
                                "If you want to change or delete a reservation now can do it.", "Warning", JOptionPane.OK_CANCEL_OPTION);
                        //0=yes 1=no 2-cancel
                        //if user entered yes another available doctor will add
                        if (yes==JOptionPane.YES_OPTION) return d;
                            //else stopping find another doctor
                        else break;
                    }
                }
            }
        }
        // if any other doctor is not available or user didn't agree to add another doctor will return null
        return null;
    }

    public boolean checkMedicalLicenceExists(String medicalLicenceNumber){

        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(medicalLicenceNumber);
        if (!m.matches()) {
            System.out.println();
            print("|                This can only contains numbers                 |");
            return false;
        }

        Classes.Doctor[] doctors = WestminsterSkinConsultationManager.getDoctorList();
        for (int i = 0; i < doctors.length; i++) {
            if (doctors[i]!=null){
                if (medicalLicenceNumber.equals(doctors[i].getMedicalLicenceNumber())) {
                    print("|            Medical Licence number already exists              |");
                    return false;
                }
            }
        }
        return true;
    }
    public Classes.Doctor checkDoctorIsAvailable(String medicalLicenceNumber, int specializationNum,
                                                        Date date, int hours) throws ParseException {
        /*
         * checking doctor is available base on given specialization number and medical licence number
         * */

        consultation = new Consultation();
        Classes.Doctor[] specializedDoctors = new Classes.Doctor[10];
        int i=0;
        for (Classes.Doctor d : WestminsterSkinConsultationManager.getDoctorList()) {
            if (d==null) continue;

            // if doctor is not null checking specialization equality to proceed and adding the doctor into specializedDoctors array
            if (d.getSpecialisation().getNumberOfSpecialisation() == specializationNum){
                specializedDoctors[i]=d;
                i++;

                // checking the doctors medical license is equal to entered medical licence number. if it is the doctor will return
                if (medicalLicenceNumber.trim().equals(d.getMedicalLicenceNumber())) return d;
            }
        }

        //checking another doctor available at that given date and time according to specialization cause of not available selected doctor
        for (Classes.Doctor d : specializedDoctors) {
            if (d==null) continue;
            //checking doctor availability
            if (consultation.checkDoctorAvailable(d, date, hours) == null) return d;
        }

        //if another doctor also is not available null will be return
        return null;
    }

    public Classes.Doctor findDoctorByMedicalLicence(String medicalLicence){
        Classes.Doctor[] doctors = WestminsterSkinConsultationManager.getDoctorList();
        for (int i = 0; i < doctors.length; i++) {
            if (doctors[i]!=null){
                if (medicalLicence.equals(doctors[i].getMedicalLicenceNumber())) return doctors[i];
            }
        }
        return null;
    }


}
