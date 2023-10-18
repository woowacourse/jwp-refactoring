package kitchenpos.domain;

import kitchenpos.exception.DuplicateCreateTableGroup;
import kitchenpos.exception.InvalidOrderTableException;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup() {
        this(null);
    }

    public TableGroup(final Long id) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
    }

    public void initOrderTables(final List<OrderTable> orderTables){
        validateOrderTables(orderTables);
        this.orderTables = orderTables;
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new InvalidOrderTableException();
        }

        for (final OrderTable savedOrderTable : orderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new DuplicateCreateTableGroup();
            }
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

    public void ungroup() {
        orderTables.stream().forEach(it -> it.setTableGroup(null));
    }
}
