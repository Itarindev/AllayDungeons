package allayplugins.dev.itarin.enuns;

public enum DungeonState {

    IN_EXPLORATION("§aEm exploração"),
    TO_BE_EXPLORED("§eA ser explorada");

    private final String displayName;

    DungeonState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}