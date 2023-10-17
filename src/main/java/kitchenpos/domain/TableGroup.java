package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.domain.exception.TableGroupExceptionType.ORDER_TABLE_SIZE_IS_LOWER_THAN_ZERO_OR_EMPTY;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.domain.exception.TableGroupException;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.MERGE)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {

    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.createdDate = createdDate;
        groupOrderTables(orderTables);
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new TableGroupException(ORDER_TABLE_SIZE_IS_LOWER_THAN_ZERO_OR_EMPTY);
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

    public void groupOrderTables(final List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.changeEmpty(false);
            savedOrderTable.changeTableGroup(this);
        }
        orderTables.addAll(savedOrderTables);
    }
}
