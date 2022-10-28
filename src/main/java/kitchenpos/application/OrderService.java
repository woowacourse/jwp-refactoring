package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.ChangeOrderStatusRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository,
                        OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        final List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException("주문 항목이 없습니다.");
        }

        final List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("없는 메뉴는 주문할 수 없습니다.");
        }

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("없는 테이블에서는 주문할 수 없습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("사용 중이지 않은 테이블입니다.");
        }

        final Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());

        final Order savedOrder = orderRepository.save(order);

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = orderLineItemRequests.stream()
                .map(orderLineItemRequest -> saveOrderLineItem(orderId, orderLineItemRequest))
                .collect(Collectors.toList());
        return OrderResponse.of(savedOrder, savedOrderLineItems);
    }

    public List<OrderResponse> findAll() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> {
                    final List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(order.getId());
                    return OrderResponse.of(order, orderLineItems);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrder.changeOrderStatus(request.getOrderStatus());

        final List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(orderId);

        return OrderResponse.of(savedOrder, orderLineItems);
    }

    private OrderLineItem saveOrderLineItem(final Long orderId, final OrderLineItemRequest orderLineItemRequest) {
        final OrderLineItem orderLineItem = new OrderLineItem(orderId, orderLineItemRequest.getMenuId(),
                orderLineItemRequest.getQuantity());
        return orderLineItemRepository.save(orderLineItem);
    }
}
