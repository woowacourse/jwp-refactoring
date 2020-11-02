package kitchenpos.ui.dto;

import java.util.Objects;
import javax.validation.constraints.NotNull;
import kitchenpos.domain.table.Table;

public class TableCreateRequest {

    @NotNull
    private Integer numberOfGuests;

    @NotNull
    private Boolean empty;

    private TableCreateRequest() {
    }

    public TableCreateRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Table toOrderTable() {
        return Table.entityOf(numberOfGuests, empty);
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
        TableCreateRequest that = (TableCreateRequest) o;
        return Objects.equals(numberOfGuests, that.numberOfGuests) &&
            Objects.equals(empty, that.empty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests, empty);
    }

    @Override
    public String toString() {
        return "TableCreateRequest{" +
            "numberOfGuests=" + numberOfGuests +
            ", empty=" + empty +
            '}';
    }
}
