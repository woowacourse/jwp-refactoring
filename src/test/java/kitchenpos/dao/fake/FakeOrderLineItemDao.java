package kitchenpos.dao.fake;

import static java.util.stream.Collectors.*;
import static kitchenpos.application.fixture.OrderLineItemFixtures.generateOrderLineItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.OrderLineItem;

public class FakeOrderLineItemDao implements OrderLineItemDao {

    private static final Map<Long, OrderLineItem> stores = new HashMap<>();
    private static Long id = 0L;

    @Override
    public OrderLineItem save(final OrderLineItem entity) {
        OrderLineItem orderLineItem = generateOrderLineItem(++id, entity);
        stores.put(id, orderLineItem);
        return orderLineItem;
    }

    @Override
    public Optional<OrderLineItem> findById(final Long id) {
        return Optional.of(stores.get(id));
    }

    @Override
    public List<OrderLineItem> findAll() {
        return new ArrayList<>(stores.values());
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(final Long orderId) {
        return stores.values()
                .stream()
                .filter(orderLineItem -> orderLineItem.getOrderId() == orderId)
                .collect(toList());
    }

    public static void deleteAll() {
        stores.clear();
    }
}
