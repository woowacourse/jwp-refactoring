package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.Order;

public class OrderFakeDao extends BaseFakeDao<Order> implements OrderDao {

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId,
                                                        final List<String> orderStatuses) {
        final var found = entities.values()
                .stream()
                .filter(order -> order.getOrderTableId().equals(orderTableId)
                        && orderStatuses.contains(order.getOrderStatus()))
                .findAny();
        return found.isPresent();
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
                                                          final List<String> orderStatuses) {
        final var found = entities.values()
                .stream()
                .filter(order -> orderTableIds.contains(order.getOrderTableId())
                        && orderStatuses.contains(order.getOrderStatus()))
                .findAny();
        return found.isPresent();
    }
}
