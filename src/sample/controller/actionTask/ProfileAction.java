package sample.controller.actionTask;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import sample.controller.taskControllers.ProfileViewController;
import sample.model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ProfileAction {

    public static void updateCurrentUserData(SystemUser systemUser){
        editUserAccount(systemUser);
    }

    private static void editUserAccount(SystemUser systemUser){
        switch (systemUser.getUserRoll()){
            case ADMIN:
                editAdminRecord(systemUser.getAdmin());
                saveAdminPhoto_File(systemUser.getAdmin());
                break;
            case PATIENT:
                editPatientRecord(systemUser.getPatient());
                savePatientPhoto_File(systemUser.getPatient());
                break;
            case RECEPTIONIST:
                editReceptionistRecord(systemUser.getReceptionist());
                saveReceptionistPhoto_File(systemUser.getReceptionist());
                break;
            case MEDICALOFFICER:
                editMedicalOfficer(systemUser.getMedicalOfficer());
                saveMedicalOfficerPhoto_File(systemUser.getMedicalOfficer());
                break;
            default:
                break;
            }
        }

    private static void editMedicalOfficer(MedicalOfficer medicalOfficer) {
        LoginUser loginUser=new LoginUser();
        loginUser.setUserPassword(medicalOfficer.getUserPassword());
        loginUser.setUserName(medicalOfficer.getUserName());
        UserAction.updateMedicalOfficerRecord(medicalOfficer.getUserRoll(),medicalOfficer,medicalOfficer.getIdCardNumber(),loginUser);
    }

    private static void editReceptionistRecord(Receptionist receptionist) {
        LoginUser loginUser =new LoginUser(receptionist.getUserName(),receptionist.getUserPassword());
        UserAction.updateReceptionRecord(receptionist.getUserRoll(),receptionist,receptionist.getIdCardNumber(),loginUser);
    }

    private static void editPatientRecord(Patient patient) {
        LoginUser loginUser=new LoginUser(patient.getUserName(),patient.getUserPassword());
        UserAction.updatePatientRecord(patient.getUserRoll(),patient,patient.getIdCardNumber(),loginUser);
    }

    private static void editAdminRecord(Admin admin) {
        LoginUser loginUser =new LoginUser(admin.getUserName(),admin.getUserPassword());
        UserAction.updateAdmin(admin.getUserRoll(),admin,admin.getIdCardNumber(),loginUser);
    }

    //write a method for save admin photo and file
    private static void saveAdminPhoto_File(Admin admin){

        try{

            String staffId = admin.getIdCardNumber();

            if (admin.getPhotoPath()!=null){
                String moPhotoPath ="src/sample/fileStorage/moduleData/userData/userPhoto/admin";
                String photoSavePath =moPhotoPath + "\\" + staffId + ".jpg";

                File imagefile = new File(admin.getPhotoPath());
                BufferedImage image = ImageIO.read(imagefile);
                ImageIO.write(image, "jpg",new File(photoSavePath));
                System.out.println("Save PHOTO");
            }else {
                System.out.println("No Photo");
            }

            if (admin.getFilePath()!=null){
                String moFilePath ="src/sample/fileStorage/moduleData/userData/userFile/admin";
                String fileSavePath = moFilePath + "\\" + staffId + ".pdf";

                OutputStream savePath = new FileOutputStream(new File(fileSavePath));
                FileInputStream inputPath = new FileInputStream(admin.getFilePath());
                PdfReader pdfReader = new PdfReader(inputPath);
                new PdfStamper(pdfReader, savePath).close();
                System.out.println("Save PDF");
            }else {
                System.out.println("No Pdf");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    //write a method for save Patient photo and file
    private static void savePatientPhoto_File(Patient patient){

        try{

            String staffId = patient.getIdCardNumber();

            if (patient.getPhotoPath()!=null){
                String moPhotoPath ="src/sample/fileStorage/moduleData/userData/userPhoto/patient";
                String photoSavePath =moPhotoPath + "\\" + staffId + ".jpg";

                File imageFile = new File(patient.getPhotoPath());
                BufferedImage image = ImageIO.read(imageFile);
                ImageIO.write(image, "jpg",new File(photoSavePath));
                System.out.println("Save PHOTO");
            }else {
                System.out.println("No Photo");
            }

            if (patient.getFilePath()!=null){
                String moFilePath ="src/sample/fileStorage/moduleData/userData/userFile/patient";
                String fileSavePath = moFilePath + "\\" + staffId + ".pdf";

                OutputStream savePath = new FileOutputStream(new File(fileSavePath));
                FileInputStream inputPath = new FileInputStream(patient.getFilePath());
                PdfReader pdfReader = new PdfReader(inputPath);
                new PdfStamper(pdfReader, savePath).close();
                System.out.println("Save PDF");
            }else {
                System.out.println("No Pdf");
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    //write a method for save Receptionist photo and file
    private static void saveReceptionistPhoto_File(Receptionist receptionist){

        try{

            String staffId = receptionist.getIdCardNumber();

            if (receptionist.getPhotoPath()!=null){
                String moPhotoPath ="src/sample/fileStorage/moduleData/userData/userPhoto/reception";
                String photoSavePath =moPhotoPath + "\\" + staffId + ".jpg";

                File imagefile = new File(receptionist.getPhotoPath());
                BufferedImage image = ImageIO.read(imagefile);
                ImageIO.write(image, "jpg",new File(photoSavePath));
                System.out.println("Save PHOTO");
            }else {
                System.out.println("No Photo");
            }

            if (receptionist.getFilePath()!=null){
                String moFilePath ="src/sample/fileStorage/moduleData/userData/userFile/reception";
                String fileSavePath = moFilePath + "\\" + staffId + ".pdf";

                OutputStream savePath = new FileOutputStream(new File(fileSavePath));
                FileInputStream inputPath = new FileInputStream(receptionist.getFilePath());
                PdfReader pdfReader = new PdfReader(inputPath);
                new PdfStamper(pdfReader, savePath).close();
                System.out.println("Save PDF");
            }else {
                System.out.println("No Pdf");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    //write a method for save medicalOfficer photo and file
    private static void saveMedicalOfficerPhoto_File(MedicalOfficer medicalOfficer){

        try{
            String staffId = medicalOfficer.getIdCardNumber();

            if (medicalOfficer.getPhotoPath()!=null){
                String moPhotoPath ="src/sample/fileStorage/moduleData/userData/userPhoto/medicalOfficer";
                String photoSavePath =moPhotoPath + "\\" + staffId + ".jpg";

                File imagefile = new File(medicalOfficer.getPhotoPath());
                BufferedImage image = ImageIO.read(imagefile);
                ImageIO.write(image, "jpg",new File(photoSavePath));
                System.out.println("Save PHOTO");
            }else {
                System.out.println("No Photo");
            }

            if (medicalOfficer.getFilePath()!=null){
                String moFilePath ="src/sample/fileStorage/moduleData/userData/userFile/medicalOfficer";
                String fileSavePath = moFilePath + "\\" + staffId + ".pdf";

                OutputStream savePath = new FileOutputStream(new File(fileSavePath));
                FileInputStream inputPath = new FileInputStream(medicalOfficer.getFilePath());
                PdfReader pdfReader = new PdfReader(inputPath);
                new PdfStamper(pdfReader, savePath).close();
                System.out.println("Save PDF");
            }else {
                System.out.println("No Pdf");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


}


