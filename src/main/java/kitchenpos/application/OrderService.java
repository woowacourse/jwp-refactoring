package kitchenpos.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderStatusUpdateRequest;
import kitchenpos.dto.response.OrderResponse;
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
    public Long create(final OrderRequest request) {
        final List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItemRequests();

        final List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("상품이 존재하지 않습니다.");
        }

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블의 상태가 비어있습니다.");
        }
        final Order savedOrder = orderRepository.save(new Order(orderTable.getId(), OrderStatus.COOKING.name()));

        for (final OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            final OrderLineItem updatedOrderLineItem = new OrderLineItem(
                    savedOrder,
                    orderLineItemRequest.getMenuId(),
                    orderLineItemRequest.getQuantity()
            );
            savedOrder.addOrderLineItem(orderLineItemRepository.save(updatedOrderLineItem));
        }

        return savedOrder.getId();
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusUpdateRequest request) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if (Objects.equals(OrderStatus.COMPLETION.name(), order.getOrderStatus())) {
            throw new IllegalArgumentException("이미 완료된 주문입니다.");
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        final Order updatedOrder = new Order(order.getId(), order.getOrderTableId(), orderStatus.name());
        final Order savedOrder = orderRepository.save(updatedOrder);

        return OrderResponse.from(savedOrder);
    }
}
