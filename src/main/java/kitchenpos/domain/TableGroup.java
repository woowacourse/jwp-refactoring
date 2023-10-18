package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class TableGroup {

    private static final int MIN_ORDER_TABLE_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @OneToMany
    @JoinColumn(name = "table_group_id")
    private final List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(Long id) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
    }

    public static TableGroup createEmpty() {
        return new TableGroup(null);
    }

    public void group(List<OrderTable> orderTables) {
        validate(orderTables);
        orderTables.forEach(it -> it.group(this));
        this.orderTables.addAll(orderTables);
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::unGroup);
    }

    private void validate(List<OrderTable> orderTables) {
        if (orderTables.size() < MIN_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException(String.format("테이블 그룹은 최소 %s개 이상의 테이블이 필요합니다.", MIN_ORDER_TABLE_SIZE));
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
