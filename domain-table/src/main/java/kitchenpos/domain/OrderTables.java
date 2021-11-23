package kitchenpos.domain;

import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    public OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables from(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("그룹을 지정하려면 둘 이상의 테이블이 필요합니다.");
        }
        return new OrderTables(orderTables);
    }

    public void ungroup(TableValidator tableValidator) {
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup(tableValidator);
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
