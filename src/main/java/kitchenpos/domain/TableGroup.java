package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.util.CollectionUtils;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    private TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup of(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        for (OrderTable orderTable : orderTables) {
            validateTableGroping(orderTable, id);
        }
        return new TableGroup(id, createdDate, orderTables);
    }

    private static void validateOrderTables(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("주문 테이블들은 2개 이상이어야 합니다.");
        }
    }

    private static void validateTableGroping(final OrderTable orderTable, final Long TableGroupId) {
        if (orderTable.getTableGroupId() != null && orderTable.getTableGroupId().equals(TableGroupId)) {
            return;
        }
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    public static TableGroup of(final List<OrderTable> orderTables) {
        return of(null, LocalDateTime.now(), orderTables);
    }

    public static TableGroup toEntity(final Long id, final LocalDateTime createdDate) {
        return new TableGroup(id, createdDate, new ArrayList<>());
    }

    public void checkOrderTableSize(final int size) {
        if (orderTables.size() != size) {
            throw new IllegalArgumentException("주문 테이블들의 사이즈가 해당 사이즈와 같지 않습니다.");
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
