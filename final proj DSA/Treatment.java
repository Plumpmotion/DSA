    import java.time.LocalDateTime;

    public class Treatment {
        public String diagnosis;
        public String medication;
        public LocalDateTime timestamp;

        public Treatment(String diagnosis, String medication) {
            this.diagnosis = diagnosis;
            this.medication = medication;
            this.timestamp = LocalDateTime.now();
        }

        public void display() {
            System.out.println("Diagnosis: " + diagnosis);
            System.out.println("Medication: " + medication);
            System.out.println("Date: " + timestamp.toLocalDate());
            System.out.println("Time: " + timestamp.toLocalTime());
        }
    }