package sample.model;

public class Patient extends User {
    private BloodGroup bloodGroup ;
    private String allergies;

    public Patient() {
        setPhoneNumber("null");
        setAllergies("Non");
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    @Override
    public String toString() {
        return super.toString()+"~"+bloodGroup + "~"+ allergies;
    }
}
