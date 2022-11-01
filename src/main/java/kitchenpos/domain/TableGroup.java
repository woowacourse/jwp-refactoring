package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class TableGroup {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTable> orderTables;

    public TableGroup() {
        this(LocalDateTime.now(), new ArrayList<>());
    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup group(final List<OrderTable> orderTables) {
        validateTableSize(orderTables);
        validateCanGroup(orderTables);

        final List<OrderTable> groupedTables = orderTables.stream()
                .map(it -> it.group(this.id))
                .collect(Collectors.toList());

        return new TableGroup(id, createdDate, groupedTables);
    }

    private static void validateCanGroup(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.checkCanGroup();
        }
    }

    private void validateTableSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
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
