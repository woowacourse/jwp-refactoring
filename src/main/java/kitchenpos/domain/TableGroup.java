package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.util.CollectionUtils;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("주문 테이블이 최소 2개 이상이어야 단체 지정(grouping)이 가능합니다.");
        }
        for (OrderTable orderTable : orderTables) {
            validateGroupingTable(orderTable);
        }
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private void validateGroupingTable(final OrderTable orderTable) {
        if (orderTable.getTableGroupId() != null && orderTable.getTableGroupId().equals(id)) {
            return;
        }
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException("주문 테이블이 비어있지 않거나 이미 단체지정되어있습니다.");
        }
    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    public TableGroup(final List<OrderTable> orderTables) {
        this(null, LocalDateTime.now(), orderTables);
    }

    public void validateOrderTableSize(final int size) {
        if (orderTables.size() != size) {
            throw new IllegalArgumentException("실제 주문 테이블 정보와 일치하지 않습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
