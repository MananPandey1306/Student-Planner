package studyplanner.util;

import studyplanner.model.Deadline;
import studyplanner.model.StudySession;
import studyplanner.model.Subject;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String DATA_DIR    = "data";
    private static final String SUBJECTS_FILE  = DATA_DIR + File.separator + "subjects.csv";
    private static final String SESSIONS_FILE  = DATA_DIR + File.separator + "sessions.csv";
    private static final String DEADLINES_FILE = DATA_DIR + File.separator + "deadlines.csv";

    static {
        // Ensure data directory exists on first run
        new File(DATA_DIR).mkdirs();
    }

    // ── Subjects ─────────────────────────────────────────────────────────────

    public static void saveSubjects(List<Subject> subjects) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SUBJECTS_FILE))) {
            for (Subject s : subjects) {
                bw.write(s.toCSV());
                bw.newLine();
            }
        }
    }

    public static List<Subject> loadSubjects() throws IOException {
        List<Subject> list = new ArrayList<>();
        File f = new File(SUBJECTS_FILE);
        if (!f.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty()) continue;
                try {
                    list.add(Subject.fromCSV(line));
                } catch (Exception e) {
                    System.err.println("Warning: Skipping malformed subject at line " + lineNum + ": " + e.getMessage());
                }
            }
        }
        return list;
    }

    // ── Study Sessions ────────────────────────────────────────────────────────

    public static void saveSessions(List<StudySession> sessions) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SESSIONS_FILE))) {
            for (StudySession s : sessions) {
                bw.write(s.toCSV());
                bw.newLine();
            }
        }
    }

    public static List<StudySession> loadSessions() throws IOException {
        List<StudySession> list = new ArrayList<>();
        File f = new File(SESSIONS_FILE);
        if (!f.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty()) continue;
                try {
                    list.add(StudySession.fromCSV(line));
                } catch (Exception e) {
                    System.err.println("Warning: Skipping malformed session at line " + lineNum + ": " + e.getMessage());
                }
            }
        }
        return list;
    }

    // ── Deadlines ─────────────────────────────────────────────────────────────

    public static void saveDeadlines(List<Deadline> deadlines) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DEADLINES_FILE))) {
            for (Deadline d : deadlines) {
                bw.write(d.toCSV());
                bw.newLine();
            }
        }
    }

    public static List<Deadline> loadDeadlines() throws IOException {
        List<Deadline> list = new ArrayList<>();
        File f = new File(DEADLINES_FILE);
        if (!f.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty()) continue;
                try {
                    list.add(Deadline.fromCSV(line));
                } catch (Exception e) {
                    System.err.println("Warning: Skipping malformed deadline at line " + lineNum + ": " + e.getMessage());
                }
            }
        }
        return list;
    }
}
