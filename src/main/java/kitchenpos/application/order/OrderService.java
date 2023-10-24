package kitchenpos.application.order;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.order.dto.ChangeOrderStatusCommand;
import kitchenpos.application.order.dto.ChangeOrderStatusResponse;
import kitchenpos.application.order.dto.CreateOrderCommand;
import kitchenpos.application.order.dto.CreateOrderResponse;
import kitchenpos.application.order.dto.OrderLineItemCommand;
import kitchenpos.application.order.dto.SearchOrderResponse;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
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
