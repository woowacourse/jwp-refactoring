package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "table_group")
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        validateSize(orderTables);
        validateDuplicateOrderTable(orderTables);
        validateOrderTableStatus(orderTables);
        addOrderTables(orderTables);
    }

    public void ungroup() {
        for (OrderTable orderTable : orderTables) {
            if (!orderTable.isAbleToUngroup()) {
                throw new IllegalArgumentException("유효하지 않은 주문 상태입니다.");
            }
            orderTable.setTableGroup(null);
            orderTable.changeEmpty(false);
        }
        orderTables.clear();
    }

    private void validateOrderTableStatus(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            if (orderTable.isGrouped() || !orderTable.isEmpty()) {
                throw new IllegalArgumentException("유효하지 않은 주문 테이블입니다. orderTable : " + orderTable);
            }
        }
    }

    private void validateDuplicateOrderTable(List<OrderTable> orderTables) {
        var distinctIds = orderTables.stream()
                .map(OrderTable::getId)
                .distinct()
                .count();
        if (orderTables.size() != distinctIds) {
            throw new IllegalArgumentException("중복된 주문 테이블이 존재합니다. orderTables = " + orderTables);
        }
    }

    private void addOrderTables(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            setGroup(orderTable);
        }
        this.orderTables.addAll(orderTables);
    }

    private void setGroup(OrderTable orderTable) {
        orderTable.changeEmpty(false);
        orderTable.setTableGroup(this);
    }

    private void validateSize(List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException("주문 테이블의 수는 최소 2개 이상이어야 합니다. 현재 크기 = " + orderTables.size());
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
}
