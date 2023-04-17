package GUI.table;

import Classes.Consultation;
import Classes.Doctor;
import Classes.Patient;

import javax.swing.table.AbstractTableModel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserTableModel extends AbstractTableModel {

    private final String[] columnNames = {"Appointment\n ID", "Patient","P.ID","Date","Time", "Doctor", "Cost","Ward\n Num"};
    private ArrayList<Consultation> myList;

    public UserTableModel(ArrayList<Consultation> list) {
        myList = new ArrayList<>();
        this.fireTableDataChanged();
        Date date = new Date();
        for (Consultation c :
                list) {
            if (date.before(c.getDate())) {
                myList.add(c);
            }
        }
    }

    public Object getValueAt(int row, int col) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
        Object temp = null;
        if (col == 0) {
            temp = myList.get(row).getAppointmentID();
        }
        else if (col == 1) {
            Patient p = myList.get(row).getPATIENT();
            temp = " "+p.getName()+" "+p.getSurname();
        } else if (col == 2) {
            temp = String.valueOf(myList.get(row).getPATIENT().getPatientID());
        } else if (col == 3) {
            temp = dateFormat.format(myList.get(row).getDate());
        }
        else if (col == 4){
            temp = timeFormat.format(myList.get(row).getDate());
        }
        else if (col == 5) {
            Doctor d = myList.get(row).getDoctor();
            temp = " "+d.getName()+" "+d.getSurname();
        }
        else if (col == 6) {
            temp = String.format("£%.2f", myList.get(row).getCost());
        }
        else if (col == 7) {
            temp = String.format("%02d",myList.get(row).getDoctor()
                    .getSpecialisation().getNumberOfSpecialisation());
        }
        return temp;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Class<?> getColumnClass(int col) {
        if (col == 0) {
            return Integer.class;
        }
        else return String.class;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return myList.size();
    }

    public void setMyList(ArrayList<Consultation> myList) {
        Date date = new Date();
        this.myList = new ArrayList<>();
        for (Consultation c : myList) {
            if (date.before(c.getDate())) {
                this.myList.add(c);
            }
        }
        this.fireTableDataChanged();
    }
}
