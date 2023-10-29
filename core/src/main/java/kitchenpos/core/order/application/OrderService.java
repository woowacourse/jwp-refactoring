package kitchenpos.core.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.core.menu.application.MenuService;
import kitchenpos.core.menu.application.dto.MenuResponse;
import kitchenpos.core.order.application.dto.OrderLineItemsRequest;
import kitchenpos.core.order.application.dto.OrderResponse;
import kitchenpos.core.order.domain.Order;
import kitchenpos.core.order.domain.OrderLineItem;
import kitchenpos.core.order.domain.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderDao orderDao;
    private final MenuService menuService;
    private final List<OrderCreationValidator> orderCreationValidators;

    public OrderService(final OrderDao orderDao, final MenuService menuService,
                        final List<OrderCreationValidator> orderCreationValidators) {
        this.orderDao = orderDao;
        this.menuService = menuService;
        this.orderCreationValidators = orderCreationValidators;
    }

    @Transactional
    public OrderResponse create(final Long orderTableId, final List<OrderLineItemsRequest> orderLineItemRequests) {
        final List<OrderLineItem> orderLineItems = convertToLineItems(orderLineItemRequests);

        final Order order = new Order(orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        for (OrderCreationValidator orderCreationValidator : orderCreationValidators) {
            orderCreationValidator.validate(order);
        }

        return OrderResponse.from(orderDao.save(order));
    }

    private List<OrderLineItem> convertToLineItems(final List<OrderLineItemsRequest> orderLineItemRequests) {
        return orderLineItemRequests
                .stream().map(orderLineItemsRequest -> {
                    final MenuResponse menu = menuService.findById(orderLineItemsRequest.getMenuId());
                    return new OrderLineItem(
                            orderLineItemsRequest.getMenuId(),
                            menu.getPrice(),
                            menu.getName(),
                            orderLineItemsRequest.getQuantity()
                    );
                })
                .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();
        return OrderResponse.from(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderDao.findMandatoryById(orderId);
        savedOrder.transitionStatusTo(orderStatus);
        orderDao.save(savedOrder);
        return OrderResponse.from(savedOrder);
    }
}
