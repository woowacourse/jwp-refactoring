package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderUpdateStatusRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();
        final List<OrderLineItem> orderLineItems = orderLineItemRequests.stream()
                .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
        validateExistMenu(orderLineItemRequests, orderLineItems);

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        final Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        return OrderResponse.toResponse(orderRepository.save(order));
    }

    private void validateExistMenu(final List<OrderLineItemRequest> orderLineItemRequests,
                           final List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderUpdateStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);

        return OrderResponse.toResponse(orderRepository.save(savedOrder));
    }
}
