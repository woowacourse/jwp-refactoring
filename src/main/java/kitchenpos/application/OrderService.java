package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderUpdateStatusRequest;
import kitchenpos.mapper.OrderMapper;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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

    public OrderResponse create(final OrderCreateRequest request) {
        final List<OrderLineItem> orderLineItems = convertToOrderLineItems(request.getOrderLineItems());
        final Order order = saveOrder(request, orderLineItems);
        orderLineItemRepository.saveAll(order.getOrderLineItems());

        return OrderMapper.toOrderResponse(order);
    }

    private List<OrderLineItem> convertToOrderLineItems(final List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream()
                .map(this::convertToOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem convertToOrderLineItem(final OrderLineItemRequest request) {
        final Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 menu입니다. menuId: " + request.getMenuId()));

        return new OrderLineItem(null, menu, request.getQuantity());
    }

    private Order saveOrder(
            final OrderCreateRequest request,
            final List<OrderLineItem> orderLineItems
    ) {
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new NoSuchElementException(
                        "존재하지 않는 order table 입니다. orderTableId: " + request.getOrderTableId()));

        return orderRepository.save(OrderMapper.toOrder(orderTable, LocalDateTime.now(), orderLineItems));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> readAll() {
        final List<Order> orders = orderRepository.findAll();

        return OrderMapper.toOrderResponses(orders);
    }

    public OrderResponse changeOrderStatus(
            final Long orderId,
            final OrderUpdateStatusRequest request
    ) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 주문입니다."));
        order.updateOrderStatus(request.getOrderStatus());

        return OrderMapper.toOrderResponse(order);
    }
}
