package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.presentation.dto.ChangeOrderStatusRequest;
import kitchenpos.order.presentation.dto.CreateOrderRequest;
import kitchenpos.order.presentation.dto.OrderLineItemRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderService {

    private final MenuRepository menuRepository;

    private final OrderTableRepository orderTableRepository;

    private final OrderRepository orderRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderTableRepository orderTableRepository,
                        final OrderRepository orderRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public Order create(final CreateOrderRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                                                          .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 상태의 주문 테이블에서는 주문할 수 없습니다.");
        }
        final Order order = new Order(orderTable,
                                      OrderStatus.COOKING,
                                      LocalDateTime.now(),
                                      new ArrayList<>());
        final List<OrderLineItem> orderLineItems = request.getOrderLineItems().stream()
                                                          .map(orderLineItemRequest -> convertFromDto(orderLineItemRequest, order))
                                                          .collect(Collectors.toList());
        order.addOrderLineItems(orderLineItems);
        return orderRepository.save(order);
    }

    private OrderLineItem convertFromDto(final OrderLineItemRequest request, final Order order) {
        final Menu menu = menuRepository.findById(request.getMenuId())
                                        .orElseThrow(() -> new IllegalArgumentException("메뉴가 존재하지 않습니다."));
        return new OrderLineItem(order, menu, request.getQuantity());
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    public Order changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order order = orderRepository.findById(orderId)
                                           .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
        if (order.isCompleted()) {
            throw new IllegalArgumentException("이미 완료된 주문의 상태는 변경할 수 없습니다.");
        }
        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        order.changeStatus(orderStatus);
        return order;
    }
}
