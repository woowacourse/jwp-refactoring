package kitchenpos.order.application;

import kitchenpos.common.event.listener.MenuEventListener;
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

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final MenuEventListener menuEventListener;

    public OrderService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final MenuEventListener menuEventListener) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.menuEventListener = menuEventListener;
    }

    @Transactional
    public Order create(final OrderCreateRequest req) {
        // 1. 주문 품목이 비어 있다면 예외
        validateOrderLineItemsEmpty(req);

        // 2. 주문 테이블이 없다면 예외
        OrderTable orderTable = findOrderTable(req);

        // 3. 주문 테이블이 비어있다면 예외
        validateOrderTableEmpty(req);

        // 4. OrderLineItem이랑 req가 같지 않는 경우 & 매핑
        List<OrderLineItem> orderLineItems = makeOrderLineItems(req.getOrderLineItems());
        validateOrderLineItemEmpty(req, orderLineItems);

        Order order = Order.createDefault(orderTable.getId(), orderLineItems);
        return orderRepository.save(order);
    }

    private OrderTable findOrderTable(final OrderCreateRequest req) {
        return orderTableRepository.findById(req.getOrderTableId())
                .orElseThrow(OrderTableNotFoundException::new);
    }

    private void validateOrderLineItemsEmpty(final OrderCreateRequest req) {
        if (req.getOrderLineItems().isEmpty()) {
            throw new OrderLineItemEmptyException();
        }
    }

    private void validateOrderTableEmpty(final OrderCreateRequest req) {
        if (!orderTableRepository.existsByIdAndEmptyIsFalse(req.getOrderTableId())) {
            throw new OrderTableEmptyException();
        }
    }

    private List<OrderLineItem> makeOrderLineItems(final List<OrderLineItemCreateRequest> orderLineItemRequests) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        orderLineItemRequests.forEach(orderLineItemRequest -> {
            menuEventListener.validateMenuExist(orderLineItemRequest.getMenuId());
            orderLineItems.add(orderLineItemRequest.toDomain());
        });

        return orderLineItems;
    }

    private void validateOrderLineItemEmpty(final OrderCreateRequest req, final List<OrderLineItem> orderLineItems) {
        if (orderLineItems.size() != req.getOrderLineItems().size()) {
            throw new OrderLineItemEmptyException();
        }
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
