package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.dto.OrderRequest;
import kitchenpos.domain.dto.OrderResponse;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderLineItemRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
        if (orderRequest.getOrderLineItems().isEmpty()) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderRequest.getOrderLineItems().stream()
                .map(OrderRequest.OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        if (Objects.isNull(orderRequest.getOrderTableId())) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final Order order = new Order(orderTable);

        orderRepository.save(order);

        final OrderLineItems orderLineItems = mapToOrderLineItems(orderRequest, order);
        orderLineItemRepository.saveAll(orderLineItems.getValues());

        return OrderResponse.from(order);
    }

    private OrderLineItems mapToOrderLineItems(final OrderRequest orderRequest, final Order order) {
        final List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems().stream()
                .map(orderLineItemRequest -> new OrderLineItem(
                        order,
                        menuRepository.findById(orderLineItemRequest.getMenuId())
                                .orElseThrow(IllegalArgumentException::new),
                        orderLineItemRequest.getQuantity()
                ))
                .collect(Collectors.toList());

        return new OrderLineItems(orderLineItems);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        validateOrderId(orderId);

        final Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        order.updateOrderStatus(orderRequest.getOrderStatus());

        orderRepository.save(order);

        return OrderResponse.from(order);
    }

    private void validateOrderId(final Long orderId) {
        if (Objects.isNull(orderId)) {
            throw new IllegalArgumentException();
        }
    }
}
