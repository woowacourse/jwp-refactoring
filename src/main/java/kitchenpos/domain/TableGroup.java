package kitchenpos.domain;

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

    @OneToMany(mappedBy = "tableGroup", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void initOrderTables(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            this.orderTables.add(orderTable);
            orderTable.groupBy(this);
            orderTable.full();
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
