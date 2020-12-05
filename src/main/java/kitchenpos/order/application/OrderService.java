package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Menus;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemCreateRequest;
import kitchenpos.order.dto.request.OrderStatusChangeRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public OrderResponse create(OrderCreateRequest request) {
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블에 주문을 할 수 없습니다."));

        Order order = new Order();
        orderTable.addOrder(order);
        Menus menus = new Menus(menuRepository.findAllById(request.getMenuIds()));

        for (OrderLineItemCreateRequest orderLineItemRequest : request.getOrderLineItemCreateRequests()) {
            Menu menu = menus.findById(orderLineItemRequest.getMenuId());
            order.addOrderLineItem(new OrderLineItem(orderLineItemRequest.getQuantity(), menu));
        }

        orderRepository.save(order);

        return OrderResponse.of(order);
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
