package studyplanner.service;

import studyplanner.model.Subject;
import studyplanner.util.FileManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubjectService {

    private List<Subject> subjects;

    public SubjectService() {
        try {
            subjects = FileManager.loadSubjects();
        } catch (IOException e) {
            System.err.println("Warning: Could not load subjects. Starting fresh. (" + e.getMessage() + ")");
            subjects = new ArrayList<>();
        }
    }

    public void add(Subject subject) {
        if (findByName(subject.getName()).isPresent()) {
            throw new IllegalArgumentException("Subject '" + subject.getName() + "' already exists.");
        }
        subjects.add(subject);
        save();
    }

    public boolean remove(String name) {
        boolean removed = subjects.removeIf(s -> s.getName().equalsIgnoreCase(name));
        if (removed) save();
        return removed;
    }

    public Optional<Subject> findByName(String name) {
        return subjects.stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public List<Subject> getAll() {
        return new ArrayList<>(subjects);
    }

    public boolean exists(String name) {
        return findByName(name).isPresent();
    }

    public void save() {
        try {
            FileManager.saveSubjects(subjects);
        } catch (IOException e) {
            System.err.println("Error saving subjects: " + e.getMessage());
        }
    }
}
