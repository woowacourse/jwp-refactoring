package kitchenpos.application;

import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.application.dto.OrderUpdateRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
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
        validateOrderCreateRequest(request);

        final Order order = orderRepository.save(new Order(request.getOrderTableId(), OrderStatus.COOKING.name(), LocalDateTime.now()));
        order.addOrderLineItems(new OrderLineItems(request.getOrderLineItems()
            .stream()
            .map(orderLineItem -> createOrderLineItem(order, orderLineItem))
            .collect(Collectors.toList())));

        return OrderResponse.createResponse(order);
    }

    private void validateOrderCreateRequest(final OrderCreateRequest request) {
        if (CollectionUtils.isEmpty(request.getOrderLineItems())) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        orderTable.validateNotEmpty();
    }

    private OrderLineItem createOrderLineItem(final Order order, final OrderLineItemRequest request) {
        final Menu menu = menuRepository.findById(request.getMenuId())
            .orElseThrow(IllegalArgumentException::new);
        return new OrderLineItem(order.getId(), menu.getId(), request.getQuantity());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
            .map(OrderResponse::createResponse)
            .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderUpdateRequest request) {
        final Order order = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new)
            .changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));

        return OrderResponse.createResponse(order);
    }
}
