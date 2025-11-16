import java.util.LinkedList;
import java.util.Scanner;

public class Doctor {
    private static int nextId = 2001;
    int doctorID;
    String firstName;
    String lastName;
    String specialization;
    String contactNumber;
    String workSchedule;
    boolean activeStatus = true;

    static LinkedList<Doctor> doctorList = new LinkedList<>();
    static LinkedList<Doctor> archivedDoctorList = new LinkedList<>();
    private static final Scanner a = new Scanner(System.in);

    public Doctor(String firstName, String lastName, String specialization, String contactNumber, String workSchedule) {
        this.doctorID = nextId++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.contactNumber = contactNumber;
        this.workSchedule = workSchedule;
    }

    // --- Preload Doctors ---
    public static void preloadDoctors(){
        if(doctorList.isEmpty()){
            doctorList.add(new Doctor("Alice", "Smith", "Cardiology", "09551234567", "09:00-17:00"));
            doctorList.add(new Doctor("Bob", "Johnson", "Neurology", "09171234567", "10:00-18:00"));
            doctorList.add(new Doctor("Charlie", "Brown", "Pediatrics", "09981234567", "08:00-16:00"));
        }
    }

    public void displayDoctorInfo() {
        System.out.println("Doctor ID: D" + doctorID);
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("Specialization: " + specialization);
        System.out.println("Contact Number: " + contactNumber);
        System.out.println("Work Schedule: " + workSchedule);
        System.out.println("Active Status: " + (activeStatus ? "Active" : "Inactive"));
        System.out.println("---------------------------");
    }

    public static void registerDoctor(){
        System.out.println("--- Register New Doctor ---");
        System.out.print("Enter First Name: ");
        String firstName = a.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = a.nextLine();
        System.out.print("Enter Specialization: ");
        String specialization = a.nextLine();
        
        String contactNumber;
        while (true) {
            System.out.print("Enter Contact Number (11 digits): ");
            contactNumber = a.nextLine();
            if (contactNumber.length() == 11) {
                break;
            } else {
                System.out.println("Invalid contact number. Must contain exactly 11 digits and only numbers.");
            }
        }
        
        System.out.print("Enter Work Schedule (HH:mm-HH:mm): ");
        String workSchedule = a.nextLine();

        if (firstName.isEmpty() || lastName.isEmpty() || specialization.isEmpty() || workSchedule.isEmpty()) {
            System.out.println("Error: All fields are required. Doctor registration failed.");
            return;
        }

        for (Doctor doc : doctorList) {
            if (doc.firstName.equalsIgnoreCase(firstName)
                    && doc.lastName.equalsIgnoreCase(lastName)
                    && doc.specialization.equalsIgnoreCase(specialization)) {
                System.out.println("Error: Doctor with the same name and specialization already exists. Registration failed.");
                return;
            }
        }

        Doctor newDoctor = new Doctor(firstName, lastName, specialization, contactNumber, workSchedule);
        doctorList.add(newDoctor);
        System.out.println("Doctor registered successfully with ID: D" + newDoctor.doctorID);
    }

    public static void listDoctors(){
        preloadDoctors();
        if(doctorList.isEmpty()){
            System.out.println("No doctors registered.");
            return;
        }

        System.out.println("\n--- LIST OF DOCTORS ---");
        final int pageSize = 5;
        int totalDoctors = doctorList.size();
        int totalPages = (int) Math.ceil((double) totalDoctors / pageSize);
        int currentPage = 1;

        while (true) {
            int start = (currentPage - 1) * pageSize;
            int end = Math.min(start + pageSize, totalDoctors);

            System.out.println("\nPage " + currentPage + " of " + totalPages);
            System.out.println("---------------------------");
            for (int i = start; i < end; i++) {
                doctorList.get(i).displayDoctorInfo();
            }

            if (totalPages == 1) break;

            System.out.println("n - Next Page | p - Previous Page | q - Quit");
            System.out.print("Enter choice: ");
            String choice = a.nextLine();

            if (choice.equals("n") && currentPage < totalPages) {
                currentPage++;
            } else if (choice.equals("p") && currentPage > 1) {
                currentPage--;
            } else if (choice.equals("q")) {
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void viewSingleDoctor(){
        preloadDoctors();
        System.out.println("\n--- VIEW DOCTOR ---");
        System.out.print("Enter Doctor ID to view: D");
        
        try {
            int id = Integer.parseInt(a.nextLine());
            boolean found = false;
            
            for(Doctor d : doctorList){
                if(d.doctorID == id){
                    d.displayDoctorInfo();
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                System.out.println("Doctor with ID D" + id + " not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter numbers only.");
        }
    }

    public static void updateDoctor(){
        System.out.println("\n--- UPDATE DOCTOR ---");

        if (doctorList.isEmpty()) {
            System.out.println("No doctors available to update.");
            return;
        }
        
        System.out.print("Enter Doctor ID to update: D");
        
        try {
            int id = Integer.parseInt(a.nextLine());
            boolean found = false;

            for(Doctor d : doctorList){
                if(d.doctorID == id){
                    found = true;
                    System.out.print("Enter new First Name (current: " + d.firstName + "): ");
                    String firstName = a.nextLine();
                    System.out.print("Enter new Last Name (current: " + d.lastName + "): ");
                    String lastName = a.nextLine();
                    System.out.print("Enter new Specialization (current: " + d.specialization + "): ");
                    String specialization = a.nextLine();
                    
                    String contactNumber;
                    while (true) {
                        System.out.print("Enter new Contact Number (current: " + d.contactNumber + "): ");
                        contactNumber = a.nextLine();
                        if (contactNumber.isEmpty()) {
                            break;
                        }
                        if (contactNumber.length() == 11) {
                            break;
                        } else {
                            System.out.println("Invalid contact number. Must contain exactly 11 digits and only numbers.");
                        }
                    }
                    
                    System.out.print("Enter new Work Schedule (current: " + d.workSchedule + "): ");
                    String workSchedule = a.nextLine();

                    if(!firstName.isEmpty()) d.firstName = firstName;
                    if(!lastName.isEmpty()) d.lastName = lastName;
                    if(!specialization.isEmpty()) d.specialization = specialization;
                    if(!contactNumber.isEmpty()) d.contactNumber = contactNumber;
                    if(!workSchedule.isEmpty()) d.workSchedule = workSchedule;

                    System.out.println("Doctor with ID D" + id + " updated successfully.");
                    break;
                }
            }
            
            if (!found) {
                System.out.println("Doctor with ID D" + id + " not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter numbers only.");
        }
    }

    public static void deleteDoctor(){
        System.out.println("\n--- DELETE DOCTOR ---");

        if (doctorList.isEmpty()) {
            System.out.println("No doctors available to delete.");
            return;
        }

        System.out.print("Enter Doctor ID to delete: D");
        
        try {
            int id = Integer.parseInt(a.nextLine());
            boolean found = false;

            for(Doctor d : doctorList){
                if(d.doctorID == id){
                    found = true;
                    
                    d.activeStatus = false;
                    archivedDoctorList.add(d);
                    doctorList.remove(d);
                    System.out.println("Doctor with ID D" + id + " deleted (archived) successfully.");
                    break;
                }
            }
            
            if (!found) {
                System.out.println("Doctor with ID D" + id + " not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter numbers only.");
        }
    }

    public static void reactivateDoctor(){
        System.out.println("\n--- REACTIVATE DOCTOR ---");
        
        if (archivedDoctorList.isEmpty()) {
            System.out.println("No archived doctors available.");
            return;
        }
        
        System.out.print("Enter Doctor ID to reactivate: D");
        
        try {
            int id = Integer.parseInt(a.nextLine());
            boolean found = false;

            for(Doctor d : archivedDoctorList){
                if(d.doctorID == id){
                    found = true;
                    d.activeStatus = true;
                    doctorList.add(d);
                    archivedDoctorList.remove(d);
                    System.out.println("Doctor with ID D" + id + " reactivated successfully.");
                    break;
                }
            }
            
            if (!found) {
                System.out.println("Doctor with ID D" + id + " not found in archived list.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter numbers only.");
        }
    }

    public static void viewArchivedDoctors() {
        System.out.println("\n--- ARCHIVED DOCTORS ---");

        if (archivedDoctorList.isEmpty()) {
            System.out.println("No archived doctors.");
            return;
        }

        for (Doctor d : archivedDoctorList) {
            d.displayDoctorInfo();
        }
    }
    
    // Helper method to find doctor by ID
    public static Doctor getDoctorById(int doctorId) {
        for (Doctor doctor : doctorList) {
            if (doctor.doctorID == doctorId) {
                return doctor;
            }
        }
        for (Doctor doctor : archivedDoctorList) {
            if (doctor.doctorID == doctorId) {
                return doctor;
            }
        }
        return null;
    }
}