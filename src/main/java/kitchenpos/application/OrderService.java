package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderChangeRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderLineItemResponse;
import kitchenpos.dto.OrderResponse;
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
    public OrderResponse create(final OrderCreateRequest orderCreateRequest) {
        final List<OrderLineItemRequest> orderLineItemRequests
            = orderCreateRequest.getOrderLineItemRequests();
        validateOrderLineItemRequests(orderLineItemRequests);

        final List<Long> menuIds = orderLineItemRequests.stream()
            .map(o -> o.getMenuId())
            .collect(Collectors.toList());
        validateMenuIds(orderLineItemRequests, menuIds);

        final OrderTable orderTable = orderTableRepository
            .findById(orderCreateRequest.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);
        validateOrderTable(orderTable);

        final Order savedOrder = saveOrder(orderTable);
        List<OrderLineItemResponse> orderLineItemResponses
            = createOrderLineItemResponses(savedOrder, orderLineItemRequests);

        return OrderResponse.of(savedOrder, orderLineItemResponses);
    }

    private void validateOrderLineItemRequests(
        List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenuIds(List<OrderLineItemRequest> orderLineItemRequests,
        List<Long> menuIds) {
        if (orderLineItemRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private Order saveOrder(OrderTable orderTable) {
        Order order = Order.of(orderTable, OrderStatus.COOKING, LocalDateTime.now());
        return orderRepository.save(order);
    }

    private List<OrderLineItemResponse> createOrderLineItemResponses(Order savedOrder,
        List<OrderLineItemRequest> orderLineItemRequests) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            Menu menu = menuRepository.getOne(orderLineItemRequest.getMenuId());
            long quantity = orderLineItemRequest.getQuantity();
            OrderLineItem orderLineItem = orderLineItemRequest
                .toEntity(savedOrder, menu, quantity);

            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }

        return OrderLineItemResponse.toResponseList(savedOrderLineItems);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        final List<Order> ordersWithLineItems = new ArrayList<>();

        for (final Order order : orders) {
            Order orderWithLineItem
                = new Order(order.getId(), order.getOrderTable(),
                order.getOrderStatus(), order.getOrderedTime(),
                orderLineItemRepository.findAllByOrderId(order.getId()));

            ordersWithLineItems.add(orderWithLineItem);
        }

        return OrderResponse.toResponseList(ordersWithLineItems);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
        final OrderChangeRequest orderChangeRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        validateStatus(savedOrder);

        final OrderStatus orderStatus = OrderStatus.valueOf(orderChangeRequest.getOrderStatus());

        Order changedOrder
            = new Order(savedOrder.getId(), savedOrder.getOrderTable(), orderStatus,
            savedOrder.getOrderedTime(), orderLineItemRepository.findAllByOrderId(orderId));

        orderRepository.save(changedOrder);

        return OrderResponse.of(changedOrder);
    }

    private void validateStatus(Order savedOrder) {
        if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }
    }
}
