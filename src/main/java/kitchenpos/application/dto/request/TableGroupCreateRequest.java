package kitchenpos.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;
import java.util.Objects;

public class TableGroupCreateRequest {

    private final List<Long> orderTableIds;

    @JsonCreator
    public TableGroupCreateRequest(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof TableGroupCreateRequest)) return false;
        TableGroupCreateRequest request = (TableGroupCreateRequest) o;
        return Objects.equals(orderTableIds, request.orderTableIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTableIds);
    }
}
