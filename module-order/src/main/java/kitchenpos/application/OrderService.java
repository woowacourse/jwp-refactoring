package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;

import java.util.List;
import kitchenpos.application.dto.OrderCreateCommand;
import kitchenpos.application.dto.OrderLineItemCreateCommand;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.infra.MenuRepository;
import kitchenpos.infra.OrderLineItemRepository;
import kitchenpos.infra.OrderRepository;
import kitchenpos.infra.OrderTableRepository;
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

    @Transactional
    public Order create(OrderCreateCommand command) {
        OrderTable orderTable = orderTableRepository.getById(command.orderTableId());
        Order order = new Order(orderTable, COOKING);

        List<OrderLineItemCreateCommand> orderLineItemCommands = command.orderLineItemCreateCommands();
        for (OrderLineItemCreateCommand orderLineItemCommand : orderLineItemCommands) {
            Menu menu = menuRepository.getById(orderLineItemCommand.menuId());
            OrderLineItem orderLineItem = new OrderLineItem(order, menu.id(), new Quantity(orderLineItemCommand.quantity()));
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
