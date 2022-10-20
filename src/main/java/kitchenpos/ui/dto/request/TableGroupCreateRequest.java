package kitchenpos.ui.dto.request;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {
    private List<Map<String, Long>> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<Long> tableIds) {
        this.orderTables = tableIds.stream()
                .filter(Objects::nonNull)
                .map(id -> Map.of("id", id))
                .collect(Collectors.toList());
    }

    public List<Map<String, Long>> getOrderTables() {
        return orderTables;
    }

    public List<Long> ids() {
        return orderTables.stream()
                .map(map -> map.get("id"))
                .collect(Collectors.toList());
    }
}
