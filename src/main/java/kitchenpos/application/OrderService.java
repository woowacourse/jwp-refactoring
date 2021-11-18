package kitchenpos.application;

import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.OrderTableRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.*;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderResponse;
import kitchenpos.ui.dto.OrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public OrderResponse create(final OrderRequest request) {
        final List<OrderLineItem> orderLineItems = getOrderLineItems(request);
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Order order = new Order(orderTable, OrderStatus.COOKING);
        order.changeOrderLineItems(orderLineItems);
        final Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }

    private List<OrderLineItem> getOrderLineItems(OrderRequest request) {
        return request.getOrderLineItems()
                .stream()
                .map(orderLineItemsRequest -> {
                    final Menu menu = menuRepository.findById(orderLineItemsRequest.getMenuId()).orElseThrow(IllegalArgumentException::new);
                    return new OrderLineItem(menu, orderLineItemsRequest.getQuantity());
                }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.toList(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        if (savedOrder.isCompletion()) {
            throw new IllegalArgumentException();
        }
        savedOrder.changeStatus(request.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }
}
