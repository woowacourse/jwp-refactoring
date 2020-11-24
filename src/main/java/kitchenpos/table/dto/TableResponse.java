package kitchenpos.table.dto;

import java.time.LocalDateTime;
import java.util.Objects;

import kitchenpos.table.domain.Table;

public class TableResponse {

    private final Long id;
    private final Long tableId;
    private final LocalDateTime tableCreateDate;
    private final Integer tableNumberOfGuests;
    private final boolean empty;

    public TableResponse(Long id, Long tableId, LocalDateTime tableCreateDate, Integer tableNumberOfGuests,
        boolean empty) {
        this.id = id;
        this.tableId = tableId;
        this.tableCreateDate = tableCreateDate;
        this.tableNumberOfGuests = tableNumberOfGuests;
        this.empty = empty;
    }

    public static TableResponse from(Table table) {
        Long tableId = null;
        LocalDateTime tableCreatedDate = null;

        if (Objects.nonNull(table.getTableGroup())) {
            tableId = table.getTableGroup().getId();
            tableCreatedDate = table.getTableGroup().getCreatedDate();
        }

        return new TableResponse(table.getId(), tableId, tableCreatedDate,
            table.getTableNumberOfGuests().getNumberOfGuests(), table.getTableEmpty().getEmpty());
    }

    public Long getTableId() {
        return tableId;
    }

    public LocalDateTime getTableCreateDate() {
        return tableCreateDate;
    }

    public Long getId() {
        return id;
    }

    public Integer getTableNumberOfGuests() {
        return tableNumberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
