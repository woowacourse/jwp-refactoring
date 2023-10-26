package kitchenpos.domain;

public class UnGroupEvent {
    private final GroupedTables groupedTables;

    public UnGroupEvent(final GroupedTables groupedTables) {
        this.groupedTables = groupedTables;
    }

    public GroupedTables getGroupedTables() {
        return groupedTables;
    }
}
