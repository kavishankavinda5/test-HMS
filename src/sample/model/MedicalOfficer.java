package sample.model;

import java.util.List;

public class MedicalOfficer extends Employee{
    private String speciality;

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    @Override
    public String toString() {
        return super.toString()+"~"+speciality;
    }
}
