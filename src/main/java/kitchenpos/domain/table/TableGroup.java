package kitchenpos.domain.table;

import kitchenpos.exception.InvalidOrderTableException;
import kitchenpos.util.ValidateUtil;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup from(List<OrderTable> orderTables) {
        ValidateUtil.validateNonNull(orderTables);
        validateOrderTables(orderTables);
        LocalDateTime createdDate = LocalDateTime.now();

        return new TableGroup(createdDate, orderTables);
    }

    private static void validateOrderTables(List<OrderTable> orderTables) {
        orderTables.forEach(orderTable -> {
            if (!orderTable.isEmpty() || orderTable.hasTableGroup()) {
                throw new InvalidOrderTableException
                        ("단체 지정 생성 시 소속된 주문 테이블은 주문을 등록할 수 없으며(빈 테이블) 다른 단체 지정이 존재해서는 안됩니다!");
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

    public void setOrderTables(List<OrderTable> orderTables) {
        ValidateUtil.validateNonNull(orderTables);

        this.orderTables = orderTables;
    }
}
