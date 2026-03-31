package studyplanner.service;

import studyplanner.model.StudySession;
import studyplanner.util.FileManager;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class StudySessionService {

    private List<StudySession> sessions;

    public StudySessionService() {
        try {
            sessions = FileManager.loadSessions();
        } catch (IOException e) {
            System.err.println("Warning: Could not load sessions. Starting fresh. (" + e.getMessage() + ")");
            sessions = new ArrayList<>();
        }
    }

    public void add(StudySession session) {
        sessions.add(session);
        save();
    }

    public List<StudySession> getAll() {
        return new ArrayList<>(sessions);
    }

    public List<StudySession> getBySubject(String subjectName) {
        return sessions.stream()
                .filter(s -> s.getSubjectName().equalsIgnoreCase(subjectName))
                .collect(Collectors.toList());
    }

    /**
     * Returns total minutes studied, grouped by subject name.
     */
    public Map<String, Integer> getTotalMinutesBySubject() {
        Map<String, Integer> totals = new LinkedHashMap<>();
        for (StudySession s : sessions) {
            totals.merge(s.getSubjectName(), s.getDurationMinutes(), Integer::sum);
        }
        return totals;
    }

    /**
     * Returns total minutes studied for a specific subject.
     */
    public int getTotalMinutesForSubject(String subjectName) {
        return sessions.stream()
                .filter(s -> s.getSubjectName().equalsIgnoreCase(subjectName))
                .mapToInt(StudySession::getDurationMinutes)
                .sum();
    }

    public void removeSessionsForSubject(String subjectName) {
        sessions.removeIf(s -> s.getSubjectName().equalsIgnoreCase(subjectName));
        save();
    }

    public void save() {
        try {
            FileManager.saveSessions(sessions);
        } catch (IOException e) {
            System.err.println("Error saving sessions: " + e.getMessage());
        }
    }
}
