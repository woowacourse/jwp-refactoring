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
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블 단체 지정은 최소 2개 이상의 주문 테이블이 있어야 합니다.");
        }
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public void validate(final int sizeOfRequestOrderTable) {
        validateOrderTableSize(sizeOfRequestOrderTable);
        validateEmptyOrGroupedTable();
    }

    private void validateOrderTableSize(final int sizeOfRequestOrderTable) {
        if (orderTables.size() != sizeOfRequestOrderTable) {
            throw new IllegalArgumentException("주문 테이블이 올바르지 않습니다.");
        }
    }

    private void validateEmptyOrGroupedTable() {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException("빈 테이블 또는 이미 그룹 지정이 되어있습니다.");
            }
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
