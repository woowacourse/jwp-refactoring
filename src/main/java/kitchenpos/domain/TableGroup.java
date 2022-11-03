package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroup {
    private final Long id;
    private final LocalDateTime createdDate;
    private Tables orderTables;

    public TableGroup(Long id, LocalDateTime createdDate, Tables tables) {
        validateTableSize(tables);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = tables;
    }

    public TableGroup(LocalDateTime createdDate, Tables orderTables) {
        this(null, createdDate, orderTables);
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    private void validateTableSize(Tables orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException("등록되는 테이블 수가 2 이상이어야 한다.");
        }
    }

    public void fillTables() {
        orderTables.fillTables();
    }

    public void placeTableGroupId() {
        orderTables.placeTableGroupId(id);
    }

    public void placeOrderTables(final Tables orderTables) {
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getValue();
    }
}
