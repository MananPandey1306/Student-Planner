package studyplanner;

import studyplanner.model.*;
import studyplanner.service.*;
import studyplanner.util.InputHelper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class StudyPlannerApp {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final SubjectService subjectService     = new SubjectService();
    private final StudySessionService sessionService = new StudySessionService();
    private final DeadlineService deadlineService    = new DeadlineService();

    // ── Entry Point ───────────────────────────────────────────────────────────

    public static void main(String[] args) {
        new StudyPlannerApp().run();
    }

    public void run() {
        printBanner();
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = InputHelper.readIntInRange("  Enter choice: ", 0, 5);
            System.out.println();
            switch (choice) {
                case 1: manageSubjectsMenu(); break;
                case 2: manageSessionsMenu(); break;
                case 3: manageDeadlinesMenu(); break;
                case 4: showDashboard();       break;
                case 5: showAllSessions();     break;
                case 0: running = false;       break;
            }
        }
        System.out.println("\n  Goodbye! Keep studying! 📚\n");
    }

    // ── Banners & Menus ───────────────────────────────────────────────────────

    private void printBanner() {
        System.out.println();
        System.out.println("  ╔════════════════════════════════════════╗");
        System.out.println("  ║      STUDENT STUDY PLANNER & TRACKER   ║");
        System.out.println("  ║            Your Academic Companion      ║");
        System.out.println("  ╚════════════════════════════════════════╝");
        System.out.println("  Today: " + LocalDate.now().format(FMT));
        System.out.println();
    }

    private void printMainMenu() {
        System.out.println("  ┌─── MAIN MENU ───────────────────────────┐");
        System.out.println("  │  1. Manage Subjects                      │");
        System.out.println("  │  2. Log / View Study Sessions            │");
        System.out.println("  │  3. Manage Deadlines                     │");
        System.out.println("  │  4. Dashboard (Summary)                  │");
        System.out.println("  │  5. View All Study Sessions              │");
        System.out.println("  │  0. Exit                                 │");
        System.out.println("  └──────────────────────────────────────────┘");
    }

    // ── Subjects ──────────────────────────────────────────────────────────────

    private void manageSubjectsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("  ── SUBJECTS ─────────────────────────────");
            System.out.println("  1. View all subjects");
            System.out.println("  2. Add a subject");
            System.out.println("  3. Remove a subject");
            System.out.println("  0. Back");
            int choice = InputHelper.readIntInRange("  Enter choice: ", 0, 3);
            System.out.println();
            switch (choice) {
                case 1: viewSubjects();  break;
                case 2: addSubject();    break;
                case 3: removeSubject(); break;
                case 0: back = true;     break;
            }
        }
    }

    private void viewSubjects() {
        List<Subject> list = subjectService.getAll();
        if (list.isEmpty()) {
            System.out.println("  No subjects added yet.\n");
            return;
        }
        System.out.println("  ── Your Subjects ────────────────────────");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, list.get(i));
        }
        System.out.println();
    }

    private void addSubject() {
        String name = InputHelper.readNonEmpty("  Subject name: ");
        String desc = InputHelper.readString("  Description (optional): ");
        try {
            subjectService.add(new Subject(name, desc));
            System.out.println("  ✓ Subject '" + name + "' added.\n");
        } catch (IllegalArgumentException e) {
            System.out.println("  ✗ " + e.getMessage() + "\n");
        }
    }

    private void removeSubject() {
        viewSubjects();
        if (subjectService.getAll().isEmpty()) return;
        String name = InputHelper.readNonEmpty("  Enter subject name to remove: ");
        if (!subjectService.exists(name)) {
            System.out.println("  ✗ Subject not found.\n");
            return;
        }
        boolean confirm = InputHelper.readYesNo("  This will also remove related sessions and deadlines. Confirm?");
        if (confirm) {
            subjectService.remove(name);
            sessionService.removeSessionsForSubject(name);
            deadlineService.removeDeadlinesForSubject(name);
            System.out.println("  ✓ Subject and all related data removed.\n");
        } else {
            System.out.println("  Cancelled.\n");
        }
    }

    // ── Study Sessions ────────────────────────────────────────────────────────

    private void manageSessionsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("  ── STUDY SESSIONS ───────────────────────");
            System.out.println("  1. Log a new study session");
            System.out.println("  2. View sessions for a subject");
            System.out.println("  0. Back");
            int choice = InputHelper.readIntInRange("  Enter choice: ", 0, 2);
            System.out.println();
            switch (choice) {
                case 1: logSession();         break;
                case 2: viewSessionsBySubject(); break;
                case 0: back = true;          break;
            }
        }
    }

    private void logSession() {
        if (subjectService.getAll().isEmpty()) {
            System.out.println("  ✗ No subjects available. Add a subject first.\n");
            return;
        }
        viewSubjects();
        String subjectName = promptExistingSubject();
        if (subjectName == null) return;

        LocalDate date = InputHelper.readDate("  Date (yyyy-MM-dd or 'today'): ");
        int duration   = InputHelper.readIntInRange("  Duration in minutes: ", 1, 1440);
        String notes   = InputHelper.readString("  Notes (optional): ");

        try {
            sessionService.add(new StudySession(subjectName, date, duration, notes));
            System.out.println("  ✓ Session logged: " + duration + " minutes for " + subjectName + ".\n");
        } catch (IllegalArgumentException e) {
            System.out.println("  ✗ " + e.getMessage() + "\n");
        }
    }

    private void viewSessionsBySubject() {
        if (subjectService.getAll().isEmpty()) {
            System.out.println("  No subjects available.\n");
            return;
        }
        viewSubjects();
        String name = promptExistingSubject();
        if (name == null) return;

        List<StudySession> sessions = sessionService.getBySubject(name);
        if (sessions.isEmpty()) {
            System.out.println("  No sessions logged for " + name + ".\n");
            return;
        }
        System.out.println("  ── Sessions for " + name + " ─────────────────");
        for (StudySession s : sessions) System.out.println("  " + s);
        int total = sessionService.getTotalMinutesForSubject(name);
        System.out.printf("  Total: %dh %dm (%d minutes)%n%n", total / 60, total % 60, total);
    }

    private void showAllSessions() {
        List<StudySession> sessions = sessionService.getAll();
        if (sessions.isEmpty()) {
            System.out.println("  No study sessions logged yet.\n");
            return;
        }
        System.out.println("  ── All Study Sessions ───────────────────");
        for (StudySession s : sessions) System.out.println("  " + s);
        System.out.println();
    }

    // ── Deadlines ─────────────────────────────────────────────────────────────

    private void manageDeadlinesMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("  ── DEADLINES ────────────────────────────");
            System.out.println("  1. View all deadlines");
            System.out.println("  2. Add a deadline");
            System.out.println("  3. Mark deadline as completed");
            System.out.println("  4. Remove a deadline");
            System.out.println("  0. Back");
            int choice = InputHelper.readIntInRange("  Enter choice: ", 0, 4);
            System.out.println();
            switch (choice) {
                case 1: viewDeadlines();       break;
                case 2: addDeadline();         break;
                case 3: markDeadlineDone();    break;
                case 4: removeDeadline();      break;
                case 0: back = true;           break;
            }
        }
    }

    private void viewDeadlines() {
        List<Deadline> list = deadlineService.getAll();
        if (list.isEmpty()) {
            System.out.println("  No deadlines added yet.\n");
            return;
        }
        System.out.println("  ── All Deadlines ─────────────────────────────────────────────────────");
        System.out.printf("  %-12s %-20s %-15s %-10s %s%n", "Status", "Title", "Subject", "Priority", "Due Date");
        System.out.println("  " + "─".repeat(70));
        for (Deadline d : list) System.out.println("  " + d);
        System.out.println();
    }

    private void addDeadline() {
        if (subjectService.getAll().isEmpty()) {
            System.out.println("  ✗ No subjects available. Add a subject first.\n");
            return;
        }
        String title = InputHelper.readNonEmpty("  Deadline title (e.g. 'Assignment 1'): ");
        viewSubjects();
        String subjectName = promptExistingSubject();
        if (subjectName == null) return;

        LocalDate dueDate = InputHelper.readDate("  Due date (yyyy-MM-dd or 'today'): ");

        System.out.println("  Priority: 1=Low  2=Medium  3=High");
        int p = InputHelper.readIntInRange("  Choose priority: ", 1, 3);
        Priority priority = p == 1 ? Priority.LOW : p == 2 ? Priority.MEDIUM : Priority.HIGH;

        deadlineService.add(new Deadline(title, subjectName, dueDate, priority));
        System.out.println("  ✓ Deadline '" + title + "' added.\n");
    }

    private void markDeadlineDone() {
        viewDeadlines();
        if (deadlineService.getAll().isEmpty()) return;
        String title = InputHelper.readNonEmpty("  Enter deadline title to mark as done: ");
        if (deadlineService.markCompleted(title)) {
            System.out.println("  ✓ Marked as completed.\n");
        } else {
            System.out.println("  ✗ Deadline not found.\n");
        }
    }

    private void removeDeadline() {
        viewDeadlines();
        if (deadlineService.getAll().isEmpty()) return;
        String title = InputHelper.readNonEmpty("  Enter deadline title to remove: ");
        if (deadlineService.remove(title)) {
            System.out.println("  ✓ Deadline removed.\n");
        } else {
            System.out.println("  ✗ Deadline not found.\n");
        }
    }

    // ── Dashboard ─────────────────────────────────────────────────────────────

    private void showDashboard() {
        System.out.println("  ╔════════════════════════════════════════╗");
        System.out.println("  ║              📊 DASHBOARD               ║");
        System.out.println("  ╚════════════════════════════════════════╝");

        // ── Study hours per subject ──
        System.out.println("\n  📚 Study Time Per Subject:");
        Map<String, Integer> totals = sessionService.getTotalMinutesBySubject();
        if (totals.isEmpty()) {
            System.out.println("  No study sessions logged yet.");
        } else {
            int grandTotal = 0;
            for (Map.Entry<String, Integer> entry : totals.entrySet()) {
                int mins = entry.getValue();
                grandTotal += mins;
                System.out.printf("  %-20s %dh %dm%n", entry.getKey(), mins / 60, mins % 60);
            }
            System.out.printf("  %-20s %dh %dm (TOTAL)%n", "──────────────────", grandTotal / 60, grandTotal % 60);
        }

        // ── Overdue ──
        List<Deadline> overdue = deadlineService.getOverdue();
        System.out.println("\n  🚨 Overdue Deadlines (" + overdue.size() + "):");
        if (overdue.isEmpty()) {
            System.out.println("  None — great job!");
        } else {
            for (Deadline d : overdue) {
                System.out.println("  ⚠  " + d.getTitle() + " [" + d.getSubjectName() + "]"
                        + " was due " + d.getDueDate().format(FMT));
            }
        }

        // ── Due in 7 days ──
        List<Deadline> upcoming = deadlineService.getUpcoming(7);
        System.out.println("\n  📅 Due in Next 7 Days (" + upcoming.size() + "):");
        if (upcoming.isEmpty()) {
            System.out.println("  Nothing due soon.");
        } else {
            for (Deadline d : upcoming) {
                long days = d.daysUntilDue();
                String when = days == 0 ? "TODAY" : "in " + days + " day" + (days == 1 ? "" : "s");
                System.out.printf("  %-22s %-15s due %s (%s)%n",
                        d.getTitle(), "[" + d.getSubjectName() + "]",
                        d.getDueDate().format(FMT), when);
            }
        }

        // ── Pending count ──
        long pending = deadlineService.getPending().size();
        System.out.println("\n  📋 Total Pending Deadlines: " + pending);
        System.out.println("  📁 Total Subjects: " + subjectService.getAll().size());
        System.out.println("  🕐 Total Sessions Logged: " + sessionService.getAll().size());
        System.out.println();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Prompts user to type an existing subject name. Returns null if invalid.
     */
    private String promptExistingSubject() {
        String name = InputHelper.readNonEmpty("  Enter subject name: ");
        if (!subjectService.exists(name)) {
            System.out.println("  ✗ Subject '" + name + "' not found. Add it first.\n");
            return null;
        }
        return name;
    }
}
