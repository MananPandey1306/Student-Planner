package studyplanner.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Deadline implements Serializable {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private String title;
    private String subjectName;
    private LocalDate dueDate;
    private Priority priority;
    private boolean completed;

    public Deadline(String title, String subjectName, LocalDate dueDate, Priority priority) {
        this.title = title;
        this.subjectName = subjectName;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = false;
    }

    public String getTitle()         { return title; }
    public String getSubjectName()   { return subjectName; }
    public LocalDate getDueDate()    { return dueDate; }
    public Priority getPriority()    { return priority; }
    public boolean isCompleted()     { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public boolean isOverdue() {
        return !completed && dueDate.isBefore(LocalDate.now());
    }

    public long daysUntilDue() {
        return ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
    }

    public String getStatusTag() {
        if (completed) return "[DONE]";
        if (isOverdue()) return "[OVERDUE]";
        long days = daysUntilDue();
        if (days == 0) return "[DUE TODAY]";
        if (days <= 3) return "[DUE SOON]";
        return "[UPCOMING]";
    }

    @Override
    public String toString() {
        return String.format("%-12s %-20s %-15s %-10s %s",
                getStatusTag(), title, subjectName,
                priority.toString(), dueDate.format(FMT));
    }

    // CSV: title|subjectName|dueDate|priority|completed
    public String toCSV() {
        return title + "|" + subjectName + "|"
                + dueDate.format(FMT) + "|"
                + priority.name() + "|"
                + completed;
    }

    public static Deadline fromCSV(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 5) throw new IllegalArgumentException("Invalid deadline data: " + line);
        Deadline d = new Deadline(
                parts[0].trim(),
                parts[1].trim(),
                LocalDate.parse(parts[2].trim(), FMT),
                Priority.fromString(parts[3].trim())
        );
        d.setCompleted(Boolean.parseBoolean(parts[4].trim()));
        return d;
    }
}
