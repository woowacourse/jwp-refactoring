package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderUpdateRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderService(final OrderRepository orderRepository,
                        final MenuRepository menuRepository,
                        final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 주문 테이블입니다.");
        }

        final List<OrderLineItem> orderLineItems = request.getOrderLineItems().stream()
                .map(this::createOrderLineItem)
                .collect(Collectors.toList());

        final Order order = new Order(orderTable.getId(), orderLineItems);
        return OrderResponse.from(orderRepository.save(order));
    }

    private OrderLineItem createOrderLineItem(final OrderLineItemCreateRequest request) {
        final Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 항목입니다."));

        return new OrderLineItem(menu.getId(), menu.getName(), menu.getPrice(), request.getQuantity());
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderUpdateRequest request) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        order.changeOrderStatus(request.getOrderStatus());
        return OrderResponse.from(order);
    }
}
