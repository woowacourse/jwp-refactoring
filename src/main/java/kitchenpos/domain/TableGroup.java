package kitchenpos.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", cascade = {CascadeType.PERSIST})
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        validateOrderTable(orderTables);
        setGroupForThis(orderTables);
        this.orderTables = new ArrayList<>(orderTables);
    }

    private void validateOrderTable(final List<OrderTable> orderTables) {
        if (Objects.isNull(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
        for (final OrderTable savedOrderTable : orderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void setGroupForThis(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeGroup(this);
        }
    }

    public void unGroupTables() {
        for (OrderTable orderTable : orderTables) {
            orderTable.deleteGroup();
        }
        orderTables.clear();
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
