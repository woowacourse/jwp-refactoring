package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItemRepository;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderStatusRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(OrderTableNotFoundException::new);
        final Order order = new Order(orderTable);
        order.initOrderLineItem(getOrderLineItems(order, orderRequest.getOrderLineItems()));

        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> getOrderLineItems(final Order order, final List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest -> {
                    final Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                            .orElseThrow(MenuNotFoundException::new);
                    return new OrderLineItem(order, menu, orderLineItemRequest.getQuantity());
                }).collect(Collectors.toList());
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

        savedOrder.changeOrderSteatus(orderStatusRequest.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }
}
