package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.application.mapper.OrderMapper;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderUpdateStatusRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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

    public OrderResponse create(final OrderCreateRequest request) {
        final List<OrderLineItem> orderLineItems = convertToOrderLineItems(request.getOrderLineItems());
        final Order order = saveOrder(request, orderLineItems);

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

        return new OrderLineItem(menu.getId(), request.getQuantity());
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
