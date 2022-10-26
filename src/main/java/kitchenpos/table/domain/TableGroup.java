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

@Entity
public class TableGroup {

    private static final int MINIMUM_TABLE_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    private TableGroup(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public static TableGroup group(List<OrderTable> orderTables, LocalDateTime createdDate) {
        validateTableSize(orderTables);
        TableGroup tableGroup = new TableGroup(createdDate);
        tableGroup.addOrderTables(orderTables);
        return tableGroup;
    }

    private static void validateTableSize(List<OrderTable> orderTables) {
        if (orderTables.size() < MINIMUM_TABLE_SIZE) {
            throw new IllegalArgumentException("테이블 그룹은 2개 이상부터 지정할 수 있습니다.");
        }
    }

    public void addOrderTables(List<OrderTable> orderTables) {
        orderTables.forEach(table -> table.groupedBy(this));
        this.orderTables.addAll(orderTables);
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
        this.orderTables.clear();
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
