package kitchenpos.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateOrderCommand;
import kitchenpos.application.dto.CreateOrderResponse;
import kitchenpos.application.dto.common.OrderLineItemCommand;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
        List<OrderLineItemCommand> orderLineItemCommands = command.orderLineItemCommands();
        if (CollectionUtils.isEmpty(orderLineItemCommands)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItemCommands.stream()
                .map(OrderLineItemCommand::menuId)
                .collect(Collectors.toList());

        if (orderLineItemCommands.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(command.orderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.empty()) {
            throw new IllegalArgumentException();
        }

        Order order = new Order(orderTable, OrderStatus.COOKING.name());
        orderLineItemCommands.stream()
                .map(it -> new OrderLineItem(menuRepository.getById(it.menuId()), it.quantity()))
                .forEach(order::addOrderLineItem);
        Order savedOrder = orderRepository.save(order);
        return CreateOrderResponse.from(savedOrder);
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
