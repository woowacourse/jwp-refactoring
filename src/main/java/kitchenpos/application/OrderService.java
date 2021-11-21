package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.*;
import kitchenpos.ui.dto.order.OrderRequest;
import kitchenpos.ui.dto.order.OrderResponse;
import kitchenpos.ui.dto.order.OrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
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
        final OrderTable orderTable = getOrderTable(request);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블의 주문은 생성할 수 없습니다.");
        }
        final Order order = new Order(orderTable, OrderStatus.COOKING);
        order.changeOrderLineItems(orderLineItems);
        final Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }

    private OrderTable getOrderTable(OrderRequest request) {
        return orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new NoSuchElementException("해당 주문 테이블이 존재하지 않습니다. id: " + request.getOrderTableId()));
    }

    private List<OrderLineItem> getOrderLineItems(OrderRequest request) {
        return request.getOrderLineItems()
                .stream()
                .map(lineItem -> {
                    final Menu menu = menuRepository.findById(lineItem.getMenuId())
                            .orElseThrow(() -> new NoSuchElementException("해당 메뉴가 존재하지 않습니다. id: " + lineItem.getMenuId()));
                    return new OrderLineItem(menu, lineItem.getQuantity());
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
                .orElseThrow(() -> new NoSuchElementException("해당 주문이 존재하지 않습니다. id: " + orderId));
        if (savedOrder.isCompletion()) {
            throw new IllegalArgumentException("계산 완료된 주문의 상태는 변경할 수 없습니다.");
        }
        savedOrder.changeStatus(request.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }
}
