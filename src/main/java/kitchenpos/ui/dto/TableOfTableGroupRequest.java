package kitchenpos.ui.dto;

import java.util.Objects;
import javax.validation.constraints.NotNull;

public class TableOfTableGroupRequest {

    @NotNull
    private Long id;

    private TableOfTableGroupRequest() {
    }

    public TableOfTableGroupRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableOfTableGroupRequest that = (TableOfTableGroupRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TableOfTableGroupRequest{" +
            "id=" + id +
            '}';
    }
}
