import java.util.*;

public class SearchFunctions {

    // LINEAR SEARCH for PATIENT (NAME)
    public static void linearSearchPatientsByName(String name) {
        System.out.println("Searching for (Patient Name): " + name);
        long start = System.nanoTime();

        if (name == null || name.isEmpty()) {
            System.out.println("Please enter a name to search.");
            return;
        }

        Patient.preloadPatients();
        String searchName = name.toLowerCase();
        boolean found = false;

        for (Patient patient : Patient.patientList) {
            if (patient.firstName.toLowerCase().contains(searchName) ||
                patient.lastName.toLowerCase().contains(searchName)) {
                patient.displayPatientInfo();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No patients found matching your search.");
        }

        long end = System.nanoTime();
        double timeMs = (end - start) / 1_000_000.0;
        System.out.println("Search Time: " + timeMs + " ms\n");
    }

    // LINEAR SEARCH for DOCTOR (NAME)
    public static void linearSearchDoctorsByName(String name) {
        System.out.println("Searching for (Doctor Name): " + name);
        long start = System.nanoTime();

        if (name == null || name.isEmpty()) {
            System.out.println("Please enter a name to search.");
            return;
        }

        Doctor.preloadDoctors();
        String searchName = name.toLowerCase();
        boolean found = false;

        for (Doctor doctor : Doctor.doctorList) {
            if (doctor.firstName.toLowerCase().contains(searchName) ||
                doctor.lastName.toLowerCase().contains(searchName)) {
                doctor.displayDoctorInfo();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No doctors found matching your search.");
        }

        long end = System.nanoTime();
        double timeMs = (end - start) / 1_000_000.0;
        System.out.println("Search Time: " + timeMs + " ms\n");
    }

    // BINARY SEARCH for PATIENT (ID) - SILENT VERSION (no output)
    public static Patient binarySearchPatientById(int patientId) {
        Patient.preloadPatients();
        if (Patient.patientList.isEmpty()) {
            return null;
        }

        List<Patient> sortedPatients = new ArrayList<>(Patient.patientList);
        Collections.sort(sortedPatients, (p1, p2) -> Integer.compare(p1.patientID, p2.patientID));

        int left = 0;
        int right = sortedPatients.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            Patient midPatient = sortedPatients.get(mid);

            if (midPatient.patientID == patientId) {
                return midPatient;
            }
            else if (midPatient.patientID < patientId) {
                left = mid + 1;
            }
            else {
                right = mid - 1;
            }
        }
        return null;
    }

    // BINARY SEARCH for DOCTOR (ID) - SILENT VERSION (no output)
    public static Doctor binarySearchDoctorById(int doctorId) {
        Doctor.preloadDoctors();
        if (Doctor.doctorList.isEmpty()) {
            return null;
        }

        List<Doctor> sortedDoctors = new ArrayList<>(Doctor.doctorList);
        Collections.sort(sortedDoctors, (d1, d2) -> Integer.compare(d1.doctorID, d2.doctorID));

        int left = 0;
        int right = sortedDoctors.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            Doctor midDoctor = sortedDoctors.get(mid);

            if (midDoctor.doctorID == doctorId) {
                return midDoctor;
            }
            else if (midDoctor.doctorID < doctorId) {
                left = mid + 1;
            }
            else {
                right = mid - 1;
            }
        }
        return null;
    }

}