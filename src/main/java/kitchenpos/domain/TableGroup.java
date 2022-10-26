package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TableGroup {

    private static final int MINIMUM_TABLE_SIZE = 2;

    private Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTable> orderTables;

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this(id, createdDate, new ArrayList<>());
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public void addOrderTables(List<OrderTable> orderTables) {
        validateTableSize(orderTables);
        this.orderTables.addAll(orderTables);
        orderTables.forEach(table -> table.groupedBy(id));
    }

    private void validateTableSize(List<OrderTable> orderTables) {
        if (orderTables.size() < MINIMUM_TABLE_SIZE) {
            throw new IllegalArgumentException("테이블 그룹은 2개 이상부터 지정할 수 있습니다.");
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
