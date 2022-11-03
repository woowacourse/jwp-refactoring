package kitchenpos.domain.tablegroup;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OrderTableRef {

    @Id
    private Long orderTableId;

    protected OrderTableRef() {
    }

    public OrderTableRef(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
