package kitchenpos.domain.order;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.domain.group.TableGroup;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    public void validateGroup() {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException("주문 테이블은 활성화되어 있거나 이미 단체 지정되어 있을 수 없습니다.");
            }
        }
    }

    public OrderTables arrangeGroup(final TableGroup tableGroup) {
        orderTables.forEach(orderLineItem -> orderLineItem.arrangeGroup(tableGroup));
        return this;
    }

    public void ungroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    private void validate(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("주문 테이블은 비어있거나 2개 미만일 수 없습니다.");
        }
    }

    public List<Long> getIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
