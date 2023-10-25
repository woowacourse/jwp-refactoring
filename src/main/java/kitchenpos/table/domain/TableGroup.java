package kitchenpos.table.domain;

import kitchenpos.table.exception.TableGroupException;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        this(null, LocalDateTime.now(), orderTables);
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        for (OrderTable orderTable : orderTables) {
            orderTable.assignTableGroup(this);
        }

        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new TableGroupException("주문 테이블의 수가 유효하지 않습니다.");
        }

        orderTables.forEach(orderTable -> {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new TableGroupException("주문 테이블의 상태가 유효하지 않습니다.");
            }
        });
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
