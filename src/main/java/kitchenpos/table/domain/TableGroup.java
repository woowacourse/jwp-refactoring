package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroup {

    private Long id;
    private LocalDateTime createdDate;
    private List<Table> tables;

    public TableGroup(List<Table> tables) {
        this(null, LocalDateTime.now(), tables);
    }

    public TableGroup(Long id, List<Table> tables) {
        this(id, LocalDateTime.now(), tables);
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<Table> tables) {
        validate(tables);
        this.id = id;
        this.createdDate = createdDate;
        this.tables = tables;
    }

    private void validate(List<Table> tables) {
        for (final Table savedTable : tables) {
            if (!savedTable.isEmpty() || savedTable.hasGroup()) {
                throw new IllegalArgumentException();
            }
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void changeOrderTables(List<Table> savedTables) {
        this.tables = savedTables;
    }

    public void changeCreatedDate(LocalDateTime localDateTime) {
        this.createdDate = localDateTime;
    }
}
