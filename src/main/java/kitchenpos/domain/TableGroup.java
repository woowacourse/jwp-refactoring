package kitchenpos.domain;

import static kitchenpos.application.exception.ExceptionType.INVALID_TABLE_GROUP_EXCEPTION;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.exception.CustomIllegalArgumentException;
import org.springframework.util.CollectionUtils;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private OrderTables orderTables;

    public TableGroup() {
    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        validateTableGroup(orderTables);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = new OrderTables(orderTables);
    }

    public static TableGroup of(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        return new TableGroup(createdDate, orderTables);
    }

    private void validateTableGroup(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new CustomIllegalArgumentException(INVALID_TABLE_GROUP_EXCEPTION);
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
        return orderTables.getValues();
    }
}
