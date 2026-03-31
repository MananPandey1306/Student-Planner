# Student Study Planner \& Tracker

> A Console-Based Java Application for Academic Time Management

!\[Java](https://img.shields.io/badge/Java-11%2B-orange?style=flat-square\&logo=java)
!\[License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)
!\[Build](https://img.shields.io/badge/Build-Passing-brightgreen?style=flat-square)
!\[Type](https://img.shields.io/badge/Type-Console%20App-lightgrey?style=flat-square)

\---

## Author

|Field|Details|
|-|-|
|**Name**|Manan Pandey|
|**Registration Number**|24BAI10033|
|**Course**|Programming in Java — BYOP Capstone|
|**Year**|2026|

\---

## Overview

The **Student Study Planner \& Tracker** is a lightweight, console-based Java application designed to help students systematically manage their study time, track subject deadlines, and maintain awareness of academic commitments — all without requiring internet access or complex setup.

Built entirely in Java using core OOP principles, the application stores all data locally in CSV files so your progress persists between sessions.

\---

## Problem Statement

Students managing multiple subjects often face:

* **Time Blindness** — No clear record of how much time has been invested per subject.
* **Deadline Chaos** — Assignments and tests tracked informally, leading to missed submissions.
* **Lack of Accountability** — No visibility into study patterns to identify subjects needing more attention.

This application solves all three problems in a single, simple, portable tool.

\---

## Features

* ➕ **Add / Remove Subjects** with optional descriptions
* ⏱️ **Log Study Sessions** with subject, date, duration, and notes
* 📅 **Manage Deadlines** with title, subject, due date, and priority level (Low / Medium / High)
* ✅ **Mark Deadlines as Complete** and auto-detect overdue items
* 📊 **Live Dashboard** — Summarises total study hours per subject, overdue deadlines, and upcoming tasks (next 7 days)
* 💾 **Persistent Storage** — All data saved to local CSV files in a `data/` directory

\---

## System Architecture

The project follows a clean **three-layer architecture**:

```
├── src/
│   └── studyplanner/
│       ├── StudyPlannerApp.java          # Main class, menu loop, UI
│       ├── model/
│       │   ├── Subject.java
│       │   ├── StudySession.java
│       │   ├── Deadline.java
│       │   └── Priority.java             # Enum: LOW / MEDIUM / HIGH
│       ├── service/
│       │   ├── SubjectService.java
│       │   ├── StudySessionService.java
│       │   └── DeadlineService.java
│       └── util/
│           ├── FileManager.java          # CSV read/write
│           └── InputHelper.java          # Input validation \& parsing
├── data/                                 # Auto-created at runtime
│   ├── subjects.csv
│   ├── sessions.csv
│   └── deadlines.csv
├── run.sh                                # Build \& run script (Linux/Mac)
└── run.bat                               # Build \& run script (Windows)
```

|Layer|Package|Responsibility|
|-|-|-|
|**Model**|`studyplanner.model`|Domain entities: Subject, StudySession, Deadline, Priority|
|**Service**|`studyplanner.service`|Business logic: add, remove, query, aggregate data|
|**Utility**|`studyplanner.util`|File I/O (FileManager), input parsing (InputHelper)|
|**Presentation**|`studyplanner` (root)|Menu system, user interaction, dashboard rendering|

\---

## Java Concepts Applied

|Concept|Where Applied|Purpose|
|-|-|-|
|Classes \& Encapsulation|Subject, StudySession, Deadline|Private fields, public getters/setters, data integrity|
|Enumerations|`Priority.java`|Type-safe representation of Low / Medium / High priority|
|Collections (ArrayList, Map)|All service classes|Dynamic storage and aggregation of domain objects|
|Exception Handling|FileManager, InputHelper, `fromCSV()`|Graceful recovery from malformed data and bad input|
|File I/O (BufferedReader/Writer)|`FileManager.java`|Persistent CSV storage for all data|
|`java.time.LocalDate`|StudySession, Deadline|Date parsing, overdue detection, days-until-due calculation|
|Stream API|DeadlineService, StudySessionService|Filtering, sorting, and aggregating collections|
|Static Factory Methods|`fromCSV()` in all models|Controlled, validated object construction from file data|
|Service Layer Pattern|SubjectService etc.|Decouples UI from data management logic|

\---

## Getting Started

### Prerequisites

* Java **11 or above** installed
* A terminal / command prompt

### Run on Linux / macOS

```bash
chmod +x run.sh
./run.sh
```

### Run on Windows

```bat
run.bat
```

### Manual Compile \& Run

```bash
# Compile
javac -d out src/studyplanner/\*\*/\*.java src/studyplanner/\*.java

# Run
java -cp out studyplanner.StudyPlannerApp
```

\---

## Application Preview

```
╔════════════════════════════════════════╗
║     STUDENT STUDY PLANNER \& TRACKER   ║
╚════════════════════════════════════════╝
Today: 2025-06-10

┌─── MAIN MENU ───────────────────────────┐
│  1. Manage Subjects                     │
│  2. Log / View Study Sessions           │
│  3. Manage Deadlines                    │
│  4. Dashboard (Summary)                 │
│  5. View All Study Sessions             │
│  0. Exit                                │
└─────────────────────────────────────────┘
```

\---

## Testing

All scenarios were validated through manual testing:

|Test Case|Expected Result|Status|
|-|-|-|
|Add duplicate subject|Error message, no duplicate added|✅ PASS|
|Log session with invalid duration|Error message, re-prompt|✅ PASS|
|Add deadline with past date|Stored, shown as OVERDUE on dashboard|✅ PASS|
|Remove subject with data|Cascading delete after confirmation|✅ PASS|
|Mark deadline as done|Status changes to `\[DONE]`|✅ PASS|
|Load malformed CSV line|Warning logged, rest of data loads|✅ PASS|
|Invalid menu choice|Re-prompts within valid range|✅ PASS|
|Empty subject name|Error message, re-prompt|✅ PASS|

\---

## Key Design Decisions

* **CSV over Binary Serialization** — Pipe-delimited (`|`) CSV files are human-readable, debuggable, and not tied to any class version.
* **Service Layer Separation** — Business logic is fully decoupled from the UI, making the codebase maintainable and extensible (e.g., a GUI could be added without rewriting services).
* **Cascading Deletes** — Removing a subject also removes all linked sessions and deadlines to prevent orphaned data, with user confirmation required.
* **Defensive Input Handling** — All input is routed through `InputHelper`, which loops on invalid input rather than crashing, with clear feedback and retry logic.

\---

## Learning Outcomes

* Practical OOP design using encapsulation and the Single Responsibility Principle
* Robust File I/O with error recovery for real-world data handling
* Understanding of the Service Layer pattern for clean architecture
* Informed exception handling — knowing when to crash vs. recover gracefully
* Hands-on use of the `java.time` API for date arithmetic and formatting

\---

## License

This project is submitted as part of the **Programming in Java — BYOP Capstone (2025)** at VIT University. All rights reserved by the author.

\---

*Made with Java by **Manan Pandey** (24BAI10033)*

