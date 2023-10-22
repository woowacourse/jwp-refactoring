package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.MenuNotFoundException;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderLineItemCreateRequest;
import kitchenpos.order.application.dto.OrderUpdateRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.OrderLineItemEmptyException;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.exception.OrderTableEmptyException;
import kitchenpos.ordertable.exception.OrderTableNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderCreateRequest req) {
        // 1. 주문 품목이 비어 있다면 예외
        if(req.getOrderLineItems().isEmpty()) {
            throw new OrderLineItemEmptyException();
        }

        // 2. 주문 테이블이 없다면 예외
        OrderTable orderTable = findOrderTable(req);

        // 3. 주문 테이블이 비어있다면 예외
        if (!orderTableRepository.existsByIdAndEmptyIsFalse(req.getOrderTableId())) {
            throw new OrderTableEmptyException();
        }


        // 4. 매핑
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemCreateRequest orderLineItemReq : req.getOrderLineItems()) {
            Menu menu = findMenu(orderLineItemReq);

            OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), orderLineItemReq.getQuantity());
            orderLineItems.add(orderLineItem);
        }
        Order order = Order.createDefault(orderTable.getId(), orderLineItems);

        if (orderLineItems.size() != req.getOrderLineItems().size()) {
            throw new OrderLineItemEmptyException();
        }



        return orderRepository.save(order);
    }

    private OrderTable findOrderTable(final OrderCreateRequest req) {
        return orderTableRepository.findById(req.getOrderTableId())
                .orElseThrow(OrderTableNotFoundException::new);
    }

    private Menu findMenu(final OrderLineItemCreateRequest orderLineItemReq) {
        return menuRepository.findById(orderLineItemReq.getMenuId())
                .orElseThrow(MenuNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderUpdateRequest req) {
        Order savedOrder = findOrder(orderId);
        savedOrder.changeOrderStatus(OrderStatus.valueOf(req.getOrderStatus()).name());

        return savedOrder;
    }

    private Order findOrder(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
    }
}
