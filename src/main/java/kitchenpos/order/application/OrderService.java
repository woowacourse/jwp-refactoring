package kitchenpos.order.application;

import kitchenpos.common.ValidateOrderTableOrderStatusEvent;
import kitchenpos.common.ValidateOrderTablesOrderStatusEvent;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.product.domain.Price;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.menu.exception.MenuNotFoundException;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.table.exception.OrderTableNotFoundException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(OrderTableNotFoundException::new);
        final Order order = new Order(orderTable.getId(), getOrderLineItems(orderRequest.getOrderLineItems()));

        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }

    private OrderLineItems getOrderLineItems(final List<OrderLineItemRequest> orderLineItemRequests) {
        return new OrderLineItems(orderLineItemRequests.stream()
                .map(request -> {
                    final Menu menu = menuRepository.findById(request.getMenuId())
                            .orElseThrow(MenuNotFoundException::new);
                    return new OrderLineItem(menu.getName(), new Price(menu.getPrice()), request.getQuantity());
                }).collect(Collectors.toList()));
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        savedOrder.changeOrderStatus(orderStatusRequest.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }

    @EventListener
    public void validateOrderStatus(final ValidateOrderTableOrderStatusEvent validateOrderTableOrderStatusEvent) {
        orderRepository.findAllByOrderTableId(validateOrderTableOrderStatusEvent.getOrderTableId())
                .forEach(Order::validateOrderComplete);
    }

    @EventListener
    public void validateOrdersStatus(final ValidateOrderTablesOrderStatusEvent validateOrderTablesOrderStatusEvent) {
        orderRepository.findByOrderTableIdIn(validateOrderTablesOrderStatusEvent.getOrderTableIds())
                .forEach(Order::validateOrderComplete);
    }
}
