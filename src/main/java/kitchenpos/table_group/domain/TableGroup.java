package kitchenpos.table_group.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.order_table.domain.OrderTable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

@NoArgsConstructor
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTables = new ArrayList<>();

    @Builder
    public TableGroup(final List<OrderTable> orderTables) {
        setOrderTables(orderTables);
    }

    private void setOrderTables(final List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        orderTables.forEach(this::addOrderTable);
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public void addOrderTable(final OrderTable orderTable) {
        orderTables.add(orderTable);
        orderTable.groupBy(this);
    }

    public void ungroup() {
        List<OrderTable> orderTables = getOrderTables();
        this.orderTables.clear();
        orderTables.forEach(OrderTable::ungroup);
    }

    public List<OrderTable> getOrderTables() {
        return new ArrayList<>(orderTables);
    }
}
