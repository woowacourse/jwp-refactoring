package kitchenpos.application;

import kitchenpos.application.dto.request.CreateOrderRequest;
import kitchenpos.application.dto.request.UpdateOrderStatusRequest;
import kitchenpos.application.dto.response.CreateOrderResponse;
import kitchenpos.application.dto.response.OrderLineItemResponse;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.mapper.OrderMapper;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderMapper orderMapper;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final OrderMapper orderMapper,
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.orderMapper = orderMapper;
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public CreateOrderResponse create(final CreateOrderRequest request) {
        final OrderTable entity = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (entity.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final List<OrderLineItem> orderLineItems = getOrderLineItems(request.getOrderLineItemIds());
        final Order order = orderMapper.toOrder(request, orderLineItems);
        final Order savedOrder = orderRepository.save(order);
        final List<OrderLineItem> savedOrderLineItems = saveOrderLineItems(orderLineItems);

        return CreateOrderResponse.of(
                savedOrder.updateOrderLineItems(savedOrderLineItems),
                savedOrderLineItems.stream()
                        .map(OrderLineItemResponse::from)
                        .collect(Collectors.toList())
        );
    }

    private List<OrderLineItem> saveOrderLineItems(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItemRepository::save)
                .collect(Collectors.toList());
    }

    private List<OrderLineItem> getOrderLineItems(List<Long> menuIds) {
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        return menuIds.stream()
                .map(orderLineItemRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final UpdateOrderStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        Order updated = savedOrder.updateStatus(orderStatus);

        orderRepository.save(updated);

        return OrderResponse.from(updated.updateOrderLineItems(savedOrder.getOrderLineItems()));
    }
}
