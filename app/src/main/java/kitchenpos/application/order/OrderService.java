package kitchenpos.application.order;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.order.dto.ChangeOrderStatusCommand;
import kitchenpos.application.order.dto.ChangeOrderStatusResponse;
import kitchenpos.application.order.dto.CreateOrderCommand;
import kitchenpos.application.order.dto.CreateOrderResponse;
import kitchenpos.application.order.dto.OrderLineItemCommand;
import kitchenpos.application.order.dto.SearchOrderResponse;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderedEvent;
import kitchenpos.domain.order.OrderedMenuEvent;
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
