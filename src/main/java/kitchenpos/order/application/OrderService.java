package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.application.dto.OrderRequestDto;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.UpdateOrderStatusRequestDto;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderLineItemRepository;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.presentation.dto.OrderLineItemRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
    public OrderResponse create(final OrderRequestDto orderRequestDto) {
        final List<OrderLineItemRequest> orderLineItemRequests = orderRequestDto.getOrderLineItems();
        final List<OrderLineItem> orderLineItems = convertToOrderLineItems(orderLineItemRequests);
        final List<Long> menuIds = getMenuIds(orderLineItems);

        validateOrderLineItemsCount(orderLineItems, menuIds);
        validateOrderTableNotEmpty(orderRequestDto);

        final Order savedOrder = orderRepository.save(
                new Order(orderRequestDto.getOrderTableId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems)
        );

        return OrderResponse.of(savedOrder, orderLineItems);
    }

    private List<OrderLineItem> convertToOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
    }

    private List<Long> getMenuIds(List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        return menuIds;
    }

    private void validateOrderTableNotEmpty(OrderRequestDto orderRequestDto) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequestDto.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        orderTable.validateNotEmptyTable();
    }

    private void validateOrderLineItemsCount(List<OrderLineItem> orderLineItemRequests, List<Long> menuIds) {
        if (orderLineItemRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    public List<Order> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders;
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final UpdateOrderStatusRequestDto orderStatusRequestDto) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        validateOrderStatus(savedOrder);
        final OrderStatus orderStatus = OrderStatus.valueOf(orderStatusRequestDto.getOrderStatus());
        final Order changedOrder = orderRepository
                .save(new Order(savedOrder.getId(), orderStatus.name(), savedOrder.getOrderedTime(), savedOrder.getOrderLineItems()));
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(orderId);
        return OrderResponse.of(changedOrder, orderLineItems);
    }

    private void validateOrderStatus(Order savedOrder) {
        if (savedOrder.isCompletion()) {
            throw new IllegalArgumentException();
        }
    }
}
