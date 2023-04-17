package Classes;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class DoctorTest {

    private static Doctor doctor;
    private static WestminsterSkinConsultationManager manager;
    private static int specialisationNum;
    private static String medicalLicenseNumber;
    private static String name;
    private static String surname;
    private static controllers.Doctor doctorController;

    @BeforeAll
    static void setUp() {

        manager = new WestminsterSkinConsultationManager("LuciferDiots", "user1");
        doctorController = new controllers.Doctor();

        //creating a doctor object and increased numOfDoctors variable count by one
        name = "john";
        surname = "cena";
        String dob = "1997-02-13";
        String mobile = "0786201390";
        medicalLicenseNumber = "333924320";
        specialisationNum = 14;

        //adding using manager class for get functionality execution easily
        int doctorID = manager.addNewDoctor(new String[]{name, surname}, mobile, "", dob, specialisationNum, medicalLicenseNumber);
        doctor = WestminsterSkinConsultationManager.getDoctorList()[doctorID-1];
    }

    @Test
    void findDoctorObjectByFullName() {
        //creating full name by concatenation
        String firstName = name.substring(0,1).toUpperCase() + name.substring(1);
        String lastName = surname.substring(0,1).toUpperCase() + surname.substring(1);
        String fullName = firstName +" "+ lastName;
        System.out.println(fullName);

        //Getting doctor object that similar full name
        Doctor expectedDoctorObject = doctorController.findDoctorObjectByFullName(fullName.trim());
        assert expectedDoctorObject != null;

        System.out.println(doctor.getID()+ expectedDoctorObject.getID());
        assertEquals(doctor.getID(), expectedDoctorObject.getID());
        ManagerOptions.print("|                      Found the object                        |");
    }

    @Test
    void checkDoctorSpecializationNumber() {
        //check giving negative specializationNumber
        specialisationNum = -23;
        assertFalse(doctorController.checkDoctorSpecializationNumber(specialisationNum));

        //check giving wrong positive specializationNumber
        specialisationNum = 2394;
        assertFalse(doctorController.checkDoctorSpecializationNumber(specialisationNum));

        //check giving right specializationNumber
        specialisationNum = 17;
        assertTrue(doctorController.checkDoctorSpecializationNumber(specialisationNum));
    }

    @Test
    void checkIsTheSpecialisationAdded() {
        doctor.setSpecialisation(specialisationNum);
        Specialisation currentSpecialisation = doctor.getSpecialisation();
        Specialisation expectedSpecialisation = Specialisation.getSpecialisationOfNumber(specialisationNum);

        assertEquals(expectedSpecialisation, currentSpecialisation);
    }

    @Test
    void printingTheDoctorDetail() {
        assertTrue(doctorController.printingTheDoctorDetail(doctor));
        ManagerOptions.print("|                   Printed doctor details                     |");
    }

    @Test
    void aRemoveTheDoctorAndDecreaseAvailableDoctorCount() {
        //When deleted a doctor, available doctor's count in list must be decreased
        boolean isDeleted = manager.deleteDoctor(doctor.getID(),medicalLicenseNumber);
        assertTrue(isDeleted);
        assertEquals(0, Doctor.getAvailableNumOfDoctors());
    }
}