package Classes;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Objects;

import static Classes.ManagerOptions.*;
import static Classes.ManagerOptions.enterYourDetails;

public class WestminsterSkinConsultationManager extends Person implements SkinConsultationManager{


    private static final Doctor[] DOCTOR_LIST = new Doctor[10];
    private final String password;
    private final String username;
    private final controllers.Doctor doctorController;
    private final controllers.Person personController;

    public WestminsterSkinConsultationManager(String name, String surname, String dateOfBirth, String mobileNumber,
                                              String password, String username) {
        /*
        * Using for read the file
        * */
        super(name, surname, dateOfBirth);
        super.setMobileNumber(mobileNumber);
        this.password = password;
        this.username = username;

        doctorController = new controllers.Doctor();
        personController = new controllers.Person();
    }

    public WestminsterSkinConsultationManager(String name, String sureName, String password, String username) {
        /*
        * Using when adding new manager
        * */
        super(name, sureName);
        this.password = password;
        this.username = username;

        doctorController = new controllers.Doctor();
        personController = new controllers.Person();
    }

    public WestminsterSkinConsultationManager(String username, String password){
        this.username = username;
        this.password = password;

        doctorController = new controllers.Doctor();
        personController = new controllers.Person();
    }

    public void handlingDoctors(BufferedReader reader) throws IOException {

        boolean managerExpression = true;
        while (managerExpression) {
            try {
                print("|             "+(10-Doctor.getAvailableNumOfDoctors())+" Doctor Vacancies are available here            |\n"+
                        "|                "+(Doctor.getAvailableNumOfDoctors())   +" Doctors are available here                  |");
                System.out.println("|--------------------------------------------------------------|");
                System.out.println("|                      Select the option                       |");
                System.out.println("|--------------------------------------------------------------|");
                System.out.println("|              1. Delete a doctor from queue                   |");
                if (Doctor.getAvailableNumOfDoctors() != 10) {
                    System.out.println("|              2. Add a new doctor's details                   |");
                }
                System.out.println("|              3. Print the all doctor detail                  |");
                System.out.println("|              4. Edit a doctor detail                         |");
                System.out.println("|              5. Save in a file                               |");
                System.out.println("|              6. Exit                                         |");
                System.out.println("|--------------------------------------------------------------|");
                System.out.print("|                           ANSWER                             |:");

                int input2 = Integer.parseInt(reader.readLine().trim());

                if (input2 == 1) deleteDoctorProcess(reader);

                //this option working if any doctor vacancies is available
                else if (input2 == 2&& Doctor.getAvailableNumOfDoctors() !=10) addNewDoctorProcess(reader);
                else if (input2==3) printTheListOfDoctors();
                else if (input2==4) editDoctorDetails(reader);
                else if (input2 == 5) saveInFile("src/TXTFiles/DoctorDetails.txt");
                else if (input2 == 6) {
                    managerExpression = false;
                    saveInFile("src/TXTFiles/DoctorDetails.txt");
                    print("|                      Save in the file                        |:");
                }
                else inputMismatch();
            }catch (NumberFormatException e){
                //if entered value is not a number this will occur
                inputMismatch();
            }
        }
    }


    @Override
    public int addNewDoctor(String[] fullName, String mobileNumber, String address, String DOB, int specializationNumber, String medicalLicenceNumber ) {
        int i = 0;

        //this variable used to check and return value according to doctor added or not
        int checkDoctorObjectCreatedSuccessfully=0;
        try {
            //fullName is an Array that contains firstName and surName as elements
            String firstName = fullName[0];
            String surName = fullName[1];

            System.out.println("|--------------------------------------------------------------|");

            //checking is doctor exists. If exists this msg will print
            if (isDoctorObjectExist2(firstName,surName) ==8){
                System.out.println("|                     User already exists.                     |");
            }
            else {

                for (i = 0; i < 10; i++) {
                    Doctor doctor = DOCTOR_LIST[i];
                    // checking a position is null, to add a doctor
                    if (doctor==null){

                        //validating entered DOB and if its wrong this error will throw
                        if (!personController.checkDate(DOB)) {
                            inputMismatch();
                            print("|          Date of birth should be YYYY-MM-DD patters          |");
                            throw new InputMismatchException();

                        }
                        //validating entered mobile Number and if its wrong this error will throw
                        else if (!personController.checkMobile(mobileNumber)) {
                            print("|      The mobile phone number should contain 10 digits.       |");
                            throw new InputMismatchException();

                        }
                        //validating entered specialization number and if its wrong this error will throw
                        else if (!doctorController.checkDoctorSpecializationNumber(specializationNumber)) {
                            print("|      Entered specialization Number not meets require         |");
                            throw new InputMismatchException();

                        }
                        // if medical Licence number is null this will occur
                        else if (medicalLicenceNumber == null) {
                            throw new NullPointerException();
                        }
                        //All validates are only correct this else statements will execute
                        else {
                            //creating doctor object
                            doctor = new Doctor(firstName, surName, DOB, i);
                            // confirming doctor object created using checkDoctorObjectCreatedSuccessfully and updating it
                            checkDoctorObjectCreatedSuccessfully++;
                            doctor.setMobileNumber(mobileNumber);
                            doctor.setAddress(address);
                            doctor.setSpecialisation(specializationNumber);
                            doctor.setMedicalLicenceNumber(medicalLicenceNumber);

                            //adding created doctor into null position in array
                            DOCTOR_LIST[i] = doctor;
                            print("|                     Added successfully                       |");

                            //printing details of created doctor's details
                            doctorController.printingTheDoctorDetail(doctor);
                            break;
                        }
                    }
                }

            }

        }
        catch (InputMismatchException e) {
            //if doctor didn't create and doctor count got increased this will remove doctor counts
            //if validation part missed and another error occurred during setting the values into object this will help
            if (checkDoctorObjectCreatedSuccessfully>=1) {
                Doctor.removeTheDoctors();
                checkDoctorObjectCreatedSuccessfully=0;
            }
            print("|-------------------Canceled the operation---------------------|");

        }
        catch (NullPointerException e){
            print("|            Entered mobile number cannot be empty             |");
        }
        //if doctor added into the list this will return index position of the doctor in the array, otherwise it will return 10
        if (checkDoctorObjectCreatedSuccessfully>=1) return (i+1);
        else return 0;
    }

    @Override
    public boolean deleteDoctor(int doctorID, String doctorMedicalLicence) {
        /*
        * deleting doctor base on doctor's id and medical licence's number
        * */
        Doctor doctor;

        //using i variable, we can identify deleted or not
        int i = 0;

        //find the doctors index of in array, equals to doctor id and medicalLicense number
        int doctorIndex = findTheDoctor(doctorID, doctorMedicalLicence);

        //if doctor is exists doctorIndex isn't equals to 10
        if (doctorIndex!= 10) {
            //getting doctor object according to doctorIndex and printing the details
            doctor = DOCTOR_LIST[doctorIndex];

            //Again checking doctors details correct or not
            if (doctor != null && doctorID == doctor.getID() && Objects.equals(doctorMedicalLicence, doctor.getMedicalLicenceNumber())) {

                //if requirements meet doctor index will null
                DOCTOR_LIST[doctorIndex] = null;
                Doctor.removeTheDoctors();
                print("|                      Found the doctor                        |");
                doctorController.printingTheDoctorDetail(doctor);

                //if deleted i=1
                i = 1;
            }
        }

        //printing the deleted or not base on i according to updated value
        String print = i == 1? "|                    Deleted successfully                      |":
                "|                      No such a doctor                        |";
        print(print);

        //return deleted or not
        return i == 1;
    }

    @Override
    public boolean printTheListOfDoctors() {
        /*
        * Printing all details of doctors
        * */

        //if neither any data of doctors do print this will update
        int isPrinted = 0;

        //coping the array to sort, otherwise doctorList will sort and id will be wrong in the index positions
        Doctor[] temp = Arrays.copyOf(DOCTOR_LIST, DOCTOR_LIST.length);

        //using comparator copied array will sort
        Arrays.sort(temp, new DoctorComparator());
        for (int i = 0; i < 10; i++) {
            Doctor doctor = temp[i];

            //checking doctor array has null point or not. if doctor vacancy available this will leave behind
            if (doctor!=null){

                //if doctor isn't null will print the details of selected doctor by loop
                boolean k = doctorController.printingTheDoctorDetail(doctor);
                if (k) isPrinted =1;
            }
        }

        //will return details of print or not
        return isPrinted==1;
    }

    @Override
    public void saveInFile(String filePath) throws IOException {
        /*
        * will print details of doctors in selected given path
        * */
        FileWriter writer = new FileWriter(filePath);
        BufferedWriter bufferedWriter  = new BufferedWriter(writer);

        for (int i = 0; i < 10; i++) {
            Doctor doctor = DOCTOR_LIST[i];
            //checking doctor index is null or not to print
            if (doctor!=null){
                String write = "Medical licence number: "+doctor.getMedicalLicenceNumber()+"\n"+
                               "Name                  : "+doctor.getName()+" , "+doctor.getSurname()+"\n"+
                               "Mobile Number    : "+doctor.getMobileNumber()+"   || "+"DOB    : "+doctor.getDateOfBirth()+"\n"+
                               "Address          : "+doctor.getAddress()+"\n"+
                               "Specialisation Number : "+doctor.getSpecialisation().getNumberOfSpecialisation()+"\n\n\n";
                bufferedWriter.write(write);
                System.out.println(write);

            }
        }

        //using this printed keyword, we can identify when reading file again, lines are ended or not
        bufferedWriter.write("stop");
        bufferedWriter.close();
    }

    private void addNewDoctorProcess(BufferedReader reader) throws IOException {
        /*
        * using addNewDoctor method, this will get inputs from user and create new doctor
        * */

        try {

            print("|     If you want to cancel the function during operation      |\n|            Please type \"Cancel\" as the input                 |");
            print("|            Enter \"null\" if field is not required             |");

            //using setFullName method, can get input for name and surname and parse them as an Array
            String[] fullName = personController.setFullName(reader);

            //using setDoctorSpecializationNumber method, can get input base on specialization type and will return specialization according to inputted int
            int specializationNumber;
            do {
                specializationNumber = doctorController.setDoctorSpecializationNumber(reader);
            } while (!doctorController.checkDoctorSpecializationNumber(specializationNumber));

            String medicalLicenceNumber;
            while (true) {
                try {
                    //asking and reading medicalLicenceNumber. if it's empty, null will be return for medicalLicenceNumber
                     medicalLicenceNumber = readingNull(
                            enterYourDetails("|                  Enter your Medical License number           |", reader));
                    if (medicalLicenceNumber == null || medicalLicenceNumber.equals("") ||  !doctorController.checkMedicalLicenceExists(medicalLicenceNumber)) throw new NullPointerException();
                    break;
                } catch (NullPointerException e) {
                    //if entered medicalLicenceNumber is equal to empty string it value will null and this error will print.
                    // After that, will again ask for medical License number until give valid one
                    print("|            Entered licence number cannot be empty             |");
                    inputMismatch();
                }
            }

            //until entering valid mobile number, checkMobileNumber will false
            boolean checkMobileNumber = true;
            String mobileNumber = null;
            while (checkMobileNumber) {
                //will ask for input the mobile number.
                mobileNumber = enterYourDetails("|                      Enter your mobile number                |", reader);

                //if entered mobile number is valid , checkMobileNumber = flase and will stop loop and stop asking mobile number again and again
                if (personController.checkMobile(mobileNumber)) checkMobileNumber = false;
            }

            //asking and reading address. if it's empty, null will be return for address
            String address = readingNull(enterYourDetails("|                      Enter your address                      |:", reader));

            String DOB = null;
            //if entered DOB is in correct pattern this will false and end the asking of input again and again
            boolean checkDOB = true;
            while (checkDOB) {
                // this will ask input for DOB
                DOB = enterYourDetails("|            Enter your Date Of Birth (EX: 2001-06-29)         |:", reader);

                //checking the entered date is in correct format and can parse for correct data type.
                // if cant loop will continue, because checkDOB = true
                if (personController.checkDate(DOB)) checkDOB = false;
            }

            //If all the validations are correct and operation didn't cancel doctor will add using this method
            addNewDoctor(fullName, mobileNumber, address, DOB, specializationNumber, medicalLicenceNumber);
        }
        catch (InputMismatchException cancel){
            //if anyone typed "Cancel" during entering data, will throw this exception and cancel the adding operation
            print("|                     Exit operation                           |");
        }

    }

    private void deleteDoctorProcess(BufferedReader reader) {
        while (true){
            try {
                //printing doctor details to look at details to get and deleted
                printTheListOfDoctors();
                //parsing the id of doctor, if entered value is not an integer NumberFormatException will throw and ask again for id
                int doctorID = Integer.parseInt(enterYourDetails("|               Enter the ID number of the doctor              |:",reader));
                String doctorMedicalLicence = enterYourDetails("|               Enter the Medical Licence                      |:",reader);

                //base on got selected id and medicalLicence number, doctor will delete
                deleteDoctor(doctorID, doctorMedicalLicence);
                break;
            } catch (IOException | NumberFormatException ignored) {
                //if entered id is not an integer this will occur
                print("|                      No such a ID                            |");
            } catch (InputMismatchException cancel){
                //while someone entered "Cancel" when giving inputs, cancel the operation and this will print
                print("|                     Exit operation                           |");
                break;
            }
        }
    }

    public void editDoctorDetails(BufferedReader reader) {
        /*
        * will ask for what do you want edit and ask for inputs, and update the details
        * */

        //if enter the number required for exit. this will false
        boolean editDoctor = true;
        while (editDoctor){
            try {

                // will ask for id and doctorMedicalLicence, if that correct and same, details will update
                int doctorID = Integer.parseInt(enterYourDetails("|               Enter the ID number of the doctor              |:",reader));
                String doctorMedicalLicence = enterYourDetails("|               Enter the Medical Licence                      |:",reader);

                //finding the doctor that have same id and doctorMedicalLicence
                int doctorIndex = findTheDoctor(doctorID, doctorMedicalLicence);
                //if doctor didn't found number will equal to 10
                if (doctorIndex==10) print("|                      No such a doctor                        |");
                else {
                    //get doctor object base on index
                    Doctor doctor = DOCTOR_LIST[doctorIndex];
                    doctorController.printingTheDoctorDetail(DOCTOR_LIST[doctorIndex]);
                    print(  "|               Enter the field number you want edit           |");
                    System.out.print(
                            """
                                    |              1. Medical licence number                       |
                                    |              2. Name                                         |
                                    |              3. DOB                                          |
                                    |              4. Mobile Number                                |
                                    |              5. Address                                      |
                                    |              6. Specialisation Number                        |
                                    |              7. Exit                                         |
                                    """);

                    //ask for field number to update
                    switch (Integer.parseInt(enterYourDetails("|                    ANSWER                                    |:", reader))){
                        case 1 -> {
                            while (true){
                                //asking and reading medicalLicenceNumber. if it's empty, null will be return for medicalLicenceNumber
                                String medicalLicenceNumber = readingNull(enterYourDetails("|                  Enter your medical license number           |", reader));

                                //if entered medicalLicenceNumber is equal to empty string it value will null and this error will print.
                                // After that, will again ask for medical License number until give valid one
                                if (medicalLicenceNumber==null) inputMismatch();
                                else {
                                    //if entered medicalLicenceNumber is correct it will set to the object
                                    doctor.setMedicalLicenceNumber(medicalLicenceNumber);
                                    break;
                                }
                            }
                        }case 2 ->{
                            //asking for names and will return list of names
                            String[] fullName =personController.setFullName(reader);
                            doctor.setName(fullName[0]);
                            doctor.setSurname(fullName[1]);
                        }
                        case 3 ->{
                            while (true){
                                try {
                                    //asking DOB for correct pattern
                                    String DOB = enterYourDetails("|            Enter your Date Of Birth (EX: 2001-06-29)         |:", reader);
                                    //split for validate the date
                                    String[] splinted = DOB.split("-");

                                    //validating the date.
                                    //when splinted using "-", must have array of size 3 and length of year string is 4, otherwise will throw error
                                    if (splinted.length!=3 || splinted[0].length()!=4) throw new DateTimeParseException("Format wrong",DOB,3);
                                    LocalDate.parse(DOB);
                                    break;
                                } catch (DateTimeParseException e) {
                                    //if entered date pattern isn't correct this will occur
                                    inputMismatch();
                                    print("|          Date of birth should be YYYY-MM-DD patters          |");
                                }
                            }
                        }
                        case 4 ->{
                            while (true){
                                try {
                                    //asking mobile
                                    String mobileNumber = enterYourDetails("|                      Enter your mobile number                |", reader);
                                    //check mobile have exact 10 value and only contain numbers, otherwise this error will throw
                                    if (mobileNumber.length()!=10 || !mobileNumber.matches("[0-9]+")) throw new NumberFormatException();
                                    doctor.setMobileNumber(mobileNumber);
                                    break;
                                } catch (NumberFormatException e) {
                                    //if entered number is wrong this will execute
                                    inputMismatch();
                                    print("|      The mobile phone number should contain 10 digits.       |");
                                }
                            }
                        }
                        case 5 -> doctor.setAddress(readingNull(enterYourDetails("|                      Enter your address                      |:", reader)));
                        case 6 -> {
                            //asking input and validation will do by this
                            int specializationNumber = doctorController.setDoctorSpecializationNumber(reader);

                            //if canceled the operation by user else will occur
                            if (specializationNumber!=0) doctor.setSpecialisation(specializationNumber);
                            else print("|                  Canceled the operation                      |");
                        }
                        case 7 ->{
                            editDoctor=false;
                            print("|                           Exit                               |");
                        }
                    }

                    doctorController.printingTheDoctorDetail(DOCTOR_LIST[doctorIndex]);
                }
                break;
            } catch (IOException | NumberFormatException ignored) {
                //if entered one of fields that required integer, is not an integer this will occur
                print("|                      No such a ID                            |");
            } catch (InputMismatchException cancel){
                //while someone entered "Cancel" when giving inputs, cancel the operation and this will print
                print("|                     Exit operation                           |");
                break;
            }
        }
    }

    public static void readFile(String filePath) throws IOException{
        FileReader reader = new FileReader(filePath);
        BufferedReader buffer = new BufferedReader(reader);

        String name = "", surname = "", address = "",
                medicalLicenceNumber = "", mobileNumber= "", dateOfBirth = "";
        int specialisation = 0;

        int i=0;

        String line;
        while(true){
            i++;
            line=buffer.readLine().trim();
            if (line.equals("stop")) break;
            switch (i % 7) {
                case 1 -> medicalLicenceNumber = line.split(":")[1].trim();
                case 2 -> {
                    String fullName = line.split(":")[1];
                    name = fullName.split(",")[0].trim();
                    surname = fullName.split(",")[1].trim();
                }
                case 3 -> {
                    String[] splinted = line.split("\\|\\|");
                    mobileNumber = splinted[0].split(":")[1].trim();
                    dateOfBirth = splinted[1].split(":")[1].trim();
                }
                case 4 -> address = line.split(":")[1].trim();
                case 5 -> specialisation = Integer.parseInt(line.split(":")[1].trim());
                case 0 -> {
                    Doctor doctor = new Doctor(name, surname, dateOfBirth, medicalLicenceNumber, specialisation);
                    doctor.setAddress(address);
                    doctor.setMobileNumber(mobileNumber);
                    int added = 0;
                    for (int j = 0; j < 10; j++) {
                        if (DOCTOR_LIST[j]==null) {
                            DOCTOR_LIST[j] = doctor;
                            doctor.setID(j+1);
                            added = 1;
                            System.out.println("Added Medical License Number :"+ doctor.getMedicalLicenceNumber());
                            break;
                        }
                    }
                    if (added == 0) System.out.println("The queue is full of doctors.");
                }
            }

        }
        buffer.close();
    }

    private static int findTheDoctor(int ID, String medicalLicence){
        /*
        * Finding the doctor objects index equals to doctor id and medical license number
        * */
        for (int i = 0; i < 10; i++) {
            Doctor doctor = DOCTOR_LIST[i];
            // when going through in the loop if medical license number and id equals to givens, it will return index position in array
            if (doctor!=null && medicalLicence.trim().equals(doctor.getMedicalLicenceNumber())){
                if (doctor.getID()==ID) return i;
            }
        }
        // if any doctor isn't available for that id and medical license, will return 10
        return 10;
    }

    public static int isDoctorObjectExist2(String firstName, String sureName){
        /*
        * Checking doctor is exists according to entered name
        * */
        for (int i = 0; i <4 ; i++) {
            Doctor doctor = DOCTOR_LIST[i];
            if (doctor==null) continue;
            if (doctor.getName().equalsIgnoreCase(firstName) &&
                    doctor.getSurname().equalsIgnoreCase(sureName)) {
                // if doctor is exists will return 8
                return 8;
            }
        }
        //if any doctor isn't have same names this will return
        return 10;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public static Doctor[] getDoctorList() {
        return DOCTOR_LIST;
    }

    @Override
    public String toString() {
        return  "WestminsterSkinConsultationManager{" +"\n"+
                "name='" + super.getName() + "\n" +
                "surname='" + super.getSurname() + "\n" +
                "dateOfBirth=" + super.getDateOfBirth() + "\n" +
                "mobileNumber='" + super.getMobileNumber() + "\n" +
                "address='" + super.getAddress() + "\n" +
                "password='" + password + "\n" +
                "username='" + username + "\n" +
                "\n\n";
    }

}
