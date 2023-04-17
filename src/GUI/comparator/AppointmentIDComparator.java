package GUI.comparator;

import Classes.Consultation;

import java.util.Comparator;

public class AppointmentIDComparator implements Comparator<Consultation> {
    @Override
    public int compare(Consultation o1, Consultation o2) {
        if (o1==null || o2==null) return -1;
        int s1 = o1.getAppointmentID();
        int s2 = o2.getAppointmentID();
        return Integer.compare(s1, s2);
    }

}
