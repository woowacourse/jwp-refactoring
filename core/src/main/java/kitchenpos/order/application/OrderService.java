package kitchenpos.order.application;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.persistence.MenuRepository;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.persistence.OrderRepository;
import kitchenpos.order.request.OrderCreateRequest;
import kitchenpos.table.OrderTable;
import kitchenpos.table.persistence.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
        MenuRepository menuRepository,
        OrderRepository orderRepository,
        OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(OrderCreateRequest request) {
        List<OrderLineItem> orderLineItems = request.getOrderLineItems().stream()
            .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
            .collect(toList());
        validateOrderLineItemSize(orderLineItems);
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);
        return orderRepository.save(Order.cooking(orderTable, orderLineItems, LocalDateTime.now()));
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, OrderStatus changedOrderStatus) {
        Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);
        savedOrder.changeOrderStatus(changedOrderStatus);
        return savedOrder;
    }

    public List<Order> list() {
        return orderRepository.findAllWithFetch();
    }

    private void validateOrderLineItemSize(List<OrderLineItem> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(toList());
        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }
}
