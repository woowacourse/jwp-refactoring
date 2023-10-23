package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.exception.OrderTableExceptionType.ORDER_TABLE_SIZE_NOT_ENOUGH;
import static kitchenpos.exception.TableGroupExceptionType.ILLEGAL_ADD_ORDER_TABLE_EXCEPTION;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.exception.OrderTableException;
import kitchenpos.exception.TableGroupException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    private static final int MIN_ORDER_TABLE_SIZE = 2;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate) {
        this(null, createdDate);
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = null;
        this.createdDate = createdDate;
        checkSize(orderTables);
        add(orderTables);
    }

    private void checkSize(List<OrderTable> orderTables) {
        if (orderTables.size() < MIN_ORDER_TABLE_SIZE) {
            throw new OrderTableException(ORDER_TABLE_SIZE_NOT_ENOUGH);
        }
    }

    public void add(List<OrderTable> orderTables) {
        orderTables.forEach(this::add);
    }

    public void add(OrderTable orderTable) {
        if (!orderTable.isEmpty() || orderTable.hasTableGroup()) {
            throw new TableGroupException(ILLEGAL_ADD_ORDER_TABLE_EXCEPTION);
        }
        orderTable.setTableGroup(this);
        orderTables.add(orderTable);
    }

    public void ungroup(OrderTable orderTable) {
        orderTables.remove(orderTable);
    }

    public Long id() {
        return id;
    }

    public LocalDateTime createdDate() {
        return createdDate;
    }

    public List<OrderTable> orderTables() {
        return orderTables;
    }
}
