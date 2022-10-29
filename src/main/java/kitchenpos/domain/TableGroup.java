package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class TableGroup {

    private final Long id;
    private final LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    /**
     * DB 에 저장되지 않은 객체
     */
    public TableGroup(final List<OrderTable> orderTables) {
        this(null, null, orderTables);
    }

    /**
     * DB 에 저장된 객체
     */
    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        validateOrderTableSizeIsValid(orderTables);
        this.id = id;
        this.createdDate = createdDate;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
