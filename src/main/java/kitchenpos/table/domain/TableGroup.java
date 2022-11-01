package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "table_group")
@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private final List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup() {
    }

    public void group(List<OrderTable> orderTables) {
        validateOrderTableSize(orderTables);
        validateOrderTableIsNotEmpty(orderTables);
        for (OrderTable orderTable : orderTables) {
            orderTable.joinTableGroup(this);
            this.orderTables.add(orderTable);
        }
    }

    private void validateOrderTableSize(List<OrderTable> orderTables) {
        if (orderTables.isEmpty() || orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블을 그룹화하려면 2개 이상의 테이블이 필요합니다.");
        }
    }

    private void validateOrderTableIsNotEmpty(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            validateInNotGroupedTable(orderTable);
            validateTableIsEmpty(orderTable);
        }
    }

    private void validateInNotGroupedTable(OrderTable orderTable) {
        if (orderTable.isAlreadyGrouped()) {
            throw new IllegalArgumentException("이미 테이블 그룹이 형성된 테이블입니다.");
        }
    }

    private void validateTableIsEmpty(OrderTable orderTable) {
        if (orderTable.isNotEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있지 않습니다.");
        }
    }

    public void ungroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.leaveTableGroup();
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
