package kitchenpos.ui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {
    private List<IdRequest> orderTables;

    @JsonCreator
    public TableGroupCreateRequest(List<IdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(IdRequest::getId)
                .collect(Collectors.toList());
    }

    public int size() {
        return orderTables.size();
    }

    public List<IdRequest> getOrderTables() {
        return orderTables;
    }
}
