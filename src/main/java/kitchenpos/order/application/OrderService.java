package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Menus;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(OrderCreateRequest request) {
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블에 주문을 할 수 없습니다."));

        Order order = new Order(orderTable);
        Menus menus = new Menus(menuRepository.findAllById(request.getMenuIds()));

        List<OrderLineItem> orderLineItems = new ArrayList<>();

        for (OrderLineItemCreateRequest orderLineItemRequest : request.getOrderLineItemCreateRequests()) {
            Menu menu = menus.findById(orderLineItemRequest.getMenuId());
            orderLineItems.add(order.createOrderLineItem(orderLineItemRequest.getQuantity(), menu));
        }

        orderRepository.save(order);
        orderLineItemRepository.saveAll(orderLineItems);

        return OrderResponse.of(order, orderLineItems);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public void changeOrderStatus(Long orderId, OrderStatusChangeRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문을 변경할 수 없습니다."));
        order.changeStatus(request.getOrderStatus());
    }
}
