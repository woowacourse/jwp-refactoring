package kitchenpos.order.application;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.dto.OrderRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderStatusRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
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
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = getOrderTable(orderRequest);
        OrderLineItems orderLineItems = getOrderLineItems(orderRequest);

        Order order = new Order(orderTable.getId(), orderLineItems);

        return OrderResponse.from(orderRepository.save(order));
    }

    private OrderTable getOrderTable(OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return orderTable;
    }

    private OrderLineItems getOrderLineItems(OrderRequest orderRequest) {
        Map<Long, Long> menuQuantityMap = orderRequest.getMenuQuantityMap();

        Set<Long> menuIds = menuQuantityMap.keySet();
        List<Menu> menus = menuRepository.findAllByIdIn(menuIds);

        if (menuIds.size() != menus.size()) {
            throw new IllegalArgumentException("등록되지 않은 상품은 주문할 수 없습니다.");
        }

        return menus.stream()
                .map(menu -> OrderLineItem.of(menu, menuQuantityMap.get(menu.getId())))
                .collect(Collectors.collectingAndThen(Collectors.toList(), OrderLineItems::new));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        order.updateOrderStatus(orderStatusRequest.getOrderStatus());

        return OrderResponse.from(orderRepository.save(order));
    }
}
