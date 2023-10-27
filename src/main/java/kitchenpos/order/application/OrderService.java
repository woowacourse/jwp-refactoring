package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.common.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemCreateRequest;
import kitchenpos.order.dto.request.OrderUpdateStatusRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
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
    public Long create(final OrderCreateRequest request) {
        final List<OrderLineItemCreateRequest> orderLineItemCreateRequests = request.getOrderLineItemRequests();
        validateMenus(orderLineItemCreateRequests);

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        orderTable.checkEmptyIsFalse();

        final Order savedOrder = orderRepository.save(new Order(orderTable.getId(), OrderStatus.COOKING.name()));
        saveOrderLineItem(orderLineItemCreateRequests, savedOrder);
        return savedOrder.getId();
    }

    private void validateMenus(final List<OrderLineItemCreateRequest> orderLineItemCreateRequests) {
        final List<Long> menuIds = orderLineItemCreateRequests.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemCreateRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("상품이 존재하지 않습니다.");
        }
    }

    private void saveOrderLineItem(final List<OrderLineItemCreateRequest> orderLineItemCreateRequests,
                                   final Order savedOrder) {
        for (final OrderLineItemCreateRequest orderLineItemCreateRequest : orderLineItemCreateRequests) {
            final OrderLineItem updatedOrderLineItem = new OrderLineItem(
                    savedOrder,
                    orderLineItemCreateRequest.getMenuId(),
                    orderLineItemCreateRequest.getQuantity()
            );
            savedOrder.addOrderLineItem(orderLineItemRepository.save(updatedOrderLineItem));
        }
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderUpdateStatusRequest request) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        order.changeOrderStatus(request.getOrderStatus());
        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }
}
