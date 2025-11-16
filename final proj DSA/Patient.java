import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Patient {
    private static int nextId = 1001;
    int patientID;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String gender;
    String phoneNumber;
    String address;
    String bloodType;
    LinkedList<String> allergies = new LinkedList<>();
    LinkedList<String> currentMedication = new LinkedList<>();
    LinkedList<String> medicalHistory = new LinkedList<>();
    LinkedList<Treatment> treatmentHistory = new LinkedList<>();
    boolean activeStatus = true;
    LocalDateTime created;
    LocalDateTime modified;

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

   

    static LinkedList<Patient> patientList = new LinkedList<>();
    static LinkedList<Patient> archivedList = new LinkedList<>();
    private static final Scanner sc = new Scanner(System.in);

    public Patient(String firstName, String lastName, LocalDate dateOfBirth, String gender,
                   String phoneNumber, String address, String bloodType) {
        this.patientID = nextId++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.bloodType = bloodType;
        this.created = LocalDateTime.now();
        this.modified = LocalDateTime.now();
    }

    // preload patient 
    public static void preloadPatients() {
        if (patientList.isEmpty()){
            patientList.add(new Patient("Ana", "Garcia", LocalDate.of(1985, 4, 12), "Female",
                    "09171234567", "123 Main St", "O+"));    
            patientList.add(new Patient("Luis", "Martinez", LocalDate.of(1990, 8, 23), "Male",
                    "09551234567", "456 Elm St", "A-"));
            patientList.add(new Patient("Carmen", "Dela Cruz", LocalDate.of(1978, 12, 5), "Female",
                    "09981234567", "789 Oak St", "B+"));
            patientList.add(new Patient("Jose", "Reyes", LocalDate.of(2000, 1, 30), "Male",
                    "09351234567", "321 Pine St", "AB-"));
            patientList.add(new Patient("Maria", "Lopez", LocalDate.of(1995, 6, 15), "Female",
                    "09771234567", "654 Cedar St", "O-"));          
            patientList.add(new Patient("Pedro", "Santos", LocalDate.of(1988, 11, 20), "Male",
                    "09181234567", "987 Birch St", "A+"));
        }
    }

    public int getAge() {
        return Period.between(this.dateOfBirth, LocalDate.now()).getYears();
    }

    public void displayPatientInfo() {
        System.out.println("Patient ID: P" + patientID);
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("DOB: " + dateOfBirth + " (Age: " + getAge() + ")");
        System.out.println("Gender: " + gender);
        System.out.println("Phone: " + phoneNumber);
        System.out.println("Address: " + address);
        System.out.println("Blood Type: " + bloodType);

        // Allergies
        System.out.print("Allergies: ");
        if (allergies.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < allergies.size(); i++) {
                System.out.print(allergies.get(i));
                if (i != allergies.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }

        // Current Medications
        System.out.print("Current Medications: ");
        if (currentMedication.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < currentMedication.size(); i++) {
                System.out.print(currentMedication.get(i));
                if (i != currentMedication.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }

        // if the user set appointments make sure to update medical history
        
        System.out.print("Medical History: ");
        if (medicalHistory.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < medicalHistory.size(); i++) {
                System.out.print(medicalHistory.get(i));
                if (i != medicalHistory.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }

        // Status
        if (activeStatus) {
            System.out.println("Status: Active");
        } else {
            System.out.println("Status: Archived");
        }
        System.out.println("Created At: " + created.format(TIMESTAMP_FORMATTER));
        System.out.println("Updated At: " + modified.format(TIMESTAMP_FORMATTER));
        System.out.println("--------------------------------");
    }

    // --- Register ---
    public static void registerPatient() {
        System.out.println("\n--- Register a New Patient ---");
        System.out.print("First Name: ");
        String firstName = sc.nextLine();
        System.out.print("Last Name: ");
        String lastName = sc.nextLine();

        LocalDate dob = null;
        while (dob == null) {
            System.out.print("Date of Birth (YYYY-MM-DD): ");
            String dobInput = sc.nextLine();
            try {
                dob = LocalDate.parse(dobInput);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Try again.");
            }
        }

        System.out.print("Gender: ");
        String gender = sc.nextLine();

        String phone;
        while (true) {
            System.out.print("Phone Number (11 digits): ");
            phone = sc.nextLine();
            if (phone.length() == 11) {
                break;
            } else {
                System.out.println("Invalid phone number. Must contain exactly 11 digits and only numbers.");
            }
        }
        
        System.out.print("Address: ");
        String address = sc.nextLine();
        System.out.print("Blood Type: ");
        String blood = sc.nextLine();
        
        // Input allergies
        LinkedList<String> allergies = new LinkedList<>();
        System.out.println("Enter Allergies (type 'done' when finished):");
        while (true) {
            System.out.print("Allergy: ");
            String allergy = sc.nextLine();
            if (allergy.equalsIgnoreCase("done")) break;
            if (!allergy.isEmpty()) allergies.add(allergy);
        }
        
        // Input current medications
        LinkedList<String> medications = new LinkedList<>();
        System.out.println("Enter Current Medications (type 'done' when finished):");
        while (true) {
            System.out.print("Medication: ");
            String med = sc.nextLine();
            if (med.equalsIgnoreCase("done")) break;
            if (!med.isEmpty()) medications.add(med);
        }

        // Input medical history
        LinkedList<String> history = new LinkedList<>();
        System.out.println("Enter Medical History (type 'done' when finished):");
        while (true) {
            System.out.print("Medical History: ");
            String hist = sc.nextLine();
            if (hist.equalsIgnoreCase("done")) break;
            if (!hist.isEmpty()) history.add(hist);
        }

        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || address.isEmpty() || blood.isEmpty()) {
            System.out.println("All fields are required.");
            return;
        }

        for (Patient p : patientList) {
            if (p.firstName.equalsIgnoreCase(firstName)
                    && p.lastName.equalsIgnoreCase(lastName)
                    && p.dateOfBirth.equals(dob)) {
                System.out.println("Error: Patient already exists.");
                return;
            }
        }

        Patient newPatient = new Patient(firstName, lastName, dob, gender, phone, address, blood);
        newPatient.allergies = allergies;
        newPatient.currentMedication = medications;
        newPatient.medicalHistory = history;
        patientList.add(newPatient);
        System.out.println("Patient registered successfully with ID: P" + newPatient.patientID);
    }

    // --- List with pagination (5 per page) ---
    public static void listPatients() {
        preloadPatients();
        if (patientList.isEmpty()) {
            System.out.println("No patients found.");
            return;
        }

        System.out.println("\n--- List of Patients ---");
        final int pageSize = 5;
        int total = patientList.size();
        int pages = (int) Math.ceil((double) total / pageSize);
        int page = 1;

        while (true) {
            int from = (page - 1) * pageSize;
            int to = Math.min(total, from + pageSize);
            System.out.println("\nPage " + page + " of " + pages);
            System.out.println("--------------------------------");
            for (int i = from; i < to; i++) {
                patientList.get(i).displayPatientInfo();
            }

            if (pages == 1) break;

            System.out.println("[N]ext page | [P]revious page | [E]xit listing");
            System.out.print("Choose an option: ");
            String cmd = sc.nextLine().toLowerCase();

            if (cmd.equals("n") && page < pages) {
                page++;
            } else if (cmd.equals("p") && page > 1) {
                page--;
            } else if (cmd.equals("e")) {
                break;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }

    // --- View ---
    public static void viewSinglePatient() {
        System.out.println("----------------------------");
        System.out.print("Enter Patient ID to view: P");
        try {
            int id = Integer.parseInt(sc.nextLine());
            boolean found = false;
            
            for (Patient p : patientList) {
                if (p.patientID == id) {
                    p.displayPatientInfo();
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                System.out.println("Patient not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    // --- Update ---
    public static void updatePatient() {
        System.out.print("Enter Patient ID to update: P");
        try {
            int id = Integer.parseInt(sc.nextLine());
            boolean found = false;

            for (Patient p : patientList) {
                if (p.patientID == id) {
                    found = true;
                    System.out.print("New Phone (current: " + p.phoneNumber + "): ");
                    String newPhone = sc.nextLine();
                    if (!newPhone.isEmpty()) p.phoneNumber = newPhone;

                    System.out.print("New Address (current: " + p.address + "): ");
                    String newAddress = sc.nextLine();
                    if (!newAddress.isEmpty()) p.address = newAddress;

                    p.modified = LocalDateTime.now();
                    System.out.println("Patient updated successfully.");
                    break;
                }
            }
            
            if (!found) {
                System.out.println("Patient not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    // --- Delete (archive safely) ---
    public static void deletePatient() {
        System.out.print("Enter Patient ID to archive: P");
        try {
            int id = Integer.parseInt(sc.nextLine());
            Patient toArchive = null;

            for (Patient p : patientList) {
                if (p.patientID == id) {
                    toArchive = p;
                    break;
                }
            }
            
            if (toArchive == null) {
                System.out.println("Patient not found in active list!");
                return;
            }

            toArchive.activeStatus = false;
            patientList.remove(toArchive);
            archivedList.add(toArchive);

            System.out.println("Patient archived and removed from active list.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    // --- Reactivate ---
    public static void reactivatePatient() {
        if (archivedList.isEmpty()) {
            System.out.println("No archived patients.");
            return;
        }
        
        System.out.print("Enter Patient ID to reactivate: P");
        try {
            int id = Integer.parseInt(sc.nextLine());
            boolean found = false;

            for (Patient p : archivedList) {
                if (p.patientID == id) {
                    found = true;
                    p.activeStatus = true;
                    p.modified = LocalDateTime.now();
                    archivedList.remove(p);
                    patientList.add(p);
                    System.out.println("Patient reactivated successfully (P" + id + ")");
                    break;
                }
            }
            
            if (!found) {
                System.out.println("Patient not found in archived list.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    public static void viewArchivedPatients() {
        System.out.println("\n--- ARCHIVED PATIENTS ---");
        if (archivedList.isEmpty()) {
            System.out.println("No archived patients.");
            return;
        }

        for (Patient p : archivedList) {
            System.out.println("ID: P" + p.patientID + " | Name: " + p.firstName + " " + p.lastName);
        }
    }

    // Helper method to find patient by ID
    public static Patient getPatientById(int patientId) {
        for (Patient patient : patientList) {
            if (patient.patientID == patientId) {
                return patient;
            }
        }
        for (Patient patient : archivedList) {
            if (patient.patientID == patientId) {
                return patient;
            }
        }
        return null;
    }

    // Add Treatment
     public static void addTreatment() {
    System.out.println("\n--- Add Treatment ---");
    
    System.out.print("Enter Patient ID: P");
    try {
        int patientId = Integer.parseInt(sc.nextLine());
        Patient patient = Patient.getPatientById(patientId);
        
        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.print("Enter Diagnosis: ");
        String diagnosis = sc.nextLine();
        
        System.out.print("Enter Medication: ");
        String medication = sc.nextLine();

        // Create treatment
        Treatment treatment = new Treatment(diagnosis, medication);
        patient.treatmentHistory.add(treatment);
        
        // Update medical history
        if (!patient.medicalHistory.contains(diagnosis)) {
            patient.medicalHistory.add(diagnosis);
        }
        
        // Update current medication
        if (!patient.currentMedication.contains(medication)) {
            patient.currentMedication.add(medication);
        }

        System.out.println("Treatment added and medical history updated!");
        
    } catch (NumberFormatException e) {
        System.out.println("Invalid ID format.");
    }
}

    // View Treatments
    public static void viewTreatments() {
        System.out.print("Enter Patient ID: P");
        try {
            int id = Integer.parseInt(sc.nextLine());
            
            for (Patient p : patientList) {
                if (p.patientID == id) {
                    if (p.treatmentHistory.isEmpty()) {
                        System.out.println("No treatments found.");
                        return;
                    }
                    
                    System.out.println("Treatment History for " + p.firstName + " " + p.lastName + ":");
                    for (Treatment t : p.treatmentHistory) {
                        t.display();
                        System.out.println("---");
                    }
                    return;
                }
            }
            System.out.println("Patient not found.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

   

}