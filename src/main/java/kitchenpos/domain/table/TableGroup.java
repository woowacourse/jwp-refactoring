package kitchenpos.domain.table;

import static javax.persistence.CascadeType.ALL;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.OrderValidator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroupId", cascade = ALL)
    private List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup() {
    }

    public TableGroup(Long id) {
        this.id = id;
    }

    public void addOrderTables(final List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        orderTables.forEach(orderTable -> orderTable.group(this.id));
        this.orderTables = orderTables;
    }

    private void validateOrderTablesSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("단체에 속한 테이블은 최소 2개 이상이어야 합니다.");
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

    public void unGroup(final OrderValidator orderValidator) {
        orderTables.forEach(orderTable -> orderTable.unGroup(orderValidator));
        orderTables.clear();
    }
}
