package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.order.ChangeOrderStatusCommand;
import kitchenpos.application.dto.order.ChangeOrderStatusResponse;
import kitchenpos.application.dto.order.CreateOrderCommand;
import kitchenpos.application.dto.order.CreateOrderResponse;
import kitchenpos.application.dto.order.SearchOrderResponse;
import kitchenpos.application.dto.orderlineitem.OrderLineItemCommand;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
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
        Order order = new Order(orderTable, OrderStatus.COOKING, getOrderLineItems(command.orderLineItemCommands()));
        return CreateOrderResponse.from(orderRepository.save(order));
    }

    private OrderLineItems getOrderLineItems(List<OrderLineItemCommand> orderLineItemCommands) {
        List<OrderLineItem> orderLineItems = orderLineItemCommands.stream()
                .map(it -> new OrderLineItem(menuRepository.getById(it.menuId()), it.quantity()))
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
