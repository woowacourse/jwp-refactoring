package kitchenpos.domain.table;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {
    }

    public TableGroup(final OrderTables orderTables) {
        this(null, orderTables);
    }

    public TableGroup(final Long id, final OrderTables orderTables) {
        changeOrderTableTableGroup(orderTables);
        this.id = id;
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
    }

    public void ungroup() {
        orderTables.ungroup();
    }

    private void changeOrderTableTableGroup(final OrderTables orderTables) {
        orderTables.getOrderTables().forEach(orderTable -> orderTable.changeTableGroup(this.getId()));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }
}

//이 과정에서, 도메인 객체가 다른 도메인과 협력을 통해 검증해야 되는 경우에만 Validator를 사용하도록 했어요.
//즉, 객체 내부에서 검증할 수 있는 부분은 별도의 `Validator를 사용하지 않았습니다 !
