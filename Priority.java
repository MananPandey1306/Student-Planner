package studyplanner.model;

public enum Priority {
    LOW, MEDIUM, HIGH;

    public static Priority fromString(String s) {
        switch (s.trim().toUpperCase()) {
            case "LOW":    return LOW;
            case "MEDIUM": return MEDIUM;
            case "HIGH":   return HIGH;
            default: throw new IllegalArgumentException("Unknown priority: " + s);
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case LOW:    return "Low";
            case MEDIUM: return "Medium";
            case HIGH:   return "High";
            default:     return name();
        }
    }
}
