package Classes;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Date;

public class Patient extends Person{
    @Serial
    private static final long serialVersionUID = 1L;
    private int patientID;
    private static int NUM_OF_PATIENT=0;
    private static final ArrayList<Patient> OLD_PATIENTS = new ArrayList<>();

    protected Patient(String name, String surname, Date dateOfBirth) {
        super(name, surname, dateOfBirth);
        //incrementing num of patients when creating a patient object
        //setting id to the patient base on number of patients

        //if patients is newcomer this object will add into the arrayList
        if (!OLD_PATIENTS.contains(this)) {
            OLD_PATIENTS.add(this);
            NUM_OF_PATIENT = NUM_OF_PATIENT+1;
        }
    }


    public int getPatientID() {
        return patientID;
    }

    public void setPatientID() {
        this.patientID = NUM_OF_PATIENT;
    }

    public static int getNumOfPatient() {
        return NUM_OF_PATIENT;
    }

    public static void setNumOfPatient(int numOfPatient) {
        NUM_OF_PATIENT = numOfPatient;
    }

    public static ArrayList<Patient> getOldPatients() {
        return OLD_PATIENTS;
    }

    @Override
    public String toString() {
        return "patientID=" + patientID ;
    }
}
