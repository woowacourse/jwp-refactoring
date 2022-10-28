package kitchenpos.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroupId")
    private List<OrderTable> values;

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> values) {
        validate(values);
        this.values = values;
    }

    private void validate(List<OrderTable> values) {
        if (values.size() < 2) {
            throw new IllegalArgumentException("주문 테이블은 2개 이상이어야 합니다.");
        }

        for (OrderTable orderTable : values) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException("빈 주문 테이블이어야 합니다.");
            }
        }
    }

    public static OrderTables group(List<OrderTable> orderTables, List<Long> orderTableIds) {
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException("주문 테이블의 수가 다릅니다.");
        }
        return new OrderTables(orderTables);
    }
}
