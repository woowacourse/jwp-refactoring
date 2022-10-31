package kitchenpos.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.order.ChangeOrderStatusRequest;
import kitchenpos.dto.request.order.CreateOrderLineItemRequest;
import kitchenpos.dto.request.order.CreateOrderRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

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
    public Order create(final CreateOrderRequest request) {
        return orderRepository.save(new Order(
            findOrderTableById(request.getOrderTableId()),
            toEntities((request.getOrderLineItems()))
        ));
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order order = findOrderById(orderId);
        order.changeStatus(request.getStatus());

        return order;
    }

    private OrderTable findOrderTableById(final Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블 입니다."));
    }

    private Menu findMenuById(final Long id) {
        return menuRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));
    }

    private Order findOrderById(final Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 주문입니다."));
    }

    private Map<Menu, Long> toEntities(List<CreateOrderLineItemRequest> orderLineItems) {
        final Map<Menu, Long> entities = new HashMap<>();
        for (CreateOrderLineItemRequest orderLineItem : orderLineItems) {
            entities.put(
                findMenuById(orderLineItem.getMenuId()),
                orderLineItem.getQuantity()
            );
        }

        return entities;
    }
}
