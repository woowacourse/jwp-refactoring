package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTableGroup {

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY)
    private List<OrderTable> orderTables = new ArrayList<>();

    public static OrderTableGroup create(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        for (OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }

        final OrderTableGroup orderTableGroup = new OrderTableGroup();
        orderTableGroup.orderTables = orderTables;
        return orderTableGroup;
    }

    public static OrderTableGroup createSingleTables(List<OrderTable> orderTables) {
        final OrderTableGroup orderTableGroup = new OrderTableGroup();
        orderTableGroup.orderTables = orderTables;
        return orderTableGroup;
    }

    public List<OrderTable> values() {
        return orderTables;
    }
}
