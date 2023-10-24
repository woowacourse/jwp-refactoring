package kitchenpos.application.order;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderStatusRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.exception.menuException.MenuNotFoundException;
import kitchenpos.exception.orderTableException.OrderTableNotFoundException;
import kitchenpos.exception.orderException.OrderNotFoundException;
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
        final Order order = new Order(orderTable, getOrderLineItems(orderRequest.getOrderLineItems()));

        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }

    private OrderLineItems getOrderLineItems(final List<OrderLineItemRequest> orderLineItemRequests) {
        return new OrderLineItems(orderLineItemRequests.stream()
                .map(orderLineItemRequest -> {
                    final Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                            .orElseThrow(MenuNotFoundException::new);
                    return new OrderLineItem(menu, orderLineItemRequest.getQuantity());
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
}
