package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ChangeOrderStatusCommand;
import kitchenpos.application.dto.CreateOrderCommand;
import kitchenpos.application.dto.CreateOrderCommand.OrderLineItemRequest;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderTableRepository orderTableRepository,
            final OrderRepository orderRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order create(final CreateOrderCommand command) {
        final List<OrderLineItemRequest> orderLineItemRequests = command.getOrderLineItems();
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = command.getMenuIds();
        if (orderLineItemRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("주문항목은 각각 다른 메뉴이며 존재해야합니다.");
        }

        final OrderTable orderTable = orderTableRepository.findById(command.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final List<OrderLineItem> orderLineItems = orderLineItemRequests.stream()
                .map(request -> request.toDomain(null))
                .collect(Collectors.toList());
        Order order = new Order(null, orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final ChangeOrderStatusCommand command) {
        final Long orderId = command.getOrderId();
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(command.getOrderStatus());
        order.changeOrderStatus(orderStatus);
        return order;
    }
}
