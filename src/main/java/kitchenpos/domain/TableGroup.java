package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.annotation.Id;
import org.springframework.util.CollectionUtils;

public class TableGroup {

    @Id
    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTable> orderTables;

    private TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup from(final LocalDateTime createdDate) {
        return new TableGroup(null, createdDate, Collections.emptyList());
    }

    public TableGroup addOrderTables(final List<OrderTable> orderTables) {
        verifyOrderTableSize(orderTables);
        orderTables.forEach(OrderTable::verifyCanGroup);

        final List<OrderTable> groupedOrderTables = orderTables.stream()
                .map(it -> it.group(id))
                .collect(Collectors.toList());
        return new TableGroup(id, createdDate, groupedOrderTables);
    }

    private void verifyOrderTableSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
        final long distinctOrderTableSize = orderTables.stream()
                .map(OrderTable::getId)
                .distinct()
                .count();
        if (distinctOrderTableSize != orderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
