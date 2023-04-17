package Classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class WestminsterSkinConsultationManagerTest {

    private static WestminsterSkinConsultationManager manager;
    private final String filePath = "test/Classes/text/ManagerTesting.txt";

    @BeforeEach
    void setUp() {
        manager = new WestminsterSkinConsultationManager("LuciferDiots", "user1");
    }

    @Test
    void addNewDoctor() {
        String name = "supun";
        String surname = "cena";
        String dob = "1997-02-13";
        String mobile = "0786201390";
        String medicalLicenseNumber = "333924320";
        int specialisationNum = 14;

        //Adding doctor using manager function.
        int doctorID = manager.addNewDoctor(new String[]{name, surname}, mobile, "",
                dob, specialisationNum, medicalLicenseNumber);

        assertNotEquals(0, doctorID);
        //checking whether doctor added successfully and num of doctors increased
        assertEquals(1, Doctor.getAvailableNumOfDoctors());
        ManagerOptions.print("\n\n|||||          Doctor added successfully            |||||");

//------------------------------------------------------------------------------------------------------------------------
        //Adding same doctor to check it's not adding
        int doctorID0 = manager.addNewDoctor(new String[]{name, surname}, mobile, "",
                dob, specialisationNum, medicalLicenseNumber);

        assertEquals(0, doctorID0);
        //checking num of doctor count is still same
        assertEquals(1, Doctor.getAvailableNumOfDoctors());
        ManagerOptions.print("\n\n|||||       User exists validation successfully     |||||");


//------------------------------------------------------------------------------------------------------------------------
        String name1 = "supun1";
        String surname1 = "cena1";
        String dob1 = "1997-02-wvfw";
        String mobile1 = "0786201390";
        String medicalLicenseNumber1 = "333924320";
        int specialisationNum1 = 14;

        //Adding doctor using manager function
        int doctorID1 = manager.addNewDoctor(new String[]{name1, surname1}, mobile1, "",
                dob1, specialisationNum1, medicalLicenseNumber1);

        assertEquals(0, doctorID1);
        //checking num of doctor count is still same
        assertEquals(1, Doctor.getAvailableNumOfDoctors());
        ManagerOptions.print("\n\n|||||         Date Validation successfully          |||||");


//------------------------------------------------------------------------------------------------------------------------
        String name2 = "supun2";
        String surname2 = "cena2";
        String dob2 = "1997-02-13";
        String mobile2 = "07862";
        String medicalLicenseNumber2 = "333924320";
        int specialisationNum2 = 14;

        //Adding doctor using manager function
        int doctorID2 = manager.addNewDoctor(new String[]{name2, surname2}, mobile2, "",
                dob2, specialisationNum2, medicalLicenseNumber2);

        assertEquals(0, doctorID2);
        //checking num of doctor count is still same
        assertEquals(1, Doctor.getAvailableNumOfDoctors());
        ManagerOptions.print("\n\n|||||     Mobile Number validation successfully     |||||");


//------------------------------------------------------------------------------------------------------------------------
        String name3 = "supun2";
        String surname3 = "cena2";
        String dob3 = "1997-02-13";
        String mobile3 = "0786201390";
        String medicalLicenseNumber3 = "333924320";
        int specialisationNum3 = 0;

        //Adding doctor using manager function
        int doctorID3 = manager.addNewDoctor(new String[]{name3, surname3}, mobile3, "",
                dob3, specialisationNum3, medicalLicenseNumber3);

        assertEquals(0, doctorID3);
        //checking num of doctor count is still same
        assertEquals(1, Doctor.getAvailableNumOfDoctors());
        ManagerOptions.print("\n\n|||||    specialisation validation successfully     |||||");
        assertEquals(1, Doctor.getAvailableNumOfDoctors());

//------------------------------------------------------------------------------------------------------------------------
        //deleting doctor, because we have to keep available number of doctor count as 1
        manager.deleteDoctor(doctorID, medicalLicenseNumber);
    }

    @Test
    void DeleteDoctor() {
        String name = "supun";
        String surname = "cena";
        String dob = "1997-02-13";
        String mobile = "0786201390";
        String medicalLicenseNumber = "333924320";
        int specialisationNum = 14;

        //Adding doctor using manager function.
        int doctorID = manager.addNewDoctor(new String[]{name, surname}, mobile, "",
                dob, specialisationNum, medicalLicenseNumber);

        System.out.println(doctorID);
        assertNotEquals(0, doctorID);
        //ensuring doctor not deleted and num of doctors didn't get decreased
        assertEquals(1, Doctor.getAvailableNumOfDoctors());
        ManagerOptions.print("\n\n|||||          Doctor added successfully            |||||");

        // delete doctor using delete method in manager object
        int doctorID1 = 6;
        boolean isDeletedTest1 = manager.deleteDoctor(doctorID1,medicalLicenseNumber);
        //ensuring doctor not deleted and num of doctors didn't get decreased
        assertEquals(1,Doctor.getAvailableNumOfDoctors());
        assertFalse(isDeletedTest1);
        ManagerOptions.print("\n\n|||||       Checking entering wrong doctor id       |||||");

        String medicalLicenseNumber2 = "vds";
        boolean isDeletedTest2 = manager.deleteDoctor(doctorID,medicalLicenseNumber2);
        //ensuring doctor not deleted and num of doctors didn't get decreased
        assertEquals(1,Doctor.getAvailableNumOfDoctors());
        assertFalse(isDeletedTest2);
        ManagerOptions.print("\n\n|||||    Checking entering wrong medical License    |||||");

        //ensuring doctor not deleted and num of doctors didn't get decreased
        assertEquals(1,Doctor.getAvailableNumOfDoctors());
        boolean isDeletedTest3 = manager.deleteDoctor(doctorID,medicalLicenseNumber);
        //ensuring doctor deleted and decreased
        assertEquals(0,Doctor.getAvailableNumOfDoctors());
        assertTrue(isDeletedTest3);

        ManagerOptions.print("\n\n|||||           Doctor delete successfully          |||||");
    }

    @Test
    void printTheListOfDoctors() {
        String name = "supun";
        String surname = "cena";
        String dob = "1997-02-13";
        String mobile = "0786201390";
        String medicalLicenseNumber = "333924320";
        int specialisationNum = 14;

        //Adding doctor using manager function.
        int doctorID = manager.addNewDoctor(new String[]{name, surname}, mobile, "",
                dob, specialisationNum, medicalLicenseNumber);

        assertNotEquals(10, doctorID);
        //checking is there have any changed or decreased by and crash in available num of doctors
        assertEquals(1, Doctor.getAvailableNumOfDoctors());
        ManagerOptions.print("\n\n|||||          Doctor added successfully            |||||");

        //One object have added and assign values to it.
        //in boolean printTheListOfDoctors() method, if something printed return value will be true

        assertTrue(manager.printTheListOfDoctors());
        ManagerOptions.print("\n\n||||| printTheListOfDoctors() function working fine |||||");

//------------------------------------------------------------------------------------------------------------------------
        //deleting doctor, because we have to keep available number of doctor count as 1
        manager.deleteDoctor(doctorID, medicalLicenseNumber);
    }

    @Test
    void saveInFile() throws IOException {
        String name = "supun";
        String surname = "cena";
        String dob = "1997-02-13";
        String mobile = "0786201390";
        String medicalLicenseNumber = "333924320";
        int specialisationNum = 14;

        //Adding doctor using manager function.
        //ensuring any doctor isn't available yet
        assertEquals(0, Doctor.getAvailableNumOfDoctors());
        int doctorID = manager.addNewDoctor(new String[]{name, surname}, mobile, "",
                dob, specialisationNum, medicalLicenseNumber);

        assertNotEquals(10, doctorID);
        //ensuring doctor added
        assertEquals(1, Doctor.getAvailableNumOfDoctors());
        ManagerOptions.print("\n\n|||||          Doctor added successfully            |||||");


        manager.saveInFile(filePath);

        //Checking file created
        File file = new File(filePath);
        assertTrue(file.exists());

        BufferedReader reader = new BufferedReader(new FileReader(file));

        //checking data wrote into txt file
        boolean wrote = false;
        int i=0;
        while (!wrote || i==7){
            String read = reader.readLine().trim();
            if (read.equals("stop")) {
                wrote = true;
                assertTrue(true);
            }
            i++;
        }
        ManagerOptions.print("\n\n|||||      saveInFile() function working fine       |||||");
        //deleting doctor to keep num of doctors keep as same
        manager.deleteDoctor(doctorID, medicalLicenseNumber);
    }

    @Test
    void xReadFile() throws IOException {
        // We have written into txt file data of the object
        /* But when we're reading again data of saved file it will create again,
         same doctor with same details, but it will different object.
         So there must be increase of available doctors. We can assert that
         */
        WestminsterSkinConsultationManager.readFile(filePath);

        assertEquals(1, Doctor.getAvailableNumOfDoctors());
        manager.printTheListOfDoctors();

        ManagerOptions.print("\n\n|||||        readFile() function working fine       |||||");
    }

}