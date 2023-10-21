package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup() {
    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        validate(orderTables);
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private void validate(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
        for (final OrderTable savedOrderTable : orderTables) {
            if (!savedOrderTable.isGroupable()) {
                throw new IllegalArgumentException();
            }
        }

        for (final OrderTable savedOrderTable : orderTables) {
            savedOrderTable.setTableGroup(this);
            savedOrderTable.changeEmpty(false);
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

    public void unGroup() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(null);
            orderTable.changeEmpty(false);
        }
    }
}
