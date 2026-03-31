package studyplanner.service;

import studyplanner.model.Deadline;
import studyplanner.util.FileManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DeadlineService {

    private List<Deadline> deadlines;

    public DeadlineService() {
        try {
            deadlines = FileManager.loadDeadlines();
        } catch (IOException e) {
            System.err.println("Warning: Could not load deadlines. Starting fresh. (" + e.getMessage() + ")");
            deadlines = new ArrayList<>();
        }
    }

    public void add(Deadline deadline) {
        deadlines.add(deadline);
        save();
    }

    public List<Deadline> getAll() {
        return new ArrayList<>(deadlines);
    }

    public List<Deadline> getPending() {
        return deadlines.stream()
                .filter(d -> !d.isCompleted())
                .sorted(Comparator.comparing(Deadline::getDueDate))
                .collect(Collectors.toList());
    }

    public List<Deadline> getOverdue() {
        return deadlines.stream()
                .filter(Deadline::isOverdue)
                .sorted(Comparator.comparing(Deadline::getDueDate))
                .collect(Collectors.toList());
    }

    public List<Deadline> getUpcoming(int withinDays) {
        return deadlines.stream()
                .filter(d -> !d.isCompleted() && !d.isOverdue())
                .filter(d -> d.daysUntilDue() <= withinDays)
                .sorted(Comparator.comparing(Deadline::getDueDate))
                .collect(Collectors.toList());
    }

    public boolean markCompleted(String title) {
        for (Deadline d : deadlines) {
            if (d.getTitle().equalsIgnoreCase(title)) {
                d.setCompleted(true);
                save();
                return true;
            }
        }
        return false;
    }

    public boolean remove(String title) {
        boolean removed = deadlines.removeIf(d -> d.getTitle().equalsIgnoreCase(title));
        if (removed) save();
        return removed;
    }

    public void removeDeadlinesForSubject(String subjectName) {
        deadlines.removeIf(d -> d.getSubjectName().equalsIgnoreCase(subjectName));
        save();
    }

    public void save() {
        try {
            FileManager.saveDeadlines(deadlines);
        } catch (IOException e) {
            System.err.println("Error saving deadlines: " + e.getMessage());
        }
    }
}
