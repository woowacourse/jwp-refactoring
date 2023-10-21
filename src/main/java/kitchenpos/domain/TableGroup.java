package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroup {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    private TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        // TODO: orderTable들의 tableGroupId 변경하기
        // TODO: orderTable들의 empty false로 변경하기
        validateOrderTableSize(orderTables.size());
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private TableGroup(final List<OrderTable> orderTables) {
        this(null, LocalDateTime.now(), orderTables);
    }

    public static TableGroup groupOrderTables(final List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }

    private void validateOrderTableSize(final int orderTableSize) {
        if (orderTableSize < 2) {
            throw new IllegalArgumentException("그룹화 할 테이블 개수는 2 이상이어야 합니다.");
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
