package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.domain.exception.TableGroupException.CannotAssignOrderTableException;
import kitchenpos.domain.exception.TableGroupException.InsufficientOrderTableSizeException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    private static final int LOWER_BOUND_ORDER_TABLE_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @CreatedDate
    private LocalDateTime createdDate;
    @OneToMany
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    private TableGroup(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        orderTables.forEach(orderTable -> orderTable.group(this));
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
        return new TableGroup(orderTables);
    }

    public void ungroup() {
        orderTables.forEach(orderTable -> {
            orderTable.setTableGroup(null);
            orderTable.setEmpty(false);
        });
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
