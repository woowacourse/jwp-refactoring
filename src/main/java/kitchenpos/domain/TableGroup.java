package kitchenpos.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    private TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        validateOrderTableSize(orderTables.size());
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private TableGroup(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroup createWithGrouping(final List<OrderTable> orderTables) {
        return new TableGroup(null, LocalDateTime.now(), orderTables);
    }

    public static TableGroup createWithoutGrouping() {
        return new TableGroup(null, LocalDateTime.now());
    }

    private void validateOrderTableSize(final int orderTableSize) {
        if (orderTableSize < 2) {
            throw new IllegalArgumentException("그룹화 할 테이블 개수는 2 이상이어야 합니다.");
        }
    }

    public void group(final List<OrderTable> orderTables) {
        validateOrderTableSize(orderTables.size());
        orderTables.forEach(orderTable -> orderTable.group(this));
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
