package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
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

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("없는 테이블에서는 주문할 수 없습니다."));

        final Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now());
        final Order savedOrder = orderRepository.save(order);

        final List<OrderLineItem> orderLineItems = saveOrderLineItems(orderLineItemRequests, savedOrder);
        return OrderResponse.of(savedOrder, orderLineItems);
    }

    public List<OrderResponse> findAll() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrder.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));

        final List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(orderId);

        return OrderResponse.of(savedOrder, orderLineItems);
    }

    private List<OrderLineItem> saveOrderLineItems(final List<OrderLineItemRequest> orderLineItemRequests,
                                                   final Order order) {
        final List<OrderLineItem> orderLineItems = orderLineItemRequests.stream()
                .map(orderLineItemRequest -> toOrderLineItem(orderLineItemRequest, order))
                .collect(Collectors.toList());
        orderLineItemRepository.saveAll(orderLineItems);
        return orderLineItems;
    }

    private OrderLineItem toOrderLineItem(final OrderLineItemRequest request, final Order order) {
        final Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("없는 메뉴는 주문할 수 없습니다."));
        return new OrderLineItem(order, menu, request.getQuantity());
    }

    private OrderResponse toResponse(final Order order) {
        final List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(order.getId());
        return OrderResponse.of(order, orderLineItems);
    }
}
