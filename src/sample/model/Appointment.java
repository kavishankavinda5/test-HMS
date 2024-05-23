package sample.model;

import java.time.LocalDate;

public class Appointment {
    private int appointmentID;
    private Patient patient;
    private LocalDate appointmentDate;
    private AppointmentTime appointmentTime;
    private MedicalOfficer medicalOfficer;
    private AppointmentStatus  appointmentStatus;
    private String symtomes;

    public Appointment() {
        appointmentID = 0;
        appointmentStatus =AppointmentStatus.PENDING;

    }


    public String getSymtomes() {
        return symtomes;
    }

    public void setSymtomes(String symtomes) {
        this.symtomes = symtomes;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public AppointmentTime getTime() {
        return appointmentTime;
    }

    public void setTime(AppointmentTime timeModel) {
        this.appointmentTime = timeModel;
    }

    public MedicalOfficer getMedicalOfficer() {
        return medicalOfficer;
    }

    public void setMedicalOfficer(MedicalOfficer medicalOfficer) {
        this.medicalOfficer = medicalOfficer;
    }

    public AppointmentStatus getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    @Override
    public String toString() {
        return appointmentID+"~"+
                patient.getName()+"%%"+
                patient.getIdCardNumber() + "%%" +
                patient.getPhoneNumber()+"~"+
                appointmentDate + "~" +
                appointmentTime + "~" +
                medicalOfficer.getName()+ "%%"+
                medicalOfficer.getIdCardNumber()  + "%%" +
                medicalOfficer.getSpeciality()+ "~" +
                appointmentStatus +"~"+
                symtomes;
    }

}
