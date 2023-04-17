package controllers;

import Classes.WestminsterSkinConsultationManager;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Classes.ManagerOptions.*;
import static Classes.ManagerOptions.enterYourDetails;

public class Person {
    private final Consultation consultation;

    public Person(){
        consultation = new Consultation();
    }


    public String[] setFullName(BufferedReader reader) throws IOException {
        String firstName,surName;
        while (true){
            firstName = readingNull(enterYourDetails("|                      Enter your first name                   |", reader));
            surName = readingNull(enterYourDetails("|                      Enter your surname                      |", reader));
            if (firstName!=null && surName!=null) {
                if (!(isEmptyAndOnlyLetters(firstName) && isEmptyAndOnlyLetters(surName))) continue;
                if (checkHaveSameName(firstName, surName)) continue;
            }else {
                print("|                       Cant be null                           |");
                continue;
            }
            break;
        }

        // After getting inputs this will return fields as List
        return new String[]{firstName, surName};
    }

    private boolean checkHaveSameName(String firstName, String surName){
        Classes.Doctor[] doctors = WestminsterSkinConsultationManager.getDoctorList();
        for (int i=0;i<doctors.length;i++){
            Classes.Doctor d = doctors[i];
            if (d!=null) {
                if (firstName.equalsIgnoreCase(d.getName()) &&
                        surName.equalsIgnoreCase(d.getSurname())) {
                    print("|                        User exists                           |");
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkMobile(String mobileNumber, Component y){
        /*
         * Mobile number validating
         * */
        try {
            //checking mobile number length and is there includes letters other than numbers
            //Is there includes any of them NumberFormatException will throw and catch will execute
            if (mobileNumber.length()!=10 || !mobileNumber.matches("[0-9]+")) {
                throw new NumberFormatException();
            }else {
                //if all the requirements are meets, will return true
                return true;
            }
        } catch (NumberFormatException e) {
            //If entered number didn't meet requirements this will execute and will return false
            inputMismatch();
            print("|      The mobile phone number should contain 10 digits.       |");
            JOptionPane.showConfirmDialog(y, "Please enter correct mobile number", "Warning", JOptionPane.OK_CANCEL_OPTION);
            return false;
        }
    }


    public boolean checkMobile(String mobileNumber){
        /*
         * Mobile number validating
         * */
        try {
            //checking mobile number length and is there includes letters other than numbers
            //Is there includes any of them NumberFormatException will throw and catch will execute
            if (mobileNumber.length()!=10 || !mobileNumber.matches("[0-9]+")) throw new NumberFormatException();

            //if all the requirements are meets, will return true
            return true;
        } catch (NumberFormatException e) {
            //If entered number didn't meet requirements this will execute and will return false
            inputMismatch();
            print("|      The mobile phone number should contain 10 digits.       |");
            return false;
        }
    }

    public boolean checkDate(String DOB){
        /*
         * Entered date validation
         * */
        try {
            //If entered date's pattern is wrong this if condition will execute and will throw DateTimeParseException
            //pattern is YYYY-MM-DD. so when splinted the input using "-", array size must be 3 and first indexed length must be 4
            String[] splinted = DOB.split("-");
            if (splinted.length!=3 || splinted[0].length()!=4) throw new DateTimeParseException("Format wrong",DOB,3);

            //if above condition parsed, this will occur and, will ensure entered date of birth is can parse into local date
            //also we can check is anyone entered a letter or not. if someone entered a letter this will throw an error
            LocalDate.parse(DOB);

            //if above all test are passed true will return
            return true;
        }
        catch (DateTimeParseException e) {
            // if entered date have an error will print this and return false
            inputMismatch();
            print("|          Date of birth should be YYYY-MM-DD patters          |");
            return false;
        }
    }

    public boolean isSelected(String txt, Component y){
        //if fields are empty this will pop up
        if (txt.equals("")) {
            JOptionPane.showConfirmDialog(y, "Required fields are empty","Warning", JOptionPane.OK_CANCEL_OPTION);
            return false;
        }
        //if any doctor didn't select this will pop up
        else if (txt.equals("None")) {
            JOptionPane.showConfirmDialog(y, "Please select a doctor", "Warning", JOptionPane.OK_CANCEL_OPTION);
            return false;
        }
        //if you don't have time slot selected this will pop up
        else if (txt.equals("00 : 00 PM")) {
            JOptionPane.showConfirmDialog(y, "Please select time you booking", "Warning", JOptionPane.OK_CANCEL_OPTION);
            return false;
        } else return true;
    }
    public boolean isEmptyAndOnlyLetters(String txt, Component y){
        //if fields are empty this will pop up
        if (txt.equals("")) {
            JOptionPane.showConfirmDialog(y, "Required fields are empty","Warning", JOptionPane.OK_CANCEL_OPTION);
            return false;
        }
        else {
            // contains only digits
            String regex = "[A-Za-z]+";

            // Compile the ReGex
            Pattern p = Pattern.compile(regex);

            // Find match between given string
            // and regular expression
            // using Pattern.matcher()
            Matcher m = p.matcher(txt);
            if (!m.matches()){
                //if fields not only contain letters this will pop up
                JOptionPane.showConfirmDialog(y, "Name fields can only contains letters", "Warning", JOptionPane.OK_CANCEL_OPTION);
                return false;
            }else return true;
        }
    }
    public boolean isEmptyAndOnlyLetters(String txt){
        //if fields are empty this will pop up
        if (txt.equals("")) {
            print("|                       Required fields are empty              |");
            return false;
        }
        else {
            // contains only digits
            String regex = "[A-Za-z]+";

            // Compile the ReGex
            Pattern p = Pattern.compile(regex);

            // Find match between given string
            // and regular expression
            // using Pattern.matcher()
            Matcher m = p.matcher(txt);
            if (!m.matches()){
                //if fields not only contain letters this will pop up
                print("|               Name fields can only contains letters          |");
                return false;
            }else return true;
        }
    }


    public boolean isEmpty(String txt, Component y) {
        if (txt.equals("")) {
            JOptionPane.showConfirmDialog(y, "Required fields are empty","Warning", JOptionPane.OK_CANCEL_OPTION);
            return false;
        }else return true;
    }


    public boolean isDateOfBirthBeforeMonth(Date DOB_Date, Component y) throws ParseException {
        Date timeBeforeHourFromNow = consultation.getEndTime(new Date(), -(24*31));
        if (!timeBeforeHourFromNow.after(DOB_Date)) {
            JOptionPane.showConfirmDialog(y, "Date of birth must be at least month before from now", "Warning", JOptionPane.OK_CANCEL_OPTION);
            return false;
        }else return true;
    }

    public String[] checkAndGetInputOfDetails(BufferedReader reader)throws IOException{
        /*
         * This will get mobile number, address, date of birth as an input
         * */
        String mobileNumber = null,address,DOB = null;
        try {
            // Checking input mobile number correct and will update isMobileNumCorrect variable
            boolean isMobileNumCorrect = true;
            while (isMobileNumCorrect){

                // Getting input and assign it to the variable
                mobileNumber = enterYourDetails("|                      Enter your mobile number                |", reader);

                // Checking mobile numbers is valid and updating it
                if (checkMobile(mobileNumber)) isMobileNumCorrect = false;
            }

            // Getting input and assign it to the variable
            address =readingNull(enterYourDetails("|                      Enter your address                      |:", reader));

            // Checking input date of birth correct and will update is_DOB_Correct variable
            boolean is_DOB_Correct = true;
            while (is_DOB_Correct){

                // Getting input and assign it to the variable
                DOB = enterYourDetails("|            Enter your Date Of Birth (EX: 2001-06-29)         |:", reader);

                // Checking date is valid and updating it
                if (checkDate(DOB)) is_DOB_Correct = false;
            }

        }catch (InputMismatchException Ignore){
            // inputted fields in enterYourDetails method, entered value equals to "Cancel",
            // this error will occur and again occur to catch in the method that used this one
            throw new InputMismatchException();
        }

        // if entered value not equals to "Cansel" all the data will transfer as a List
        return new String[]{mobileNumber,DOB,address};
    }

    public String nameCapitalising(String name){
        //When user entered two names separated by space as input this method using
        String n = name;
        StringBuilder s = new StringBuilder();

        //Splitting the name using spaces
        String[] split = name.split(" ");

        //if user entered name doesn't have spaces it won't split and split array size would be 0
        //We're checking split or user didn't enter anything
        if (split.length>0 && !name.equals("")) {
            //going through splinted array and using substring method capitalizing first letter of each word of name
            for (String value : split) {
                String temp = value.substring(0, 1).toUpperCase()+ value.substring(1) + " ";
                // Using StringBuilder we can append string to string
                s.append(temp);
            }
            // All appended stringBuilder values turning into String and assigning it into n variable
            n = s.toString().trim();
        }

        //Will return names that all the first letter turned into capital
        return n;
    }

    public Date getDateOfBirth(LocalDate localDate){
        /*
         * converting LocalDate into Date class
         * */

        //getting default zone id to use it for date class cause of localDate only can parse it into Date using that pattern
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
    }

}
