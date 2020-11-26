package kitchenpos.ui.dto;

import java.util.Objects;
import javax.validation.constraints.NotNull;

public class TableChangeEmptyRequest {

    @NotNull
    private Boolean empty;

    private TableChangeEmptyRequest() {
    }

    public TableChangeEmptyRequest(boolean empty) {
        this.empty = empty;
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
        TableChangeEmptyRequest that = (TableChangeEmptyRequest) o;
        return Objects.equals(empty, that.empty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empty);
    }

    @Override
    public String toString() {
        return "TableChangeEmptyRequest{" +
            "empty=" + empty +
            '}';
    }
}
