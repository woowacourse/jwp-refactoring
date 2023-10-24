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
import kitchenpos.domain.mapper.OrderLineItemMapper;
import kitchenpos.domain.mapper.OrderMapper;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderLineItemRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kitchenpos.application.dto.request.CreateOrderRequest.CreateOrderLineItem;

@Service
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderLineItemMapper orderLineItemMapper;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final OrderMapper orderMapper,
            final OrderLineItemMapper orderLineItemMapper,
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.orderMapper = orderMapper;
        this.orderLineItemMapper = orderLineItemMapper;
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

        final List<OrderLineItem> orderLineItems = getOrderLineItems(request.getOrderLineItems());
        final Order order = orderMapper.toOrder(request, orderLineItems);
        final Order savedOrder = orderRepository.save(order);

        return CreateOrderResponse.of(
                savedOrder.updateOrderLineItems(orderLineItems),
                orderLineItems.stream()
                        .map(OrderLineItemResponse::from)
                        .collect(Collectors.toList())
        );
    }

    private List<OrderLineItem> getOrderLineItems(List<CreateOrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        return orderLineItems.stream()
                .map(orderLineItemMapper::toOrderLineItem)
                .collect(Collectors.toList());
    }

    private void validateOrderLineItems(List<CreateOrderLineItem> createOrderLineItems) {
        List<Long> menuIds = createOrderLineItems.stream()
                .map(CreateOrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (createOrderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
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
