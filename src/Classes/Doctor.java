package Classes;

import java.io.Serial;
import java.util.Comparator;

public class Doctor extends Person{
    @Serial
    private static final long serialVersionUID = 7530541900870500369L;
    private static int AVAILABLE_DOCTOR_COUNT;
    private int ID;
    private String medicalLicenceNumber;
    private Specialisation specialisation;

    public Doctor(String name, String surname, String dateOfBirth,int ID) {
        super(name, surname, dateOfBirth);
        this.ID = ID+1;
        // increasing num of doctors while creating doctor object
        AVAILABLE_DOCTOR_COUNT++;
    }

    public Doctor(String name, String surname, String dateOfBirth, String medicalLicenceNumber, int specialisationNum) {
        /*
        * Using for initializing object using all attributes*/
        super(name, surname, dateOfBirth);
        this.medicalLicenceNumber = medicalLicenceNumber;
        setSpecialisation(specialisationNum);
        // increasing num of doctors while creating doctor object
        AVAILABLE_DOCTOR_COUNT++;
    }

    public void setSpecialisation(int specialisationNum) {
        /*
        * setting specialization base on parsed number*/
        this.specialisation = Specialisation.getSpecialisationOfNumber(specialisationNum);
    }


    public String getMedicalLicenceNumber() {
        return medicalLicenceNumber;
    }

    public void setMedicalLicenceNumber(String medicalLicenceNumber) {
        this.medicalLicenceNumber = medicalLicenceNumber;
    }

    public Specialisation getSpecialisation() {
        return specialisation;
    }

    public static int getAvailableNumOfDoctors() {
        return AVAILABLE_DOCTOR_COUNT;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public static void removeTheDoctors() {
        /*
         * when doctor removed this method must be called
         * */
        AVAILABLE_DOCTOR_COUNT--;
    }

    @Override
    public String toString() {
        return      "\nDoctor" +
                    "\nname                 : " + super.getName() +
                    "\nsurname              : " + super.getSurname() +
                    "\ndateOfBirth          : " + super.getDateOfBirth() +
                    "\nmobileNumber         : " + super.getMobileNumber() +
                    "\naddress              : " + super.getAddress() +
                    "\nMedicalLicenceNumber : " + medicalLicenceNumber +
                    "\nSpecialisation       : " + specialisation +
                    "\n\n";
    }

}

class DoctorComparator implements Comparator<Doctor>{

    @Override
    public int compare(Doctor o1, Doctor o2) {
        /*
        * array will sort base on surname alphabetically order
        * */
        if (o1!=null && o2!=null) {
            String surname1 = o1.getSurname();
            String surname2 = o2.getSurname();
            return surname1.compareTo(surname2);
        }
        else return -1;
    }
}
