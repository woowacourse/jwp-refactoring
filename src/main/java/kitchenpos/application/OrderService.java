package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.exception.OrderException.EmptyOrderTableException;
import kitchenpos.domain.exception.OrderException.NotExistsMenuException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final Order order) {
        validateOrder(order);
        return orderRepository.save(order);
    }

    private void validateOrder(final Order order) {
        validateOrderLineItems(order.getOrderLineItems());
        validateOrderTable(order.getOrderTableId());
    }

    private void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new NotExistsMenuException();
        }
    }

    private void validateOrderTable(final Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.getById(orderTableId);

        if (orderTable.isEmpty()) {
            throw new EmptyOrderTableException(orderTable.getId());
        }
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.getById(orderId);

        savedOrder.changeOrderStatus(order.getOrderStatus());

        return savedOrder;
    }
}
