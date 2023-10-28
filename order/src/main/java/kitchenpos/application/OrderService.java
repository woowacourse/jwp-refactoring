package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItemRepository;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.OrderValidator;
import kitchenpos.ui.request.OrderCreateRequest;
import kitchenpos.ui.request.OrderUpdateOrderStatusRequest;
import kitchenpos.ui.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository,
            final MenuRepository menuRepository,
            final OrderValidator orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final List<OrderLineItem> orderLineItems = request.getOrderLineItems().stream()
                .map(it -> {
                    final Menu menu = menuRepository.findById(it.getMenuId())
                            .orElseThrow(NoSuchElementException::new);
                    return new OrderLineItem(menu.getName(), menu.getPrice(), it.getMenuId(), it.getQuantity());
                })
                .collect(Collectors.toList());

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId()).orElseThrow();

        final Order order = new Order(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        order.validate(orderValidator);
        final Order savedOrder = orderRepository.save(order);

        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }

        return OrderResponse.from(savedOrder);
    }

    public List<OrderResponse> list() {
        return OrderResponse.from(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderUpdateOrderStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(NoSuchElementException::new);
        savedOrder.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return OrderResponse.from(savedOrder);
    }
}
