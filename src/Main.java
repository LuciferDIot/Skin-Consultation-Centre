import Classes.WestminsterSkinConsultationManager;
import GUI.Login;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new Login();
        WestminsterSkinConsultationManager.readFile("src/TXTFiles/DoctorDetails.txt");
    }
}
