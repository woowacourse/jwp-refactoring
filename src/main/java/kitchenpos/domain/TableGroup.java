package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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

    public TableGroup(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public void group(final List<OrderTable> orderTables, final int duplicateCheckSize) {
        validateSize(orderTables.size(), duplicateCheckSize);
        orderTables.forEach(orderTable -> orderTable.groupTable(this));
        this.orderTables.addAll(orderTables);
    }

    public void ungroup() {
        this.orderTables.forEach(OrderTable::ungroupTable);
        this.orderTables.clear();
    }

    private void validateSize(final int orderTablesSize, final int duplicateCheckSize) {
        if (orderTablesSize != duplicateCheckSize) {
            throw new IllegalArgumentException("존재하지 않거나 중복된 테이블을 단체 지정할 수 없습니다.");
        }
    }

    public void addCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void addOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
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
