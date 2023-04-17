package GUI.comparator;

import Classes.Doctor;

import java.util.Comparator;

public class AlphabeticComparatorDoctor implements Comparator<Doctor> {
    @Override
    public int compare(Doctor o1, Doctor o2) {
        if (o1==null || o2==null) return -1;
        String s1 = o1.getName()+" "+o1.getSurname();
        String s2 = o2.getName()+" "+o2.getSurname();
        if (s1.equals(s2))
            return 0;
        else if (s1.compareTo(s2) > 0)
            return 1;
        else
            return -1;
    }

}
