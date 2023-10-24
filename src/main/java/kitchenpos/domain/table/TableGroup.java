package kitchenpos.domain.table;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.exception.table.TableGroupExceptionType.CAN_NOT_UNGROUP_COOKING_OR_MEAL;
import static kitchenpos.exception.table.TableGroupExceptionType.ORDER_TABLES_CAN_NOT_LESS_THAN_TWO;
import static kitchenpos.exception.table.TableGroupExceptionType.ORDER_TABLE_CAN_NOT_EMPTY;
import static kitchenpos.exception.table.TableGroupExceptionType.ORDER_TABLE_CAN_NOT_HAVE_TABLE_GROUP;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.exception.table.TableGroupException;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", cascade = ALL, fetch = EAGER)
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        this(null, LocalDateTime.now(), orderTables);
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        validate(orderTables);
        orderTables.forEach(it -> {
            it.setTableGroup(this);
            it.setEmpty(false);
        });
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private void validate(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new TableGroupException(ORDER_TABLES_CAN_NOT_LESS_THAN_TWO);
        }
        for (OrderTable orderTable : orderTables) {
            if (!orderTable.empty()) {
                throw new TableGroupException(ORDER_TABLE_CAN_NOT_EMPTY);
            }
            if (Objects.nonNull(orderTable.tableGroup())) {
                throw new TableGroupException(ORDER_TABLE_CAN_NOT_HAVE_TABLE_GROUP);
            }
        }
    }

    public void ungroup() {
        if (orderTables.stream().anyMatch(OrderTable::hasOrderOfCookingOrMeal)) {
            throw new TableGroupException(CAN_NOT_UNGROUP_COOKING_OR_MEAL);
        }
        orderTables.forEach(OrderTable::ungroup);
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
