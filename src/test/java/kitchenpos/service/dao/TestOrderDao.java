package kitchenpos.service.dao;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;

public class TestOrderDao extends TestAbstractDao<Order> implements OrderDao {


    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId,
                                                        List<String> orderStatuses) {
        return database.values().stream()
            .filter(order -> order.getOrderTableId().equals(orderTableId))
            .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds,
                                                          List<String> orderStatuses) {
        return database.values().stream()
            .filter(order -> orderTableIds.contains(order.getOrderTableId()))
            .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }

    @Override
    protected BiConsumer<Order, Long> setIdConsumer() {
        return Order::setId;
    }

    @Override
    protected Comparator<Order> comparatorForSort() {
        return Comparator.comparingLong(Order::getId);
    }
}
