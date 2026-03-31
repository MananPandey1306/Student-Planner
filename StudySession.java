package studyplanner.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StudySession implements Serializable {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private String subjectName;
    private LocalDate date;
    private int durationMinutes;
    private String notes;

    public StudySession(String subjectName, LocalDate date, int durationMinutes, String notes) {
        if (durationMinutes <= 0) throw new IllegalArgumentException("Duration must be positive.");
        this.subjectName = subjectName;
        this.date = date;
        this.durationMinutes = durationMinutes;
        this.notes = notes;
    }

    public String getSubjectName()   { return subjectName; }
    public LocalDate getDate()       { return date; }
    public int getDurationMinutes()  { return durationMinutes; }
    public String getNotes()         { return notes; }

    public String getFormattedDuration() {
        int h = durationMinutes / 60;
        int m = durationMinutes % 60;
        if (h > 0) return h + "h " + m + "m";
        return m + "m";
    }

    @Override
    public String toString() {
        return String.format("[%s] %s — %s%s",
                date.format(FMT),
                subjectName,
                getFormattedDuration(),
                (notes != null && !notes.isEmpty() ? " | Notes: " + notes : ""));
    }

    // CSV: subjectName|date|durationMinutes|notes
    public String toCSV() {
        return subjectName + "|"
                + date.format(FMT) + "|"
                + durationMinutes + "|"
                + (notes != null ? notes : "");
    }

    public static StudySession fromCSV(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 4) throw new IllegalArgumentException("Invalid session data: " + line);
        return new StudySession(
                parts[0].trim(),
                LocalDate.parse(parts[1].trim(), FMT),
                Integer.parseInt(parts[2].trim()),
                parts[3].trim()
        );
    }
}
