package controllers;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Patient {



    public boolean checkPatientID(String text, Component y) {

        Pattern p = Pattern.compile("[0-9]+");

        Matcher m = p.matcher(text);
        if (m.matches()){
            //if fields not only contain letters this will pop up
            int patientID = Integer.parseInt(text);
            return patientID <= Classes.Patient.getNumOfPatient();
        }else {
            JOptionPane.showConfirmDialog(y, "Name fields can only contains numbers", "Warning", JOptionPane.OK_CANCEL_OPTION);
            return false;
        }
    }

    public Classes.Patient isCustomer(String firstName, String lastName){
        /*
         * when making appointment checking
         * */
        for (Classes.Patient p : Classes.Patient.getOldPatients()){

            //checking doctors firstname and lastname equals to existing objects of a patients
            // , while go through all the old patients array. and will return the patient if exits
            if (Objects.equals(p.getName(), firstName) &&
                    Objects.equals(p.getSurname(), lastName)) {
                System.out.println("Previous Customer");
                return p;
            }
        }
        //if any patient is not there equals to given firstname and lastname before will return null
        return null;
    }
}
