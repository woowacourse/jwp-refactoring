package kitchenpos.application.dto.result;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.table.TableGroup;

public class TableGroupResult {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTableResult> orderTableResults;

    public TableGroupResult(
            final Long id,
            final LocalDateTime createdDate,
            final List<OrderTableResult> orderTableResults
    ) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableResults = orderTableResults;
    }

    public static TableGroupResult from(final TableGroup tableGroup) {
        return new TableGroupResult(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                tableGroup.getOrderTables().stream()
                        .map(OrderTableResult::from)
                        .collect(Collectors.toList())
        );
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResult> getOrderTableResults() {
        return orderTableResults;
    }
}
