package sample.controller.actionTask;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.jetbrains.annotations.NotNull;
import sample.Main;
import sample.controller.SystemDataReader;
import sample.controller.taskControllers.SystemDataWriter;
import sample.model.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.spec.KeySpec;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class UserAction {

    // scerate key and salt for the user login data encryption
    private static final String secretKey = "gBsq^W)R1z3wXPPxj!UKgBsq^W)R1z3wXPPxj!UK";
    private static final String salt = "t?u5j@_ku$Gmk9Yt?u5j@_ku$Gmk9Y";
    private static Scanner scanner;

    public static String patientloginData = "src/sample/fileStorage/loginData/patientLoginData.txt";
    public static String adminloginData = "src/sample/fileStorage/loginData/adminLoginData.txt";
    public static String receptionLoginData = "src/sample/fileStorage/loginData/receptionLoginData.txt";
    public static String medicalLoginData = "src/sample/fileStorage/loginData/medicalofficerLoginData.txt";

    public static String patientDataFilePath = "src/sample/fileStorage/moduleData/userData/patientData.txt";
    public static String receptionistFilePath = "src/sample/fileStorage/moduleData/userData/receptionistData.txt";
    public static String medicalOfficerFilePath = "src/sample/fileStorage/moduleData/userData/medicalOfficerData.txt";
    public static String adminFilePath = "src/sample/fileStorage/moduleData/userData/adminData.txt";

    private static String userActionLogFile = "src/sample/fileStorage/logData/userLogData.txt";


    /* =============================================================================================================
       LOGIN_USER Action tasks
      ============================================================================================================= */

    // verify user login credentials
    // parameters UserName , User Password , UserType (reception,admin.doctor,patient)
    public static int verifyLogin(String userName, String password, String userType) {

        int feedback = 0;

        //load respective user dta from the selectedfiles from the local storage
        ArrayList<LoginUser> loginData = loadUserLoginData(userType);

        for (int i = 0; i < loginData.size(); i++) {
            String saveduserName = loginData.get(i).getUserName().trim();
            String savedUserPassword = loginData.get(i).getUserPassword().trim();

            boolean a = (saveduserName.equals(encrypt(userName, secretKey)));
            boolean b = (savedUserPassword.equals(encrypt(password, secretKey)));

            if (a && b) {
                feedback = 1;
            }
        }
        return feedback;
    }

    //load user data from the file storage(from text files stored in the local storage)
    public static @NotNull
    ArrayList<LoginUser> loadUserLoginData(String userType) {

        ArrayList<LoginUser> userData = new ArrayList<>();

        File file = new File("src/sample/fileStorage/loginData/" + userType + "LoginData.txt");

        if (file != null) {
            try (FileReader fileReader = new FileReader(file)) {
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                LineNumberReader lineNumberReader = new LineNumberReader(bufferedReader);
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {

                  //  System.out.println(lineNumberReader.getLineNumber());
                    List<String> tempList = Arrays.asList(line.split("[~\n]"));
                    LoginUser loginUser = new LoginUser();
                    loginUser.setUserName(tempList.get(0).trim());
                    loginUser.setUserPassword(tempList.get(1).trim());
                    userData.add(loginUser);
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return userData;
    }

    public static String encryptUserData(String userDataEncrypt){
        return encrypt(userDataEncrypt,secretKey);
    }

    public static String decryptUserData(String userDataDecrypt){
         String decrypteUserData =decrypt(userDataDecrypt,secretKey);

         return decrypteUserData;
    }

    private static String encrypt(String strToEncrypt, String secret) {
        try {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    private static String decrypt(String strToDecrypt, String secret) {
        try {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;

    }


    /* =============================================================================================================
       PATIENT Action tasks
      =============================================================================================================*/

    //method for add patient data
    public static void addPatient(Patient user,UserRoll roll) {

        if (roll.equals(UserRoll.ADMIN) || roll.equals(UserRoll.RECEPTIONIST)) {
            savePatient(user);
            savePatientPhoto_File(user);
            JOptionPane.showMessageDialog(null,"Record Add Successfully");
            adduserlog(user.getUserRoll(),user.getUserName(),"Patient Record Added");
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

    //method for save patient data
    private static void savePatient(Patient user) {
       SystemDataWriter systemDataWriter = new SystemDataWriter();
       systemDataWriter.writeDataToFile(user.toString(),patientDataFilePath,5);
       addUserLoginData(patientloginData, new LoginUser(user.getUserName(), user.getUserPassword()));


    }

    //method for update patient data
    public static void updatePatientRecord(UserRoll userRoll,Patient patientRecord,String searchedID,LoginUser loginUser){
/*
        String staffId = patientRecord.getIdCardNumber();
        String photoPath = "src/sample/fileStorage/moduleData/userData/userPhoto/patient";
        String photosavePath = photoPath + "\\" + staffId + ".jpg";
        if (Files.isReadable(Path.of(photosavePath))){
            JOptionPane.showMessageDialog(null,"Photo");
        }
*/
        if (userRoll.equals(UserRoll.RECEPTIONIST) || userRoll.equals(UserRoll.ADMIN)){

            Object[] options = { "OK", "CANCEL" };
            Toolkit.getDefaultToolkit().beep();
            int selectedValue = JOptionPane.showOptionDialog(null,
                    "Are You Sure Update This Record"+"\nClick OK to continue", "Warning",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);

            if (selectedValue == JOptionPane.WHEN_FOCUSED) {
                editPatientRecord(patientDataFilePath,patientRecord,searchedID,loginUser);
                savePatientPhoto_File(patientRecord);
                adduserlog(patientRecord.getUserRoll(),patientRecord.getUserName(),"Patient Record Updated");
            }
        }else {
            System.out.println("acces denied(cannot update)");
        }
    }

    //method for edit patient data
    private static void editPatientRecord(String filePath,Patient patientEdit,String searchPetientid,LoginUser loginUser){

        Boolean recordFound = false;

        ArrayList<Patient> updatedPAtientList =new ArrayList<>();
        ArrayList<Patient> allPatientRecord =getAllPatients();

        for (int i=0;i<allPatientRecord.size();i++){
            if (allPatientRecord.get(i).getIdCardNumber().equals(searchPetientid)){
                updatedPAtientList.add(patientEdit);
                recordFound=true;
            }
            updatedPAtientList.add(allPatientRecord.get(i));
        }

        SystemDataWriter systemDataWriter =new SystemDataWriter();
        systemDataWriter.writeDataToFile(getStringArrayFromPatientArray(updatedPAtientList),patientDataFilePath,10);

        if (recordFound){
            JOptionPane.showMessageDialog(null,"Patient Record Update Successfully");
        }
        else {
            JOptionPane.showMessageDialog(null,
                    "Record Not Found"+"\nPlease Check ID Number in Search Box",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }

        updateUserLoginData(patientloginData,loginUser);

    }

    //method for search patient
    public static Patient searchPatient(String seachTerm,String userName,String userpassword){
        Patient foundPatient = searchPatientRecord(seachTerm,userName,userpassword);
        System.out.println("return Patient : "+foundPatient.toString());
        return foundPatient;
    }

    //method for search patient data
    private static Patient searchPatientRecord(String searchTerm, String userName,String password){

        Patient foundpatient=null;
        boolean found = false;

        ArrayList<Patient> allPatientRecord =getAllPatients();

        for (int i=0;i<allPatientRecord.size();i++){
            Boolean idNumberMatched =allPatientRecord.get(i).getIdCardNumber().equals(searchTerm);
            Boolean isUserLoginDataTrue =allPatientRecord.get(i).getUserName().equals(encryptUserData(userName))
                    && allPatientRecord.get(i).getUserPassword().equals(encryptUserData(password));

            if ( (idNumberMatched ||isUserLoginDataTrue)) {
             foundpatient = allPatientRecord.get(i);
             found = true;
         }
        }

        if (found){
            if (password==null){
                JOptionPane.showMessageDialog(null,"Patient Record Found");
            }
        }
        else {
            if (password==null){
                JOptionPane.showMessageDialog(null,"Patient Record Not Found");
            }
        }

        return foundpatient;
    }

    public static  ArrayList<Patient> getAllPatients(){

        SystemDataReader systemDataReader = new SystemDataReader();
        ArrayList<String> patientStrinArray= systemDataReader.getTempDataArray(patientDataFilePath);
        ArrayList<Patient> allPatientRecords  =getPatientArrayFromStringArray(patientStrinArray);

        return allPatientRecords;
    }

    private static ArrayList<Patient> getPatientArrayFromStringArray(ArrayList<String> patientStrinArray) {
        ArrayList<Patient> patientArrayList =new ArrayList<>();
        for (int i=0;i<patientStrinArray.size();i++){
            String line =patientStrinArray.get(i);
            Patient patient =getPatientFromStringLine(line);
            patientArrayList.add(patient);
        }

        return patientArrayList;
    }

    private static ArrayList<String>  getStringArrayFromPatientArray(ArrayList<Patient> patientArrayList){
        ArrayList<String> stringPatientArray =new ArrayList<>();

        for (int i=0;i<patientArrayList.size();i++){
            stringPatientArray.add(patientArrayList.get(i).toString());
        }

        return stringPatientArray;
    }

    private static Patient getPatientFromStringLine(String line) {

        List<String> tenpPatientRec =Arrays.asList(line.split("[~\n]"));
        Patient returnPatient = new Patient();

        returnPatient.setUserRoll(getUserRoll(tenpPatientRec.get(0)));
        returnPatient.setName(tenpPatientRec.get(1));
        returnPatient.setGender(getGender(tenpPatientRec.get(2)));
        returnPatient.setMaritalStatus(tenpPatientRec.get(3));
        returnPatient.setDob(getLocalDatefromString(tenpPatientRec.get(4)));
        returnPatient.setPhoneNumber(tenpPatientRec.get(5));
        returnPatient.setIdCardNumber(tenpPatientRec.get(6));
        returnPatient.setAddress(tenpPatientRec.get(7));
        returnPatient.setUserName(tenpPatientRec.get(8));
        returnPatient.setUserPassword(tenpPatientRec.get(9));
        returnPatient.setBloodGroup(getBloodGroup(tenpPatientRec.get(10)));
        returnPatient.setAllergies(tenpPatientRec.get(11));

        return returnPatient;
    }


    /* =============================================================================================================
       RECEPTIONIST Action tasks
      =============================================================================================================
    */

    //method for add receptionist data
    public static void addReceptionist(Receptionist receptionist,UserRoll roll){

        if (roll.equals(UserRoll.ADMIN)){
            saveReceptionist(receptionist);
            saveReceptionistPhoto_File(receptionist);
            JOptionPane.showMessageDialog(null,"Record Add Successfully");
            adduserlog(receptionist.getUserRoll(),receptionist.getUserName(),"Receptionist Record Added");
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

    //method for save receptionist data
    private static  void  saveReceptionist(Receptionist receptionist){

        SystemDataWriter systemDataWriter=new SystemDataWriter();
        systemDataWriter.writeDataToFile(receptionist.toString(),receptionistFilePath,5);
        addUserLoginData(receptionLoginData,new LoginUser(receptionist.getUserName(), receptionist.getUserPassword()));

    }

    //method for update receptionist data
    public static void updateReceptionRecord(UserRoll userRoll,Receptionist receptionist,String searchedID,LoginUser loginUser){
        if (userRoll.equals(UserRoll.RECEPTIONIST) || userRoll.equals(UserRoll.ADMIN)){

            Object[] options = { "OK", "CANCEL" };
            Toolkit.getDefaultToolkit().beep();
            int selectedValue = JOptionPane.showOptionDialog(
                    null,
                    "Are You Sure Update This Record"+"\nClick OK to continue",
                    "Warning",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);

            if (selectedValue == JOptionPane.WHEN_FOCUSED) {
                editReceptionRecord(receptionist,searchedID,loginUser);
                saveReceptionistPhoto_File(receptionist);
                adduserlog(receptionist.getUserRoll(),receptionist.getUserName(),"Receptionist Record Updated");
            }

        }else {
            System.out.println("acces denied(cannot update)");
        }
    }

    //method for edit receptionist data
    private static void editReceptionRecord(Receptionist receptionist,String searchPetientid,LoginUser loginUser){

        Boolean isUpdated =false;

        ArrayList<Receptionist> allReceptionRecords = getAllReceptionist();
        ArrayList<Receptionist> finalEditedArray =new ArrayList<>();

        for (int i=0;i<allReceptionRecords.size();i++){
            if (allReceptionRecords.get(i).getIdCardNumber().equals(searchPetientid)){
                finalEditedArray.add(receptionist);
                isUpdated=true;
            }else {
                finalEditedArray.add(allReceptionRecords.get(i));
            }
        }

        if (isUpdated){
            JOptionPane.showMessageDialog(null,"Update Successfully");
        }
        else {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null,
                    "Record Not Found"+"\nPlease Check ID Number in Search Box",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }

        SystemDataWriter systemDataWriter=new SystemDataWriter();
        systemDataWriter.writeDataToFile(getStringArrayFromReceptionArry(finalEditedArray),receptionistFilePath,10);
        updateUserLoginData(receptionLoginData,loginUser);

    }

    //method for search receptionist data
    public static Receptionist searchReceptionRecord(String seachTerm,String userName,String userPass){

        Receptionist foundReception = searchReceptionist(seachTerm,userName,userPass);
        System.out.println("return Reception :"+foundReception.toString());
        return foundReception;
    }

    private static ArrayList<String> getStringArrayFromReceptionArry(ArrayList<Receptionist> finalEditedArray) {
        ArrayList<String> finalReceptionStringArray =new ArrayList<>();
        for (int i=0;i<finalEditedArray.size();i++){
            finalReceptionStringArray.add(finalEditedArray.get(i).toString());
        }

        return finalReceptionStringArray;
    }

    //method for search receptionist
    private static Receptionist searchReceptionist(String searchTerm, String userName,String userPassword){

        ArrayList<Receptionist> allReceptionistData =getAllReceptionist();
        boolean found = false;
        boolean isPassWordPresent =(userName!=null)&&(userPassword!=null);
        Receptionist searchedReceptionist = new Receptionist();

        for (int i=0;i<allReceptionistData.size();i++){
            boolean isloginDataTrue = allReceptionistData.get(i).getUserName().equals(encryptUserData(userName)) &&
                    allReceptionistData.get(i).getUserPassword().equals(encryptUserData(userPassword));
            boolean isSearchTermMatched =allReceptionistData.get(i).getIdCardNumber().equals(searchTerm);

            if (isloginDataTrue || isSearchTermMatched){
                found=true;
                searchedReceptionist=allReceptionistData.get(i);
            }
        }

        if (found && !isPassWordPresent){
            JOptionPane.showMessageDialog(null,"Record Found");
        }
        else if (!isPassWordPresent){
            JOptionPane.showMessageDialog(null,"Record Not Found");
        }


        return searchedReceptionist;
    }

    public static  ArrayList<Receptionist> getAllReceptionist(){

        ArrayList<Receptionist> allReceptionRecords = new ArrayList<>();
    SystemDataReader systemDataReader = new SystemDataReader();
    ArrayList<String> allReceptionistStringArray = systemDataReader.getTempDataArray(receptionistFilePath);

    for(int i=0;i<allReceptionistStringArray.size();i++){
        Receptionist receptionist = getReceptionistFromString(allReceptionistStringArray.get(i));
        allReceptionRecords.add(receptionist);
    }

        return allReceptionRecords;
    }

    private static Receptionist getReceptionistFromString(String tempReceptionRecLine) {

        List<String> tempReceptionList = Arrays.asList(tempReceptionRecLine.split("[~\n]"));

        Receptionist returnReceptionist = new Receptionist();

        System.out.println("getAllReception --->"+tempReceptionRecLine);
        returnReceptionist.setUserRoll(getUserRoll(tempReceptionList.get(0)));
        returnReceptionist.setName(tempReceptionList.get(1));
        returnReceptionist.setGender(getGender(tempReceptionList.get(2)));
        returnReceptionist.setMaritalStatus(tempReceptionList.get(3));
        returnReceptionist.setDob(getLocalDatefromString(tempReceptionList.get(4)));
        returnReceptionist.setPhoneNumber(tempReceptionList.get(5));
        returnReceptionist.setIdCardNumber(tempReceptionList.get(6));
        returnReceptionist.setAddress(tempReceptionList.get(7));
        returnReceptionist.setUserName(tempReceptionList.get(8));
        returnReceptionist.setUserPassword(tempReceptionList.get(9));
        returnReceptionist.setStaffID(Integer.parseInt(tempReceptionList.get(10)));
        returnReceptionist.setStaffEmailAddress(tempReceptionList.get(11));
        returnReceptionist.setDateOfJoining(getLocalDatefromString(tempReceptionList.get(12)));


        return returnReceptionist;
    }


    /* =============================================================================================================
       ADMIN Action tasks
      =============================================================================================================*/

    //method for add admin data
    public static void addAdmin (Admin admin,UserRoll userRoll){

        if (userRoll.equals(UserRoll.ADMIN)) {
            saveAdmin(admin);
            saveAdminPhoto_File(admin);
            JOptionPane.showMessageDialog(null,"Record Add Successfully");
        }
        else {
            System.out.println("cannot save");}
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

    //method for save admin data
    private static void saveAdmin(Admin admin) {
        SystemDataWriter systemDataWriter = new SystemDataWriter();
        systemDataWriter.writeDataToFile(admin.toString(),adminFilePath,5);
            System.out.println("file path :  admin saved");
            addUserLoginData(adminloginData,new LoginUser(admin.getUserName(), admin.getUserPassword()));
            adduserlog(admin.getUserRoll(),admin.getUserName(),"Admin Record Added");

    }

    //method for update admin data
    public static void updateAdmin(UserRoll userRoll,Admin adminRecord,String searchedID,LoginUser loginUser){
        if (userRoll.equals(UserRoll.ADMIN)){

            Object[] options = { "OK", "CANCEL" };
            Toolkit.getDefaultToolkit().beep();
            int selectedValue = JOptionPane.showOptionDialog(null,
                    "Are You Sure Update This Record"+"\nClick OK to continue", "Warning",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);

            if (selectedValue == JOptionPane.WHEN_FOCUSED) {
                editAdminRecord(adminRecord,searchedID,loginUser);
                saveAdminPhoto_File(adminRecord);
                adduserlog(adminRecord.getUserRoll(),adminRecord.getUserName(),"Admin Record Updated");
            }

        }
        else {
            System.out.println("access denied(cannot update)");
        }
    }

    //method for edit admin data
    private static void editAdminRecord(Admin adminEdit,String searchAdminRec,LoginUser loginUser){

        ArrayList<Admin> allAdminRecord =getAllAdmin();
        ArrayList<Admin> finalEditedAdminArray=new ArrayList<>();

        boolean isUpdated = false;

        for (int i=0;i<allAdminRecord.size();i++){
            if (allAdminRecord.get(i).getIdCardNumber().equals(searchAdminRec)){
                finalEditedAdminArray.add(adminEdit);
                isUpdated=true;
            }else {
                finalEditedAdminArray.add(allAdminRecord.get(i));
            }

        }

        if (isUpdated){
            SystemDataWriter systemDataWriter =new SystemDataWriter();
            systemDataWriter.writeDataToFile(getStringArrayFromAdminArray(finalEditedAdminArray),adminFilePath,10);
            updateUserLoginData(adminloginData,loginUser);
            JOptionPane.showMessageDialog(null,"Update Successfully");
        }
        else {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null,
                    "Record Not Found"+"\nPlease Check ID Number in Search Box",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    private static ArrayList<String> getStringArrayFromAdminArray(ArrayList<Admin> finalEditedAdminArray) {
        ArrayList<String> adminStringArray= new ArrayList<>();
        for (int i =0;i<finalEditedAdminArray.size();i++){
            adminStringArray.add(finalEditedAdminArray.get(i).toString());
        }

        return adminStringArray;
    }

    //method for search admin
    public static Admin searchAdmin(String seachTermOrUserName,String userPassword){

        Admin foundAdmin = searchAdminRecord(seachTermOrUserName,userPassword);
        System.out.println("return Admin :"+foundAdmin.toString());
        return foundAdmin;
    }

    //method for search admin data
    private static Admin searchAdminRecord(String searchTermORuserName, String password){

        ArrayList<Admin> allAdminRecords =getAllAdmin();
        boolean PassPresent=password!=null;
        boolean found = false;
        Admin searchedAdmin = new Admin();

        for (int i=0;i<allAdminRecords.size();i++){
            boolean isPassMatch =allAdminRecords.get(i).getUserName().equals(encryptUserData(searchTermORuserName)) &&
                    allAdminRecords.get(i).getUserPassword().equals(encryptUserData(password));
            boolean isSerchTermMatched =allAdminRecords.get(i).getIdCardNumber().equals(searchTermORuserName);
            if (isPassMatch || isSerchTermMatched){
                searchedAdmin =allAdminRecords.get(i);
                found=true;
            }
        }

        if (found && !PassPresent){
            JOptionPane.showMessageDialog(null,"Record Found");
        }
        else if (!PassPresent){
            JOptionPane.showMessageDialog(null,"Record Not Found");
        }

        return searchedAdmin;
    }

    public static ArrayList<Admin> getAllAdmin(){
        ArrayList<Admin> allAdminRecords =new ArrayList<>();

        SystemDataReader systemDataReader = new SystemDataReader();
        ArrayList<String> allAdminStrinArray =systemDataReader.getTempDataArray(adminFilePath);
        for (int i=0;i<allAdminStrinArray.size();i++){
            String line = allAdminStrinArray.get(i);
            Admin admin =getAdminFromList(line);
            allAdminRecords.add(admin);
        }

        return allAdminRecords;
    }

    public static  ArrayList<UserLoginLog> getAllloginLog(){
        ArrayList<UserLoginLog> allLoginRecordRecords =new ArrayList<>();
        SystemDataReader systemDataReader = new SystemDataReader();
        ArrayList<String> allLogStringArrya = systemDataReader.getTempDataArray(userActionLogFile);
        for (int i=0;i<allLogStringArrya.size();i++){
            String line = allLogStringArrya.get(i);
            UserLoginLog log =getUserLogFromList(line);
            allLoginRecordRecords.add(log);
        }

        return allLoginRecordRecords;
    }

    private static  UserLoginLog getUserLogFromList(String loginLine){
        UserLoginLog loginLog =new UserLoginLog();
        if(loginLog != null){
            List<String> loginLogList = Arrays.asList(loginLine.split("[~\n]"));
           // System.out.println("GetUserFromLoginLog "+ loginLogList);

            loginLog.setLogDate(getLocalDatefromString(loginLogList.get(0)));
            loginLog.setLocalTime(LocalTime.parse(loginLogList.get(1)));
            loginLog.setUserRoll(getUserRoll(loginLogList.get(4)));
        }

        return loginLog;
    }

    private static Admin getAdminFromList(String adminLine) {

        Admin returnAdmiin =new Admin();
        if(adminLine != null){
            List<String> adminRecList = Arrays.asList(adminLine.split("[~\n]"));
            System.out.println(adminRecList);

            returnAdmiin.setUserRoll(getUserRoll(adminRecList.get(0)));
            returnAdmiin.setName(adminRecList.get(1));
            returnAdmiin.setGender(getGender(adminRecList.get(2)));
            returnAdmiin.setMaritalStatus(adminRecList.get(3));
            returnAdmiin.setDob(getLocalDatefromString(adminRecList.get(4)));
            returnAdmiin.setPhoneNumber(adminRecList.get(5));
            returnAdmiin.setIdCardNumber(adminRecList.get(6));
            returnAdmiin.setAddress(adminRecList.get(7));
            returnAdmiin.setUserName(adminRecList.get(8));
            returnAdmiin.setUserPassword(adminRecList.get(9));
        }

        return returnAdmiin;
    }

    /* =============================================================================================================
       MEDICALOFFICER Action tasks
      =============================================================================================================*/

    //method for add medicalofficer data
    public static void addMedicalOfficer(MedicalOfficer medicalOfficer,UserRoll userRoll){

        if (userRoll.equals(UserRoll.ADMIN)) {
            saveMedicalOfficer(medicalOfficer);
            saveMedicalOfficerPhoto_File(medicalOfficer);
            JOptionPane.showMessageDialog(null,"Record Add Successfully");
        }

    }

    //write a method for save medicalofficer photo and file
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

    //method for save medicalofficer data
    private static void saveMedicalOfficer(MedicalOfficer medicalOfficer) {
     SystemDataWriter systemDataWriter=new SystemDataWriter();
     systemDataWriter.writeDataToFile(medicalOfficer.toString(),medicalOfficerFilePath,5);
     addUserLoginData(medicalLoginData,new LoginUser(medicalOfficer.getUserName(),medicalOfficer.getUserPassword()));
     adduserlog(medicalOfficer.getUserRoll(),medicalOfficer.getUserName(),"MedicalOfficer record Added ");
    }

    //method for update medicalofficer data record
    public static void updateMedicalOfficerRecord(UserRoll userRoll,MedicalOfficer medicalOfficerRecord,String searchedID,LoginUser loginUser){
        if (userRoll.equals(UserRoll.ADMIN)){

            Object[] options = { "OK", "CANCEL" };
            Toolkit.getDefaultToolkit().beep();
            int selectedValue = JOptionPane.showOptionDialog(null,
                    "Are You Sure Update This Record"+"\nClick OK to continue", "Warning",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);

            if (selectedValue == JOptionPane.WHEN_FOCUSED) {
                editMedicalOfficerRecord(medicalOfficerRecord,searchedID,loginUser);
                saveMedicalOfficerPhoto_File(medicalOfficerRecord);
            }

        }
        else{
            System.out.println("Access denied(Cannot update)");
        }
    }

    //method foe edit medicalofficer data record
    private static void editMedicalOfficerRecord(MedicalOfficer medicalOfficer,String searchMedicalOfficerId,LoginUser loginUser){

        ArrayList<MedicalOfficer> allmedicalOfficerArray= getAllMedicalOfficer();
        ArrayList<MedicalOfficer> finalEditedMedicalArray =new ArrayList<>();
        boolean recordUpdated =false;

        for (int i=0;i<allmedicalOfficerArray.size();i++){
            if (allmedicalOfficerArray.get(i).getIdCardNumber().equals(searchMedicalOfficerId)){
                finalEditedMedicalArray.add(medicalOfficer);
                recordUpdated=true;
            }
            finalEditedMedicalArray.add(allmedicalOfficerArray.get(i));
        }

        if (recordUpdated){

            SystemDataWriter systemDataWriter =new SystemDataWriter();
            systemDataWriter.writeDataToFile(getStringArrayFromMedicalArray(finalEditedMedicalArray),medicalOfficerFilePath,10);
            updateUserLoginData(medicalLoginData,loginUser);
            adduserlog(UserRoll.MEDICALOFFICER,loginUser.getUserName(),"MedicalOfficer Record Updated");
            JOptionPane.showMessageDialog(null,"Update Successfully");
        }
        else {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null,
                    "Record Not Found"+"\nPlease Check ID Number in Search Box",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    private static ArrayList<String> getStringArrayFromMedicalArray(ArrayList<MedicalOfficer> finalEditedMedicalArray) {
        ArrayList<String> strinMedicalArray =new ArrayList<>();

        for (int i=0;i<finalEditedMedicalArray.size();i++){
            strinMedicalArray.add(finalEditedMedicalArray.get(i).toString());
        }

        return strinMedicalArray;
    }



    //method for search MedicalOfficer
    public static MedicalOfficer searchMedicalOfficerRecord(String searchTerm,String userPass){

        MedicalOfficer foundMedicalOfficer = searchMedicalOfficer(searchTerm,userPass);
        System.out.println("return MedicalOfficer :"+foundMedicalOfficer.toString());
        return foundMedicalOfficer;
    }



    //method for Search medicalofficer data record
    public static MedicalOfficer searchMedicalOfficer(String seachTermOrUserName, String password) {
        boolean found = false;
        boolean passPresent =(password!=null);
        MedicalOfficer searchedMedicalOfficer = new MedicalOfficer();
        ArrayList<MedicalOfficer> allMedicalOfficer=getAllMedicalOfficer();

        for(int i=0;i<allMedicalOfficer.size();i++){
            boolean isPassMatched =allMedicalOfficer.get(i).getUserName().equals(encryptUserData(seachTermOrUserName)) &&
                    allMedicalOfficer.get(i).getUserPassword().equals(encryptUserData(password));
            boolean isSearchTermMatched= allMedicalOfficer.get(i).getIdCardNumber().equals(seachTermOrUserName);

            if (isPassMatched||isSearchTermMatched){
                searchedMedicalOfficer =allMedicalOfficer.get(i);
                found=true;
            }
        }

        if (found && !passPresent){
            JOptionPane.showMessageDialog(null,"Record Found");
        }
        else if (!passPresent){
            JOptionPane.showMessageDialog(null,"Record Not Found");
        }

        return searchedMedicalOfficer;

    }

    public static ArrayList<MedicalOfficer> getAllMedicalOfficer(){
        ArrayList<MedicalOfficer> allMedicalOfficerRecords =new ArrayList<>();

        SystemDataReader systemDataReader =new SystemDataReader();
        ArrayList<String> stringArrayListMedical =systemDataReader.getTempDataArray(medicalOfficerFilePath);

        for (int i=0;i<stringArrayListMedical.size();i++){
            String line = stringArrayListMedical.get(i);
            MedicalOfficer medicalOfficer = getMedicalOfficerFromStrinLine(line) ;
            allMedicalOfficerRecords.add(medicalOfficer);
        }

        return allMedicalOfficerRecords;
    }

    private static MedicalOfficer getMedicalOfficerFromStrinLine(String  MedicLine) {
        MedicalOfficer returnMedicalOfficer =new MedicalOfficer();

        List<String> tempMedicList =Arrays.asList(MedicLine.split("[~\n]"));

        returnMedicalOfficer.setUserRoll(getUserRoll(tempMedicList.get(0)));
        returnMedicalOfficer.setName(tempMedicList.get(1));
        returnMedicalOfficer.setGender(getGender(tempMedicList.get(2)));
        returnMedicalOfficer.setMaritalStatus(tempMedicList.get(3));
        returnMedicalOfficer.setDob(getLocalDatefromString(tempMedicList.get(4)));
        returnMedicalOfficer.setPhoneNumber(tempMedicList.get(5));
        returnMedicalOfficer.setIdCardNumber(tempMedicList.get(6));
        returnMedicalOfficer.setAddress(tempMedicList.get(7));
        returnMedicalOfficer.setUserName(tempMedicList.get(8));
        returnMedicalOfficer.setUserPassword(tempMedicList.get(9));
        returnMedicalOfficer.setStaffID(Integer.parseInt(tempMedicList.get(10)));
        returnMedicalOfficer.setStaffEmailAddress(tempMedicList.get(11));
        returnMedicalOfficer.setDateOfJoining(getLocalDatefromString(tempMedicList.get(12)));
        returnMedicalOfficer.setSpeciality(tempMedicList.get(13));
        return returnMedicalOfficer;
    }

    public static ArrayList<MedicalOfficer> getMedicalOfficerBySpeciality(String speciality){
        ArrayList<MedicalOfficer> medicalOfficerBySpec = new ArrayList<>();
        ArrayList<MedicalOfficer> tempMedicalOfficer = getAllMedicalOfficer();
        for (int i=0;i<tempMedicalOfficer.size();i++){
            if (tempMedicalOfficer.get(i).getSpeciality().trim().equals(speciality.trim())){
                MedicalOfficer medicalOfficer = tempMedicalOfficer.get(i);
                medicalOfficer.setUserPassword(null);
                medicalOfficerBySpec.add(medicalOfficer);
               // System.out.println("Medical officer found by speciality");
            }else {
                System.out.println("Medical officer not found by speciality");
            }
        }

        return medicalOfficerBySpec;
    }

    private static MedicalOfficer getMedicalOfficerById(String medicalofficeridNo){
        MedicalOfficer selectedOfficer =null;
        ArrayList<MedicalOfficer> allMedicalOfficer = getAllMedicalOfficer();
        for (int i = 0; i < allMedicalOfficer.size(); i++) {
            if (allMedicalOfficer.get(i).getIdCardNumber().equals(medicalofficeridNo)){
                selectedOfficer = allMedicalOfficer.get(i);
            }
        }
        return selectedOfficer;
    }



    /* =============================================================================================================
       Common User  Action tasks
      =============================================================================================================*/

    //method for delete user data
    public static void deleteUserRecord(UserRoll taskUserRoll, String searchTerm, UserRoll currentUserRoll,LoginUser loginUser){
        if (taskUserRoll.equals(UserRoll.RECEPTIONIST)){
            Object[] options = { "OK", "CANCEL" };
            Toolkit.getDefaultToolkit().beep();
            int selectedValue = JOptionPane.showOptionDialog(null, "Are You Sure Delete This Record"+"\nClick OK to continue", "Warning",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);

            if (selectedValue == JOptionPane.WHEN_FOCUSED) {
                removeUserRecord(patientDataFilePath,searchTerm,loginUser,patientloginData);
                deleteUserFile(searchTerm,"patient");
            }

        }
        else if (taskUserRoll.equals(UserRoll.ADMIN)){

            Object[] options = { "OK", "CANCEL" };
            Toolkit.getDefaultToolkit().beep();
            int selectedValue = JOptionPane.showOptionDialog(null, "Are You Sure Delete This Record"+"\nClick OK to continue", "Warning",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);

            if (selectedValue == JOptionPane.WHEN_FOCUSED) {
                switch (currentUserRoll){
                    case ADMIN:
                        removeUserRecord(adminFilePath,searchTerm,loginUser,adminloginData);
                        adduserlog(UserRoll.ADMIN,loginUser.getUserName(),"Admin Record deleted");
                        deleteUserFile(searchTerm,"admin");
                        break;
                    case RECEPTIONIST:
                        removeUserRecord(receptionistFilePath,searchTerm,loginUser,receptionLoginData);
                        adduserlog(UserRoll.RECEPTIONIST,loginUser.getUserName(),"Receptionist Record deleted");
                        deleteUserFile(searchTerm,"reception");
                        break;
                    case PATIENT:
                        removeUserRecord(patientDataFilePath,searchTerm,loginUser,patientloginData);
                        adduserlog(UserRoll.PATIENT,loginUser.getUserName(),"Patient Record deleted");
                        deleteUserFile(searchTerm,"patient");
                        break;
                    case MEDICALOFFICER:
                        removeUserRecord(medicalOfficerFilePath,searchTerm,loginUser,medicalLoginData);
                        adduserlog(UserRoll.MEDICALOFFICER,loginUser.getUserName(),"MedicalOfficer Record deleted");
                        deleteUserFile(searchTerm,"medicalOfficer");
                        break;
                    default:
                        break;
                }
            }

        }
    }

    //method for remove user data
    private static void removeUserRecord(String filePath, String serachTerm,LoginUser loginUser,String loginDataPath){
        ArrayList<String> tempPatientList =new ArrayList<>();


        File file = new File(filePath);
        boolean found =false;
        try{
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line =null;
            while ((line = bufferedReader.readLine()) != null) {
                List<String> tempList = Arrays.asList(line.split("[~\n]"));
                if(!tempList.get(6).equals(serachTerm)){
                    tempPatientList.add(line);
                }else {
                    found = true;
                }
            }

            bufferedReader.close();
            fileReader.close();

            if (found){
                try {

                    File fileNew = new File(filePath);
                    if(file.exists()){
                        file.delete();
                    }
                    file.createNewFile();

                    FileWriter fileWriter = new FileWriter(fileNew);
                    BufferedWriter newbufferedWriter = new BufferedWriter(fileWriter);
                    newbufferedWriter.write("");
                    for (int i=0;i<tempPatientList.size();i++){
                        newbufferedWriter.write(tempPatientList.get(i));
                        newbufferedWriter.newLine();

                    }
                    newbufferedWriter.close();
                    fileWriter.close();
                    JOptionPane.showMessageDialog(null,"Delete Successfully");
                    //tem.out.println("User deleted success");
                   // System.out.println(tempPatientList.toString());
                    deleteUserLoginData(loginDataPath,loginUser);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else
            {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null,
                        "Record Not Found"+"\nPlease Check ID Number in Search Box",
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //write a method for Delete files
    private static void deleteUserFile(String  userID,String usertype){

        try{
            String staffId = userID;

            String moPhotoPath ="src/sample/fileStorage/moduleData/userData/userPhoto/"+usertype;
            String photoSavePath =moPhotoPath + "\\" + staffId + ".jpg";
            Files.delete(Path.of(photoSavePath));
            System.out.println("Delete PHOTO");

            String moFilePath ="src/sample/fileStorage/moduleData/userData/userFile/"+usertype;
            String fileSavePath = moFilePath + "\\" + staffId + ".pdf";
            Files.delete(Path.of(fileSavePath));
            System.out.println("Delete PDF");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BloodGroup getBloodGroup (String name){
                BloodGroup blooGroup = null;
                switch (name) {
                    case "A_POSITIVE":
                        blooGroup = BloodGroup.A_POSITIVE;
                        break;

                    case "A_NEGATIVE":
                        blooGroup = BloodGroup.A_NEGATIVE;
                        break;

                    case "AB_POSITIVE":
                        blooGroup = BloodGroup.AB_POSITIVE;
                        break;

                    case "AB_NEGATIVE":
                        blooGroup = BloodGroup.AB_NEGATIVE;
                        break;
                    case "B_POSITIVE":
                        blooGroup = BloodGroup.B_POSITIVE;
                        break;
                    case "B_NEGATIVE":
                        blooGroup = BloodGroup.B_NEGATIVE;
                        break;
                    case "O_POSITIVE":
                        blooGroup = BloodGroup.O_POSITIVE;
                        break;
                    case "O_NEGATIVE":
                        blooGroup = BloodGroup.O_NEGATIVE;
                        break;


        }

        return blooGroup;
    }

    public static Gender getGender(String gender){
                Gender userGender=null;
                switch (gender){
                    case "MALE":
                        userGender =Gender.MALE;
                        break;
                    case "FEMALE":
                        userGender =Gender.FEMALE;
                        break;
                    case "OTHER":
                        userGender = Gender.OTHER;
                    default:
                        break;
                }
                return userGender;
            }

    public static LocalDate getLocalDatefromString (String string){
                String pattern = "yyyy-MM-dd";
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }

    public static String getStringfromLocalDate (LocalDate date){
                String pattern = "yyyy-MM-dd";
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

    //get UserRoll enum object with the String type input of the name
    public static UserRoll getUserRoll (String name){
                UserRoll userRoll = null;
                switch (name) {
                    case "PATIENT":
                        userRoll = UserRoll.PATIENT;
                        break;

            case "ADMIN":
                userRoll = UserRoll.ADMIN;
                break;

            case "RECEPTIONIST":
                userRoll = UserRoll.RECEPTIONIST;
                break;

            case "MEDICALOFFICER":
                userRoll = UserRoll.MEDICALOFFICER;
                break;

        }

        return userRoll;
    }



    /* =============================================================================================================
       User Login Action  tasks
      =============================================================================================================*/

    private static void addUserLoginData(String fileName, LoginUser user) {

        SystemDataWriter systemDataWriter = new SystemDataWriter();
        systemDataWriter.writeDataToFile(user.toString(),fileName,5);
    }

    private static void updateUserLoginData(String fileName, LoginUser user){
        ArrayList<String> tempLoginList =new ArrayList<>();
        File file = new File(fileName);
        boolean found =false;
        try{
            FileReader loginfileReader = new FileReader(file);
            BufferedReader loginbufferedReader = new BufferedReader(loginfileReader);
            String line =null;
            while ((line = loginbufferedReader.readLine()) != null) {
                List<String> tempLoginUserList = Arrays.asList(line.split("~"));
                if((!tempLoginUserList.get(0).equals(user.getUserName()) )&&
                        (!tempLoginUserList.get(1).equals(user.getUserPassword()) )){
                         tempLoginList.add(line);
                }else {
                    found = true;
                    String newLine = user.toString();
                    line =newLine;
                    tempLoginList.add(line);

                }
            }

            loginbufferedReader.close();
            loginfileReader.close();

            if (found){
                try {

                    File fileNew = new File(fileName);
                    if(file.exists()){
                        file.delete();
                    }
                    file.createNewFile();

                    FileWriter fileWriter = new FileWriter(fileNew);
                    BufferedWriter newbufferedWriter = new BufferedWriter(fileWriter);
                    newbufferedWriter.write("");
                    for (int i=0;i<tempLoginList.size();i++){
                        newbufferedWriter.write(tempLoginList.get(i));
                        newbufferedWriter.newLine();

                    }
                    newbufferedWriter.close();
                    fileWriter.close();
                    System.out.println("Admin edited  success");
                    System.out.println(tempLoginList.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deleteUserLoginData(String fileName, LoginUser user){
        ArrayList<String> tempLoginList =new ArrayList<>();
        File file = new File(fileName);
        boolean found =false;
        try{
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line =null;
            while ((line = bufferedReader.readLine()) != null) {
                List<String> tempLoginUserList = Arrays.asList(line.split("~"));
                if((!tempLoginUserList.get(0).equals(user.getUserName()) )&&
                        (!tempLoginUserList.get(1).equals(user.getUserPassword()) )){
                    tempLoginList.add(line);
                }else {
                    found = true;

                }
            }

            bufferedReader.close();
            fileReader.close();

            if (found){
                try {

                    File fileNew = new File(fileName);
                    if(file.exists()){
                        file.delete();
                    }
                    file.createNewFile();

                    FileWriter fileWriter = new FileWriter(fileNew);
                    BufferedWriter newbufferedWriter = new BufferedWriter(fileWriter);
                    newbufferedWriter.write("");
                    for (int i=0;i<tempLoginList.size();i++){
                        newbufferedWriter.write(tempLoginList.get(i));
                        newbufferedWriter.newLine();

                    }
                    newbufferedWriter.close();
                    fileWriter.close();
                    System.out.println("User login record deleted success");
                    System.out.println(tempLoginList.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /* =============================================================================================================
       User Log Action  tasks
      =============================================================================================================*/

    private static void adduserlog(UserRoll userRoll,String userName,String actiontaken){
        SystemDataWriter systemDataWriter =new SystemDataWriter();

        UserActionLog userActionLog = new UserActionLog(
                LocalDate.now(),
                LocalTime.now(),
                Main.getCurrentSystemUser().getUserRoll(),
                decryptUserData(Main.getCurretUserName()),
                userRoll,
                decryptUserData(userName),
                actiontaken
        );

        systemDataWriter.writeDataToFile(userActionLog.toString(),userActionLogFile,5);

    }

    private static ArrayList<UserLoginCredential> getLoginDataList(){
        ArrayList<Patient> allPatientData =getAllPatients();
        ArrayList<UserLoginCredential> allpatientDataVals = new ArrayList<>();

        for (int i = 0; i < allPatientData.size(); i++) {
            Patient selectedPatient= allPatientData.get(i);
            UserLoginCredential newdata =new UserLoginCredential(
                    selectedPatient.getName(),
                    selectedPatient.getUserName(),
                   decryptUserData( selectedPatient.getUserPassword())
            );
            allpatientDataVals.add(newdata);
        }

        return allpatientDataVals;
    }

    public static ArrayList<UserLoginCredential> getAllUserCredentials(){
        return getLoginDataList();
    }

    public static void getMedicalOfficerData(){

    }


}
