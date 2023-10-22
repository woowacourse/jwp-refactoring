package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class TableGroup {
    private static final int MINIMUM_SIZE = 2;
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroup() {
    }

    public static TableGroup of(List<Long> orderTableIds, List<OrderTable> savedOrderTables) {
        validateExistenceAndDuplication(orderTableIds, savedOrderTables);
        validateSize(savedOrderTables);
        validateOrderTableStatus(savedOrderTables);
        return new TableGroup(LocalDateTime.now(), savedOrderTables);
    }

    private static void validateExistenceAndDuplication(List<Long> orderTableIds, List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블이 있거나 주문 테이블이 중복되었습니다.");
        }
    }

    private static void validateSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_SIZE) {
            throw new IllegalArgumentException("단체 지정의 주문 테이블 개수는 최소 " + MINIMUM_SIZE + " 이상이어야 합니다.");
        }
    }

    private static void validateOrderTableStatus(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (orderTable.isFilled() || orderTable.isGrouped()) {
                throw new IllegalArgumentException("단체 지정의 주문 테이블이 차 있거나 이미 단체 지정되었습니다.");
            }
        }
    }

    public void validateGroupCanBeUngrouped(boolean hasCookingOrMealOrder) {
        if (hasCookingOrMealOrder) {
            throw new IllegalArgumentException("조리 혹은 식사 주문이 존재하는 단체 지정은 단체 지정을 취소할 수 없습니다.");
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

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
