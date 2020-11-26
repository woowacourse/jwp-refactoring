package kitchenpos.ui.dto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.table.Table;

public class TableResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    private TableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static TableResponse of(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new TableResponse(id, tableGroupId, numberOfGuests, empty);
    }

    public static TableResponse of(Table table) {
        return new TableResponse(table.getId(), table.getTableGroupId(),
            table.getNumberOfGuests(), table.isEmpty());
    }

    public static List<TableResponse> listOf(List<Table> tables) {
        return tables.stream()
            .map(TableResponse::of)
            .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableResponse that = (TableResponse) o;
        return numberOfGuests == that.numberOfGuests &&
            empty == that.empty &&
            Objects.equals(id, that.id) &&
            Objects.equals(tableGroupId, that.tableGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, empty);
    }

    @Override
    public String toString() {
        return "TableResponse{" +
            "id=" + id +
            ", tableGroupId=" + tableGroupId +
            ", numberOfGuests=" + numberOfGuests +
            ", empty=" + empty +
            '}';
    }
}
