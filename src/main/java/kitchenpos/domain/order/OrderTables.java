package kitchenpos.domain.order;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.REMOVE)
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    private void validate(List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException("주문 테이블의 개수가 2 미만일 경우 생성할 수 없습니다.");
        }
        for (final OrderTable savedOrderTable : orderTables) {
            if (!savedOrderTable.isEmpty() || savedOrderTable.hasTableGroup()) {
                throw new IllegalArgumentException("빈 테이블이 아닌 경우 생성할 수 없다.");
            }
        }
    }

    public void setTableGroup(TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(tableGroup);
            orderTable.changeEmpty(false);
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void ungroup() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(null);
            orderTable.changeEmpty(false);
        }
    }
}
