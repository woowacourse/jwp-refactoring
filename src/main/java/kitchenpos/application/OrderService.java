package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.*;
import kitchenpos.dto.request.order.ChangeOrderStatusRequest;
import kitchenpos.dto.request.order.CreateOrderRequest;
import kitchenpos.dto.response.order.CreateOrderResponse;
import kitchenpos.dto.response.order.OrderResponse;

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
    public CreateOrderResponse create(final CreateOrderRequest request) {
        final OrderTable orderTable = getOrderTable(request);
        final List<OrderLineItem> orderLineItems = getOrderLineItems(request);

        final Order order = new Order(orderTable, orderLineItems);
        final Order savedOrder = orderRepository.save(order);

        return CreateOrderResponse.from(savedOrder);
    }

    private OrderTable getOrderTable(CreateOrderRequest request) {
        return orderTableRepository.findById(request.getOrderTableId())
                                   .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블은 주문할 수 없습니다."));
    }

    private List<OrderLineItem> getOrderLineItems(CreateOrderRequest request) {
        return request.getOrderLineItems()
                      .stream()
                      .map(item -> {
                          final Menu menu = menuRepository.findById(item.getMenuId())
                                                          .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 메뉴는 주문할 수 없습니다."));
                          return new OrderLineItem(menu, item.getQuantity());
                      })
                      .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                              .map(OrderResponse::from)
                              .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order order = orderRepository.findById(orderId)
                                           .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문의 상태는 변경할 수 없습니다."));

        order.changeOrderStatus(request.getOrderStatus());
        return OrderResponse.from(order);
    }
}
