package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.exception.NotFoundOrderException;
import kitchenpos.exception.NotFoundOrderTableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderCreateRequest orderCreateRequest) {
        final Order order = orderCreateRequest.toOrder();
        validateOrderLineItemMatchMenu(order);

        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(NotFoundOrderTableException::new);
        orderTable.validateOrderable();

        final Order newOrder = new Order(null, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
                order.getOrderLineItems());

        return orderRepository.save(newOrder);
    }

    private void validateOrderLineItemMatchMenu(final Order order) {
        final List<Long> menuIds = order.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        order.validateOrderLineItemSize(menuRepository.countByIdIn(menuIds));
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final String orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(NotFoundOrderException::new);

        savedOrder.updateOrderStatus(OrderStatus.valueOf(orderStatus).name());
        return savedOrder;
    }
}
