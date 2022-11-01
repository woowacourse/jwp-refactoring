package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "주문 테이블이 존재하지 않습니다. orderTableId = " + request.getOrderTableId()));
        return OrderResponse.from(
                orderRepository.save(
                        new Order(orderTable, mapToOrderLineItems(request.getOrderLineItems())))
        );
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다. orderId = " + orderId));
        order.changeStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return OrderResponse.from(order);
    }

    private List<OrderLineItem> mapToOrderLineItems(List<OrderLineItemRequest> requests) {
        var result = new ArrayList<OrderLineItem>();
        for (OrderLineItemRequest request : requests) {
            var menuId = request.getMenuId();
            if (!menuRepository.existsById(menuId)) {
                throw new IllegalArgumentException("주문 항목의 메뉴가 존재하지 않습니다. menuId = " + menuId);
            }
            result.add(new OrderLineItem(menuId, request.getQuantity()));
        }
        return result;
    }
}
