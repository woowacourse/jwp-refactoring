package kitchenpos.application;

import kitchenpos.application.dto.OrderChangeStatusRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItemRepository;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.application.dto.OrderRequest.OrderLineItemRequest;

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
    public Order create(final OrderRequest request) {
        final List<OrderLineItemRequest> orderLineItems = request.getOrderLineItems();

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 주문 항목이 있습니다");
        }

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다"));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 빈 테이블입니다");
        }

        Order entity = getOrder(orderTable, request);
        final Order savedOrder = orderRepository.save(entity);

        List<OrderLineItem> savedOrderLineItems = orderLineItems.stream()
                .map(it -> orderLineItemRepository.save(new OrderLineItem(it.getMenuId(), it.getQuantity())))
                .collect(Collectors.toList());
        savedOrder.changeOrderLineItems(savedOrderLineItems);

        return savedOrder;
    }

    private Order getOrder(OrderTable orderTable, OrderRequest request) {
        List<OrderLineItem> orderLineItems = request.getOrderLineItems().stream()
                .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new Order(orderTable, orderLineItems);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderChangeStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다"));
        order.changeOrderStatus(request.getOrderStatus());
        return order;
    }
}
