package kitchenpos.infrastructure;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItemDao;

public class OrderLineItemFakeDao extends BaseFakeDao<OrderLineItem> implements OrderLineItemDao {

    @Override
    public List<OrderLineItem> findAllByOrderId(final Long orderId) {
        return entities.values()
                .stream()
                .filter(orderLineItem -> orderLineItem.getOrderId().equals(orderId))
                .collect(Collectors.toList());
    }
}
