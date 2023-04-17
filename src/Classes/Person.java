package Classes;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Objects;

public class Person implements Serializable {
    @Serial
    private static final long serialVersionUID = -3467688883417965279L;
    private String name;
    private String surname;

    //I used LocalDate because only using this I can only use Period Class and counting the age
    private LocalDate dateOfBirth;
    private String mobileNumber;
    private String address;

    protected Person(String name, String surname, String dateOfBirth) {
        /*
        * if date of birth parse as string it will parse and set dateOfBirth into LocalDate type
        * */
        String firstName = name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase();
        String surName = surname.substring(0,1).toUpperCase()+surname.substring(1).toLowerCase();
        this.name = firstName;
        this.surname = surName;
        setDateOfBirth(dateOfBirth);
    }
    protected Person(String name, String surname, Date dateOfBirth) {
        /*
         * if date of birth parse as Date it will parse and set dateOfBirth into LocalDate type
         * */
        String firstName = name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase();
        String surName = surname.substring(0,1).toUpperCase()+surname.substring(1).toLowerCase();
        this.name = firstName;
        this.surname = surName;
        setDateOfBirth(dateOfBirth);
    }
    protected Person(){}
    protected Person(String name, String surname) {
        /*
         * if date of birth parse as Date it will parse and set dateOfBirth into LocalDate type
         * */
        String firstName = name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase();
        String surName = surname.substring(0,1).toUpperCase()+surname.substring(1).toLowerCase();
        this.name = firstName;
        this.surname = surName;
    }

    public void setDateOfBirth(String dateOfBirth) {
        /*
        * Parsing string and validate it for ensure it can parse into LocalDate
        * */
        String[] splinted = dateOfBirth.split("-");

        //If entered date's pattern is wrong this if condition will execute and will throw DateTimeParseException
        //pattern is YYYY-MM-DD. so when splinted the input using "-", array size must be 3 and first indexed length must be 4

        if ((splinted.length!=3 || splinted[0].length()!=4) &&
                //checking months in the valid range
                (Integer.parseInt(splinted[1])>13 && Integer.parseInt(splinted[1])<0) &&
                //checking dates in the valid range
                (Integer.parseInt(splinted[2])>32 && Integer.parseInt(splinted[2])<0)
        ) {

            //If any validation is got an error this error will throw
            throw new DateTimeParseException("Format wrong", dateOfBirth, 3);
        }
        //if any wrong isn't there error won't throw and date will parse correctly
        this.dateOfBirth = LocalDate.parse(dateOfBirth);
    }

    public void setDateOfBirth(Date dateOfBirth){
        /*
        * parsing dateOfBirth as a Date type and will be allocated as LocalDate type
        * */
        this.dateOfBirth =Instant.ofEpochMilli(dateOfBirth.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public String getAge(){
        /*
        * Get counting age from given date of birth to today
        * */
        LocalDate today = LocalDate.now();

        //Giving starting date and ending date to get Period between two dates
        Period period = Period.between(this.dateOfBirth,today);
        return "Years : "+period.getYears()+" Months : "+period.getMonths()+" Days : "+period.getDays();
    }

    public void setMobileNumber(String mobileNumber) {
        /*
        * Checking the meets of requirements to validate the mobile number
        * */

        //if parsed mobile number length is 10 and only contains numerics this won't occur. if this occurs error will throw
        if (mobileNumber.length()!=10 || !mobileNumber.matches("[0-9]+")) throw new NumberFormatException("Not a Phone Number!!!");
        else this.mobileNumber = mobileNumber;
    }

    public void setAddress(String address) {
        /*
        * If parsed address string is empty address value will be assigned as null
        * */
        if (Objects.equals(address, "")) this.address=null;
        else this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        /*
         * parsed name's first letter will do uppercase
         * */
        this.name = name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase();
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        /*
        * parsed surname's first letter will do uppercase
        * */
        this.surname = surname.substring(0,1).toUpperCase()+surname.substring(1).toLowerCase();
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return  "\nPerson{" +
                "\nname                 : " + name +
                "\nsurname              : " + surname +
                "\ndateOfBirth          : " + dateOfBirth.toString() +
                "\nmobileNumber         : " + mobileNumber +
                "\naddress              : " + address +
                "}\n\n";
    }
}