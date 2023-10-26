package kitchenpos.order.service;

import static kitchenpos.order.domain.OrderStatus.COOKING;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.infra.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.infra.OrderLineItemRepository;
import kitchenpos.order.infra.OrderRepository;
import kitchenpos.order.service.dto.OrderCreateCommand;
import kitchenpos.order.service.dto.OrderLineItemCreateCommand;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.infra.OrderTableRepository;
import kitchenpos.vo.Quantity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            OrderLineItemRepository orderLineItemRepository,
            OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    // TODO: 패키지 양방향 의존 발생
    @Transactional
    public Order create(OrderCreateCommand command) {
        OrderTable orderTable = orderTableRepository.getById(command.orderTableId());
        Order order = new Order(orderTable, COOKING);

        List<OrderLineItemCreateCommand> orderLineItemCommands = command.createOrderLineItemRequests();
        for (OrderLineItemCreateCommand orderLineItemRequest : orderLineItemCommands) {
            Menu menu = menuRepository.getById(orderLineItemRequest.menuId());
            OrderLineItem orderLineItem = new OrderLineItem(order, menu.id(), new Quantity(orderLineItemRequest.quantity()));
            orderLineItemRepository.save(orderLineItem);
        }

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order order = orderRepository.getById(orderId);
        order.changeOrderStatus(orderStatus);
        return order;
    }
}
