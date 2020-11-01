package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<Table> tables;

    public TableGroup(LocalDateTime createdDate, List<Table> tables) {
        this(null, createdDate, tables);
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<Table> tables) {
        this.id = id;
        this.createdDate = createdDate;
        this.tables = tables;
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
