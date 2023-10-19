package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.domain.exception.TableGroupException.CannotAssignOrderTableException;
import kitchenpos.domain.exception.TableGroupException.InsufficientOrderTableSizeException;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

    private static final int LOWER_BOUND_ORDER_TABLE_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private LocalDateTime createdDate;
    @OneToMany
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    private TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
        orderTables.forEach(orderTable -> {
            orderTable.setTableGroup(this);
            orderTable.setEmpty(false);
        });
    }

    public static TableGroup from(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < LOWER_BOUND_ORDER_TABLE_SIZE) {
            throw new InsufficientOrderTableSizeException();
        }

        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new CannotAssignOrderTableException();
            }
        }
        return new TableGroup(LocalDateTime.now(), orderTables);
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
