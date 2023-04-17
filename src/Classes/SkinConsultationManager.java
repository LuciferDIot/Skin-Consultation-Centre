package Classes;

import java.io.IOException;

public interface SkinConsultationManager {

    int addNewDoctor(String[] fullName,
                     String mobileNumber,
                     String address,
                     String DOB,
                     int specializationNumber,
                     String medicalLicenceNumber )throws IOException;

    boolean deleteDoctor(int doctorID, String doctorMedicalLicence);

    boolean printTheListOfDoctors();

    void saveInFile(String filePath) throws IOException;

}
