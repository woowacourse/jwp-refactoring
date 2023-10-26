package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ChangeOrderStatusCommand;
import kitchenpos.application.dto.CreateOrderCommand;
import kitchenpos.application.dto.CreateOrderCommand.OrderLineItemRequest;
import kitchenpos.application.dto.domain.OrderDto;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.MenuVo;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderValidator orderValidator;

    public OrderService(final OrderRepository orderRepository, final MenuRepository menuRepository,
                        final OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderDto create(final CreateOrderCommand command) {
        Order order = toDomain(command);
        order.place(orderValidator);
        return OrderDto.from(orderRepository.save(order));
    }

    public Order toDomain(CreateOrderCommand command) {
        List<OrderLineItemRequest> orderLineItemRequests = command.getOrderLineItemRequests();
        List<OrderLineItem> orderLineItems = orderLineItemRequests.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
        return new Order(command.getOrderTableId(), orderLineItems);
    }

    public OrderLineItem toDomain(OrderLineItemRequest command) {
        Long menuId = command.getMenuId();
        Menu menu = menuRepository.getById(menuId);
        return new OrderLineItem(null, new MenuVo(menuId, menu.getName(), menu.getPrice()), command.getQuantity());
    }

    public List<OrderDto> list() {
        return orderRepository.findAll().stream()
                .map(OrderDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto changeOrderStatus(final ChangeOrderStatusCommand command) {
        final Long orderId = command.getOrderId();
        final Order order = orderRepository.getById(orderId);

        final OrderStatus orderStatus = OrderStatus.valueOf(command.getOrderStatus());
        changeOrderStatus(order, orderStatus);
        return OrderDto.from(order);
    }

    public void changeOrderStatus(final Order order, final OrderStatus newStatus) {
        if (newStatus == OrderStatus.COOKING) {
            order.startCooking();
        }
        if (newStatus == OrderStatus.MEAL) {
            order.startMeal();
        }
        if (newStatus == OrderStatus.COMPLETION) {
            order.completeOrder();
        }
    }

}
