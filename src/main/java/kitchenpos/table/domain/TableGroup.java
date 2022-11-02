package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import kitchenpos.common.exception.InvalidTableException;
import org.springframework.util.CollectionUtils;

public class TableGroup {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTable> orderTables;

    private TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this(id, createdDate, List.of());
    }

    public TableGroup(LocalDateTime createdDate) {
        this(null, createdDate, List.of());
    }

    public static TableGroup create(LocalDateTime createdDate, List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        return new TableGroup(null, createdDate, orderTables);
    }

    private static void validateOrderTables(List<OrderTable> orderTables) {
        validateOrderTableSize(orderTables);
        validateOrderTableStatus(orderTables);
    }

    private static void validateOrderTableSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new InvalidTableException("단체 지정할 테이블은 2개 이상이어야 합니다.");
        }
    }

    private static void validateOrderTableStatus(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new InvalidTableException("테이블 그룹을 지정할 수 없는 테이블입니다.");
            }
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
