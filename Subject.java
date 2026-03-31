package studyplanner.model;

import java.io.Serializable;

public class Subject implements Serializable {

    private String name;
    private String description;

    public Subject(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return name + (description != null && !description.isEmpty() ? " (" + description + ")" : "");
    }

    // CSV format: name|description
    public String toCSV() {
        return name + "|" + (description != null ? description : "");
    }

    public static Subject fromCSV(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 2) throw new IllegalArgumentException("Invalid subject data: " + line);
        return new Subject(parts[0].trim(), parts[1].trim());
    }
}
