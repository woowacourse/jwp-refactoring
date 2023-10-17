package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.application.dto.order.CreateOrderCommand;
import kitchenpos.application.dto.order.CreateOrderResponse;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
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
    public CreateOrderResponse create(CreateOrderCommand command) {
        OrderTable orderTable = orderTableRepository.getById(command.orderTableId());
        List<OrderLineItem> orderLineItems = command.orderLineItemCommands().stream()
                .map(it -> new OrderLineItem(menuRepository.getById(it.menuId()), it.quantity()))
                .collect(Collectors.toList());
        Order order = new Order(null, orderTable, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        return CreateOrderResponse.from(orderRepository.save(order));
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.orderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(order.orderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        return orderRepository.save(savedOrder);
    }
}
