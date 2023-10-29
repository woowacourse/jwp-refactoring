package kitchenpos.order.application.event;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.MenuHistory;
import kitchenpos.order.domain.MenuHistoryRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderHistoryEventListener {

    private final OrderRepository orderRepository;
    private final MenuHistoryRepository menuHistoryRepository;
    private final MenuRepository menuRepository;

    public OrderHistoryEventListener(final OrderRepository orderRepository,
                                     final MenuHistoryRepository menuHistoryRepository,
                                     final MenuRepository menuRepository
    ) {
        this.orderRepository = orderRepository;
        this.menuHistoryRepository = menuHistoryRepository;
        this.menuRepository = menuRepository;
    }

    @EventListener
    @Transactional(propagation = Propagation.REQUIRED)
    public void menuHistoryEvent(final OrderPreparedEvent orderPreparedEvent) {
        final Order findOrder = orderRepository.findOrderById(orderPreparedEvent.getOrderId());
        menuHistoryRepository.saveAllAndFlush(getMenuHistories(findOrder.getId(), getMenuIds(findOrder)));
    }

    private static List<Long> getMenuIds(final Order findOrder) {
        return findOrder.getOrderLineItems()
                .getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    private List<MenuHistory> getMenuHistories(final Long orderId, final List<Long> findMenuIds) {
        return menuRepository.findInMenuIds(findMenuIds)
                .stream()
                .map(menu -> MenuHistory.of(orderId, menu.getName(), menu.getPrice()))
                .collect(Collectors.toList());
    }
}
