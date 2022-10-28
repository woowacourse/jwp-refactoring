package kitchenpos.application;

import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderResponse create(final OrderCreateRequest request) {
        validateOrder(request);

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final Order order = orderRepository.save(new Order(request.getOrderTableId(), OrderStatus.COOKING.name(), LocalDateTime.now()));
        order.addOrderLineItems(request.getOrderLineItemRequests()
            .stream()
            .map(orderLineItem -> createOrderLineItem(order, orderLineItem))
            .collect(Collectors.toList()));

        return OrderResponse.createResponse(order);
    }

    private void validateOrder(final OrderCreateRequest request) {
        if (CollectionUtils.isEmpty(request.getOrderLineItemRequests())) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = request.getOrderLineItemRequests().stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (request.getOrderLineItemRequests().size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private OrderLineItem createOrderLineItem(final Order order, final OrderLineItemRequest request) {
        return new OrderLineItem(order.getId(), request.getMenuId(), request.getQuantity());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
            .map(OrderResponse::createResponse)
            .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus.name());

        return OrderResponse.createResponse(order);
    }
}
