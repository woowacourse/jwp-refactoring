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
        final List<OrderLineItemRequest> orderLineItemRequests = orderCreateRequest
            .getOrderLineItemRequests();

        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItemRequests.stream()
            .map(o -> o.getMenuId())
            .collect(Collectors.toList());

        if (orderLineItemRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository
            .findById(orderCreateRequest.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now());
        final Order savedOrder = orderRepository.save(order);

        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            Menu menu = menuRepository.getOne(orderLineItemRequest.getMenuId());
            long quantity = orderLineItemRequest.getQuantity();
            OrderLineItem orderLineItem = orderLineItemRequest
                .toEntity(savedOrder, menu, quantity);

            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }

        List<OrderLineItemResponse> orderLineItemResponses
            = OrderLineItemResponse.toResponseList(savedOrderLineItems);

        return OrderResponse.of(savedOrder, orderLineItemResponses);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId()));
        }

        return OrderResponse.toResponseList(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
        final OrderChangeRequest orderChangeRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(orderChangeRequest.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        orderRepository.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));

        return OrderResponse.of(savedOrder);
    }
}
