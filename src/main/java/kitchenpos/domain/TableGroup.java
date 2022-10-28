package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class TableGroup {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        validateOrderTableSizeIsValid(orderTables);
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
    }

    private void validateOrderTableSizeIsValid(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException(String.format("테이블의 수가 2개 이상이어야 합니다. [%s]", orderTables.size()));
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
