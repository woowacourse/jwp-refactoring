package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.domain.ordertable.OrderTable;
import org.springframework.data.annotation.CreatedDate;

@Entity
public class TableGroup {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @OneToMany
    private List<OrderTable> orderTables;

    @CreatedDate
    private LocalDateTime createdDate;

    public TableGroup() {

    }

    public TableGroup(final List<OrderTable> orderTables) {
        this(null, orderTables);
    }

    public TableGroup(final Long id, final List<OrderTable> orderTables) {
        this.id = id;
        validateCanGroup(orderTables);
        orderTables.forEach(orderTable -> orderTable.group(this));
        this.orderTables = orderTables;
    }

    private void validateCanGroup(final List<OrderTable> orderTables) {
        if (orderTables.size() <= 1) {
            throw new IllegalArgumentException("그룹화할 테이블은 2개 이상이어야 합니다.");
        }
        if (orderTables.stream()
                       .anyMatch(OrderTable::isNotEmpty)) {
            throw new IllegalArgumentException("빈 테이블만 그룹화할 수 있습니다.");
        }
        if (orderTables.stream()
                       .anyMatch(OrderTable::hasTableGroup)) {
            throw new IllegalArgumentException("이미 그룹화된 테이블입니다.");
        }
    }

    public void unGroup() {
        orderTables.forEach(OrderTable::unGroup);
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

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
