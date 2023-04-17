package Classes;

import java.io.Serial;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Consultation implements Serializable {

    //used during deserialization to verify that the sender and receiver of a serialized object have loaded classes for
    // that object that are compatible with respect to serialization
    @Serial
    private static final long serialVersionUID = -1353870817994522994L;

    private int appointmentID;
    private Date date;
    private int hours;
    private double cost;
    private String notes;
    private Doctor doctor;
    private final Patient PATIENT;
    private String[] paths = new String[3];

    private static int MAX_CONSULTATION_ID=0;
    private static ArrayList<Consultation> CONSULTATIONS = new ArrayList<>();


    public Consultation(Date date, int hours, Doctor doctor, Patient PATIENT, String notes) {
        /*
        * If patient exists before this constructor used to make a consultation. And by this will add cost as 25$
        * */
        this.cost = (25*hours);
        this.date = date;
        this.doctor = doctor;
        this.hours = hours;
        this.PATIENT = PATIENT;
        setNotes(notes);
        // Appointment id will create base on created consultation count
        setAppointmentID();

        //Created consultation will add to the memory
        CONSULTATIONS.add(this);
    }

    public Consultation(Date date, int hours, Doctor doctor, String firstName, String lastName, String address, Date DOB,
                        String notes) {
        /*
         * If patient don't exist using this constructor patient object will create
         * */
        this.cost = (15*hours);
        this.date = date;
        this.doctor = doctor;
        this.PATIENT = new Patient(firstName,lastName,DOB);
        Patient.getOldPatients().add(this.PATIENT);
        this.PATIENT.setAddress(address);
        this.hours = hours;
        setNotes(notes);
        // Appointment id will create base on created consultation count
        setAppointmentID();

        //Created consultation will add to the memory
        CONSULTATIONS.add(this);
    }

    public Date getEndTime() throws ParseException {
        /*
        * returning end time belongs to given date after given hours.
        * */
        SimpleDateFormat parser = new SimpleDateFormat("yyyy:MM:dd:hh:mm");
        //Parsing date as given pattern cause of must-have that pattern to use in calendar class
        Date myDate = parser.parse(parser.format(this.date));
        Calendar cal =Calendar.getInstance();
        cal.setTime(myDate);

        //By this method hours will add to the calendar
        cal.add(Calendar.HOUR_OF_DAY,this.hours);

        return cal.getTime();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        //if entered field is empty field will assign null value
        if (notes.equals("")) this.notes = null;
        else this.notes = notes;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPATIENT() {
        return PATIENT;
    }

    public static ArrayList<Consultation> getCONSULTATIONS(){
        return CONSULTATIONS;
    }

    public String[] getPaths() {
        return paths;
    }

    public void setPaths(String[] paths) {
        this.paths = paths;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID() {
        // setting appointment id getting size of the array and adding one to the value
        setAppointmentID(MAX_CONSULTATION_ID +1);
        MAX_CONSULTATION_ID = MAX_CONSULTATION_ID+1;
    }
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public void setHours(int hours) {
        /*
        * setting cost base on hours entered
        * */
        this.cost = (25*hours);
        this.hours = hours;
    }

    public static int getMaxConsultationId() {
        return MAX_CONSULTATION_ID;
    }

    public static void setMaxConsultationId(int maxConsultationId) {
        MAX_CONSULTATION_ID = maxConsultationId;
    }

    public int getHours() {
        return hours;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh-mm aa");
        return "Name             : " + PATIENT.getName() + " , " + PATIENT.getSurname() + "\n" +
                "Patient id       : "+ PATIENT.getPatientID() + "\n" +
                "Mobile Number    : " + PATIENT.getMobileNumber() + "   || " + "DOB    : " + PATIENT.getDateOfBirth() + "\n" +
                "Address          : " + PATIENT.getAddress() + "\n" +
                "Note         : " + getNotes() + "\n" +
                "Date         : " + formatter.format(getDate()) + "\n" +
                "Hours        : " + getHours() + "\n" +
                "Doctor Medical License  : " + getDoctor().getMedicalLicenceNumber() + "\n" +
                "Specialisation Number   : " + getDoctor().getSpecialisation().getNumberOfSpecialisation() + "\n" +
                "Appointment ID          : " + getAppointmentID() + "\n\n\n";
    }


    public static void setCONSULTATIONS(ArrayList<Consultation> CONSULTATIONS) {
        Consultation.CONSULTATIONS = CONSULTATIONS;
    }
}