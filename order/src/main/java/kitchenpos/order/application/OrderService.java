package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.event.OrderedEvent;
import kitchenpos.event.OrderedMenuEvent;
import kitchenpos.order.application.dto.ChangeOrderStatusCommand;
import kitchenpos.order.application.dto.ChangeOrderStatusResponse;
import kitchenpos.order.application.dto.CreateOrderCommand;
import kitchenpos.order.application.dto.CreateOrderResponse;
import kitchenpos.order.application.dto.OrderLineItemCommand;
import kitchenpos.order.application.dto.SearchOrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final ApplicationEventPublisher publisher;
    private final OrderRepository orderRepository;

    public OrderService(
            ApplicationEventPublisher publisher,
            OrderRepository orderRepository
    ) {
        this.publisher = publisher;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public CreateOrderResponse create(CreateOrderCommand command) {
        Long orderTableId = command.orderTableId();
        List<OrderLineItemCommand> orderLineItemCommands = command.orderLineItemCommands();
        publisher.publishEvent(new OrderedEvent(orderTableId));
        publisher.publishEvent(new OrderedMenuEvent(getMenuIds(orderLineItemCommands)));
        Order order = new Order(orderTableId, OrderStatus.COOKING, getOrderLineItems(orderLineItemCommands));
        return CreateOrderResponse.from(orderRepository.save(order));
    }

    private List<Long> getMenuIds(List<OrderLineItemCommand> orderLineItemCommands) {
        return orderLineItemCommands.stream()
                .map(OrderLineItemCommand::menuId)
                .collect(Collectors.toList());
    }

    private OrderLineItems getOrderLineItems(List<OrderLineItemCommand> orderLineItemCommands) {
        List<OrderLineItem> orderLineItems = orderLineItemCommands.stream()
                .map(it -> new OrderLineItem(it.menuId(), it.quantity()))
                .collect(Collectors.toList());
        return new OrderLineItems(orderLineItems);
    }

    public List<SearchOrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(SearchOrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChangeOrderStatusResponse changeOrderStatus(ChangeOrderStatusCommand command) {
        Order savedOrder = orderRepository.getById(command.orderId());
        savedOrder.changeOrderStatus(command.orderStatus());
        return ChangeOrderStatusResponse.from(orderRepository.save(savedOrder));
    }
}
