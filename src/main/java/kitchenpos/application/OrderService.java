package kitchenpos.application;

import kitchenpos.application.dto.OrderChangeStatusRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.application.dto.OrderRequest.OrderLineItemRequest;

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
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다"));
        Order order = new Order(orderTable, new ArrayList<>());
        setupOrderLineItems(request, order);
        orderRepository.save(order);
        return OrderResponse.from(order);
    }

    private void setupOrderLineItems(OrderRequest request, Order order) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : request.getOrderLineItems()) {
            Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 항목이 있습니다"));
            orderLineItems.add(new OrderLineItem(menu, order, orderLineItemRequest.getQuantity()));
        }
        order.changeOrderLineItems(orderLineItems);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다"));
        order.changeOrderStatus(request.getOrderStatus());
        return OrderResponse.from(order);
    }
}
