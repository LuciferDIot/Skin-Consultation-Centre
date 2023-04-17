package GUI.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DatesPanel extends JPanel implements ItemListener {
    private JComboBox dateYear, dateMonth, dateDate, DOBYear, DOBMonth, DOBDate;
    boolean dateBoolean = false,DOB = false;

    DatesPanel(){
        dateOfBirthInitializer();
        dateInitializer();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.ipadx = 5;
        gbc.insets = new Insets(0,0,10,0);
        gbc.gridy =0;
        gbc.gridx =0;
        add(DOBYear,gbc);

        gbc.gridx =1;
        add(DOBMonth,gbc);

        gbc.gridx =2;
        add(DOBDate,gbc);
        setBackground(Color.BLACK);

        gbc.insets = new Insets(0,0,0,0);
        gbc.gridy =1;
        gbc.gridx =0;
        add(dateYear,gbc);

        gbc.gridx =1;
        add(dateMonth,gbc);

        gbc.gridx =2;
        add(dateDate,gbc);

        dateDate.addItemListener(this);
        DOBDate.addItemListener(this);
        dateMonth.addItemListener(this);
        DOBMonth.addItemListener(this);
    }
    private void dateOfBirthInitializer(){
        LocalDate currentDate = LocalDate.now();

        String[] year = new String[90];
        int currentYear = currentDate.getYear();
        for (int i = 0; i < year.length; i++) {
            year[i] = String.valueOf(currentYear--);
        }
        DOBYear = new JComboBox<>(year);
        DOBMonth = monthInitializer();
        DOBDate = noOfDateInitializer();



    }

    private void dateInitializer(){
        LocalDate currentDate = LocalDate.now();

        String[] year = new String[5];
        int currentYear = currentDate.getYear();
        for (int i = 0; i < year.length; i++) {
            year[i] = String.valueOf(currentYear++);
        }

        dateYear = new JComboBox<>(year);
        dateMonth = monthInitializer();
        dateDate = noOfDateInitializer();

    }

    private JComboBox monthInitializer(){
        String[] month = new String[12];
        for (int i = 0; i < month.length; i++) {
            month[i] = String.valueOf(i+1);
        }
        return new JComboBox<>(month);
    }

    private JComboBox noOfDateInitializer(){
        String[] days = new String[31];
        for (int i = 0; i < days.length; i++) {
            days[i] = String.valueOf(i+1);
        }
        return new JComboBox<>(days);
    }

    public static int getMaxDaysInMonth(int month, int year){

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, (month-1));
        cal.set(Calendar.YEAR, year);

        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);

    }

    public Date getSelectedDate() throws ParseException {
        int date = Integer.parseInt((String) Objects.requireNonNull(dateDate.getSelectedItem()));
        int month = Integer.parseInt((String) Objects.requireNonNull(dateMonth.getSelectedItem()));
        int year = Integer.parseInt((String) Objects.requireNonNull(dateYear.getSelectedItem()));
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        String dateString = date+"/"+month+"/"+year;
        return format.parse(dateString);
    }

    public Date getSelectedDOB() throws ParseException {
        int date = Integer.parseInt((String) Objects.requireNonNull(DOBDate.getSelectedItem()));
        int month = Integer.parseInt((String) Objects.requireNonNull(DOBMonth.getSelectedItem()));
        int year = Integer.parseInt((String) Objects.requireNonNull(DOBYear.getSelectedItem()));
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        String dateString = date+"/"+month+"/"+year;
        return format.parse(dateString);
    }

    public boolean isDOB() {
        return DOB;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource()==dateDate || e.getSource()==dateMonth){
            int date = Integer.parseInt((String) Objects.requireNonNull(dateDate.getSelectedItem()));
            int month = Integer.parseInt((String) Objects.requireNonNull(dateMonth.getSelectedItem()));
            int year = Integer.parseInt((String) Objects.requireNonNull(dateYear.getSelectedItem()));
            int maxDay = getMaxDaysInMonth(month, year);

            if (date>maxDay) {
                JOptionPane.showMessageDialog(this, "Selected month don't have date like this");
                dateDate.setSelectedItem(String.valueOf(maxDay));
                dateBoolean = false;
            }else dateBoolean = true;
        }

        else if (e.getSource()==DOBDate || e.getSource()==DOBMonth){
            int date = Integer.parseInt((String) Objects.requireNonNull(DOBDate.getSelectedItem()));
            int month = Integer.parseInt((String) Objects.requireNonNull(DOBMonth.getSelectedItem()));
            int year = Integer.parseInt((String) Objects.requireNonNull(DOBYear.getSelectedItem()));
            int maxDay = getMaxDaysInMonth(month, year);

            if (date>maxDay) {
                JOptionPane.showMessageDialog(this, "Selected month don't have date like this");
                DOBDate.setSelectedItem(String.valueOf(maxDay));
                DOB =false;
            }else DOB=true;
        }
    }

    public void setDOBValue(Date dateOfBirth) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateOfBirth);

        DOBDate.setSelectedItem(String.valueOf(cal.get(Calendar.DATE)));
        DOBMonth.setSelectedItem(String.valueOf(cal.get(Calendar.MONTH)+1));
        DOBYear.setSelectedItem(String.valueOf(cal.get(Calendar.YEAR)));
    }

    public void setDateValue(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        dateDate.setSelectedItem(String.valueOf(cal.get(Calendar.DATE)));
        dateMonth.setSelectedItem(String.valueOf(cal.get(Calendar.MONTH)+1));
        dateYear.setSelectedItem(String.valueOf(cal.get(Calendar.YEAR)));
    }
}
