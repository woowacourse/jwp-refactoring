package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.entity.Order;
import kitchenpos.domain.entity.OrderLineItem;
import kitchenpos.domain.entity.OrderStatus;
import kitchenpos.domain.entity.OrderTable;
import kitchenpos.dto.request.order.ChangeOrderRequest;
import kitchenpos.dto.request.order.CreateOrderRequest;
import kitchenpos.dto.request.order.OrderLineItemsDto;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final CreateOrderRequest request) {
        final List<OrderLineItemsDto> orderLineItemDtos = request.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItemDtos)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItemDtos.stream()
                .map(OrderLineItemsDto::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemDtos.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final Order order = Order.builder()
                .orderTableId(request.getOrderTableId())
                .orderStatus(OrderStatus.COOKING)
                .orderedTime(LocalDateTime.now())
                .build();

        final Long orderId = orderRepository.save(order).getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        final List<OrderLineItem> orderLineItems = orderLineItemDtos.stream()
                .map(OrderLineItem::from)
                .collect(Collectors.toList());
        for (final OrderLineItem orderLineItem : orderLineItems) {
            final OrderLineItem newOrderLineItem = new OrderLineItem(
                    orderLineItem.getSeq(),
                    orderId,
                    orderLineItem.getMenuId(),
                    orderLineItem.getQuantity()
            );
            savedOrderLineItems.add(orderLineItemRepository.save(newOrderLineItem));
        }

        return OrderResponse.from(Order.builder()
                .id(orderId)
                .orderTableId(order.getOrderTable())
                .orderStatus(order.getOrderStatus())
                .orderedTime(order.getOrderedTime())
                .orderLineItems(savedOrderLineItems)
                .build()
        );
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> Order.builder()
                        .id(order.getId())
                        .orderTableId(order.getOrderTable())
                        .orderStatus(order.getOrderStatus())
                        .orderedTime(order.getOrderedTime())
                        .orderLineItems(orderLineItemRepository.findAllByOrderId(order.getId()))
                        .build()
                )
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());

        final Order order = Order.builder()
                .id(orderId)
                .orderTableId(savedOrder.getOrderTable())
                .orderStatus(orderStatus)
                .orderedTime(savedOrder.getOrderedTime())
                .orderLineItems(orderLineItemRepository.findAllByOrderId(orderId))
                .build();

        orderRepository.save(order);

        return OrderResponse.from(order);
    }
}
