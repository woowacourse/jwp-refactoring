package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.data.annotation.CreatedDate;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdDate;

    protected TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeEmpty(false);
            addOrderTable(orderTable);
        }
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            validateEmptyOrderTable(orderTable);
            orderTable.validateNoTableGroup();
        }
    }

    private void validateEmptyOrderTable(final OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블은 비어있어야 합니다.");
        }
    }

    public void addOrderTable(final OrderTable orderTable) {
        orderTable.setTableGroup(this);
        this.orderTables.add(orderTable);
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
