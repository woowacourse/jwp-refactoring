package kitchenpos.menu.event;

import kitchenpos.menu.domain.MenuHistory;
import kitchenpos.menu.domain.MenuHistoryRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.event.OrderPreparedEvent;
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
public class MenuHistoryEventListener {

    private OrderRepository orderRepository;
    private MenuHistoryRepository menuHistoryRepository;
    private MenuRepository menuRepository;

    public MenuHistoryEventListener(final OrderRepository orderRepository,
                                    final MenuRepository menuRepository,
                                    final MenuHistoryRepository menuHistoryRepository
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
                .map(menu -> MenuHistory.of(orderId, menu))
                .collect(Collectors.toList());
    }
}
