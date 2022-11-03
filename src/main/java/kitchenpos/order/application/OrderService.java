package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.ui.dto.request.OrderCreateRequest;
import kitchenpos.order.ui.dto.request.OrderLineItemRequest;
import kitchenpos.order.ui.dto.request.OrderStatusRequest;
import kitchenpos.order.ui.dto.response.OrderResponse;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
    public OrderResponse create(final OrderCreateRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
        orderTable.validateEmpty();

        final List<Long> menuIds = extractMenuIds(orderRequest);
        final Order order = Order.create(orderRequest.getOrderTableId(), mapToOrderLineItem(orderRequest),
                menuRepository.countByIdIn(menuIds));

        return OrderResponse.from(orderRepository.save(order));
    }

    private List<Long> extractMenuIds(final OrderCreateRequest orderRequest) {
        return orderRequest.getOrderLineItemRequests()
                .stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    private List<OrderLineItem> mapToOrderLineItem(final OrderCreateRequest orderRequest) {
        return orderRequest.getOrderLineItemRequests()
                .stream()
                .map(OrderLineItemRequest::toEntity)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문번호입니다."));

        savedOrder.changeOrderStatus(orderStatusRequest.getOrderStatus());

        return OrderResponse.from(orderRepository.save(savedOrder));
    }
}
