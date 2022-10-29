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

    public TableGroup(final List<OrderTable> orderTables) {
        this(null, LocalDateTime.now(), orderTables);
    }


    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("주문 테이블들은 2개 이상이어야 합니다.");
        }
        for (OrderTable orderTable : orderTables) {
            validateTableGroping(orderTable);
        }
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private void validateTableGroping(final OrderTable orderTable) {
        if (orderTable.getTableGroupId() != null && orderTable.getTableGroupId().equals(id)) {
            return;
        }
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    public void checkOrderTableSize(final int size) {
        if (orderTables.size() != size) {
            throw new IllegalArgumentException("주문 테이블들의 사이즈가 해당 사이즈와 같지 않습니다.");
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
