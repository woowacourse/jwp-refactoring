package kitchenpos.domain;

import org.hibernate.annotations.BatchSize;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {

    @BatchSize(size = 10)
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "tableGroup")
    private List<OrderTable> orderTableItems;

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> orderTableItems) {
        this.orderTableItems = orderTableItems;
    }

    public static OrderTables empty() {
        return new OrderTables(new ArrayList<>());
    }

    public void addOrderTables(final OrderTables requestOrderTables) {
        if (requestOrderTables.orderTableItems.stream().anyMatch(OrderTable::isGrouped)) {
            throw new IllegalArgumentException("주문 테이블 목록을 생성할 때 이미 단체 지정이 된 주문 테이블이 존재할 수 없습니다.");
        }

        requestOrderTables.orderTableItems.forEach(orderTable -> orderTable.changeOrderTableEmpty(false));
        orderTableItems.addAll(requestOrderTables.orderTableItems);
    }

    public void assignTableGroup(final TableGroup tableGroup) {
        orderTableItems.forEach(orderTable -> orderTable.assignTableGroup(tableGroup));
    }

    public List<OrderTable> getOrderTableItems() {
        return orderTableItems;
    }
}
