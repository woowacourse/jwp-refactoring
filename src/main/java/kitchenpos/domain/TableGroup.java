package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.util.CollectionUtils;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        validateOrderTableAlreadyInGroup(orderTables);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables.addAll(orderTables);
    }

    private void validateOrderTablesSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("[ERROR] 테이블이 두 개 미만이거나 비어있을 수 없습니다.");
        }
    }

    private void validateOrderTableAlreadyInGroup(final List<OrderTable> orderTables) {
        if (orderTables.stream().anyMatch(orderTable -> this.id != orderTable.getTableGroupId())) {
            throw new IllegalArgumentException("[ERROR] 주문 테이블이 이미 단체로 지정되어 있습니다.");
        }
    }

    public static TableGroup ofNullId(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        return new TableGroup(null, createdDate, orderTables);
    }

    public void updateId(final Long id) {
        this.id = id;
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
