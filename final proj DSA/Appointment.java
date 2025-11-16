import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Appointment {
    private static int nextId = 3001;
    int appointmentId;
    int patientId;
    int doctorId;
    LocalDate appointmentDate;
    LocalTime appointmentTime;
    String reason;
    String status = "Scheduled";
    int priority; // 0 = normal, 1 = emergency

    static Queue<Appointment> normalQueue = new LinkedList<>();
    static PriorityQueue<Appointment> emergencyQueue =
            new PriorityQueue<>(Comparator.comparing(appt -> appt.appointmentDate));
    static LinkedList<Appointment> appointmentList = new LinkedList<>();
    private static final Scanner a = new Scanner(System.in);

    public Appointment(int patientId, int doctorId, LocalDate date, LocalTime time, String reason, int priority) {
        this.appointmentId = nextId++;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = date;
        this.appointmentTime = time;
        this.reason = reason;
        this.priority = priority;
    }

    public void displayAppointmentInfo() {
        System.out.println("Appointment ID: A" + appointmentId);
        System.out.println("Patient ID: P" + patientId);
        System.out.println("Doctor ID: D" + doctorId);
        System.out.println("Date: " + appointmentDate);
        System.out.println("Time: " + appointmentTime);
        System.out.println("Reason: " + reason);
        System.out.println("Type: " + (priority == 1 ? "Emergency" : "Normal"));
        System.out.println("Status: " + status);
        System.out.println("--------------------------------");
    }

    // ------------------ Create Appointment ------------------
    public static void createAppointment() {
        System.out.println("\n--- Create Appointment ---");

        // Select Patient
        Patient.preloadPatients();
        if (Patient.patientList.isEmpty()) {
            System.out.println("No patients available.");
            return;
        }
        Patient.listPatients();
        System.out.print("Enter Patient ID: P");
        try {
            int pid = Integer.parseInt(a.nextLine());
            Patient selectedPatient = Patient.getPatientById(pid);
            
            if (selectedPatient == null || !selectedPatient.activeStatus) {
                System.out.println("Invalid or archived patient.");
                return;
            }

            // Select Doctor
            Doctor.preloadDoctors();
            if (Doctor.doctorList.isEmpty()) {
                System.out.println("No doctors available.");
                return;
            }

            Doctor.listDoctors();
            System.out.print("Enter Doctor ID: D");
            int did = Integer.parseInt(a.nextLine());
            Doctor selectedDoctor = Doctor.getDoctorById(did);
            
            if (selectedDoctor == null || !selectedDoctor.activeStatus) {
                System.out.println("Doctor not found or inactive.");
                return;
            }

            // Check if emergency FIRST
            System.out.print("Is this an EMERGENCY? (y/n): ");
            String emergencyInput = a.nextLine().toLowerCase();
            boolean isEmergency = emergencyInput.equals("y") || emergencyInput.equals("yes");
            int priority = isEmergency ? 1 : 0;

            if (isEmergency) {
                System.out.println("EMERGENCY CASE - Finding immediate slot...");
                // For emergencies, try to schedule today or tomorrow
                createEmergencyAppointment(pid, did, selectedDoctor);
            } else {
                // For normal appointments, proceed as before
                createNormalAppointment(pid, did, selectedDoctor, priority);
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    // ------------------ Emergency Appointment ------------------
    private static void createEmergencyAppointment(int patientId, int doctorId, Doctor doctor) {
        // Try to schedule emergency for today or tomorrow
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        
        LocalDate selectedDate = null;
        List<LocalTime> availableSlots;
        
        // Check today first
        availableSlots = getAvailableTimeSlots(doctor, today);
        if (!availableSlots.isEmpty()) {
            selectedDate = today;
            System.out.println("Emergency slot available TODAY!");
        } else {
            // Check tomorrow
            availableSlots = getAvailableTimeSlots(doctor, tomorrow);
            if (!availableSlots.isEmpty()) {
                selectedDate = tomorrow;
                System.out.println("Emergency slot available TOMORROW!");
            }
        }
        
        if (selectedDate == null) {
            System.out.println("No emergency slots available for today or tomorrow.");
            System.out.println("Please try another doctor or visit emergency room.");
            return;
        }

        // Auto-assign the earliest available slot for emergency
        LocalTime selectedTime = availableSlots.get(0);
        System.out.println("Auto-assigned emergency slot: " + selectedDate + " at " + selectedTime);

        System.out.print("Enter Reason: ");
        String reason = a.nextLine();

        Appointment newAppt = new Appointment(patientId, doctorId, selectedDate, selectedTime, reason, 1);
        appointmentList.add(newAppt);
        emergencyQueue.add(newAppt);
        
        System.out.println("EMERGENCY Appointment created successfully! ID: A" + newAppt.appointmentId);
        System.out.println("Added to EMERGENCY Queue - Will be served first!");
    }

    // ------------------ Normal Appointment ------------------
    private static void createNormalAppointment(int patientId, int doctorId, Doctor doctor, int priority) {
        // Input Date
        LocalDate date = null;
        while (date == null) {
            System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
            String input = a.nextLine();
            try {
                date = LocalDate.parse(input);
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("Cannot schedule appointment in the past.");
                    date = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Try again.");
            }
        }

        // Get available time slots
        List<LocalTime> availableSlots = getAvailableTimeSlots(doctor, date);
        if (availableSlots.isEmpty()) {
            System.out.println("No available time slots for this doctor on " + date);
            return;
        }

        // Show available time slots
        System.out.println("Available time slots:");
        for (int i = 0; i < availableSlots.size(); i++) {
            System.out.println((i + 1) + ". " + availableSlots.get(i));
        }

        // Input Time
        LocalTime time = null;
        while (time == null) {
            System.out.print("Enter Appointment Time (HH:MM): ");
            String input = a.nextLine();
            try {
                time = LocalTime.parse(input);
                if (!availableSlots.contains(time)) {
                    System.out.println("Time slot not available.");
                    time = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Try again.");
            }
        }

        // Enter Reason
        System.out.print("Enter Reason: ");
        String reason = a.nextLine();

        Appointment newAppt = new Appointment(patientId, doctorId, date, time, reason, priority);
        appointmentList.add(newAppt);

        // Add to queue
        if (priority == 1) {
            emergencyQueue.add(newAppt);
            System.out.println("Added to EMERGENCY Queue.");
        } else {
            normalQueue.add(newAppt);
            System.out.println("Added to NORMAL Queue.");
        }

        System.out.println("Appointment created successfully! ID: A" + newAppt.appointmentId);
    }

    // ------------------ Get Available Time Slots ------------------
    public static List<LocalTime> getAvailableTimeSlots(Doctor doctor, LocalDate date) {
        List<LocalTime> slots = new ArrayList<>();
        
        try {
            String[] times = doctor.workSchedule.split("-");
            LocalTime start = LocalTime.parse(times[0]);
            LocalTime end = LocalTime.parse(times[1]);
            
            for (LocalTime time = start; time.isBefore(end); time = time.plusMinutes(60)) {
                boolean isOccupied = false;
                for (Appointment appt : appointmentList) {
                    if (appt.doctorId == doctor.doctorID &&
                        appt.appointmentDate.equals(date) &&
                        appt.appointmentTime.equals(time) &&
                        appt.status.equals("Scheduled")) {
                        isOccupied = true;
                        break;
                    }
                }
                if (!isOccupied) slots.add(time);
            }
        } catch (Exception e) {
            System.out.println("Error parsing doctor schedule.");
        }
        
        return slots;
    }

    // ------------------ Check Emergency Availability ------------------
    public static void checkEmergencyAvailability() {
        System.out.println("\n--- Emergency Availability Check ---");
        Doctor.preloadDoctors();
        
        boolean foundEmergencySlot = false;
        
        for (Doctor doctor : Doctor.doctorList) {
            if (doctor.activeStatus) {
                // Check today
                List<LocalTime> todaySlots = getAvailableTimeSlots(doctor, LocalDate.now());
                if (!todaySlots.isEmpty()) {
                    System.out.println("Dr. " + doctor.firstName + " " + doctor.lastName + 
                                     " - Available TODAY at: " + todaySlots.get(0));
                    foundEmergencySlot = true;
                    continue;
                }
                
                // Check tomorrow
                List<LocalTime> tomorrowSlots = getAvailableTimeSlots(doctor, LocalDate.now().plusDays(1));
                if (!tomorrowSlots.isEmpty()) {
                    System.out.println("Dr. " + doctor.firstName + " " + doctor.lastName + 
                                     " - Available TOMORROW at: " + tomorrowSlots.get(0));
                    foundEmergencySlot = true;
                }
            }
        }
        
        if (!foundEmergencySlot) {
            System.out.println("No emergency slots available today or tomorrow.");
        }
    }

    // ... (keep all your existing methods: listAppointments, cancelAppointment, 
    // serveAppointment, viewAppointmentQueue, rescheduleAppointment, 
    // findQuickSlot, showOtherDoctors exactly as they are)
    
    // ------------------ List All Appointments ------------------
    public static void listAppointments() {
        if (appointmentList.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }
        System.out.println("\n--- List of Appointments ---");
        for (Appointment appt : appointmentList) {
            appt.displayAppointmentInfo();
        }
    }

    // ------------------ Cancel Appointment ------------------
    public static void cancelAppointment() {
        System.out.print("Enter Appointment ID to cancel: A");
        try {
            int id = Integer.parseInt(a.nextLine());
            boolean found = false;
            
            for (Appointment appt : appointmentList) {
                if (appt.appointmentId == id && appt.status.equals("Scheduled")) {
                    appt.status = "Cancelled";
                    normalQueue.remove(appt);
                    emergencyQueue.remove(appt);
                    System.out.println("Appointment A" + id + " cancelled.");
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                System.out.println("Appointment not found or already cancelled/completed.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    // ------------------ Serve Appointment ------------------
    public static void serveAppointment() {
        Appointment next = null;
        if (!emergencyQueue.isEmpty()) {
            next = emergencyQueue.poll();
        } else if (!normalQueue.isEmpty()) {
            next = normalQueue.poll();
        }

        if (next != null) {
            System.out.println("Serving Appointment:");
            next.displayAppointmentInfo();
            next.status = "Completed";
            System.out.println("Appointment marked as completed.");
        } else {
            System.out.println("No appointments to serve.");
        }
    }

    // ------------------ View Appointment Queue ------------------
    public static void viewAppointmentQueue() {
        System.out.println("\n--- Emergency Queue ---");
        if (emergencyQueue.isEmpty()) {
            System.out.println("None");
        } else {
            for (Appointment appt : emergencyQueue) {
                System.out.println("A" + appt.appointmentId + " | Patient P" + appt.patientId + 
                                 " | Doctor D" + appt.doctorId + " | " + appt.appointmentDate + 
                                 " " + appt.appointmentTime);
            }
        }

        System.out.println("\n--- Normal Queue ---");
        if (normalQueue.isEmpty()) {
            System.out.println("None");
        } else {
            for (Appointment appt : normalQueue) {
                System.out.println("A" + appt.appointmentId + " | Patient P" + appt.patientId + 
                                 " | Doctor D" + appt.doctorId + " | " + appt.appointmentDate + 
                                 " " + appt.appointmentTime);
            }
        }
    }

    // ------------------ Reschedule Appointment (Simple Version) ------------------
    public static void rescheduleAppointment() {
        System.out.print("Enter Appointment ID to reschedule: A");
        
        try {
            int id = Integer.parseInt(a.nextLine());
            Appointment appt = null;
            
            // Find appointment
            for (Appointment appointment : appointmentList) {
                if (appointment.appointmentId == id && appointment.status.equals("Scheduled")) {
                    appt = appointment;
                    break;
                }
            }

            if (appt == null) {
                System.out.println("Appointment not found.");
                return;
            }

            // Show current details
            System.out.println("\nCurrent Appointment:");
            appt.displayAppointmentInfo();

            // Ask about doctor change
            System.out.print("Change doctor? (y/n): ");
            String answer = a.nextLine().toLowerCase();
            
            if (answer.equals("y") || answer.equals("yes")) {
                // Show all doctors
                System.out.println("\nAll Doctors:");
                for (Doctor doctor : Doctor.doctorList) {
                    if (doctor.activeStatus) {
                        System.out.println("D" + doctor.doctorID + " - Dr. " + doctor.firstName + " " + 
                                         doctor.lastName + " (" + doctor.specialization + ")");
                    }
                }
                
                System.out.print("Enter new Doctor ID: D");
                int newDoctorId = Integer.parseInt(a.nextLine());
                Doctor newDoctor = Doctor.getDoctorById(newDoctorId);
                
                if (newDoctor == null || !newDoctor.activeStatus) {
                    System.out.println("Doctor not available.");
                    return;
                }
                
                appt.doctorId = newDoctorId;
                System.out.println("Doctor changed to: Dr. " + newDoctor.firstName + " " + newDoctor.lastName);
            }

            // Get new date
            System.out.print("Enter new date (YYYY-MM-DD): ");
            String dateInput = a.nextLine();
            LocalDate newDate;
            
            try {
                newDate = LocalDate.parse(dateInput);
                if (newDate.isBefore(LocalDate.now())) {
                    System.out.println("Cannot pick past date.");
                    return;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Wrong date format.");
                return;
            }

            // Get current doctor
            Doctor currentDoctor = Doctor.getDoctorById(appt.doctorId);
            if (currentDoctor == null) {
                System.out.println("Doctor not found.");
                return;
            }

            // Check available times
            List<LocalTime> freeSlots = getAvailableTimeSlots(currentDoctor, newDate);
            
            if (freeSlots.isEmpty()) {
                System.out.println("No free slots on " + newDate);
                System.out.println("Try another date or doctor.");
                return;
            }

            // Show available times
            System.out.println("Free time slots:");
            for (int i = 0; i < freeSlots.size(); i++) {
                System.out.println((i + 1) + ". " + freeSlots.get(i));
            }

            // Get new time
            System.out.print("Pick time (HH:MM): ");
            String timeInput = a.nextLine();
            LocalTime newTime;
            
            try {
                newTime = LocalTime.parse(timeInput);
                if (!freeSlots.contains(newTime)) {
                    System.out.println("Time not available.");
                    return;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Wrong time format.");
                return;
            }

            // Update appointment
            appt.appointmentDate = newDate;
            appt.appointmentTime = newTime;

            // Update queue
            if (appt.priority == 1) {
                // Remove and add back to emergency queue
                emergencyQueue.remove(appt);
                emergencyQueue.add(appt);
            } else {
                // Remove and add back to normal queue
                normalQueue.remove(appt);
                normalQueue.add(appt);
            }

            System.out.println("Appointment rescheduled!");
            System.out.println("New date: " + newDate);
            System.out.println("New time: " + newTime);

        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
        }
    }

    // ------------------ Find Quick Slot ------------------
    public static void findQuickSlot() {
        System.out.print("Enter Appointment ID: A");
        
        try {
            int id = Integer.parseInt(a.nextLine());
            Appointment appt = null;
            
            // Find appointment
            for (Appointment appointment : appointmentList) {
                if (appointment.appointmentId == id && appointment.status.equals("Scheduled")) {
                    appt = appointment;
                    break;
                }
            }

            if (appt == null) {
                System.out.println("Appointment not found.");
                return;
            }

            System.out.println("Finding earliest available slot...");

            LocalDate today = LocalDate.now();
            boolean found = false;

            // Check next 7 days
            for (int day = 0; day <= 7; day++) {
                LocalDate checkDate = today.plusDays(day);
                
                // Check all doctors
                for (Doctor doctor : Doctor.doctorList) {
                    if (!doctor.activeStatus) continue;
                    
                    List<LocalTime> slots = getAvailableTimeSlots(doctor, checkDate);
                    if (!slots.isEmpty()) {
                        // Found a slot - update appointment
                        appt.doctorId = doctor.doctorID;
                        appt.appointmentDate = checkDate;
                        appt.appointmentTime = slots.get(0);
                        
                        // Update queue
                        if (appt.priority == 1) {
                            emergencyQueue.remove(appt);
                            emergencyQueue.add(appt);
                        } else {
                            normalQueue.remove(appt);
                            normalQueue.add(appt);
                        }
                        
                        System.out.println("Found slot!");
                        System.out.println("Doctor: Dr. " + doctor.firstName + " " + doctor.lastName);
                        System.out.println("Date: " + checkDate);
                        System.out.println("Time: " + slots.get(0));
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }

            if (!found) {
                System.out.println("No slots found in next 7 days.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
        }
    }

    // ------------------ Show Other Doctors ------------------
    public static void showOtherDoctors(LocalDate date) {
        System.out.println("\nDoctors with free slots on " + date + ":");
        
        boolean found = false;
        
        for (Doctor doctor : Doctor.doctorList) {
            if (!doctor.activeStatus) continue;
            
            List<LocalTime> slots = getAvailableTimeSlots(doctor, date);
            if (!slots.isEmpty()) {
                System.out.println("- Dr. " + doctor.firstName + " " + doctor.lastName + 
                                 " (D" + doctor.doctorID + ")");
                System.out.println("  Free at: " + slots.get(0));
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No doctors free on this date.");
        }
    }
}