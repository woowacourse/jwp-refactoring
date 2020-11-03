package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.MenuQuantityRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemResponse;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderTableRepository orderTableRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest orderCreateRequest) {
        final List<OrderLineItem> orderLineItems = makeOrderLineItemsWithoutOrder(orderCreateRequest.getMenuQuantities());

        final OrderTable orderTable = orderTableRepository.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        validateEmpty(orderTable);

        final Order savedOrder = orderRepository.save(orderCreateRequest.toEntity(orderTable));

        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.updateOrder(savedOrder);
            orderLineItemRepository.save(orderLineItem);
        }

        return OrderResponse.of(savedOrder, OrderLineItemResponse.ofList(orderLineItems));
    }

    private List<OrderLineItem> makeOrderLineItemsWithoutOrder(final List<MenuQuantityRequest> menuQuantities) {
        return menuQuantities.stream()
                .map(menuQuantityRequest -> {
                    final Menu menu = menuRepository.findById(menuQuantityRequest.getMenuId())
                            .orElseThrow(IllegalArgumentException::new);
                    return new OrderLineItem(null, menu, menuQuantityRequest.getQuantity());
                }).collect(Collectors.toList());
    }

    private void validateEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> {
                    List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(order.getId());
                    return OrderResponse.of(order, OrderLineItemResponse.ofList(orderLineItems));
                }).collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        validateIsNotCompletionOrder(savedOrder);

        final OrderStatus orderStatus = OrderStatus.valueOf(orderStatusRequest.getOrderStatus());
        savedOrder.updateStatus(orderStatus.name());

        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(orderId);

        return OrderResponse.of(savedOrder, OrderLineItemResponse.ofList(orderLineItems));
    }

    private void validateIsNotCompletionOrder(final Order order) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), order.getOrderStatus())) {
            throw new IllegalArgumentException();
        }
    }
}
