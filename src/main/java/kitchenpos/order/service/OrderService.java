package kitchenpos.order.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.service.dto.OrderRequest;
import kitchenpos.order.service.dto.OrderResponse;
import kitchenpos.order.service.dto.OrderStatusRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository,
                        final OrderLineItemRepository orderLineItemRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        final Order order = new Order(findOrderTableById(request.getOrderTableId()), LocalDateTime.now());
        order.addOrderLineItem(extractOrderLineItems(request));

        orderRepository.save(order);
        orderLineItemRepository.saveAll(order.getOrderLineItems());
        return OrderResponse.from(order);
    }

    private List<OrderLineItem> extractOrderLineItems(final OrderRequest request) {
        return request.getOrderLineItems().stream()
                .map(each -> new OrderLineItem(findMenuById(each.getMenuId()), each.getQuantity()))
                .collect(Collectors.toList());
    }

    private OrderTable findOrderTableById(final long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
    }

    private Menu findMenuById(final long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴가 포함되어 있습니다."));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.from(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
        order.updateStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return OrderResponse.from(order);
    }
}
