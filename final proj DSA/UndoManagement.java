import java.util.Stack;

public class UndoManagement {
    private static Stack<UndoAction> undoStack = new Stack<>();
    
    public static class UndoAction {
        String objectType; // "patient", "doctor", "appointment"
        String action;     // "add", "update", "delete"
        int objectId;
        Object previousState;
        
        public UndoAction(String objectType, String action, int objectId) {
            this.objectType = objectType;
            this.action = action;
            this.objectId = objectId;
        }
        
        public UndoAction(String objectType, String action, int objectId, Object previousState) {
            this(objectType, action, objectId);
            this.previousState = previousState;
        }
    }
    
    public static void push(UndoAction action) {
        undoStack.push(action);
    }
    
    public static void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }
        
        UndoAction action = undoStack.pop();
        
        switch (action.objectType) {
            case "patient" -> undoPatientAction(action);
            case "doctor" -> undoDoctorAction(action);
            case "appointment" -> undoAppointmentAction(action);
            default -> System.out.println("Unknown object type.");
        }
    }
    
    private static void undoPatientAction(UndoAction action) {
        switch (action.action) {
            case "add" -> {
                // Remove the patient that was added
                for (Patient p : Patient.patientList) {
                    if (p.patientID == action.objectId) {
                        Patient.patientList.remove(p);
                        System.out.println("Undo: Removed patient P" + action.objectId);
                        return;
                    }
                }
            }
            case "update" -> {
                // Restore previous state
                if (action.previousState instanceof Patient previousPatient) {
                    for (int i = 0; i < Patient.patientList.size(); i++) {
                if (Patient.patientList.get(i).patientID == action.objectId) {
                    Patient.patientList.set(i, previousPatient);
                    System.out.println("Undo: Restored patient P" + action.objectId);
                    return;
                }
            }
                }
            }
            case "delete" -> {
                // Reactivate the patient
                for (Patient p : Patient.archivedList) {
                    if (p.patientID == action.objectId) {
                        p.activeStatus = true;
                        Patient.archivedList.remove(p);
                        Patient.patientList.add(p);
                        System.out.println("Undo: Reactivated patient P" + action.objectId);
                        return;
                    }
                }
            }
        }
    }
    
    private static void undoDoctorAction(UndoAction action) {
        switch (action.action) {
            case "add" -> {
                for (Doctor d : Doctor.doctorList) {
                    if (d.doctorID == action.objectId) {
                        Doctor.doctorList.remove(d);
                        System.out.println("Undo: Removed doctor D" + action.objectId);
                        return;
                    }
                }
            }
            case "update" -> {
                if (action.previousState instanceof Doctor previousDoctor) {
                    for (int i = 0; i < Doctor.doctorList.size(); i++) {
                    if (Doctor.doctorList.get(i).doctorID == action.objectId) {
                    Doctor.doctorList.set(i, previousDoctor);
                    System.out.println("Undo: Restored doctor D" + action.objectId);
                return;
            }
        }
    }
}
            case "delete" -> {
                for (Doctor d : Doctor.archivedDoctorList) {
                    if (d.doctorID == action.objectId) {
                        d.activeStatus = true;
                        Doctor.archivedDoctorList.remove(d);
                        Doctor.doctorList.add(d);
                        System.out.println("Undo: Reactivated doctor D" + action.objectId);
                        return;
                    }
                }
            }
        }
    }
    
    private static void undoAppointmentAction(UndoAction action) {
        switch (action.action) {
            case "add" -> {
                for (Appointment apt : Appointment.appointmentList) {
                    if (apt.appointmentId == action.objectId) {
                        Appointment.appointmentList.remove(apt);
                        Appointment.normalQueue.remove(apt);
                        Appointment.emergencyQueue.remove(apt);
                        System.out.println("Undo: Removed appointment A" + action.objectId);
                        return;
                    }
                }
            }
            case "cancel" -> {
                for (Appointment apt : Appointment.appointmentList) {
                    if (apt.appointmentId == action.objectId) {
                        apt.status = "Scheduled";
                        // Add back to appropriate queue
                        if (apt.priority == 1) {
                            Appointment.emergencyQueue.add(apt);
                        } else {
                            Appointment.normalQueue.add(apt);
                        }
                        System.out.println("Undo: Reactivated appointment A" + action.objectId);
                        return;
                    }
                }
            }
        }
    }
    
    public static boolean canUndo() {
        return !undoStack.isEmpty();
    }
}