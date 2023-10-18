package kitchenpos.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.exception.TableGroupExceptionType.CAN_NOT_UNGROUP_COOKING_OR_MEAL;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.exception.TableGroupException;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", cascade = ALL)
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        this(null, LocalDateTime.now(), orderTables);
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        orderTables.forEach(it -> it.setTableGroup(this));
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
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
