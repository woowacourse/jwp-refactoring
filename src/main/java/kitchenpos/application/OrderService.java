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
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        final Order order = orderRepository.save(
            new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new OrderLineItems(request.getOrderLineItems()
                .stream()
                .map(this::createOrderLineItem)
                .collect(Collectors.toList()))));

        return OrderResponse.createResponse(order);
    }

    private void validateOrderCreateRequest(final OrderCreateRequest request) {
        if (CollectionUtils.isEmpty(request.getOrderLineItems())) {
            throw new IllegalArgumentException();
        }
    }

    private OrderLineItem createOrderLineItem(final OrderLineItemRequest request) {
        final Menu menu = menuRepository.findById(request.getMenuId())
            .orElseThrow(IllegalArgumentException::new);
        return new OrderLineItem(menu.getName(), menu.getPrice(), request.getQuantity());
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
            .orElseThrow(IllegalArgumentException::new);
        order.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));

        return OrderResponse.createResponse(order);
    }
}
