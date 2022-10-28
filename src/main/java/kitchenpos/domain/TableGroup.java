package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.request.TableIdRequest;
import org.springframework.util.CollectionUtils;

public class TableGroup {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTable> orderTables;

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = new ArrayList<>(orderTables);
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this(id, createdDate, new ArrayList<>());
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    public void validateOrderTableGrouping(int actualSize) {
        validateOrderTableSize();
        validateEachTableStatus();
        validateOrderTableExistence(actualSize);
    }

    private void validateEachTableStatus() {
        for (OrderTable orderTable : orderTables) {
            orderTable.validateEmpty();
            orderTable.validateNotInTableGroup();
        }
    }

    private void validateOrderTableSize() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableExistence(int actualSize) {
        if (orderTables.size() != actualSize) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return new ArrayList<>(orderTables);
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
