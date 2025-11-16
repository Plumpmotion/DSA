import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Hospital Management System ---");
            System.out.println("1. Patient Management");
            System.out.println("2. Doctor Management");
            System.out.println("3. Appointment Management");
            System.out.println("4. Search Functions");
            System.out.println("5. Treatment Management");
            System.out.println("6. Exit");
            System.out.println("----------------------------------");
            System.out.print("Select an option: ");
            
            String input = scanner.nextLine();
            
            if (input.isEmpty()) {
                System.out.println("Please enter a number (1-6)");
                continue;
            }

            int choice = getNumber(input);
            if (choice == -1) continue;

            switch (choice) {
                case 1 -> handlePatientMenu(scanner);
                case 2 -> handleDoctorMenu(scanner);
                case 3 -> handleAppointmentMenu(scanner);
                case 4 -> handleSearchMenu(scanner);
                case 5 -> handleTreatmentMenu(scanner);
                case 6 -> {
                    System.out.println("Exiting system... Goodbye!");
                    scanner.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid option. Please enter 1-6");
            }
        }
    }

    // Simple method to parse numbers with error handling
    private static int getNumber(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number");
            return -1;
        }
    }

    private static void handlePatientMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n--- PATIENT MANAGEMENT ---");
            System.out.println("1. Register Patient");
            System.out.println("2. List Patients");
            System.out.println("3. View Patient");
            System.out.println("4. Update Patient");
            System.out.println("5. Delete Patient");
            System.out.println("6. Reactivate Patient");
            System.out.println("7. View Archived Patients");
            System.out.println("8. Back to Main Menu");
            System.out.print("Select an option: ");

            int choice = getNumber(scanner.nextLine());
            if (choice == -1) continue;

            switch (choice) {
                case 1 -> Patient.registerPatient();
                case 2 -> Patient.listPatients();
                case 3 -> Patient.viewSinglePatient();
                case 4 -> Patient.updatePatient();
                case 5 -> Patient.deletePatient();
                case 6 -> Patient.reactivatePatient();
                case 7 -> Patient.viewArchivedPatients();
                case 8 -> { return; }
                default -> System.out.println("Please enter 1-8");
            }
        }
    }

    private static void handleDoctorMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n--- DOCTOR MANAGEMENT ---");
            System.out.println("1. Register Doctor");
            System.out.println("2. List Doctors");
            System.out.println("3. View Doctor");
            System.out.println("4. Update Doctor");
            System.out.println("5. Delete Doctor");
            System.out.println("6. Reactivate Doctor");
            System.out.println("7. View Archived Doctors");
            System.out.println("8. Back to Main Menu");
            System.out.print("Select an option: ");

            int choice = getNumber(scanner.nextLine());
            if (choice == -1) continue;

            switch (choice) {
                case 1 -> Doctor.registerDoctor();
                case 2 -> Doctor.listDoctors();
                case 3 -> Doctor.viewSingleDoctor();
                case 4 -> Doctor.updateDoctor();
                case 5 -> Doctor.deleteDoctor();
                case 6 -> Doctor.reactivateDoctor();
                case 7 -> Doctor.viewArchivedDoctors();
                case 8 -> { return; }
                default -> System.out.println("Please enter 1-8");
            }
        }
    }

    private static void handleAppointmentMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n--- APPOINTMENT MANAGEMENT ---");
            System.out.println("1. Schedule Appointment");
            System.out.println("2. Serve Next Appointment");
            System.out.println("3. View Appointments");
            System.out.println("4. Cancel Appointment");
            System.out.println("5. Reschedule Appointment");
            System.out.println("6. View Appointment Queue");
            System.out.println("7. Back to Main Menu");
            System.out.print("Select an option: ");

            int choice = getNumber(scanner.nextLine());
            if (choice == -1) continue;

            switch (choice) {
                case 1 -> Appointment.createAppointment();
                case 2 -> Appointment.serveAppointment();
                case 3 -> Appointment.listAppointments();
                case 4 -> Appointment.cancelAppointment();
                case 5 -> Appointment.rescheduleAppointment();
                case 6 -> Appointment.viewAppointmentQueue();
                case 7 -> { return; }
                default -> System.out.println("Please enter 1-7");
            }
        }
    }

    private static void handleSearchMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n--- SEARCH FUNCTIONS ---");
            System.out.println("1. Search Patients by Name");
            System.out.println("2. Search Patients by ID");
            System.out.println("3. Search Doctors by Name");
            System.out.println("4. Search Doctors by ID");
            System.out.println("5. Back to Main Menu");
            System.out.print("Select an option: ");

            int choice = getNumber(scanner.nextLine());
            if (choice == -1) continue;

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter patient name: ");
                    String name = scanner.nextLine();
                    SearchFunctions.linearSearchPatientsByName(name);
                }
                case 2 -> {
                    System.out.print("Enter patient ID: P");
                    int id = getNumber(scanner.nextLine());
                    if (id != -1) {
                        Patient patient = SearchFunctions.binarySearchPatientById(id);
                        if (patient != null) patient.displayPatientInfo();
                        else System.out.println("Patient not found");
                    }
                }
                case 3 -> {
                    System.out.print("Enter doctor name: ");
                    String name = scanner.nextLine();
                    SearchFunctions.linearSearchDoctorsByName(name);
                }
                case 4 -> {
                    System.out.print("Enter doctor ID: D");
                    int id = getNumber(scanner.nextLine());
                    if (id != -1) {
                        Doctor doctor = SearchFunctions.binarySearchDoctorById(id);
                        if (doctor != null) doctor.displayDoctorInfo();
                        else System.out.println("Doctor not found");
                    }
                }
                case 5 -> { return; }
                default -> System.out.println("Please enter 1-5");
            }
        }
    }

    private static void handleTreatmentMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n--- TREATMENT MANAGEMENT ---");
            System.out.println("1. Add Treatment to Patient");
            System.out.println("2. View Patient Treatment History");
            System.out.println("3. Back to Main Menu");
            System.out.print("Select an option: ");

            int choice = getNumber(scanner.nextLine());
            if (choice == -1) continue;

            switch (choice) {
                case 1 -> Patient.addTreatment();
                case 2 -> Patient.viewTreatments();
                case 3 -> { return; }
                default -> System.out.println("Please enter 1-3");
            }
        }
    }
}