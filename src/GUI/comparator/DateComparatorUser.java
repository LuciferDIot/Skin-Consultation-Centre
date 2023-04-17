package GUI.comparator;

import Classes.Consultation;

import java.util.Comparator;
import java.util.Date;

public class DateComparatorUser implements Comparator<Consultation> {
    @Override
    public int compare(Consultation o1, Consultation o2) {
        if (o1==null || o2==null) return -1;
        Date s1 = o1.getDate();
        Date s2 = o2.getDate();
        if (s1.equals(s2))
            return 0;
        else if (s1.compareTo(s2) > 0)
            return 1;
        else
            return -1;
    }
}
