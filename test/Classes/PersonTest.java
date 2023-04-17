package Classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    Person person;
    private SimpleDateFormat format;
    private static controllers.Person personController;

    @BeforeEach
    void setUp(){
        person = new Person();
        format = new SimpleDateFormat("yyyy-MM-dd");
        personController = new controllers.Person();
    }

    @Test
    void checkEnteringWrongPatternDateOfBirth(){
        // Pattern must be "yyyy-MM-dd" for converting string date into LocalDate type
        assertThrows(DateTimeParseException.class,()-> person.setDateOfBirth("01-04-2001"));
    }

    @Test
    void checkDateTypeDOBSettingAndGetting() throws ParseException {
        //setting dob into Date class object and check whether it's correctly setting to the person object
        /*
        This method checking method,
         1. setDateOfBirth(Date dateOfBirth): void
         2. getDateOfBirth(boolean dateClassReturned): Date
        */
        String dobString = "2001-04-10";
        Date dob = format.parse(dobString);
        person.setDateOfBirth(dob);

        Date returnedDOB = personController.getDateOfBirth(person.getDateOfBirth());
        assertEquals(returnedDOB,dob);
    }

    @Test
    void checkGetAgeMethod() {
        // setting dob for person object
        String dobString = "2001-04-10";
        LocalDate dob = LocalDate.parse(dobString);
        person.setDateOfBirth(dobString);

        //getting period between today and given dob using Period class
        LocalDate today = LocalDate.now();
        Period period = Period.between(dob,today);

        String age = "Years : "+period.getYears()+" Months : "+period.getMonths()+" Days : "+period.getDays();
        assertEquals(age, person.getAge());
    }

    @Test
    void checkAndAddingMobileNumber(){
        //Adding wrong mobile numbers two times and checking getting error

        //Less than 10 digits that have mobile number
        String lessMobileNumber = "0719";
        assertThrows(NumberFormatException.class, ()-> person.setMobileNumber(lessMobileNumber));

        //Greater than 10 digits that have mobile number
        String greaterMobileNumber = "0716201390130";
        assertThrows(NumberFormatException.class, ()-> person.setMobileNumber(greaterMobileNumber));

        //enter correct mobile Number and check it added
        String mobileNumber = "0786201390";
        person.setMobileNumber(mobileNumber);
        assertEquals(mobileNumber, person.getMobileNumber());
    }

    @Test
    void checkNullAddingToAddress(){
        //if address string entered as empty it must add as null value
        String address = "";
        person.setAddress(address);

        assertNull(person.getAddress());
    }

    @Test
    void enteredNameCapitalizing(){
        //user can have added multiple words of name
        //using public static String nameCapitalising(String name) method, first letter of each word will capitalize
        String fullName = "pasindu geevinda";
        String capitalizedName = personController.nameCapitalising(fullName);

        String expected = "Pasindu Geevinda";
        assertEquals(expected, capitalizedName);
    }

    @Test
    void checkEnteredDatePatternCorrect() {
        //1. check entering wrong pattern getting error and return as false
        assertFalse(personController.checkDate("01-04-2001"));

        //2. checking returns true
        assertTrue(personController.checkDate("2001-06-29"));
    }

    @Test
    void checkMobileValidity(){
        //Adding wrong mobile numbers two times and checking getting error

        //Less than 10 digits that have mobile number
        String lessMobileNumber = "0719";
        assertFalse(personController.checkMobile(lessMobileNumber));

        //Greater than 10 digits that have mobile number
        String greaterMobileNumber = "0716201390130";
        assertFalse(personController.checkMobile(greaterMobileNumber));

        //enter correct mobile Number and check it added
        String mobileNumber = "0786201390";
        assertTrue(personController.checkMobile(mobileNumber));
    }
}