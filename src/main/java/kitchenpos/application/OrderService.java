package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineRequest;
import kitchenpos.dto.request.OrderStatusChangeRequest;
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
    public OrderResponse create(final OrderCreateRequest request) {
        List<OrderLineRequest> orderLines = request.getOrderLines();

        if (CollectionUtils.isEmpty(orderLines)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLines.stream()
            .map(OrderLineRequest::getMenuId)
            .collect(Collectors.toList());

        if (orderLines.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("해당 테이블은 비어있습니다.");
        }

        Order newOrder = new Order(null,
            orderTable.getId(),
            OrderStatus.COOKING.name(),
            LocalDateTime.now(),
            new ArrayList<>());

        final Order savedOrder = orderRepository.save(newOrder);

        for (final OrderLineRequest orderLineItem : orderLines) {
            OrderLineItem newOrderLineItem = new OrderLineItem(null, savedOrder.getId(), savedOrder,
                orderLineItem.getQuantity());
            savedOrder.addOrderLineItem(orderLineItemRepository.save(newOrderLineItem));
        }

        return OrderResponse.of(savedOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        for (final Order order : orders) {
            order.addOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId()));
        }

        return OrderResponse.of(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.changeStatus(orderStatus);

        Order changedOrder = orderRepository.save(savedOrder);

        changedOrder.addOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));

        return OrderResponse.of(changedOrder);
    }
}
