package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.ChangeOrderStatusRequest;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest orderCreateRequest) {
        OrderLineItems orderLineItems = orderCreateRequest.extractOrderLineItems();
        OrderTable orderTable = validate(orderCreateRequest, orderLineItems);
        Order savedOrder = orderRepository.save(Order.builder()
                .orderTableId(orderTable.getId())
                .orderStatus(OrderStatus.COOKING)
                .orderedTime(LocalDateTime.now())
                .orderLineItems(orderLineItems)
                .build());
        return OrderResponse.from(savedOrder);
    }

    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
                                           final ChangeOrderStatusRequest changeOrderStatusRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
        order.changeOrderStatus(OrderStatus.valueOf(changeOrderStatusRequest.getOrderStatus()));
        return OrderResponse.from(order);
    }

    private OrderTable validate(final OrderCreateRequest orderCreateRequest, final OrderLineItems orderLineItems) {
        List<Long> menuIds = orderLineItems.extractMenuIds();
        validateDuplicate(menuIds);
        OrderTable orderTable = orderTableRepository.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
        validateEmpty(orderTable);
        return orderTable;
    }

    private void validateDuplicate(final List<Long> menuIds) {
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("주문 항목엔 중복되는 메뉴나 존재하지 않는 메뉴가 있을 수 없습니다.");
        }
    }

    private void validateEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("비활성화된 주문 테이블은 주문을 받을 수 없습니다.");
        }
    }
}
